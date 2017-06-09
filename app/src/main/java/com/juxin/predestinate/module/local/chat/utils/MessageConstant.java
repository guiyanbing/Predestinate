package com.juxin.predestinate.module.local.chat.utils;

/**
 * 消息常量类
 * Created by Kind on 2017/5/24.
 */

public class MessageConstant {

    public static int KF_ID = 1;//机器人ID

    public static int Ru_Friend = 1;//1则为熟人消息，否则为0
    public static int Ru_Stranger = 0;//1则为熟人消息，否则为0


    //数据来源 1.本地  2.网络  3.离线(默认是本地) 4.模拟
    public static int ONE = 1;
    public static int TWO = 2;
    public static int THREE = 3;
    public static int FOUR = 4;

    public final static int NumDefault = 0;//数字默认值
    public final static String StrDefault = "";//字符默认值

    //显示权重
    public static int Max_Weight = 1000;//最大权重
    public static int Great_Weight = 100;//大权重
    public static int In_Weight = 50;//中等权重
    public static int Small_Weight = 1;//小权重



    public static final int ERROR = -1; //失败
    public static final int OK = 1;//成功

    public static final int NumNo = -1;// 数据库数字不修改

    public static final int OK_STATUS = 1;//发送成功
    public static final int FAIL_STATUS = 2;//发送失败
    public static final int SENDING_STATUS = 3;//发送中
    public static final int UNREAD_STATUS = 10;//未读
    public static final int READ_STATUS = 11;//对方已读


    public static final String Stranger_New = "Stranger_New"; //默认的人
}
