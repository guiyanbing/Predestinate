package com.juxin.predestinate.bean.db.utils;

/**
 * 消息数量库
 * Created by Kind on 2017/3/28.
 */

public class DBConstant {
    public static final int ERROR = -1;

    public static final int NumNo = -1;// 数据库数字不修改

    public static final int OK_STATUS = 1;//发送成功
    public static final int FAIL_STATUS = 2;//发送失败
    public static final int SENDING_STATUS = 3;//发送中
    public static final int UNREAD_STATUS = 10;//未读
    public static final int READ_STATUS = 11;//对方已读
    public static final int LOCAL_READ_STATUS = 13;//本地已读
}
