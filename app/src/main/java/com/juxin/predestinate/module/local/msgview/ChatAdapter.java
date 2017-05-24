package com.juxin.predestinate.module.local.msgview;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import com.juxin.library.log.PLogger;
import com.juxin.library.observe.Msg;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.utils.TypeConvertUtil;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.local.msgview.chatview.ChatInterface;
import com.juxin.predestinate.module.local.msgview.chatview.ChatPanel;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatContentAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatExtendPanel;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatInputPanel;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatMediaPlayer;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatRecordPanel;
import com.juxin.predestinate.module.local.msgview.chatview.input.ChatSmilePanel;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;
import com.juxin.predestinate.ui.user.complete.CommonGridBtnPanel;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kind on 2017/3/30.
 */
public class ChatAdapter implements ChatMsgInterface.ChatMsgListener, ExListView.IXListViewListener, ChatInterface.OnClickChatItemListener {

    private Map<Long, UserInfoLightweight> userInfos = new HashMap<>();

    /**
     * 频道Id（群聊的），私聊时聊天对象的uid；群聊时是群聊Id。
     */
    private String channelId = "";

    /**
     * 频道Id（私聊的），私聊时聊天对象的uid；群聊时是群聊Id。
     */
    private String whisperId = "";

    /**
     * 是否显示扩展功能按钮。
     */
    private boolean showExtend = true;

    /**
     * 提供给业务层使用的回调。
     */
    private ChatInterface.OnClickChatItemListener onClickChatItemListener = null;

    /**
     * 当用户信息变化时回调。
     */
    private ChatInterface.OnUserInfoListener onUserInfoListener = null;

    /**
     * 是否是客服消息。
     * 0不是客服
     */
    private int isKF_ID = 0;

    /**
     * 是否正在输入
     */
    private boolean isTyping = true;

    public boolean isTyping() {
        return isTyping;
    }

    public void setTyping(boolean typing) {
        isTyping = typing;
    }

    public int getIsKF_ID() {
        return isKF_ID;
    }

    public void setKf_id(int isKF_ID) {
        this.isKF_ID = isKF_ID;
    }

