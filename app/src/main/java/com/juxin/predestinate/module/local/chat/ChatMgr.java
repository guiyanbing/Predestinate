package com.juxin.predestinate.module.local.chat;

import android.support.annotation.Nullable;
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
import com.juxin.predestinate.bean.db.DBCallback;
import com.juxin.predestinate.bean.db.DBCenter;
import com.juxin.predestinate.bean.file.UpLoadResult;
import com.juxin.predestinate.bean.my.GiftsList;
import com.juxin.predestinate.bean.my.OffMsgInfo;
import com.juxin.predestinate.bean.my.SendGiftResultInfo;
import com.juxin.predestinate.bean.start.OfflineBean;
import com.juxin.predestinate.bean.start.OfflineMsg;
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
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.local.unread.UnreadReceiveMsgType;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.BaseUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 消息处理管理类
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

        updateStatusFail();
    }

    /**
     * 退出程序的时候把发送中都更改为发送失败
     */
    public void updateStatusFail() {
        dbCenter.getCenterFLetter().updateStatusFail(null);
        dbCenter.getCenterFMessage().updateStatusFail(null);
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
        dbCenter.getCenterFMessage().updateToRead(channelID, whisperID, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                PLogger.d("result==" + result);
                if (result == MessageConstant.OK) {
                    ModuleMgr.getChatListMgr().getWhisperListUnSubscribe();
                }
            }
        });
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
        dbCenter.getCenterFMessage().updateOtherSideRead(channelID, whisperID, sendID, msgID, null);
    }

    /**
     * 对方已读
     *
     * @param channelID
     * @param whisperID
     * @param sendID
     */
    public void updateOtherRead(String channelID, String whisperID, long sendID, BaseMessage message) {
        ModuleMgr.getChatListMgr().updateToReadPrivate(Long.valueOf(whisperID));
        ModuleMgr.getChatMgr().updateOtherSideRead(null, whisperID, Long.toString(sendID));
    }

    /**
     * 本地模拟语音视频消息
     *
     * @param otherID
     */
    public void sendVideoMsgLocalSimulation(String otherID, int type, long videoID) {
        final VideoMessage videoMessage = new VideoMessage(null, otherID, type, videoID, 3, 3);
        videoMessage.setStatus(MessageConstant.OK_STATUS);
        videoMessage.setDataSource(MessageConstant.FOUR);
        videoMessage.setJsonStr(videoMessage.getJson(videoMessage));

        dbCenter.insertMsgLocalVideo(videoMessage, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                if (result == MessageConstant.ERROR) {
                    return;
                }
                pushMsg(videoMessage);
            }
        });
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
                if (!messageRet.isOk() || !messageRet.isS())
                    return;

                Observable<Boolean> observable = dbCenter.getCenterFLetter().isHaveMsg(whisperID);
                observable.subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            dbCenter.getCenterFLetter().updateLetter(textMessage, null);
                        }
                        dbCenter.getCenterFMessage().insertMsg(textMessage, null);
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

    /**
     * 重发消息
     *
     * @param message 消息体
     */
    public void resendMsg(BaseMessage message) {
        BaseMessage.BaseMessageType messageType = BaseMessage.BaseMessageType.valueOf(message.getType());
        if (messageType != null) {
            message.setStatus(MessageConstant.SENDING_STATUS);
            onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), message);

            switch (messageType) {
                case common: {
                    final CommonMessage commonMessage = (CommonMessage) message;

                    String voiceUrl = commonMessage.getVoiceUrl();
                    String localVoiceUrl = commonMessage.getLocalVoiceUrl();
                    String img = commonMessage.getImg();
                    String localImg = commonMessage.getLocalImg();

                    if(commonMessage.getMsgID() <= 0){
                        commonMessage.setMsgID(commonMessage.getcMsgID());
                    }
                    commonMessage.setJsonStr(commonMessage.getJson(commonMessage));

                    if (!TextUtils.isEmpty(voiceUrl) || !TextUtils.isEmpty(localVoiceUrl)) {//语音

                        if (!TextUtils.isEmpty(voiceUrl) && FileUtil.isURL(voiceUrl)) {
                            sendMessage(commonMessage, null);
                        } else {
                            if (TextUtils.isEmpty(voiceUrl)) {
                                voiceUrl = localVoiceUrl;
                            }

                            sendHttpFile(Constant.UPLOAD_TYPE_VOICE, commonMessage, voiceUrl, new RequestComplete() {
                                @Override
                                public void onRequestComplete(final HttpResponse response) {
                                    if (response.isOk()) {
                                        UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                                        commonMessage.setVoiceUrl(upLoadResult.getFile_http_path());
                                        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                                        dbCenter.updateFmessage(commonMessage, new DBCallback() {
                                            @Override
                                            public void OnDBExecuted(long result) {
                                                if (result == MessageConstant.ERROR) {
                                                    onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), commonMessage);
                                                    return;
                                                }
                                                sendMessage(commonMessage, null);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else if (!TextUtils.isEmpty(img) || !TextUtils.isEmpty(localImg)) {//图片
                        if (!TextUtils.isEmpty(img) && FileUtil.isURL(img)) {
                            sendMessage(commonMessage, null);
                        } else {
                            if (TextUtils.isEmpty(img)) {
                                img = localImg;
                            }
                            sendHttpFile(Constant.UPLOAD_TYPE_PHOTO, commonMessage, img, new RequestComplete() {
                                @Override
                                public void onRequestComplete(final HttpResponse response) {
                                    if (response.isOk()) {
                                        UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                                        commonMessage.setImg(upLoadResult.getFile_http_path());
                                        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                                        dbCenter.updateFmessage(commonMessage, new DBCallback() {
                                            @Override
                                            public void OnDBExecuted(long result) {
                                                if (result != MessageConstant.OK) {
                                                    return;
                                                }

                                                onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), commonMessage);
                                                sendMessage(commonMessage, null);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } else {//文字
                        sendMessage(commonMessage, null);
                    }

                    break;
                }
                case gift: {
                    final GiftMessage giftMessage = (GiftMessage) message;
                    ModuleMgr.getCommonMgr().sendGift(giftMessage.getWhisperID(), String.valueOf(giftMessage.getGiftID()),
                            giftMessage.getGiftCount(), giftMessage.getGType(), new RequestComplete() {
                                @Override
                                public void onRequestComplete(HttpResponse response) {
                                    SendGiftResultInfo info = new SendGiftResultInfo();
                                    info.parseJson(response.getResponseString());
                                    if (response.isOk()) {
                                        MessageRet messageRet = new MessageRet();
                                        messageRet.setMsgId(info.getMsgID());
                                        updateOk(giftMessage, messageRet);
                                    } else {
                                        updateFail(giftMessage, null);
                                        showToast(response.getMsg());
                                    }
                                }
                            });
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
    public void sendTextMsg(String channelID, String whisperID, @Nullable String content) {
        PLogger.printObject("whisperID=" + whisperID + "1-content=" + content);
        final CommonMessage commonMessage = new CommonMessage(channelID, whisperID, content);
        commonMessage.setStatus(MessageConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
        commonMessage.setRu(MessageConstant.Ru_Friend);

        dbCenter.insertMsg(commonMessage, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), commonMessage);
                if (result == MessageConstant.ERROR)
                    return;

                sendMessage(commonMessage, null);
            }
        });
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

    public void sendImgMsg(String channelID, String whisperID, @Nullable final String img_url) {
        final CommonMessage commonMessage = new CommonMessage(channelID, whisperID, img_url, null);
        commonMessage.setLocalImg(BitmapUtil.getSmallBitmapAndSave(img_url, DirType.getImageDir()));
        commonMessage.setStatus(MessageConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
        commonMessage.setRu(MessageConstant.Ru_Friend);

        dbCenter.insertMsg(commonMessage, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), commonMessage);
                if (result != MessageConstant.OK)
                    return;

                MsgMgr.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (FileUtil.isURL(img_url)) {
                            sendMessage(commonMessage, null);
                            return;
                        }

                        sendHttpFile(Constant.UPLOAD_TYPE_PHOTO, commonMessage, img_url, new RequestComplete() {
                            @Override
                            public void onRequestComplete(HttpResponse response) {
                                if (!response.isOk()) {
                                    return;
                                }

                                UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                                commonMessage.setImg(upLoadResult.getFile_http_path());
                                commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                                dbCenter.updateFmessage(commonMessage, new DBCallback() {
                                    @Override
                                    public void OnDBExecuted(long result) {
                                        if (result != MessageConstant.OK) {
                                            return;
                                        }
                                        onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), commonMessage);
                                        sendMessage(commonMessage, null);
                                    }
                                });
                            }
                        });
                    }
                });

            }
        });


    }

    //语音消息
    public void sendVoiceMsg(String channelID, String whisperID, @Nullable final String url, @Nullable int length) {
        final CommonMessage commonMessage = new CommonMessage(channelID, whisperID, url, length);
        commonMessage.setLocalVoiceUrl(url);
        commonMessage.setStatus(MessageConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
        commonMessage.setRu(MessageConstant.Ru_Friend);

        dbCenter.insertMsg(commonMessage, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), commonMessage);

                if (result != MessageConstant.OK)
                    return;

                MsgMgr.getInstance().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendHttpFile(Constant.UPLOAD_TYPE_VOICE, commonMessage, url, new RequestComplete() {
                            @Override
                            public void onRequestComplete(final HttpResponse response) {
                                if (response.isOk()) {
                                    UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                                    commonMessage.setVoiceUrl(upLoadResult.getFile_http_path());
                                    commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                                    dbCenter.updateFmessage(commonMessage, new DBCallback() {
                                        @Override
                                        public void OnDBExecuted(long result) {
                                            if (result != MessageConstant.OK) {
                                                return;
                                            }

                                            onChatMsgUpdate(commonMessage.getChannelID(), commonMessage.getWhisperID(), commonMessage);
                                            sendMessage(commonMessage, null);
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
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
     * @param channelID 目前没有用
     * @param whisperID 对方ID
     * @param giftID    礼物ID
     * @param giftCount 礼物个数
     * @param gType     来源
     */
    public void sendGiftMsg(final String channelID, final String whisperID, @Nullable final int giftID, @Nullable final int giftCount, @Nullable final int gType) {
        final GiftMessage giftMessage = new GiftMessage(channelID, whisperID, giftID, giftCount);
        giftMessage.setStatus(MessageConstant.SENDING_STATUS);
        giftMessage.setJsonStr(giftMessage.getJson(giftMessage));
        giftMessage.setRu(MessageConstant.Ru_Friend);

        dbCenter.insertMsg(giftMessage, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                onChatMsgUpdate(giftMessage.getChannelID(), giftMessage.getWhisperID(), giftMessage);
                if (result != MessageConstant.OK)
                    return;

                ModuleMgr.getCommonMgr().sendGift(whisperID, String.valueOf(giftID), giftCount, gType, new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        SendGiftResultInfo info = new SendGiftResultInfo();
                        info.parseJson(response.getResponseString());
                        if (response.isOk()) {
                            MessageRet messageRet = new MessageRet();
                            messageRet.setMsgId(info.getMsgID());
                            updateOk(giftMessage, messageRet);
                            GiftsList.GiftInfo giftInfo = ModuleMgr.getCommonMgr().getGiftLists().getGiftInfo(giftID);
                            if (giftInfo == null)
                                return;
                            int stone = ModuleMgr.getCenterMgr().getMyInfo().getDiamand() - giftInfo.getMoney() * giftCount;
                            if (stone >= 0)
                                ModuleMgr.getCenterMgr().getMyInfo().setDiamand(stone);
                        } else {
                            updateFail(giftMessage, null);
                            showToast(response.getMsg());
                        }
                    }
                });
            }
        });
    }

    //tost提示
    private void showToast(final String tip) {
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PToast.showShort(tip + "");
            }
        });
    }

    /**
     * 更新小红点
     *
     * @param msgID
     */
    public void updateMsgFStatus(long msgID, DBCallback callback) {
        dbCenter.getCenterFMessage().updateMsgFStatus(msgID, callback);
    }

    /**
     * 更新送达状态
     *
     * @param msgID
     * @param callback
     */
    public void updateDeliveryStatus(long msgID, DBCallback callback) {
        dbCenter.getCenterFMessage().updateDeliveryStatus(msgID, null);
        dbCenter.getCenterFLetter().updateDeliveryStatus(msgID, callback);
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
                PLogger.d("isMsgOK=" + message.getType() + "=" + contents);

                if (messageRet.isOk() && messageRet.isS()) {
                    updateOk(message, messageRet);
                    sendMessageRefreshYcoin();
                    checkPermissions(message);
                } else {
                    if (MessageRet.MSG_CODE_PULL_BLACK == messageRet.getS()) {
                        updateFailBlacklist(message, messageRet);
                    } else {
                        updateFail(message, messageRet);
                    }
                    onInternalPro(messageRet);
                }
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
        if (userDetail.isUnlock_ycoin()) {//不需要Y币
            return;
        }

        //如何今天可以免费发一条消息不扣除Y币，并刷新Y币以防与服务器不同步
        if (ModuleMgr.getChatListMgr().getTodayChatShow()) {
            MsgMgr.getInstance().sendMsg(MsgType.MT_Update_Ycoin, true);
            return;
        }

        if (userDetail.isUnlock_vip() && userDetail.getYcoin() > 0) {//不需要VIP
            ModuleMgr.getCenterMgr().getMyInfo().setYcoin(userDetail.getYcoin() - 1);
            return;
        }

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
                    sendChatCanError();
                    break;
            }
        }
    }

    /**
     * 聊天权限处理
     *
     * @param message
     */
    private void checkPermissions(BaseMessage message) {
        if (ModuleMgr.getCenterMgr().getMyInfo().isMan() && ModuleMgr.getChatListMgr().getTodayChatShow()) {
            //更新时间
            ModuleMgr.getChatListMgr().setTodayChatShow();
            UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();

            boolean result = false;
            if ((userDetail.isVip() && userDetail.getYcoin() > 0) || (!userDetail.isVip() && userDetail.getYcoin() > 79)) {
                result = true;
            }

            Msg msg = new Msg();
            msg.setData(result);
            MsgMgr.getInstance().sendMsg(MsgType.MT_Chat_Can, msg);
        }
    }

    /**
     * 是否已经发完当天发的一条了
     */
    private void sendChatCanError() {
        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        if (!( //以下条件满足一个就不锁定聊天框
                (userDetail.isUnlock_ycoin() && userDetail.isUnlock_vip())                      //解锁VIP和Y币
                        || (userDetail.isMan() && userDetail.getYcoin() > 79 && !userDetail.isVip())    //男非VIP，Y币大于79
                        || (userDetail.isMan() && userDetail.getYcoin() > 0 && userDetail.isVip())      //男VIP，Y币大于0
                        || (userDetail.isMan() && userDetail.isUnlock_ycoin() && !userDetail.isUnlock_vip() && userDetail.isVip()//解锁Y币不解锁VIP，男VIP
                        || (userDetail.isMan() && !userDetail.isUnlock_ycoin() && userDetail.isUnlock_vip() && userDetail.getYcoin() > 0))//不解锁Y币解锁VIP，男Y币大于0
        )) {
            ModuleMgr.getChatListMgr().setTodayChatShow();
            Msg msg = new Msg();
            msg.setData(false);
            MsgMgr.getInstance().sendMsg(MsgType.MT_Chat_Can, msg);
        }
    }

    /**
     * 提示消息
     *
     * @param strMsg
     */
    private void toSendMsgToUI(final String strMsg) {
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(strMsg)) {
                    PToast.showShort(strMsg);
                }
            }
        });
    }

    // 成功后更新数据库
    private void updateOk(final BaseMessage message, MessageRet messageRet) {
        if (messageRet != null && messageRet.getMsgId() > 0) {
            message.setMsgID(messageRet.getMsgId());
            message.setTime(messageRet.getTm() <= 0 ? getTime() : messageRet.getTm());
        } else {
            message.setMsgID(MessageConstant.NumNo);
            message.setTime(getTime());
        }

        message.setStatus(MessageConstant.OK_STATUS);
        dbCenter.updateMsg(message, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), message);
            }
        });

    }

    private void updateFail(BaseMessage message, MessageRet messageRet) {
        updateFail(message, messageRet, MessageConstant.FAIL_STATUS);
    }

    private void updateFailBlacklist(BaseMessage message, MessageRet messageRet) {
        updateFail(message, messageRet, MessageConstant.BLACKLIST_STATUS);
    }

    /**
     * 发送失败
     *
     * @param message
     * @param messageRet
     * @param status
     */
    private void updateFail(final BaseMessage message, MessageRet messageRet, int status) {
        if (messageRet != null && messageRet.getMsgId() > 0) {
            message.setMsgID(messageRet.getMsgId());
            message.setTime(messageRet.getTm());
        } else {
            message.setMsgID(MessageConstant.NumNo);
            message.setTime(getTime());
        }
        message.setStatus(status);
        dbCenter.updateMsg(message, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), message);
            }
        });

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
    public void onReceiving(final BaseMessage message) {
        message.setStatus(MessageConstant.UNREAD_STATUS);
        PLogger.printObject(message);
        dbCenter.insertMsg(message, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                if (result != MessageConstant.OK) {
                    return;
                }
                pushMsg(message);
            }
        });
    }

    /**
     * 批量接收消息
     *
     * @param baseMessageList
     */
    public void onReceivingList(List<BaseMessage> baseMessageList) {
        dbCenter.getCenterFMessage().insertMsgList(baseMessageList, null);
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
            if (videoMessage.getEmLastStatus() == VideoMessage.EmLastStatus.cancel ||
                    videoMessage.getEmLastStatus() == VideoMessage.EmLastStatus.timeout) {
                videoMessage.setStatus(MessageConstant.UNREAD_STATUS);
            } else {
                videoMessage.setStatus(MessageConstant.READ_STATUS);
            }
        }

        if (TextUtils.isEmpty(videoMessage.getWhisperID()))
            return;

        dbCenter.insertMsgVideo(videoMessage, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                if (result != MessageConstant.OK) {
                    return;
                }

                pushMsg(videoMessage);
            }
        });

        //通知刷新个人资料
        long vcId = PSP.getInstance().getLong("VIDEOID" + App.uid, 0);
        if (videoMessage.getVideoTp() == 4 && videoMessage.getVideoID() != vcId) {
            MsgMgr.getInstance().sendMsg(MsgType.MT_Update_MyInfo, null);
            PSP.getInstance().put("VIDEOID" + App.uid, videoMessage.getVideoID() + "");
        }
    }

    /**
     * 本地模拟消息
     *
     * @param message
     */
    public void onLocalReceiving(final BaseMessage message) {
        message.setDataSource(MessageConstant.FOUR);
        message.setStatus(MessageConstant.READ_STATUS);

        dbCenter.insertMsg(message, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                if (result != MessageConstant.OK) {
                    return;
                }

                pushMsg(message);
            }
        });
    }

    /**
     * 获取系统推送消息
     *
     * @param page
     * @return
     */
    public Observable<List<BaseMessage>> getSystemNotice(int page) {
        String systemMsg = String.valueOf(MailSpecialID.systemMsg.getSpecialID());
        Observable<List<BaseMessage>> observable = dbCenter.getCenterFMessage().queryMsgList(null, systemMsg, page, 20);
        if (page == 0) {
            dbCenter.getCenterFMessage().updateToRead(null, systemMsg, new DBCallback() {
                @Override
                public void OnDBExecuted(long result) {
                    if (result != MessageConstant.OK) {
                        return;
                    }
                    ModuleMgr.getChatListMgr().getWhisperListUnSubscribe();
                }
            });
        }
        return observable;
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
        //把当前用户未读信息都更新成已读
        dbCenter.getCenterFMessage().updateToRead(channelID, whisperID, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                if (result == MessageConstant.OK) {
                    ModuleMgr.getChatListMgr().getWhisperListUnSubscribe();
                    if (!TextUtils.isEmpty(whisperID)) {
                        sendMailReadedMsg(channelID, Long.valueOf(whisperID));
                    }
                }
            }
        });

        return dbCenter.getCenterFMessage().queryMsgList(channelID, whisperID, 0, 20);
    }

    public void sendMailReadedMsg(String channelID, final long userID) {
        sendMailReadedMsg(channelID, userID, new IMProxy.SendCallBack() {
            @Override
            public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                MessageRet messageRet = new MessageRet();
                messageRet.parseJson(contents);
                if (messageRet.getS() == 0) {

                }
            }

            @Override
            public void onSendFailed(NetData data) {
            }
        });
    }

    private void pushMsg(BaseMessage message) {
        if (message == null)
            return;
        onChatMsgUpdate(message.getChannelID(), message.getWhisperID(), message);
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
     * @param message
     */
    public void onChatMsgUpdate(String msgID0, String msgID1, final BaseMessage message) {
        PLogger.printObject(message);
        final Set<ChatMsgInterface.ChatMsgListener> listeners = chatMapMsgListener.get(msgID0);
        final Set<ChatMsgInterface.ChatMsgListener> listeners2 = chatMapMsgListener.get(msgID1);
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listeners != null) {
                    for (ChatMsgInterface.ChatMsgListener imListener : listeners) {
                        imListener.onChatUpdate(message);
                    }
                }

                if (listeners2 != null) {
                    for (ChatMsgInterface.ChatMsgListener imListener : listeners2) {
                        imListener.onChatUpdate(message);
                    }
                }

                for (ChatMsgInterface.ChatMsgListener imListener : chatMsgListener) {
                    imListener.onChatUpdate(message);
                }

                //纯私聊消息
                if (!TextUtils.isEmpty(message.getWhisperID())) {
                    // 私聊消息
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
            observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            observable.subscribe(new Observer<UserInfoLightweight>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(UserInfoLightweight lightweight) {
                    PLogger.d("getUserInfoLightweight=" + lightweight.toString());
                    long infoTime = lightweight.getTime();
                    //如果有数据且是一小时内请求的就不用请求了
                    if (lightweight.getUid() > 0 &&
                            (infoTime > 0 && (infoTime + Constant.TWO_HOUR_TIME) > getTime())) {
                        removeInfoComplete(true, true, uid, lightweight);
                        return;
                    }
                    removeInfoComplete(false, false, uid, lightweight);
                    getNetSingleProfile(uid, new ChatMsgInterface.InfoComplete() {
                        @Override
                        public void onReqComplete(boolean ret, UserInfoLightweight infoLightweight) {
                            // 再次请求一遍之后不管成功失败都移除
                            removeInfoComplete(true, ret, uid, infoLightweight);
                        }
                    });
                }
            }).unsubscribe();
        }
    }

    /**
     * 从数据库中查询多个用户信息
     *
     * @param uids 用户uid集合
     * @return 查询结果Observable
     */
    public Observable<List<UserInfoLightweight>> getUserInfoList(List<Long> uids) {
        return dbCenter.getCacheCenter().queryProfile(uids);
    }

    /**
     * 批量请求简略个人资料
     *
     * @param userIds 用户uid集合
     */
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

                    updateUserInfoList(infoLightweights);
                    dbCenter.getCacheCenter().storageProfileData(infoLightweights, null);
                }
            }
        });
    }

    /**
     * 带回调地请求单个用户的简略个人资料
     *
     * @param userID       查询的用户uid
     * @param infoComplete 查询完成回调
     */
    public void getNetSingleProfile(final long userID, final ChatMsgInterface.InfoComplete infoComplete) {
        List<Long> longs = new ArrayList<>();
        longs.add(userID);
        ModuleMgr.getCommonMgr().reqUserInfoSummary(longs, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PLogger.d("---getNetSingleProfile--->" + response.getResponseString());
                UserInfoLightweight temp = new UserInfoLightweight();
                if (!response.isOk()) {
                    infoComplete.onReqComplete(false, temp);
                    return;
                }

                UserInfoLightweightList infoLightweightList = new UserInfoLightweightList();
                infoLightweightList.parseJsonSummary(response.getResponseJson());

                if (infoLightweightList.getUserInfos() == null || infoLightweightList.getUserInfos().isEmpty()) {
                    infoComplete.onReqComplete(false, temp);
                    return;
                }

                temp = infoLightweightList.getUserInfos().get(0);
                temp.setTime(getTime());
                temp.setUid(userID);
                infoComplete.onReqComplete(true, temp);

                dbCenter.getCacheCenter().storageProfileData(temp, null);
                dbCenter.getCenterFLetter().updateUserInfoLight(temp, null);
            }
        });
    }

    /**
     * 更新消息列表个人资料
     *
     * @param lightweight
     * @param callback
     */
    public void updateUserInfoLight(UserInfoLightweight lightweight, DBCallback callback) {
        dbCenter.getCenterFLetter().updateUserInfoLight(lightweight, callback);
    }

    /**
     * 批量更新数据库中存储的简略个人资料
     *
     * @param infoLightweights 需要批量更新的简略个人资料
     */
    public void updateUserInfoList(List<UserInfoLightweight> infoLightweights) {
        dbCenter.getCenterFLetter().updateUserInfoLightList(infoLightweights);
    }

    /**
     * 更新个人资料
     *
     * @param isRemove        是否要从回调map中移除：true是移除（回调成功之后移除本次回调）
     * @param isOK            是否请求成功：true是成功（包括数据库查询成功及请求返回数据成功）
     * @param infoLightweight 个人资料数据
     */
    private void removeInfoComplete(boolean isRemove, boolean isOK, long userID, UserInfoLightweight infoLightweight) {
        PLogger.d("---removeInfoComplete--->" + infoLightweight);
        synchronized (infoMap) {
            if (infoMap.size() <= 0)
                return;
            ChatMsgInterface.InfoComplete infoComplete = infoMap.get(userID);
            if (infoComplete != null)
                infoComplete.onReqComplete(isOK, infoLightweight);
            if (isRemove && infoComplete != null)
                infoMap.remove(userID);
        }
    }

    // ------------------------------------- 离线消息处理 ------------------------------------
    private static Map<Long, OfflineBean> lastOfflineAVMap = new HashMap<>(); // 维护离线音视频消息
    private static final long OFFLINE_MSG_INTERVAL = 10 * 1000;  // 获取离线消息间隔

    /**
     * 获取离线消息并处理
     */
    public void getOfflineMsg() {
        ModuleMgr.getCommonMgr().reqOfflineMsg(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                List<OffMsgInfo> mOffMsgInfos = new ArrayList<>();
                PLogger.d("offlineMsg:  " + response.getResponseString());
                if (!response.isOk())
                    return;

                OfflineMsg offlineMsg = (OfflineMsg) response.getBaseData();
                if (offlineMsg == null || offlineMsg.getMsgList().size() <= 0)
                    return;
                OfflineBean bea = null;
                if (offlineMsg.getMsgList().size() > 0)
                    bea = offlineMsg.getMsgList().get(offlineMsg.getMsgList().size() - 1);
                // 逐条处理离线消息
                for (OfflineBean bean : offlineMsg.getMsgList()) {
                    if (bean == null)
                        continue;

                    dispatchOfflineMsg(bean);
                    if (bean.getMtp() != BaseMessage.Recved_MsgType && bean.getMtp() != BaseMessage.System_MsgType) {//普通消息才加入送达列表中
                        mOffMsgInfos.add(new OffMsgInfo(bean.getFid(), bean.getD(), bean.getMtp()));
                    }
                    if (bean == bea) {
                        ModuleMgr.getCommonMgr().reqOfflineRecvedMsg(mOffMsgInfos, null);
                    }
                }

                // 服务器每次最多取1000条，若超过则再次请求
                if (offlineMsg.getMsgList().size() >= 1000) {
                    getOfflineMsg();
                    return;
                }
                dispatchLastOfflineAVMap();
            }
        });
    }

    /**
     * 离线消息派发
     */
    private void dispatchOfflineMsg(OfflineBean bean) {
        if (bean.getD() == 0)
            return;
        if (lastOfflineAVMap == null)
            lastOfflineAVMap = new HashMap<>();
        // 音视频消息
        if (bean.getMtp() == BaseMessage.BaseMessageType.video.getMsgType()) {
            long vc_id = bean.getVc_id();
            if (lastOfflineAVMap.get(vc_id) == null) {
                lastOfflineAVMap.put(vc_id, bean);
            } else {
                lastOfflineAVMap.remove(vc_id);
            }
            return;
        }
        //已送达消息
        if (bean.getMtp() == BaseMessage.Recved_MsgType) {
            ModuleMgr.getChatMgr().updateDeliveryStatus(bean.getMsg_id(), null);
            return;
        }

        //已读消息
        if (bean.getMtp() == BaseMessage.System_MsgType) {
            ModuleMgr.getChatListMgr().updateToReadPrivate(bean.getFid());
            ModuleMgr.getChatMgr().updateOtherSideRead(null, bean.getFid() + "", bean.getTid() + "");
            return;
        }

        offlineMessage(bean.getJsonStr());
    }

    /**
     * 处理最新的音视频离线消息
     */
    private void dispatchLastOfflineAVMap() {
        if (lastOfflineAVMap == null || lastOfflineAVMap.size() == 0)
            return;
        if (BaseUtil.isScreenLock(App.context))
            return;

        OfflineBean bean = null;
        long mt = 0;

        for (Map.Entry<Long, OfflineBean> entry : lastOfflineAVMap.entrySet()) {
            OfflineBean msgBean = entry.getValue();
            if (msgBean == null)
                return;

            // 邀请加入聊天, 过滤最新一条
            if (msgBean.getVc_tp() == 1) {
                long t = msgBean.getMt();   // 最新时间戳
                if (t > mt) {
                    mt = t;
                    bean = msgBean;
                }
            }
        }
        lastOfflineAVMap.clear();
        if (bean != null) {
            offlineMessage(bean.getJsonStr());
        }
    }

    //离线消息
    private void offlineMessage(String str) {
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

            PLogger.printObject("offlineMessage=" + message.getType());
            if (message.isSave()) {
                onReceiving(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long getTime() {
        return ModuleMgr.getAppMgr().getTime();
    }
}