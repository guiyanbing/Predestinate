package com.juxin.predestinate.module.logic.socket;

/**
 * socket长连接中一些常用的常量
 */
public class TCPConstant {

    // socket测试服连接地址
    public static final String HOST = "123.59.187.33";      //socket连接IP地址
    public static final int PORT = 8823;                    //socket连接端口

    // --------------socket常量----------------
    public static final int MSG_ID_Login = 1;               //长连接登录消息id
    public static final int MSG_ID_SERVER_RECEIVE = 997;    //通知客户端某条消息已经收到
    public static final int MSG_ID_KICK_Offline = 998;      //异地登陆踢下线消息id
    public static final int MSG_ID_Heartbeat_Reply = 999;   //回送逻辑的心跳消息，客户端发送心跳，服务器回复该类型的消息，包体为0
    public static final int MSG_ID_BeforeLogin = 65535;     //登录之前发送的消息id
    public static final int MSG_ID_Heartbeat = 65532;       //心跳包消息id
    public static final long Heartbeat_Time = 60 * 1000;    //心跳时间间隔，60s
    public static final int MSG_MS = 11;                   //消息版本号(MS) 视频通话 + 表情：11

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

    public static final int SOCKET_CONNECT_TIMEOUT = 0;             //Socket连接时超时时间
    public static final int SOCKET_AUTO_CONNECT_Increment_Time = 5000;  //Socket自动重连时间间隔
    public static final int SOCKET_CONNECT_Heartbeat_Time = 60 * 1000;  //连接服务器的心跳间隔
}
