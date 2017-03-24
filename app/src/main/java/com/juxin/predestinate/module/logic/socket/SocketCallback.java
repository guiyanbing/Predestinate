package com.juxin.predestinate.module.logic.socket;

/**
 * socket状态回调
 * Created by ZRP on 2017/3/23.
 */
public interface SocketCallback {

    /**
     * 当成功建立连接时的回调
     *
     * @param socket 当前PSocket的实例
     */
    void onConnected(PSocket socket);

    /**
     * 当接收到服务器数据时回调。
     *
     * @param socket 当前PSocket的实例
     * @param header 通讯的协议头
     * @param buffer 字节数据
     */
    void onReceive(PSocket socket, PSocketHeader header, byte[] buffer, int length);

    /**
     * 当断开连接时或者无法和服务器正常通讯时的回调
     *
     * @param socket 当前PSocket的实例
     * @param type   断开连接原因：1-连接服务器失败，2-发送数据超时，3-服务器主动断开连接
     */
    void onDisconnect(PSocket socket, int type);
}
