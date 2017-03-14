package com.juxin.predestinate.module.logic.socket;

/**
 * 管理Service中一些常用的常量。
 *
 * @author JohnsonLi
 * @version 1.0
 * @qq 505214658
 * @date 2015-04-16
 */
public class ServiceConstant {

    /**
     * 即时通许中，CoreService连接服务器时，返回的几种状态。
     */
    public static final int CHATSERVIE_STATUS_Login_Suc     = 0;    //登录成功
    public static final int CHATSERVIE_STATUS_Login_Fail    = 1;    //登录失败
    public static final int CHATSERVIE_STATUS_Connect       = 2;    //连接成功
    public static final int CHATSERVIE_STATUS_Disconnect    = 3;    //断开连接
    public static final int CHATSERVIE_STATUS_GetKey_Suc    = 4;    //获取Key成功
    public static final int CHATSERVIE_STATUS_GetKey_Fail   = 5;    //获取Key失败

    public static final int CHATSERVIE_STATUS_SUBTYPE_Default           = 0;
    public static final int CHATSERVIE_STATUS_SUBTYPE_GetKey_Invalid    = 1;

    /**
     * [消息体长度（4Bytes）][客户ID（4Byte）][消息类型 (2Bytes)][内容]
     */
    public static final int CHAT_TCP_DATA_Header_Size       = 4 + 4 + 2;

    /**
     * Socket自动重连时间间隔。
     */
    public static final int SOCKET_AUTO_CONNECT_Increment_Time  = 5000;

    /**
     * 连接服务器的心跳间隔。
     */
    public static final int SOCKET_CONNECT_Hearbeat_Time        = 60 * 1000;
}
