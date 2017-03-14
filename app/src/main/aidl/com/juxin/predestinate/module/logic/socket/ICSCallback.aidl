// ICSCallback.aidl
package com.juxin.predestinate.module.logic.socket;

// Declare any non-default types here with import statements

interface ICSCallback {

    // 处理消息
    void onMessage(long msgId, boolean group, String groupId, long sender, String contents);

    // 消息反馈：消息id，消息类型，hostIP；如果msgId=-1，msgType=-1，表示为重连失败统计，否则为已读消息反馈
    void onFeedback(long msgId, int msgType, String host);

    // 即时通讯状态
    // type: 0 登录成功；1 登录失败；2 连接成功；3 断开连接；4 获取Key成功；5 获取Key失败。
    // subType: 子类型
    // msg: 提示消息
    void onStatusChange(int type, int subType, String msg);

    //帐号无效
    void accountInvalid(int reason);

    //心跳状态回送，判断socket连接状态
    void heartbeatStatus(boolean isBeating);
}
