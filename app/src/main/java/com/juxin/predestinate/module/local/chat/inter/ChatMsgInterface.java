package com.juxin.predestinate.module.local.chat.inter;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;

import java.util.List;

/**
 * 聊天消息的接口
 * Created by Kind on 2016/11/24.
 */

public class ChatMsgInterface {

    /**
     * 监听事件的回调接口。
     */
    public interface ChatMsgListener {

        /**
         * 最近聊天内容
         *
         * @param ret
         * @param baseMessages
         */
        void onChatRecently(boolean ret, List<BaseMessage> baseMessages);

        /**
         * 分页加载历史数据
         *
         * @param ret
         * @param baseMessages
         */
        void onChatHistory(boolean ret, List<BaseMessage> baseMessages);

        /**
         * 更新数据
         *
         * @param ret
         * @param message
         */
        void onChatUpdate(boolean ret, BaseMessage message);
    }

    /**
     * 查询单个用户个人资料
     */
    public interface InfoComplete {
        void onReqComplete(boolean ret, UserInfoLightweight infoLightweight);
    }

    /**
     * 批量查询单个用户个人资料
     */
    public interface InfoListComplete {
        void onReqInfosComplete(List<UserInfoLightweight> infoLightweights);
    }

    /**
     * 活动消息返回列表
     */
    public interface ActivityDataListener {
        void onDataListener(boolean ret, List<BaseMessage> messages);
    }


    /**
     * 删除列表中的某个用户
     */
    public interface DelChatUserComplete {//接口
        void onReqChatUser(boolean ret);
    }

    public interface MsgListener {
        /**
         * 单个消息回调
         * @param messages 返回的数据 如果是空就是没有数据
         */
        void onListener(BaseMessage messages);
    }

    public interface DMarkListener {
        /**
         *
         * @param ret 请求是否成功
         * @param isUsed 是否已使用，true可以使用，false不能使用
         */
        void onDataListener(boolean ret, boolean isUsed); //数据库查询回调
    }

    /**
     * 黑名单上传聊天记录
     */
    public interface OnReportListener {
        void onReportJson(String json);
    }

    /**
     * 系统消息推送
     */
    public interface SystemMsgListener {
        void onSystemMsg(BaseMessage message);
    }

    /**
     * 未读角标消息
     */
    public interface UnreadReceiveMsgListener {
        void onUpdateUnread(BaseMessage message);
    }

    /**
     * 私聊消息
     */
    public interface WhisperMsgListener {
        void onUpdateWhisper(BaseMessage message);
    }
}
