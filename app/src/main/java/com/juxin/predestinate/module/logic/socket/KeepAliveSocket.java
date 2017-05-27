package com.juxin.predestinate.module.logic.socket;

import com.juxin.library.log.PLogger;

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
 * socket通信内核
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

    public KeepAliveSocket(String host, int port) {
        address = new InetSocketAddress(host, port);
        socketConnectCondition = socketStateLock.newCondition();
    }

    public void setSocketStateListener(SocketConnectionListener listener) {
        this.listener = listener;
    }

    public void connect() {
        checkAndWaitForConnect();
        if (state == SocketState.CONNECTED_SUCCESS) {
            PLogger.d("Socket already connected");
            return;
        }
        try {
            socketStateLock.lock();
            PLogger.d("Socket start connect");
            state = SocketState.CONNECTING;
            if (listener != null) {
                listener.onSocketConnecting();
            }
            socket = SocketFactory.getDefault().createSocket();
            socket.connect(address, TCPConstant.SOCKET_CONNECT_TIMEOUT);
            input = socket.getInputStream();
            output = socket.getOutputStream();
            if (packerReader == null || packetWriter == null) {
                packetWriter = new PacketWriter();
                packerReader = new PackerReader();
            }
            packetWriter.init();
            packerReader.init();
            state = SocketState.CONNECTED_SUCCESS;
            PLogger.d("Socket connect success");
            if (listener != null) {
                listener.onSocketConnected();
            }
        } catch (IOException e) {
            e.printStackTrace();
            state = SocketState.CONNECTED_FAILED;

            PLogger.e("Socket connect failed: " + e.getMessage());
            if (listener != null) {
                listener.onSocketConnectError();
            }
        } finally {
            socketConnectCondition.signalAll();
            socketStateLock.unlock();
        }
    }

    private void checkAndWaitForConnect() {
        socketStateLock.lock();
        while (true) {
            if (state != SocketState.CONNECTING) {
                break;
            } else {
                try {
                    PLogger.d("Socket Connect Wait for Connecting start");
                    socketConnectCondition.await();
                    PLogger.d("Socket Connect Wait for Connecting end");
                } catch (InterruptedException e) {
                }
            }
        }
        socketStateLock.unlock();
    }

    private boolean checkIfCanSendPacket() {
        boolean result = false;
        socketStateLock.lock();
        if (state == SocketState.CONNECTED_SUCCESS) {
            result = true;
        }
        socketStateLock.unlock();
        return result;
    }


    public void sendPacket(NetData data) {
        if (checkIfCanSendPacket()) {
            if (packetWriter != null) {
                packetWriter.sendPacket(data);
            }
        } else {
            PLogger.d("Socket send packet error - sate:" + state.name() + ", Data:" + data.toString());
            if (listener != null) {
                listener.onSendPacketError(state, data);
            }
        }
    }

    public void disconnect(boolean instant) {
        if(state == SocketState.CONNECTING){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        socketStateLock.lock();
        if (state != SocketState.CONNECTED_SUCCESS) {
            socketStateLock.unlock();
            return;
        }
        PLogger.d("Socket disconnect by instant " + instant + " start");
        if (packetWriter != null) {
            packetWriter.shutdown(instant);
        }

        if (packerReader != null) {
            packerReader.shutdown(instant);
        }

        try {
            socket.close();
        } catch (IOException e) {
        }
        if (packetWriter.isEndWithException() || packerReader.isEndWithException()) {
            state = SocketState.DISCONNECT_ERROR;
            PLogger.d("Socket disconnect with error");
            if (listener != null) {
                listener.onSocketDisconnectByError();
            }
        } else {
            state = SocketState.DISCONNECT_NORMAL;
            PLogger.d("Socket disconnect normally");
            if (listener != null) {
                listener.onSocketDisconnectNormally();
            }
        }
        PLogger.d("Socket disconnect by instant " + instant + " end");
        socketStateLock.unlock();

    }

    private void disconnectByError() {
        socketStateLock.lock();
        if (state != SocketState.CONNECTED_SUCCESS) {
            socketStateLock.unlock();
            return;
        }
        PLogger.d("Socket disconnect by error start");
        if (packetWriter != null) {
            packetWriter.shutdown(true);
        }

        if (packerReader != null) {
            packerReader.shutdown(true);
        }

        try {
            socket.close();
        } catch (IOException e) {
        }
        PLogger.d("Socket disconnect by error end");
        if (listener != null && state != SocketState.DISCONNECT_ERROR) {
            state = SocketState.DISCONNECT_ERROR;
            listener.onSocketDisconnectByError();
        }
        socketStateLock.unlock();
    }

    private class PacketWriter {
        private final ArrayBlockingQueue<NetData> queue = new ArrayBlockingQueue(SOCKET_PACKET_QUEUE_SIZE, true);
        private volatile Long shutDownTime = null;
        private volatile boolean shutDownDone = false;
        private volatile boolean instantShutdown = false;
        private volatile boolean endWithException = false;
        private Lock shutDownLock = new ReentrantLock();
        private Condition shutDownCondition;

        private Thread writerThread;

        public PacketWriter() {
            shutDownCondition = shutDownLock.newCondition();
        }

        public void init() {
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
            PLogger.d("Socket send packet thread init and start");
        }

        protected void sendPacket(final NetData data) {
            try {
                queue.put(data);
            } catch (Exception e) {
                if (listener != null) {
                    singleSendErrorExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            listener.onSendPacketError(state, data);
                        }
                    });
                }
                PLogger.d("Socket send packet add to queue error:" + e.getMessage());
            }
        }

        private void writePacket() {
            NetData packet = null;
            try {
                while (!done()) {
                    try {
                        packet = queue.take();
                        PLogger.d("Socket send packet :" + packet.toString());

                        byte[] datas = packet.getBytes();

                        output.write(datas);
                        output.flush();
                        packet = null;
                    } catch (InterruptedException e) {
                        PLogger.d("Socket send packet ,take packet from queue interrupt");
                    }
                }

                PLogger.d("Socket send packet thread ready to end ,instant:" + instantShutdown);
                //如果不立即中断则发送完剩下的消息
                if (!instantShutdown) {
                    while ((packet = queue.poll()) != null) {
                        PLogger.d("Socket send left packet :" + packet.toString());
                        byte[] datas = packet.getBytes();

                        output.write(datas);
                        output.flush();
                        packet = null;
                    }
                }
            } catch (IOException e) {
                shutDownTime = System.currentTimeMillis();
                final NetData errorPacket = packet;
                PLogger.d("Socket send packet error:" + e.getMessage() + ", packet:" + packet != null ? packet.toString() : "none");
                if (listener != null && errorPacket != null) {
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

            if (endWithException == true && !instantShutdown) {
                disconnectByError();
            }

            PLogger.d("Socket send packet thread shutdown done ,instant:" + instantShutdown + ", with exception:" + endWithException);
        }

        public boolean isEndWithException() {
            return endWithException;
        }

        private boolean done() {
            return shutDownTime != null;
        }

        private void shutdown(boolean instant) {
            if (shutDownDone == true) return;
            PLogger.d("Socket send packet thread shutdown instant:" + instant + " start");
            shutDownLock.lock();
            instantShutdown = instant;
            shutDownTime = System.currentTimeMillis();
            writerThread.interrupt();
            try {
                if (shutDownDone == false) {
                    PLogger.d("Socket send packet thread shutdown wait for done start");
                    shutDownCondition.await();
                    PLogger.d("Socket send packet thread shutdown wait for done end");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            PLogger.d("Socket send packet thread shutdown instant:" + instant + " end");
            shutDownLock.unlock();
        }
    }

    private class PackerReader {
        private final ArrayBlockingQueue<NetData> queue = new ArrayBlockingQueue(SOCKET_PACKET_QUEUE_SIZE, true);
        private volatile Long shutDownTime = null;
        private volatile boolean shutDownDone = false;
        private volatile boolean instantShutdown = false;
        private volatile boolean endWithException = false;
        private Lock shutDownLock = new ReentrantLock();
        private Condition shutDownCondition;

        private Thread readThread = new Thread();

        public PackerReader() {
            shutDownCondition = shutDownLock.newCondition();
        }

        protected void init() {
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
            PLogger.d("Socket read packet thread init and start");
        }

        private void readPacket() {
            while (!done()) {
                try {

                    PLogger.d("Socket read packet thread start read");
                    NetData data = NetData.parseNetData(input);
                    if (data != null) {
                        PLogger.d("Socket read packet dispatch packet:" + data.toString());
                        dispatchPacket(data);
                    } else {
                        PLogger.d("Socket read packet dispatch packet null");
                    }
                } catch (IOException e) {
                    if (shutDownTime == null) {
                        endWithException = true;
                        shutDownTime = System.currentTimeMillis();
                        PLogger.d("Socket read packet error:" + e.getMessage());
                    }
                    break;
                }
            }

            shutDownLock.lock();
            shutDownDone = true;
            shutDownCondition.signalAll();
            shutDownLock.unlock();

            if (endWithException == true && !instantShutdown) {
                disconnectByError();
            }

            PLogger.d("Socket read packet thread shutdown done ,instant:" + instantShutdown + ", with exception:" + endWithException);
        }

        public boolean isEndWithException() {
            return endWithException;
        }

        private boolean done() {
            return shutDownTime != null;
        }

        private void shutdown(boolean instant) {
            if (shutDownDone == true) return;
            PLogger.d("Socket read packet thread shutdown instant:" + instant + " start");
            shutDownLock.lock();
            shutDownTime = System.currentTimeMillis();
            instantShutdown = instant;
            try {
                input.close();
                if (shutDownDone == false) {
                    PLogger.d("Socket read packet thread shutdown wait for done start");
                    shutDownCondition.await();
                    PLogger.d("Socket read packet thread shutdown wait for done end");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PLogger.d("Socket read packet thread shutdown instant:" + instant + " end");
            shutDownLock.unlock();
        }
    }

    private void dispatchPacket(final NetData data) {
        if (listener != null) {
            singleDispatchExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    listener.onReceivePacket(data);
                }
            });
        }
    }

    public SocketState getSocketState() {
        return state;
    }

    public enum SocketState {
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
