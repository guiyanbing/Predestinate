package com.juxin.predestinate.module.logic.config;

/**
 * 应用常量
 * Created by ZRP on 2016/9/19.
 */
public class Constant {

    public static final String NO_HOST = "no_host";
    public static final String HOST_URL = "http://www.baidu.com/";

    // --------------socket常量----------------
    public static final int MSG_ID_Login = 1;               //长连接登录消息id
    public static final int MSG_ID_KICK_Offline = 65533;    //异地登陆踢下线消息id
    public static final int MSG_ID_Heartbeat_Reply = 65532; //回送逻辑的心跳消息，客户端发送心跳，服务器回复该类型的消息，包体为0
    public static final int MSG_ID_Heartbeat = 65535;       //普通心跳消息id
    public static final long Heartbeat_Time = 60 * 1000;    //心跳时间间隔，60s
}