    /**
     * 判断消息发出者是否是当前用户自己。
     *
     * @param uid 需要判断的用户uid。
     * @return 相同返回true。
     */
    public static boolean isSender(long uid) {
        return App.uid == uid;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getWhisperId() {
        return whisperId;
    }

    public Long getLWhisperId() {
        return TypeConvertUtil.toLong(whisperId);
    }

    public boolean isShowExtend() {
        return showExtend;
    }

    public void setShowExtend(boolean showExtend) {
        this.showExtend = showExtend;
    }

    public void setWhisperId(long whisperID) {
        try {
            this.channelId = null;
            this.whisperId = String.valueOf(whisperID);
            getUserInfo(getLWhisperId());

            chatInstance.chatInputPanel.showSendBtn();
            page = 1;
            attach();
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    public ChatInterface.OnClickChatItemListener getOnClickChatItemListener() {
        return onClickChatItemListener;
    }

    public void setOnClickChatItemListener(ChatInterface.OnClickChatItemListener onClickChatItemListener) {
        this.onClickChatItemListener = onClickChatItemListener;
    }

    public void setUserChatRecordPanel(ChatRecordPanel chatRecordPanel) {
        chatInstance.chatRecordPanelUser = chatRecordPanel;
    }

    /**
     * 将一个提示添加到消息的最后条。
     *
     * @param content 提示内容。
     */
//    public void addClientCustomTip(String content) {
//        addClientCustomTip(new CustomMessage(channelId, whisperId, content));
//    }

    /**
     * 将一个提示添加到消息的最后条。
     */
//    public void addClientCustomTip(CustomMessage customMessage) {
//        try {
//            getChatInstance().chatContentAdapter.updateData(customMessage);
//        } catch (Exception e) {
//            PLogger.printThrowable(e);
//        }
//    }

    /**
     * 设置输入框的背景色。
     *
     * @param bgColor     背景色。
     * @param bgLineColor 最上一条线颜色。
     */
    public void setInputPanelBg(int bgColor, int bgLineColor) {
        getChatInstance().chatInputPanel.setBg(bgColor, bgLineColor);
    }

    /**
     * 添加一个消息到聊天消息列表中。
     *
     * @param msg 消息。
     */
    public void addMessage(BaseMessage msg) {
        try {
            getChatInstance().chatContentAdapter.updateData(msg);
        } catch (Exception e) {
            PLogger.printThrowable(e);
        }
    }

    /**
     * ChatView相关的逻辑对象的实例。
     */
    public static class ChatInstance {
        public Context context = null;
        public ChatAdapter chatAdapter = null;
        public ChatInputPanel chatInputPanel = null;
        public ChatExtendPanel chatExtendPanel = null;
        public ChatSmilePanel chatSmilePanel = null;

        public ChatViewLayout chatViewLayout = null;
        public ChatContentAdapter chatContentAdapter = null;
        public ChatRecordPanel chatRecordPanel = null;
        public ChatRecordPanel chatRecordPanelUser = null;
        public ExListView chatListView = null;
    }

    /**
     * ChatInstance实例。
     */
    private ChatInstance chatInstance = null;

    /**
     * 获取ChatInstance实例。
     *
     * @return 实例。
     */
    public ChatInstance getChatInstance() {
        return chatInstance;
    }

    /**
     * 设置ChatInstance实例。
     *
     * @param chatInstance 实例。
     */
    public void setChatInstance(ChatInstance chatInstance) {
        this.chatInstance = chatInstance;
    }

    /**
     * 根据用户Uid获取用户信息，如果没有获取用户数据。
     *
     * @param uid 用户uid。
     * @return 返回用户信息，如果没有则返回null。
     */
    public synchronized UserInfoLightweight getUserInfo(long uid) {
        PLogger.printObject("userInfos====" + userInfos.size());
        UserInfoLightweight userInfo = userInfos.get(uid);
        if (userInfo == null) {
            if (uid == App.uid) {
                UserDetail temp = ModuleMgr.getCenterMgr().getMyInfo();
                if (temp != null) {
                    UserInfoLightweight infoLightweight = new UserInfoLightweight();
                    infoLightweight.parseUserInfoLightweight(temp);
                    userInfos.put(uid, infoLightweight);
                    return infoLightweight;
                }
            }
            ModuleMgr.getChatMgr().getUserInfoLightweight(uid, new ChatMsgInterface.InfoComplete() {

                @Override
                public void onReqComplete(boolean ret, UserInfoLightweight infoLightweight) {
                    if (ret) {
                        addUserInfo(infoLightweight);
                    }
                }
            });
        } else {
            if (TextUtils.isEmpty(userInfo.getNickname()) && TextUtils.isEmpty(userInfo.getAvatar())) {
                ModuleMgr.getChatMgr().getUserInfoLightweight(uid, new ChatMsgInterface.InfoComplete() {

                    @Override
                    public void onReqComplete(boolean ret, UserInfoLightweight infoLightweight) {
                        if (ret) {
                            addUserInfo(infoLightweight);
                        }
                    }
                });
            }
        }
        return userInfo;
    }

    /**
     * 添加一个用户信息到用户列表。如果当前没有对应用户信息，则刷新数据。
     *
     * @param userInfo 用户信息。
     */
    public synchronized void addUserInfo(final UserInfoLightweight userInfo) {
        if (userInfo == null) {
            return;
        }

        final UserInfoLightweight temp = userInfos.get(userInfo.getUid());

        if (temp == null) {
            PLogger.printObject(userInfo);
            userInfos.put(userInfo.getUid(), userInfo);

            MsgMgr.getInstance().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (msgPanelPair != null) {
                        addFixedPanel(msgPanelPair.first, msgPanelPair.second);
                    }

                    getChatInstance().chatContentAdapter.notifyDataSetChanged();

                    if (onUserInfoListener != null) {
                        onUserInfoListener.onComplete(userInfo);
                    }
                }
            });
        }
    }

    public ChatInterface.OnUserInfoListener getOnUserInfoListener() {
        return onUserInfoListener;
    }

    public void setOnUserInfoListener(ChatInterface.OnUserInfoListener onUserInfoListener) {
        this.onUserInfoListener = onUserInfoListener;
    }

    public void closeAllInput() {
        getChatInstance().chatInputPanel.closeAllInput();
    }

    /**
     * 设置新的扩展信息。
     *
     * @param chatExtendDatas 扩展功能表。
     */
    public void setChatExtendDatas(List<CommonGridBtnPanel.BTN_KEY> chatExtendDatas) {
        chatInstance.chatExtendPanel.setChatExtendDatas(chatExtendDatas);
    }

    /**
     * 设置群聊的基本按钮。
     */
    public void setGroupChatExtendDatas() {
        ArrayList<CommonGridBtnPanel.BTN_KEY> chatExtendDatas = new ArrayList<CommonGridBtnPanel.BTN_KEY>();
        chatExtendDatas.add(CommonGridBtnPanel.BTN_KEY.IMG);
        chatInstance.chatExtendPanel.setChatExtendDatas(chatExtendDatas);
    }

