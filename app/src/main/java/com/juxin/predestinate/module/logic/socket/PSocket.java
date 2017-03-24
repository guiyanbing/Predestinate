package com.juxin.predestinate.module.logic.socket;

import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.observe.Msg;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.RxBus;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import io.reactivex.android.schedulers.AndroidSchedulers;
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
        // 监听登录
        RxBus.getInstance().toFlowable(Msg.class)
                .onBackpressureBuffer().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Msg>() {
                    @Override
                    public void accept(Msg msg) throws Exception {
                        if (MsgType.MT_Socket_Start_Connect.equals(msg.getKey())) connect();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        PLogger.printThrowable(throwable);
                    }
                });
        // 监听写入
        RxBus.getInstance().toFlowable(InputStream.class)
                .onBackpressureBuffer().subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<InputStream>() {
                    @Override
                    public void accept(InputStream inputStream) throws Exception {
                        sendContent(inputStream);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        PLogger.printThrowable(throwable);
                    }
                });
        // 监听读取
        RxBus.getInstance().toFlowable(OutputStream.class)
                .onBackpressureBuffer().subscribeOn(Schedulers.newThread())
                .subscribe(new Consumer<OutputStream>() {
                    @Override
                    public void accept(OutputStream outputStream) throws Exception {
                        readContent(outputStream);
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
    public void disConnect() {
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
     * socket连接服务端
     */
    private void connect() {
        try {
            SocketAddress socketAddress = new InetSocketAddress(host, port);

            socket = new Socket();
            socket.connect(socketAddress, SOCKET_TIMEOUT);

            if (isValidSocket()) {
                if (socketCallback != null) socketCallback.onConnected(this);

                outputStream = new DataOutputStream(socket.getOutputStream());
                inputStream = new DataInputStream(socket.getInputStream());

                RxBus.getInstance().post(outputStream);
                RxBus.getInstance().post(inputStream);
            } else {
                if (socketCallback != null) socketCallback.onDisconnect(this, 1);
            }
        } catch (Exception e) {
            PLogger.printThrowable(e);
            if (socketCallback != null) socketCallback.onDisconnect(this, 1);
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

    /**
     * 读取服务器socket数据
     *
     * @param outputStream 输出流
     */
    private void readContent(OutputStream outputStream) {

    }

    /**
     * 发送客户端socket数据
     *
     * @param inputStream 写入流
     */
    private void sendContent(InputStream inputStream) {

    }
}
