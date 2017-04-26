package com.juxin.predestinate.module.logic.socket.v2;

import com.juxin.predestinate.module.logic.socket.NetData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.SocketFactory;

/**
 * Created by sks on 2017/4/20.
 */
public class KeepAliveSocket {
    private final int SOCKET_PACKET_QUEUE_SIZE = 500;
    private Socket socket;
    private Lock socketStateLock = new ReentrantLock();
    private Condition socketConnectCondition;
    private volatile SocketState state = SocketState.DISCONNECT_NORMAL;
    private SocketAddress address;
    private Executor singleDispatchExecutor = Executors.newSingleThreadExecutor();
    private Executor singleSendErrorExecutor = Executors.newSingleThreadExecutor();

    private InputStream input;
    private OutputStream output;
    private PacketWriter packetWriter;
    private PackerReader packerReader;

    private SocketConnectionListener listener;

    public KeepAliveSocket(String host, int port){
        address = new InetSocketAddress(host, port);
        socketConnectCondition = socketStateLock.newCondition();
    }

    public void setSocketStateListener(SocketConnectionListener listener){
        this.listener = listener;
    }

    public void connect(){
        checkAndWaitForConnect();
        if(state == SocketState.CONNECTED_SUCCESS){
            return;
        }
        try {
            socketStateLock.lock();
            state = SocketState.CONNECTING;
            if(listener != null){
                listener.onSocketConnecting();
            }
            socket = SocketFactory.getDefault().createSocket();
            socket.connect(address);
            input = socket.getInputStream();
            output = socket.getOutputStream();
            if(packerReader == null || packetWriter == null){
                packetWriter = new PacketWriter();
                packerReader = new PackerReader();
            }
            packetWriter.init();
            packerReader.init();

            if(listener == null){
                listener.onSocketConnected();
            }
        } catch (IOException e) {
            e.printStackTrace();
            state = SocketState.CONNECTED_FAILED;

            if(listener != null){
                listener.onSocketConnectError();
            }
        }finally {
            socketConnectCondition.signalAll();
            socketStateLock.unlock();
        }
    }

    private void checkAndWaitForConnect(){
        socketStateLock.lock();
        while (true) {
            if (state != SocketState.CONNECTING) {
                break;
            }else{
                try {
                    socketConnectCondition.await();
                } catch (InterruptedException e) {
                }
            }
        }
        socketStateLock.unlock();
    }

    private boolean checkIfCanSendPacket(){
        boolean result = false;
        socketStateLock.lock();
        if(state == SocketState.CONNECTED_SUCCESS){
            result = true;
        }
        socketStateLock.unlock();
        return result;
    }


    public void sendPacket(NetData data){
        if(checkIfCanSendPacket()) {
            if (packetWriter != null) {
                packetWriter.sendPacket(data);
            }
        }else {
            if(listener != null){
                listener.onSendPacketError(state, data);
            }
        }
    }

    public void disconnect(boolean instant){
        socketStateLock.lock();
        if(state != SocketState.CONNECTED_SUCCESS) {
            socketStateLock.unlock();
            return;
        }
        if(packetWriter != null){
            packetWriter.shutdown(instant);
        }

        if(packerReader != null){
            packerReader.shutdown(instant);
        }

        try {
            socket.close();
        } catch (IOException e) {
        }
        if(packetWriter.isEndWithException() || packerReader.isEndWithException()){
            state = SocketState.DISCONNECT_ERROR;
            if(listener != null){
                listener.onSocketDisconnectByError();
            }
        }else{
            state = SocketState.DISCONNECT_NORMAL;
            if(listener != null){
                listener.onSocketDisconnectNormally();
            }
        }
        socketStateLock.unlock();

    }

    private void disconnectByError(){
        socketStateLock.lock();
        if(state != SocketState.CONNECTED_SUCCESS) {
            socketStateLock.unlock();
            return;
        }
        if(packetWriter != null){
            packetWriter.shutdown(true);
        }

        if(packerReader != null){
            packerReader.shutdown(true);
        }

        try {
            socket.close();
        } catch (IOException e) {
        }
        if(listener != null && state != SocketState.DISCONNECT_ERROR){
            state = SocketState.DISCONNECT_ERROR;
            listener.onSocketDisconnectByError();
        }
        socketStateLock.unlock();
    }

    private class PacketWriter{
        private final ArrayBlockingQueue<NetData> queue = new ArrayBlockingQueue(SOCKET_PACKET_QUEUE_SIZE,true);
        private volatile Long shutDownTime = null;
        private volatile boolean shutDownDone = false;
        private volatile boolean instantShutdown = false;
        private volatile boolean endWithException = false;
        private Lock shutDownLock = new ReentrantLock();
        private Condition shutDownCondition;