    /**
     * 设置私聊的基本按钮。
     */
    public void setWhisperChatExtendDatas() {
        ArrayList<CommonGridBtnPanel.BTN_KEY> chatExtendDatas = new ArrayList<CommonGridBtnPanel.BTN_KEY>();

        chatExtendDatas.add(CommonGridBtnPanel.BTN_KEY.IMG);
        chatInstance.chatExtendPanel.setChatExtendDatas(chatExtendDatas);
    }

    /**
     * 移动到聊天列表的最后一个。
     */
    public void moveToBottom() {
        if (getChatInstance().chatListView == null) {
            return;
        }

        getChatInstance().chatListView.setSelection(getChatInstance().chatContentAdapter.getCount() - 1);
    }

    /**
     * 打开表情面板的指定表情。
     */
    public void showSpecifiedExpression(String smilePackageName) {
        getChatInstance().chatInputPanel.showChatExpression();
        getChatInstance().chatSmilePanel.selectSmile(smilePackageName);
    }

    /**
     * 设置输入内容。
     *
     * @param text
     */
    public void addInputText(String text) {
        if (text == null) {
            text = "";
        }
        chatInstance.chatInputPanel.setInputText(text);
    }

    /**
     * 注册到消息管理模块，接收消息的更新。
     */
    private void attach() {
        PLogger.printObject(this);
        ModuleMgr.getChatMgr().attachChatListener(TextUtils.isEmpty(channelId) ? whisperId : channelId, this);

        ModuleMgr.getChatMgr().getRecentlyChat(channelId, whisperId, 0);
    }

    /**
     * 反注册消息模块，解除绑定。
     */
    public void detach() {
        ModuleMgr.getChatMgr().detachChatListener(this);
        ChatMediaPlayer.getInstance().stopPlayVoice();

        //关闭页面的时候查看是否下在输入中
        if (getChatInstance() != null && getChatInstance().chatInputPanel != null) {
            getChatInstance().chatInputPanel.sendSystemMsgCancelInput();
        }
    }

    @Override
    public void onChatRecently(boolean ret, List<BaseMessage> baseMessages) {
        PLogger.printObject(baseMessages);
        List<BaseMessage> listTemp = new ArrayList<>();

        if (ret) {
            for (BaseMessage baseMessage : baseMessages) {
                if (isShowMsg(baseMessage)) {
                    listTemp.add(baseMessage);
                }
            }
        }

        chatInstance.chatContentAdapter.setList(listTemp);
        moveToBottom();
    }

    @Override
    public void onChatHistory(boolean ret, List<BaseMessage> baseMessages) {
        PLogger.printObject(baseMessages);
        chatInstance.chatListView.stopRefresh();

        if (ret) {
            if (baseMessages.size() < 20) {
                chatInstance.chatListView.setPullRefreshEnable(false);
            }

            List<BaseMessage> listTemp = new ArrayList<BaseMessage>();

            for (BaseMessage baseMessage : baseMessages) {
                if (isShowMsg(baseMessage)) {
                    listTemp.add(baseMessage);
                }
            }

            if (chatInstance.chatContentAdapter.getList() != null) {
                listTemp.addAll(chatInstance.chatContentAdapter.getList());
            }

            chatInstance.chatContentAdapter.setList(listTemp);
            chatInstance.chatListView.setSelection(baseMessages.size());
        }
    }

    @Override
    public void onChatUpdate(boolean ret, BaseMessage message) {
        PLogger.printObject(message);
        boolean show = false;
        boolean isUpdate = true;

        if (ret) {
            if (message.getTime() == 0) {
                message.setTime(ModuleMgr.getAppMgr().getTime());
            }

            show = isShowMsg(message);

            checkPermissions(message);

            if (show) {
                chatInstance.chatContentAdapter.updateData(message);
                moveToBottom();
            } else {
                ChatMsgType msgType = ChatMsgType.getMsgType(message.getType());
                switch (msgType) {
//                    case CMT_del_msg:
//                        chatInstance.chatContentAdapter.delData((DeleteMessage) message);
//                        break;
//                    case CMT_7:
//                        if (message instanceof SystemMessage) {
//                            SystemMessage tmpSystem = (SystemMessage) message;
//                            if (tmpSystem.getxType() == 3) {//已读消息
//                                isUpdate = false;
//                                chatInstance.chatContentAdapter.callAllMsg(tmpSystem);
//                            }
//                        }
//                        break;
                }
            }

            /**
             * 本地模拟消息
             * 接收的网络消息
             */
            if ((message.getcMsgID() == 0 && isUpdate) || message.getDataSource() == BaseMessage.FOUR
                    || (message.getDataSource() == BaseMessage.TWO && show)) {
                ModuleMgr.getChatMgr().updateLocalReadStatus(channelId, whisperId, message.getMsgID());
            }
        }
    }

