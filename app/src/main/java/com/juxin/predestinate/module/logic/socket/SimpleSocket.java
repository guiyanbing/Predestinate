package com.juxin.predestinate.module.logic.socket;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.module.util.ByteUtil;

import java.io.ByteArrayInputStream;
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
    private int timeOut = 0;        //长连接请求超时时长

    private DataOutputStream out = null;
    private DataInputStream in = null;

    private SocketStatus status = SocketStatus.SS_Init;
    private SocketCallback callback = null;

    /**
     * 连接服务器。
     */
    public void connect() {
        if (SocketStatus.SS_Init != status) {
            return;
        }
        status = SocketStatus.SS_Connect;
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
        if (socket == null) {
            return false;
        }

        return (!socket.isClosed() && socket.isConnected());
    }

    /**
     * 一个有效的Socket连接，需要保持连接状态且可以读写。
     *
     * @return 有效Socket返回true。
     */
    private boolean isValidSocket() {
        if (socket == null) {
            return false;
        }

        return (!socket.isClosed() && socket.isConnected() && !socket.isInputShutdown() && !socket.isOutputShutdown());
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
    private void onReceive(SimpleSocketHeader header, byte[] buffer, int length) {
        if (callback != null) {
            callback.onReceive(this, header, buffer, length);
        }
    }

    /**
     * 当断开连接时或者无法和服务器正常通讯时的回调。
     */
    private void onDisconnect(SocketCloseType type) {
        if (callback != null) {
            callback.onDisconnect(this, type);
        }
    }

    /**
     * 获取网络数据的回调类。<br>
     * 其中的一个参数id，就是当前的SimpleSocket的实例。和使用者保存的
     * 实例作对比，就可以知道是否需要处理当前的信息。当回调被复用时很有用。
     */
    public interface SocketCallback {
        /**
         * 当成功建立连接时的回调。
         *
         * @param id 当前SimpleSocket的实例。
         */
        void onConnected(SimpleSocket id);

        /**
         * 当接收到服务器数据时回调。
         *
         * @param id     当前SimpleSocket的实例。
         * @param header 通讯的协议头。
         * @param buffer 字节数据。
         */
        void onReceive(SimpleSocket id, SimpleSocketHeader header, byte[] buffer, int length);

        /**
         * 当断开连接时或者无法和服务器正常通讯时的回调。
         *
         * @param id 当前SimpleSocket的实例。
         */
        void onDisconnect(SimpleSocket id, SocketCloseType type);
    }

    /**
     * socket关闭原因
     */
    public enum SocketCloseType {
        /**
         * 连接服务器失败。
         */
        SCT_Connect,

        /**
         * 发送数据超时。
         */
        SCT_SendTimeOut,

        /**
         * 服务器主动关闭。
         */
        SCT_ServerClose
    }

    /**
     * socket连接状态
     */
    private enum SocketStatus {
        /**
         * 对象刚建立还没有建立连接。
         */
        SS_Init,

        /**
         * 建立连接中。
         */
        SS_Connect,

        /**
         * 已经连接上。
         */
        SS_Connected,

        /**
         * 断开连接中。
         */
        SS_Disconnect,

        /**
         * 已经断开连接。
         */
        SS_Disconnected
    }

    private static final Vector<byte[]> datas = new Vector<byte[]>();
    private boolean isWrite = false;
    private boolean isRead = false;
    private int bufferSize = 2 * 1024 * 1024;
    private WriteRunnable writeRunnable = new WriteRunnable();//数据发送runnable
    private ReadRunnable readRunnable = new ReadRunnable();//数据接收runnable

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
                socket.connect(socketAddress, timeOut);

                if (isValidSocket()) {
                    onConnected();

                    out = new DataOutputStream(socket.getOutputStream());
                    in = new DataInputStream(socket.getInputStream());

                    isWrite = true;
                    isRead = true;

                    new Thread(writeRunnable).start();
                    new Thread(readRunnable).start();
                } else {
                    onDisconnect(SocketCloseType.SCT_Connect);
                }
            } catch (Exception e) {
                PLogger.printThrowable(e);
                onDisconnect(SocketCloseType.SCT_Connect);
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
                        onDisconnect(SocketCloseType.SCT_SendTimeOut);
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

            SimpleSocketHeader header = new SimpleSocketHeader();
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

//                    PLogger.autoDebug("header len: " + header.getLength() + "; len: " + len);
                    onReceive(header, buffer, header.getLength());
                }
            } catch (Exception e) {
                PLogger.printThrowable(e);
            }

            if (isRead) {
                isRead = false;
                isWrite = false;
                onDisconnect(SocketCloseType.SCT_ServerClose);
            }

            if (writeRunnable != null) {
                writeRunnable.stop();
            }
        }
    }

    /**
     * 自定义的socket消息头
     */
    public class SimpleSocketHeader {

        private boolean isHeader = true;

        public long msdId = 0;              //消息ID
        public long sender = 0;             //消息发送者ID
        //以上两个参数，爱爱版本无用

        public long userId = 0;             //客户id，爱爱中消息头字段
        public long length = 0;             //消息长度
        public int type = 0;                //消息类型

        /**
         * 设置消息头信息
         *
         * @param buffer 传入的字节数组
         */
        public void setBuffer(byte[] buffer) {
            if (buffer == null) {
                return;
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, ServiceConstant.CHAT_TCP_DATA_Header_Size);
            byte[] b = new byte[4];

            bais.read(b, 0, 4);
            this.length = ByteUtil.toUnsignedInt(b);
            bais.read(b, 0, 4);
            this.userId = ByteUtil.toUnsignedInt(b);
//            bais.read(b, 0, 4);
//            this.msdId = ByteUtil.toLong(b);
//            bais.read(b, 0, 4);
//            this.sender = ByteUtil.toUnsignedInt(b);
            bais.read(b, 0, 2);
            this.type = ByteUtil.toUnsignedShort(b);

            try {
                bais.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (this.length > bufferSize) {
                this.length = bufferSize;
            }
        }

        public boolean isHeader() {
            return isHeader;
        }

        public int getHeaderSize() {
            return ServiceConstant.CHAT_TCP_DATA_Header_Size;
        }

        public int getSize() {
            if (isHeader) {
                return getHeaderSize();
            } else {
                return (int) length;
            }
        }

        public int getLength() {
            return (int) length;
        }

        private void toggle() {
            isHeader = !isHeader;
        }

        @Override
        public String toString() {
            return "SimpleSocketHeader{" +
                    "isHeader=" + isHeader +
                    ", userId=" + userId +
                    ", length=" + length +
                    ", type=" + type +
                    '}';
        }
    }
}
