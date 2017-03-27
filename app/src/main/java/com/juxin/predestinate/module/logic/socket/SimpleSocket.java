package com.juxin.predestinate.module.logic.socket;

import com.juxin.library.log.PLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Vector;

/**
 * 一个简易的Socket使用封装。不支持多线程，如果需要多线程处理，请自行维护。
 *
 * @author JohnsonLi
 * @version 1.0
 * @qq 505214658
 * @date 2015-04-15
 */
public class SimpleSocket {

    private Socket socket = null;   //长连接socket对象
    private String host = null;     //请求服务器地址
    private int port = -1;          //请求服务器端口

    private DataOutputStream out = null;
    private DataInputStream in = null;

    private SocketCallback callback = null;

    /**
     * 连接服务器。
     */
    public void connect() {
        new SocketThread().start();
    }

    /**
     * 断开服务器。
     */
    public void disconnect() {
        try {
            if (socket != null) {
                if (!socket.isInputShutdown()) socket.shutdownInput();
                if (!socket.isOutputShutdown()) socket.shutdownOutput();

                if (out != null) out.close();
                if (in != null) in.close();

                socket.close();
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
        } finally {
            out = null;
            in = null;
            socket = null;
        }
    }

    /**
     * 发送数据。
     */
    public void send(byte[] buffer) {
        if (writeRunnable != null) {
            writeRunnable.write(buffer);
        }
    }

    /**
     * 设置IP和端口。
     *
     * @param host 服务器地址。
     * @param port 端口。
     */
    public void setRemoteAddress(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 设置回调，监听网络消息及状态。
     *
     * @param callback 回调实例。
     */
    public void setCallback(SocketCallback callback) {
        this.callback = callback;
    }

    /**
     * 判定Socket是否连接状态。
     *
     * @return 连接状态返回true。
     */
    private boolean isConnected() {
        return socket != null && socket.isClosed() && socket.isConnected();
    }

    /**
     * 一个有效的Socket连接，需要保持连接状态且可以读写。
     *
     * @return 有效Socket返回true。
     */
    private boolean isValidSocket() {
        return socket != null
                && !socket.isClosed()
                && socket.isConnected()
                && !socket.isInputShutdown()
                && !socket.isOutputShutdown();
    }

    /**
     * 当成功建立连接时的回调。
     */
    private void onConnected() {
        if (callback != null) {
            callback.onConnected(this);
        }
    }

    /**
     * 当接收到服务器数据时回调。
     *
     * @param header 通讯的协议头。
     * @param buffer 字节数据。
     * @param length 字节数据长度。
     */
    private void onReceive(PSocketHeader header, byte[] buffer, int length) {
        if (callback != null) {
            callback.onReceive(this, header, buffer, length);
        }
    }

    /**
     * 当断开连接时或者无法和服务器正常通讯时的回调。
     */
    private void onDisconnect(int type) {
        if (callback != null) {
            callback.onDisconnect(this, type);
        }
    }

    private static final Vector<byte[]> datas = new Vector<byte[]>();
    private boolean isWrite = false;
    private boolean isRead = false;
    private int bufferSize = 2 * 1024 * 1024;

    private WriteRunnable writeRunnable = new WriteRunnable();//数据发送runnable
    private ReadRunnable readRunnable = new ReadRunnable();//数据接收runnable
    private Thread writeThread = new Thread(writeRunnable);//数据发送Thread
    private Thread readThread = new Thread(readRunnable);//数据接收Thread

    /**
     * 内部类，实现Socket的连接。
     */
    private class SocketThread extends Thread {

        @Override
        public void run() {
            super.run();
            try {
                SocketAddress socketAddress = new InetSocketAddress(host, port);

                socket = new Socket();
                socket.connect(socketAddress);

                if (isValidSocket()) {
                    onConnected();

                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());

                    isWrite = true;
                    isRead = true;

                    writeThread.start();
                    readThread.start();
                } else {
                    onDisconnect(1);
                }
            } catch (Exception e) {
                PLogger.printThrowable(e);
                onDisconnect(1);
            }
        }
    }

    /**
     * 内部类，实现数据的发送。
     */
    private class WriteRunnable implements Runnable {

        @Override
        public void run() {
            sendLoop();
        }

        private synchronized void sendLoop() {
            while (isWrite) {
                if (datas.size() <= 0) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        continue;
                    }
                }

                while (datas.size() > 0) {
                    byte[] buffer = datas.remove(0);
                    if (isWrite && out != null && isValidSocket()) {
                        try {
                            out.write(buffer);
                            out.flush();
                            continue;
                        } catch (IOException e) {
                            PLogger.printThrowable(e);
                        }
                    }
                    if (isWrite) {
                        isRead = false;
                        isWrite = false;
                        onDisconnect(2);
                    }
                    return;
                }
            }
        }

        /**
         * 将需要发送的数据添加到发送队列。
         *
         * @param buffer 发送数据。
         */
        public synchronized void write(byte[] buffer) {
            datas.add(buffer);
            this.notify();
        }

        /**
         * 停止发送线程。
         */
        public synchronized void stop() {
            isWrite = false;
            this.notify();
        }
    }

    /**
     * 内部类，实现数据的接收。
     */
    private class ReadRunnable implements Runnable {

        @Override
        public void run() {
            readLoop();
        }

        private synchronized void readLoop() {
            byte[] buffer = new byte[bufferSize];
            int len = 0;

            PSocketHeader header = new PSocketHeader();
            int readStart = 0;
            int readSize = header.getHeaderSize();

            try {
                while ((in != null && (len = in.read(buffer, readStart, readSize)) != -1)) {
                    if (header.isHeader()) {
                        header.setBuffer(buffer);

                        if (header.getLength() != 0) {
                            header.toggle();
                            readSize = header.getSize();
                            continue;
                        }
                    } else {
                        // 防止一次没有读完
                        if (len == readSize) {
                            header.toggle();
                            readStart = 0;
                            readSize = header.getSize();
                        } else {
                            readStart += len;
                            readSize = header.getSize() - readStart;
                            continue;
                        }
                    }
                    onReceive(header, buffer, header.getLength());
                }
            } catch (Exception e) {
                PLogger.printThrowable(e);
            }

            if (isRead) {
                isRead = false;
                isWrite = false;
                onDisconnect(3);
            }

            if (writeRunnable != null) {
                writeRunnable.stop();
            }
        }
    }
}
