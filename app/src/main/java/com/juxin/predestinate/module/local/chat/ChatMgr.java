package com.juxin.predestinate.module.local.chat;

import android.text.TextUtils;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.Msg;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.utils.BitmapUtil;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.db.DBCenter;
import com.juxin.predestinate.bean.file.UpLoadResult;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.local.chat.msgtype.GiftMessage;
import com.juxin.predestinate.module.local.chat.msgtype.MailReadedMessage;
import com.juxin.predestinate.module.local.chat.msgtype.OrdinaryMessage;
import com.juxin.predestinate.module.local.chat.msgtype.SystemMessage;
import com.juxin.predestinate.module.local.chat.msgtype.TextMessage;
import com.juxin.predestinate.module.local.chat.msgtype.VideoMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.local.unread.UnreadReceiveMsgType;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Kind on 2017/3/28.
 */
public class ChatMgr implements ModuleBase {

    private RecMessageMgr messageMgr = new RecMessageMgr();
    private ChatSpecialMgr specialMgr = ChatSpecialMgr.getChatSpecialMgr();

    @Inject
    DBCenter dbCenter;

    @Override
    public void init() {
        messageMgr.init();
        specialMgr.init();
    }

    @Override
    public void release() {
        messageMgr.release();
        specialMgr.release();
    }

    public void inject() {
        ModuleMgr.getChatListMgr().getAppComponent().inject(this);
    }

    /**
     * 更新某个用户的本地状态
     * 如果在聊天框的时候。发送过来消息立即更改为已读
     *
     * @param channelID
     * @param whisperID
     */
    public void updateLocalReadStatus(final String channelID, final String whisperID, final long msgID) {
        long ret = dbCenter.getCenterFMessage().updateToRead(channelID, whisperID);//把当前用户未读信息都更新成已读
        if (ret != MessageConstant.ERROR) {
            ModuleMgr.getChatListMgr().getWhisperList();
        }

        // dbCenter.getCenterFMessage().updateToReadVoice(channelID, whisperID);//把当前用户未读信息都更新成已读
        // DBCenter.getInstance().queryLocalReadStatus(new SystemMessage(channelID, whisperID, TypeConvUtil.toLong(whisperID), msgID));
    }

    /**
     * 对方已读
     *
     * @param channelID
     * @param whisperID
     * @param sendID
     */

    public void updateOtherSideRead(String channelID, String whisperID, String sendID) {
        updateOtherSideRead(channelID, whisperID, sendID, -1);
    }

    public void updateOtherSideRead(String channelID, String whisperID, String sendID, long msgID) {
        dbCenter.getCenterFMessage().updateOtherSideRead(channelID, whisperID, sendID, msgID);
    }

    /**
     * 对方已读
     *
     * @param channelID
     * @param whisperID
     * @param sendID
     */
    public void updateOtherRead(String channelID, String whisperID, long sendID) {
        String whisperId = PSP.getInstance().getString("whisperId", "-1");
        ModuleMgr.getChatListMgr().updateToReadPrivate(Long.valueOf(whisperID));
        if (!whisperId.equalsIgnoreCase(whisperID)) {
            updateOtherSideRead(channelID, whisperID, sendID + "");
            PSP.getInstance().put(whisperID + "id", true);
        } else {
            PSP.getInstance().put(whisperID + "id", false);
            SystemMessage systemMessage = new SystemMessage();
            systemMessage.setChannelID(channelID);
            systemMessage.setWhisperID(whisperId);
            systemMessage.setSendID(sendID);
            specialMgr.setSystemMsg(systemMessage);
        }
    }

    public long updateToReadVoice(long msgID) {
        return dbCenter.getCenterFMessage().updateToReadVoice(msgID);
    }

