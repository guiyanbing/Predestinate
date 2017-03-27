package com.juxin.predestinate.module.logic.socket;

/**
 * socket长连接中一些常用的常量
 */
public class TCPConstant {

    /**
     * 即时通许中，CoreService连接服务器时，返回的几种状态。
     */
    public static final int SOCKET_STATUS_Login_Success = 0;    //登录成功
    public static final int SOCKET_STATUS_Login_Fail = 1;       //登录失败
    public static final int SOCKET_STATUS_Connected = 2;        //连接成功
    public static final int SOCKET_STATUS_Disconnect = 3;       //断开连接

    /**
     * [消息体长度（4Bytes）][客户ID（4Byte）][消息类型 (2Bytes)][内容]
     */
    public static final int TCP_DATA_Header_Size = 4 + 4 + 2;

    public static final int SOCKET_AUTO_CONNECT_Increment_Time = 5000;  //Socket自动重连时间间隔
    public static final int SOCKET_CONNECT_Heartbeat_Time = 60 * 1000;  //连接服务器的心跳间隔
}