    /**
     * 聊天权限处理
     *
     * @param message
     */
    private void checkPermissions(BaseMessage message) {
        if (MailSpecialID.customerService.getSpecialID() == message.getLWhisperID()) return;
        UserDetail user = ModuleMgr.getCenterMgr().getMyInfo();
        /**
         * 如果是男的，而且非包月用户，而且是自己发送的消息，而且是发送成功的消息
         */
        if (user.isMan() && !user.isVip() && message.isSender() && message.getMsgID() > 0) {//男 没有开通包月
            /**
             * 文本消息,语音消息,图片消息
             * 只能免费发送一条消息
             */
            if (message.getType() == BaseMessage.BaseMessageType.common.msgType) {
                if (ModuleMgr.getChatListMgr().getTodayChatShow()) {
                    //更新时间
                    ModuleMgr.getChatListMgr().setTodayChatShow(true);
                    Msg msg = new Msg();
                    msg.setData(false);
                    MsgMgr.getInstance().sendMsg(MsgType.MT_Chat_Can, msg);
                }
            }
        }
    }

    public boolean isShowMsg(BaseMessage baseMsg) {
        if (baseMsg == null) {
            return false;
        }

        ChatMsgType chatMsgType = ChatMsgType.getMsgType(baseMsg.getType());
        baseMsg.setMsgPanelType(chatMsgType.getMsgPanelType());

        switch (chatMsgType.getMsgPanelType()) {
            case CPT_Fixed:
                addFixedPanel(baseMsg, getItemPanel(null, baseMsg, chatInstance, baseMsg.isSender()));
                return false;
        }

        return chatMsgType.getPanelClass() != null;
    }

    public ChatPanel getItemPanel(ChatPanel itemPanel, BaseMessage baseMsg, ChatAdapter.ChatInstance chatInstance, boolean sender) {
        if (baseMsg == null) {
            return null;
        }

        ChatPanel chatPanel;
        if (itemPanel != null) {
            return itemPanel;
        }

        Class<? extends ChatPanel> panelClass = ChatMsgType.getPanelClass(baseMsg.getType());
        if (panelClass == null) {
            return null;
        }

        try {
            Constructor c = panelClass.getDeclaredConstructor(Context.class, ChatInstance.class, boolean.class);
            chatPanel = (ChatPanel) c.newInstance(App.context, chatInstance, sender);
            chatPanel.initView();
        } catch (Exception e) {
            e.printStackTrace();
            chatPanel = null;
        }

        return chatPanel;
    }

    private Pair<BaseMessage, ChatPanel> msgPanelPair = null;

    /**
     * 右上角添加固定提示。
     *
     * @param chatPanel 需要显示的面板，必须有效，不能为null。
     */
    public synchronized void addFixedPanel(BaseMessage baseMessage, final ChatPanel chatPanel) {
        if (chatPanel == null) {
            return;
        }

        msgPanelPair = new Pair<>(baseMessage, chatPanel);
        UserInfoLightweight infoLightweight = getUserInfo(baseMessage.getSendID());

        if (chatPanel.reset(baseMessage, infoLightweight)) {
            msgPanelPair = null;
            chatInstance.chatViewLayout.addFixedPanel(chatPanel);
        }
    }

    @Override
    public boolean onClickHead(BaseMessage msgData) {
        if (onClickChatItemListener != null) {
            return onClickChatItemListener.onClickHead(msgData);
        }

        return false;
    }

    @Override
    public boolean onClickStatus(BaseMessage msgData) {
        if (onClickChatItemListener != null) {
            return onClickChatItemListener.onClickStatus(msgData);
        }

        return false;
    }

    @Override
    public boolean onClickContent(BaseMessage msgData, boolean longClick) {
        if (onClickChatItemListener != null) {
            return onClickChatItemListener.onClickContent(msgData, longClick);
        }

        return false;
    }

    @Override
    public boolean onClickErrorResend(BaseMessage msgData) {
        if (onClickChatItemListener != null) {
            return onClickChatItemListener.onClickErrorResend(msgData);
        }

        return false;
    }

    private int page = 1;

    @Override
    public void onRefresh() {
        // 这里是加载更多信息的。
        ModuleMgr.getChatMgr().getHistoryChat(channelId, whisperId, page++);
    }

    @Override
    public void onLoadMore() {
    }

    /**
     * 是否可以聊天
     */
    public void showIsCanChat(boolean isCanChat) {
        chatInstance.chatInputPanel.showChat(isCanChat);
    }

    /**
     * 如果是小秘书都不显示
     */
    public void showInputGONE() {
        chatInstance.chatInputPanel.showInputGONE();
    }
}