    /**
     * 本地模拟语音视频消息
     *
     * @param otherID
     */
    public void sendvideoMsglocalSimulation(String otherID, int type, int videoID) {
        final VideoMessage videoMessage = new VideoMessage(null, otherID, type, videoID, 3, 3);
        videoMessage.setStatus(MessageConstant.OK_STATUS);
        videoMessage.setDataSource(MessageConstant.FOUR);
        videoMessage.setJsonStr(videoMessage.getJson(videoMessage));

        long ret = dbCenter.getCenterFLetter().storageData(videoMessage);
        if (ret == MessageConstant.ERROR) return;
        dbCenter.getCenterFMessage().insertMsg(videoMessage);
    }

    /**
     * 打招呼
     *
     * @param whisperID
     * @param content
     * @param kf           当前发信用户为机器人  客服id
     * @param sayHelloType 当前发信用户为机器人 机器人打招呼类型(0为普通,1为向机器人一键打招呼, 3附近的人群打招呼,4为向机器人单点打招呼(包括首页和详细资料页等))
     */
    public void sendSayHelloMsg(final String whisperID, String content, int kf, int sayHelloType, final IMProxy.SendCallBack sendCallBack) {
        final TextMessage textMessage = new TextMessage(whisperID, content, kf, sayHelloType);
        textMessage.setStatus(MessageConstant.OK_STATUS);
        textMessage.setJsonStr(textMessage.getJson(textMessage));
        textMessage.setRu(MessageConstant.Ru_Friend);

        IMProxy.getInstance().send(new NetData(App.uid, textMessage.getType(), textMessage.getJsonStr()), new IMProxy.SendCallBack() {
            @Override
            public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                if (sendCallBack != null) {
                    sendCallBack.onResult(msgId, group, groupId, sender, contents);
                }
                MessageRet messageRet = new MessageRet();
                messageRet.parseJson(contents);
                if (!messageRet.isOk() || !messageRet.isS()) return;


                Observable<Boolean> observable = dbCenter.getCenterFLetter().isHaveMsg(whisperID);
                observable.subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            if (dbCenter.getCenterFLetter().updateLetter(textMessage) == MessageConstant.ERROR) {
                                return;
                            }
                        }
                        dbCenter.getCenterFMessage().insertMsg(textMessage);
                    }
                }).unsubscribe();
            }

            @Override
            public void onSendFailed(NetData data) {
                if (sendCallBack != null) {
                    sendCallBack.onSendFailed(data);
                }

            }
        });
    }

    /*****************
     * 重发消息
     *********************/
    public void resendMsg(BaseMessage message) {
        BaseMessage.BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(message.getType());
        if (messageType != null) {
            message.setStatus(MessageConstant.SENDING_STATUS);
            onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), true, message);

            switch (messageType) {
                case common: {
                    final CommonMessage commonMessage = (CommonMessage) message;

                    String voiceUrl = commonMessage.getVoiceUrl();
                    String localVoiceUrl = commonMessage.getLocalVoiceUrl();
                    String img = commonMessage.getImg();
                    String localImg = commonMessage.getLocalImg();
                    if (!TextUtils.isEmpty(voiceUrl) || !TextUtils.isEmpty(localVoiceUrl)) {//语音

                        if (!TextUtils.isEmpty(voiceUrl) && !FileUtil.isURL(voiceUrl)) {
                            sendMessage(commonMessage, null);
                        } else {
                            if (TextUtils.isEmpty(voiceUrl)) {
                                voiceUrl = localVoiceUrl;
                            }

                            sendHttpFile(Constant.UPLOAD_TYPE_VOICE, commonMessage, voiceUrl, new RequestComplete() {
                                @Override
                                public void onRequestComplete(HttpResponse response) {
                                    if (response.isOk()) {
                                        UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                                        commonMessage.setVoiceUrl(upLoadResult.getFile_http_path());
                                        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                                        long upRet = dbCenter.updateFmessage(commonMessage);
                                        if (upRet == MessageConstant.ERROR) {
                                            onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), false, commonMessage);
                                            return;
                                        }
                                        sendMessage(commonMessage, null);
                                    }
                                }
                            });
                        }
                    } else if (!TextUtils.isEmpty(img) || !TextUtils.isEmpty(localImg)) {//图片
                        if (!TextUtils.isEmpty(img) && !FileUtil.isURL(img)) {
                            sendMessage(commonMessage, null);
                        } else {
                            if (TextUtils.isEmpty(img)) {
                                img = localImg;
                            }

                            sendHttpFile(Constant.UPLOAD_TYPE_PHOTO, commonMessage, img, new RequestComplete() {
                                @Override
                                public void onRequestComplete(HttpResponse response) {
                                    if (response.isOk()) {
                                        UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                                        commonMessage.setImg(upLoadResult.getFile_http_path());
                                        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                                        long upRet = dbCenter.updateFmessage(commonMessage);
                                        if (upRet == MessageConstant.ERROR) {
                                            onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), false, commonMessage);
                                            return;
                                        }
                                        sendMessage(commonMessage, null);
                                    }
                                }
                            });
                        }
                    } else {//文字
                        sendMessage(commonMessage, null);
                    }

                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 文字消息
     *
     * @param whisperID
     * @param content
     */
    public void sendTextMsg(String channelID, String whisperID, String content) {
        CommonMessage commonMessage = new CommonMessage(channelID, whisperID, content);
        commonMessage.setStatus(MessageConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
        commonMessage.setRu(MessageConstant.Ru_Friend);

        long ret = dbCenter.insertMsg(commonMessage);

        boolean b = ret != MessageConstant.ERROR;
        onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), b, commonMessage);

        if (b) sendMessage(commonMessage, null);
    }

    /**
     * 关注
     *
     * @param userID
     * @param content
     * @param kf
     * @param gz      关注状态1为关注2为取消关注
     */
    public void sendAttentionMsg(long userID, String content, int kf, int gz, IMProxy.SendCallBack sendCallBack) {
        OrdinaryMessage message = new OrdinaryMessage(userID, content, kf, gz);
        IMProxy.getInstance().send(new NetData(App.uid, message.getType(), message.toFllowJson()), sendCallBack);
    }

    /**
     * 发送已读
     *
     * @param userID
     */
    public void sendMailReadedMsg(String channelID, long userID, IMProxy.SendCallBack sendCallBack) {
        MailReadedMessage message = new MailReadedMessage(channelID, userID);
        IMProxy.getInstance().send(new NetData(App.uid, message.getType(), message.toMailReadedJson()), sendCallBack);
    }

    public void sendImgMsg(String channelID, String whisperID, String img_url) {
        final CommonMessage commonMessage = new CommonMessage(channelID, whisperID, img_url, null);
        commonMessage.setLocalImg(BitmapUtil.getSmallBitmapAndSave(img_url, DirType.getImageDir()));
        commonMessage.setStatus(MessageConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
        commonMessage.setRu(MessageConstant.Ru_Friend);

        long ret = dbCenter.insertMsg(commonMessage);

        boolean b = ret != MessageConstant.ERROR;
        onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), b, commonMessage);

        if (!b) return;

        if (FileUtil.isURL(img_url)) {
            sendMessage(commonMessage, null);
        } else {
            sendHttpFile(Constant.UPLOAD_TYPE_PHOTO, commonMessage, img_url, new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    if (response.isOk()) {
                        UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                        commonMessage.setImg(upLoadResult.getFile_http_path());
                        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                        long upRet = dbCenter.updateFmessage(commonMessage);
                        if (upRet == MessageConstant.ERROR) {
                            onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), false, commonMessage);
                            return;
                        }
                        sendMessage(commonMessage, null);
                    }
                }
            });
        }
    }

    //语音消息
    public void sendVoiceMsg(String channelID, String whisperID, String url, int length) {
        final CommonMessage commonMessage = new CommonMessage(channelID, whisperID, url, length);
        commonMessage.setLocalVoiceUrl(url);
        commonMessage.setStatus(MessageConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
        commonMessage.setRu(MessageConstant.Ru_Friend);

        long ret = dbCenter.insertMsg(commonMessage);

        boolean b = ret != MessageConstant.ERROR;
        onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), b, commonMessage);

        if (!b) return;
        sendHttpFile(Constant.UPLOAD_TYPE_VOICE, commonMessage, url, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                    commonMessage.setVoiceUrl(upLoadResult.getFile_http_path());
                    commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                    long upRet = dbCenter.updateFmessage(commonMessage);
                    if (upRet == MessageConstant.ERROR) {
                        onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), false, commonMessage);
                        return;
                    }
                    sendMessage(commonMessage, null);
                }
            }
        });
    }

    private void sendHttpFile(String uploadType, final BaseMessage message, String url, final RequestComplete complete) {
        ModuleMgr.getMediaMgr().sendHttpFile(uploadType, url, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (!response.isOk()) {
                    updateFail(message, null);
                    return;
                }
                complete.onRequestComplete(response);
            }
        });
    }

    /**
     * 发送礼物
     *
     * @param channelID
     * @param whisperID
     * @param giftID
     * @param giftCount
     * @param giftLogID
     */
    public void sendGiftMsg(String channelID, String whisperID, int giftID, int giftCount, long giftLogID) {
        GiftMessage giftMessage = new GiftMessage(channelID, whisperID, giftID, giftCount, giftLogID);
        giftMessage.setStatus(MessageConstant.OK_STATUS);
        giftMessage.setJsonStr(giftMessage.getJson(giftMessage));
        giftMessage.setRu(MessageConstant.Ru_Friend);

        long ret = dbCenter.insertMsg(giftMessage);
        onChatMsgUpdate(giftMessage.getChannelID(), giftMessage.getWhisperID(), ret != MessageConstant.ERROR, giftMessage);
    }

    public void updateMsgFStatus(BaseMessage baseMessage) {
        baseMessage.setfStatus(0);
        long upRet = dbCenter.updateFmessage(baseMessage);
        onChatMsgUpdate(baseMessage.getChannelID(), baseMessage.getWhisperID(), upRet != MessageConstant.ERROR, baseMessage);
    }

    /**
     * 更新小红点
     *
     * @param msgID
     */
    public long updateMsgFStatus(long msgID) {
        return dbCenter.getCenterFMessage().updateMsgFStatus(msgID);
    }

    private void sendMessage(final BaseMessage message, final IMProxy.SendCallBack sendCallBack) {
        IMProxy.getInstance().send(new NetData(App.uid, message.getType(), message.getJsonStr()), new IMProxy.SendCallBack() {
            @Override
            public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                if (sendCallBack != null) {
                    sendCallBack.onResult(msgId, group, groupId, sender, contents);
                }
                MessageRet messageRet = new MessageRet();
                messageRet.parseJson(contents);
                onInternalPro(messageRet);
                if (!messageRet.isOk() || !messageRet.isS()) {
                    updateFail(message, messageRet);
                } else {
                    updateOk(message, messageRet);
                    sendMessageRefreshYcoin();
                }

                PLogger.d("isMsgOK=" + message.getType() + "=" + contents);
            }

            @Override
            public void onSendFailed(NetData data) {
                if (sendCallBack != null) {
                    sendCallBack.onSendFailed(data);
                }
                updateFail(message, null);
                PLogger.d("isMsgError=" + message.getJsonStr());
            }
        });
    }


    private void sendMessageRefreshYcoin() {
        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        if ((userDetail.isVip() && userDetail.getYcoin() > 0) || (!userDetail.isVip() && userDetail.getYcoin() > 79)) {
            if (userDetail.isVip())
                ModuleMgr.getCenterMgr().getMyInfo().setYcoin(userDetail.getYcoin() - 1);
            else
                ModuleMgr.getCenterMgr().getMyInfo().setYcoin(userDetail.getYcoin() - 80);
            MsgMgr.getInstance().sendMsg(MsgType.MT_Update_Ycoin, false);
        } else {
            MsgMgr.getInstance().sendMsg(MsgType.MT_Update_Ycoin, true);
        }
    }

    /**
     * 消息内部处理,如果失败之类等等
     */
    private void onInternalPro(MessageRet messageRet) {
        if (messageRet.isOk()) {
            PLogger.printObject("s=" + messageRet.getS());
            switch (messageRet.getS()) {
                case MessageRet.MSG_CODE_BALANCE_INSUFFICIENT: {//-1 余额不足或者不是VIP
                    sendChatCanError();
                    break;
                }
                case MessageRet.MSG_CODE_PULL_BLACK: {//已拉黑
                    toSendMsgToUI("已拉黑，消息无法发送！");
                    break;
                }
                default:
                    break;
            }
        }
    }

    /**
     * 是否已经发完当天发的一条了
     */
    private void sendChatCanError() {
        ModuleMgr.getChatListMgr().setTodayChatShow();
        Msg msg = new Msg();
        msg.setData(false);
        MsgMgr.getInstance().sendMsg(MsgType.MT_Chat_Can, msg);
    }

    /**
     * 提示消息
     *
     * @param strMsg
     */
    private void toSendMsgToUI(final String strMsg) {
        MsgMgr.getInstance().delay(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(strMsg)) {
                    PToast.showShort(strMsg);
                }
            }
        }, 0);
    }

    // 成功后更新数据库
    private void updateOk(BaseMessage message, MessageRet messageRet) {
        message.setStatus(MessageConstant.OK_STATUS);
        message.setTime(messageRet.getTm());
        message.setMsgID(messageRet.getMsgId());

        long upRet = dbCenter.updateMsg(message);
        onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), upRet != MessageConstant.ERROR, message);
    }

    private void updateFail(BaseMessage message, MessageRet messageRet) {
        if (messageRet != null && messageRet.getMsgId() > 0) {
            message.setMsgID(messageRet.getMsgId());
            message.setTime(messageRet.getTm());
        } else {
            message.setMsgID(MessageConstant.NumNo);
            message.setTime(getTime());
        }
        message.setStatus(MessageConstant.FAIL_STATUS);
        long upRet = dbCenter.updateMsg(message);
        onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), upRet != MessageConstant.ERROR, message);
    }

    /**
     * 删除多少个小时以前的消息
     *
     * @param hour
     * @return
     */
    public void deleteMessageHour(int hour) {
        dbCenter.deleteMessageHour(hour);
    }

    /**
     * 删除机器人多少小时以前的消息
     *
     * @param hour
     */
    public void deleteMessageKFIDHour(int hour) {
        dbCenter.deleteMessageKFIDHour(hour);
    }


    /**
     * 接收消息
     *
     * @param message
     */
    public void onReceiving(BaseMessage message) {
        message.setStatus(MessageConstant.UNREAD_STATUS);
        PLogger.printObject(message);
        pushMsg(dbCenter.insertMsg(message) != MessageConstant.ERROR, message);
    }

    /**
     * 视频消息
     *
     * @param videoMessage
     */
    public void onReceivingVideo(final VideoMessage videoMessage) {
        if (videoMessage.isSender()) {
            videoMessage.setStatus(MessageConstant.OK_STATUS);
        } else {
            videoMessage.setStatus(MessageConstant.READ_STATUS);
        }

        if (TextUtils.isEmpty(videoMessage.getWhisperID())) return;

        if (dbCenter.getCenterFMessage().storageDataVideo(videoMessage) == MessageConstant.ERROR){
            pushMsg(false, videoMessage);
            return;
        }

        pushMsg(dbCenter.getCenterFLetter().storageData(videoMessage) != MessageConstant.ERROR, videoMessage);
    }

    /**
     * 本地模拟消息
     *
     * @param message
     */
    public void onLocalReceiving(BaseMessage message) {
        message.setDataSource(MessageConstant.FOUR);
        message.setStatus(MessageConstant.READ_STATUS);
        pushMsg(dbCenter.insertMsg(message) != MessageConstant.ERROR, message);
    }

    /**
     * 获取历史聊天记录
     *
     * @param channelID 频道ID
     * @param whisperID 私聊ID
     * @param page      页码
     */
    public Observable<List<BaseMessage>> getHistoryChat(final String channelID, final String whisperID, int page) {
        return dbCenter.getCenterFMessage().queryMsgList(channelID, whisperID, page, 20);
    }

    /**
     * 获取某个用户最近20条聊天记录C
     *
     * @param channelID  频道ID
     * @param whisperID  私聊ID
     * @param last_msgid 群最后一条消息ID
     */
    public Observable<List<BaseMessage>> getRecentlyChat(final String channelID, final String whisperID, long last_msgid) {
        Observable<List<BaseMessage>> observable = dbCenter.getCenterFMessage().queryMsgList(channelID, whisperID, 0, 20);
        long ret = dbCenter.getCenterFMessage().updateToRead(channelID, whisperID);//把当前用户未读信息都更新成已读
        if (ret != MessageConstant.ERROR) {
            ModuleMgr.getChatListMgr().getWhisperList();
        }
        if (ret > 0 && !TextUtils.isEmpty(whisperID))
            sendMailReadedMsg(channelID, Long.valueOf(whisperID));
        return observable;
    }

    public void sendMailReadedMsg(String channelID, long userID) {
        sendMailReadedMsg(channelID, userID, new IMProxy.SendCallBack() {
            @Override
            public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                MessageRet messageRet = new MessageRet();
                messageRet.parseJson(contents);
                //                        Log.e("TTTTTTTTTTLLLL111",   "执行||||成功"+messageRet.getS());
                if (messageRet.getS() == 0) {

                }
            }

            @Override
            public void onSendFailed(NetData data) {
                //                        Log.e("TTTTTTTTTTLLLL222",   "执行||||失败");
            }
        });
    }

    private void pushMsg(boolean ret, BaseMessage message) {
        if (message == null) return;
        onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), ret, message);
    }

    /******************************************/
    private Set<ChatMsgInterface.ChatMsgListener> chatMsgListener = new LinkedHashSet<>();
    private Map<String, Set<ChatMsgInterface.ChatMsgListener>> chatMapMsgListener = new HashMap<>();

    /**
     * 注册一个私聊监听者，将监听者和所有消息类型绑定。
     * <p/>
     * 监听者实例。
     */
    public void attachChatListener(ChatMsgInterface.ChatMsgListener listener) {
        synchronized (chatMsgListener) {
            if (chatMsgListener == null) {
                return;
            }
            boolean listenerExist = false;
            for (ChatMsgInterface.ChatMsgListener item : chatMsgListener) {
                if (item != null && item == listener) {
                    listenerExist = true;
                    break;
                }
            }
            if (!listenerExist) {
                chatMsgListener.add(listener);
            }
        }
    }

    /**
     * 取消注册私聊的监听者，解除监听者的所有绑定。
     *
     * @param listener 监听者实例。
     */
    public void detachChatListener(ChatMsgInterface.ChatMsgListener listener) {
        if (chatMsgListener != null) {
            chatMsgListener.remove(listener);
        }

        for (Map.Entry<String, Set<ChatMsgInterface.ChatMsgListener>> entry : chatMapMsgListener.entrySet()) {
            entry.getValue().remove(listener);
        }
    }

    /**
     * 注册一个私聊监听者，将监听者和消息ID
     *
     * @param msgID        单个消息ID
     * @param chatListener
     */
    public void attachChatListener(final String msgID, final ChatMsgInterface.ChatMsgListener chatListener) {
        Set<ChatMsgInterface.ChatMsgListener> observers = chatMapMsgListener.get(msgID);
        if (observers == null) {
            observers = new LinkedHashSet<>();
            chatMapMsgListener.put(msgID, observers);
        }
        observers.add(chatListener);
    }

    /**
     * 取消注册私聊的监听者，解除监听者的所有绑定。
     *
     * @param chatListener
     */
    public void detachChatListener(final String msgID, final ChatMsgInterface.ChatMsgListener chatListener) {
        Set<ChatMsgInterface.ChatMsgListener> observers = chatMapMsgListener.get(msgID);
        if (observers != null) {
            observers.remove(chatListener);
        }

        if (chatMsgListener != null) {
            chatMsgListener.remove(chatListener);
        }
    }

    /**
     * 更新聊天信息
     *
     * @param msgID0  群ID
     * @param msgID1  私聊ID
     * @param ret
     * @param message
     */
    public void onChatMsgUpdate(String msgID0, String msgID1, final boolean ret, final BaseMessage message) {
        PLogger.printObject(message);
        final Set<ChatMsgInterface.ChatMsgListener> listeners = chatMapMsgListener.get(msgID0);
        final Set<ChatMsgInterface.ChatMsgListener> listeners2 = chatMapMsgListener.get(msgID1);
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listeners != null) {
                    for (ChatMsgInterface.ChatMsgListener imListener : listeners) {
                        imListener.onChatUpdate(ret, message);
                    }
                }

                if (listeners2 != null) {
                    for (ChatMsgInterface.ChatMsgListener imListener : listeners2) {
                        imListener.onChatUpdate(ret, message);
                    }
                }

                for (ChatMsgInterface.ChatMsgListener imListener : chatMsgListener) {
                    imListener.onChatUpdate(ret, message);
                }

                //纯私聊消息
                if (!TextUtils.isEmpty(message.getWhisperID())) {
                    // 私聊消息//告诉上层可以获取私聊列表了
                    specialMgr.onWhisperMsgUpdate(message);
                }

                //角标消息更改
                if (App.uid != message.getSendID() && UnreadReceiveMsgType.getUnreadReceiveMsgID(message.getType()) != null) {
                    specialMgr.updateUnreadMsg(message);
                }
            }
        });
    }

    /* ***************** 个人资料存储 **************** */
    private Map<Long, ChatMsgInterface.InfoComplete> infoMap = new HashMap<>();

    public void getUserInfoLightweight(final long uid, final ChatMsgInterface.InfoComplete infoComplete) {
        synchronized (infoMap) {
            infoMap.put(uid, infoComplete);
            Observable<UserInfoLightweight> observable = dbCenter.getCacheCenter().queryProfile(uid);
            observable.subscribe(new Action1<UserInfoLightweight>() {
                @Override
                public void call(UserInfoLightweight lightweight) {
                    PLogger.printObject("lightweight==222==" + lightweight);
                    if (lightweight.getUid() <= 0) {
                        removeInfoComplete(false, false, uid, lightweight);
                        getProFile(uid);
                        return;
                    }
                    long infoTime = lightweight.getTime();
                    if (infoTime > 0 && (infoTime + Constant.TWO_HOUR_TIME) > getTime()) {//如果有数据且是一小时内请求的就不用请求了
                        removeInfoComplete(true, true, uid, lightweight);
                    } else {
                        removeInfoComplete(false, true, uid, lightweight);
                        getProFile(uid);
                    }
                }
            }).unsubscribe();
        }
    }

    // 获取个人资料
    private void getProFile(final long userID) {
        List<Long> longs = new ArrayList<>();
        longs.add(userID);
        ModuleMgr.getCommonMgr().reqUserInfoSummary(longs, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PLogger.printObject("re=====" + response.getResponseString());
                UserInfoLightweight temp = new UserInfoLightweight();
                if (!response.isOk()) {
                    removeInfoComplete(true, false, userID, temp);
                    return;
                }
                UserInfoLightweightList infoLightweightList = new UserInfoLightweightList();
                infoLightweightList.parseJsonSummary(response.getResponseJson());

                if (infoLightweightList.getUserInfos() != null && infoLightweightList.getUserInfos().size() > 0) {//数据大于1条
                    temp = infoLightweightList.getUserInfos().get(0);
                    temp.setTime(getTime());
                    dbCenter.getCacheCenter().storageProfileData(temp);
                    dbCenter.getCenterFLetter().updateUserInfoLight(temp);

                    temp.setUid(userID);
                    removeInfoComplete(true, true, userID, temp);
                }
            }
        });
    }

    public void getProFile(List<Long> userIds) {
        ModuleMgr.getCommonMgr().reqUserInfoSummary(userIds, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (!response.isOk()) {
                    return;
                }
                UserInfoLightweightList infoLightweightList = new UserInfoLightweightList();
                infoLightweightList.parseJsonSummary(response.getResponseJson());

                if (infoLightweightList.getUserInfos() != null && infoLightweightList.getUserInfos().size() > 0) {//数据大于1条
                    ArrayList<UserInfoLightweight> infoLightweights = infoLightweightList.getUserInfos();

                    boolean ret = dbCenter.getCenterFLetter().updateUserInfoLightList(infoLightweights);
                    dbCenter.getCacheCenter().storageProfileData(infoLightweights);
                }
            }
        });
    }

    public void getNetSingleProfile(final long userID, final ChatMsgInterface.InfoComplete infoComplete) {
        List<Long> longs = new ArrayList<>();
        longs.add(userID);
        ModuleMgr.getCommonMgr().reqUserInfoSummary(longs, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                UserInfoLightweight temp = new UserInfoLightweight();
                if (!response.isOk()) {
                    infoComplete.onReqComplete(true, temp);
                    return;
                }
                UserInfoLightweightList infoLightweightList = new UserInfoLightweightList();
                infoLightweightList.parseJsonSummary(response.getResponseJson());

                if (infoLightweightList.getUserInfos() != null && infoLightweightList.getUserInfos().size() > 0) {//数据大于1条
                    temp = infoLightweightList.getUserInfos().get(0);
                    temp.setTime(getTime());
                    dbCenter.getCacheCenter().storageProfileData(temp);
                    dbCenter.getCenterFLetter().updateUserInfoLight(temp);
                    temp.setUid(userID);
                    infoComplete.onReqComplete(true, temp);
                }
            }
        });
    }

    /**
     * 更新个人资料
     *
     * @param isRemove        是否要重回调map中移除 true是移除
     * @param isOK            是否请求成功 true是成功
     * @param infoLightweight 个人资料数据
     */
    private void removeInfoComplete(boolean isRemove, boolean isOK, long userID, UserInfoLightweight infoLightweight) {
        PLogger.printObject(infoLightweight);
        for (Object key : infoMap.keySet()) {
            if (key.equals(userID)) {
                ChatMsgInterface.InfoComplete infoComplete = infoMap.get(key);
                infoComplete.onReqComplete(isOK, infoLightweight);
                if (isRemove) {
                    infoMap.remove(key);
                }
                return;
            }
        }
    }

    //离线消息
    public void offlineMessage(String str) {
        try {
            JSONObject tmp = new JSONObject(str);
            long from_id = tmp.optLong("fid");//发送者ID
            int type = tmp.optInt("mtp");
            long msgID = tmp.optLong("d");
            BaseMessage.BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(type);
            BaseMessage message;
            if (messageType != null) {
                message = messageType.msgClass.newInstance();
                message.setSave(true);
            } else {
                message = new BaseMessage();
                message.setSave(false);
            }
            message.parseJson(tmp.toString());
            message.setSendID(from_id);
            message.setWhisperID(String.valueOf(from_id));
            message.setMsgID(msgID);
            message.setDataSource(MessageConstant.THREE);
            message.setType(type);
            message.setChannelID(null);

            ModuleMgr.getChatMgr().onReceiving(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getTime() {
        return ModuleMgr.getAppMgr().getTime();
    }
}