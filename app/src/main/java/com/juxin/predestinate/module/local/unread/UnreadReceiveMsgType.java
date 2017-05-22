package com.juxin.predestinate.module.local.unread;

/**
 * 未读角标消息类型
 * Created by Kind on 16/4/8.
 */
public enum UnreadReceiveMsgType {

    /* 看过消息 */
    can_msgType(4),
    // 谁关注我消息类型
    follow_msgType(5),
    // 聊天红包等红包变更消息
    wallet_msgType(12),
    ;

    public long msgType;

    UnreadReceiveMsgType(long msgType) {
        this.msgType = msgType;
    }

    public static UnreadReceiveMsgType getUnreadReceiveMsgID(long msgType) {
        for (UnreadReceiveMsgType receiveMsgType : UnreadReceiveMsgType.values()) {
            if (receiveMsgType.msgType == msgType) {
                return receiveMsgType;
            }
        }
        return null;
    }
}