        private Thread writerThread;
        public PacketWriter(){
            shutDownCondition = shutDownLock.newCondition();
        }

        public void init(){
            shutDownTime = null;
            instantShutdown = false;
            shutDownDone = false;
            endWithException = false;

            writerThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    writePacket();
                }
            });
            writerThread.start();
        }

        protected void sendPacket(final NetData data) {
            try {
                queue.put(data);
            }catch (Exception e){
                if(listener != null){
                    singleSendErrorExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSendPacketError(state, data);
                        }
                    });
                }
            }
        }

        private void writePacket(){
            NetData packet = null;
            try {
                while(!done()){
                    try {
                        packet = queue.take();

                        byte[] datas = packet.getBytes();

                        output.write(datas);
                        output.flush();
                        packet = null;
                    } catch (InterruptedException e) {
                    }
                }

                //如果不立即中断则发送完剩下的消息
                if(!instantShutdown){
                    while( (packet = queue.poll()) != null){
                        byte[] datas = packet.getBytes();

                        output.write(datas);
                        output.flush();
                        packet = null;
                    }
                }
            } catch (IOException e) {
                shutDownTime = System.currentTimeMillis();
                final NetData errorPacket = packet;
                if(listener != null && errorPacket != null){
                    singleSendErrorExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSendPacketError(state, errorPacket);
                        }
                    });
                }
                endWithException = true;
            }

            shutDownLock.lock();
            shutDownDone = true;
            shutDownCondition.signalAll();
            shutDownLock.unlock();

            if(endWithException == true && !instantShutdown){
                disconnectByError();
            }
        }

        public boolean isEndWithException(){
            return endWithException;
        }

        private boolean done(){
            return shutDownTime != null;
        }

        private void shutdown(boolean instant){
            if(shutDownDone == true) return;
            shutDownLock.lock();
            instantShutdown = instant;
            shutDownTime = System.currentTimeMillis();
            writerThread.interrupt();
            try {
                if(shutDownDone == false)
                    shutDownCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shutDownLock.unlock();
        }
    }

    private class PackerReader{
        private final ArrayBlockingQueue<NetData> queue = new ArrayBlockingQueue(SOCKET_PACKET_QUEUE_SIZE,true);
        private volatile Long shutDownTime = null;
        private volatile boolean shutDownDone = false;
        private volatile boolean instantShutdown = false;
        private volatile boolean endWithException = false;
        private Lock shutDownLock = new ReentrantLock();
        private Condition shutDownCondition;

        private Thread readThread  = new Thread();
        public PackerReader(){
            shutDownCondition = shutDownLock.newCondition();
        }

        protected void init(){
            shutDownTime = null;
            instantShutdown = false;
            shutDownDone = false;
            endWithException = false;

            readThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    readPacket();
                }
            });
            readThread.start();
        }

        private void readPacket(){
            boolean endWithException = false;
            try{
                while (!done()){
                    NetData data = NetData.parseNetData(input);
                    dispatchPacket(data);
                }
            }catch (IOException e){
                shutDownTime = System.currentTimeMillis();
                endWithException = true;
            }

            shutDownLock.lock();
            shutDownDone = true;
            shutDownCondition.signalAll();
            shutDownLock.unlock();

            if(endWithException == true && !instantShutdown){
                disconnectByError();
            }
        }

        public boolean isEndWithException(){
            return endWithException;
        }

        private boolean done(){
            return shutDownTime == null;
        }

        private void shutdown(boolean instant){
            if(shutDownDone == true) return;
            shutDownLock.lock();
            shutDownTime = System.currentTimeMillis();
            instantShutdown = instant;
            if(instantShutdown) {
                readThread.interrupt();
            }
            try {
                if(shutDownDone == false)
                    shutDownCondition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shutDownLock.unlock();
        }
    }

    private void dispatchPacket(final NetData data){
        if(listener != null){
            singleDispatchExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    listener.onReceivePacket(data);
                }
            });
        }
    }

    public SocketState getSocketState(){
        return state;
    }

    public enum SocketState{
        CONNECTING,
        CONNECTED_SUCCESS,
        CONNECTED_FAILED,
        DISCONNECT_NORMAL,
        DISCONNECT_ERROR
    }

    public interface SocketConnectionListener {
        void onSocketConnected();
        void onSocketConnecting();
        void onSocketConnectError();
        void onSendPacketError(SocketState state, NetData failedData);
        void onSocketDisconnectByError();
        void onReceivePacket(NetData data);
        void onSocketDisconnectNormally();
    }


}
