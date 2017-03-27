package com.juxin.predestinate.module.logic.socket;

import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.observe.Msg;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.RxBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Vector;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 简单的socket封装
 * Created by ZRP on 2017/3/21.
 */
public class PSocket {

    private static final int SOCKET_TIMEOUT = 1000;
    private Socket socket;
    private static InputStream inputStream;
    private static OutputStream outputStream;

    private PSocket() {
        RxBus.getInstance().toFlowable(Msg.class)
                .onBackpressureBuffer().subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new Consumer<Msg>() {
                    @Override
                    public void accept(Msg msg) throws Exception {
                        PLogger.d("---PSocket--->connect：" + msg.toString());
                        switch (msg.getKey()) {
                            case MsgType.MT_Socket_Start_Connect:// 监听登录
                                connect();
                                break;
                            case MsgType.MT_Socket_Read_Content:// 监听数据接收
                                readContent();
                                break;
                            case MsgType.MT_Socket_Write_Content:// 监听数据发送
                                writeContent();
                                break;
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        PLogger.printThrowable(throwable);
                    }
                });
    }

    private static class SingletonHolder {
        public static PSocket instance = new PSocket();
    }

    public static PSocket getInstance() {
        return SingletonHolder.instance;
    }

    // --------------------------------------------

    private String host = "";
    private int port = -1;
    private SocketCallback socketCallback;

    public void setSocketCallback(SocketCallback socketCallback) {
        this.socketCallback = socketCallback;
    }

    /**
     * 发起连接
     *
     * @param host tcp host
     * @param port tcp port
     */
    public void connect(String host, int port) {
        this.host = host;
        this.port = port;
        if (TextUtils.isEmpty(host) || port == -1) return;
        RxBus.getInstance().post(new Msg(MsgType.MT_Socket_Start_Connect, null));
    }

    /**
     * 断开socket长连接
     */
    public void disconnect() {
        try {
            if (socket != null) {
                if (!socket.isInputShutdown()) socket.shutdownInput();
                if (!socket.isOutputShutdown()) socket.shutdownOutput();

                if (outputStream != null) outputStream.close();
                if (inputStream != null) inputStream.close();

                socket.close();
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
        } finally {
            outputStream = null;
            inputStream = null;
            socket = null;
        }
    }

    /**
     * 发送socket数据
     *
     * @param buffer 数据buffer
     */
    public void send(byte[] buffer) {
        datas.add(buffer);
        RxBus.getInstance().post(new Msg(MsgType.MT_Socket_Write_Content, null));
    }

    /**
     * socket连接服务端
     */
    private void connect() {
        try {
            SocketAddress socketAddress = new InetSocketAddress(host, port);

            socket = new Socket();
            socket.connect(socketAddress, SOCKET_TIMEOUT);

            if (isValidSocket()) {
//                if (socketCallback != null) socketCallback.onConnected(this);

                outputStream = new DataOutputStream(socket.getOutputStream());
                inputStream = new DataInputStream(socket.getInputStream());

                isWrite = true;
                isRead = true;

                RxBus.getInstance().post(new Msg(MsgType.MT_Socket_Read_Content, null));
                RxBus.getInstance().post(new Msg(MsgType.MT_Socket_Write_Content, null));
            } else {
//                if (socketCallback != null) socketCallback.onDisconnect(this, 1);
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
//            if (socketCallback != null) socketCallback.onDisconnect(this, 1);
        }
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

    private static final Vector<byte[]> datas = new Vector<byte[]>();
    private boolean isWrite = false;
    private boolean isRead = false;
    private int bufferSize = 2 * 1024 * 1024;

    /**
     * 数据发送，一直处于等待状态
     */
    private void writeContent() {
        PLogger.d("---PSocket--->writeContent：" + outputStream);
        while (isWrite) {
            if (datas.isEmpty()) continue;

            while (datas.size() > 0) {
                byte[] buffer = datas.remove(0);
                if (isWrite && outputStream != null && isValidSocket()) {
                    try {
                        outputStream.write(buffer);
                        outputStream.flush();
                        continue;//写入完成继续进入等待状态
                    } catch (IOException e) {
                        PLogger.printThrowable(e);
                    }
                }

                if (isWrite) {//写入失败就断开socket
                    isRead = false;
                    isWrite = false;
//                    if (socketCallback != null) socketCallback.onDisconnect(this, 2);
                }
                return;
            }
        }
    }

    /**
     * 数据接收
     */
    private void readContent() {
        PLogger.d("---PSocket--->readContent：" + inputStream);

        byte[] buffer = new byte[bufferSize];
        int len = 0;

        PSocketHeader header = new PSocketHeader();
        int readStart = 0;
        int readSize = header.getHeaderSize();

        try {
            while ((inputStream != null && (len = inputStream.read(buffer, readStart, readSize)) != -1)) {
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
//                if (socketCallback != null)
//                    socketCallback.onReceive(this, header, buffer, header.getLength());
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }

        if (isRead) {
            isRead = false;
            isWrite = false;
//            if (socketCallback != null) socketCallback.onDisconnect(this, 3);
        }
        isWrite = false;
    }
}
