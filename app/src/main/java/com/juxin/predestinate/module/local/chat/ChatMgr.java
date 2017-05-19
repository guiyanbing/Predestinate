package com.juxin.predestinate.module.local.chat;

import android.text.TextUtils;

import com.juxin.library.log.PLogger;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.utils.BitmapUtil;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.db.DBCenter;
import com.juxin.predestinate.bean.db.cache.DBCacheCenter;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.bean.file.UpLoadResult;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.local.chat.msgtype.OrdinaryMessage;
import com.juxin.predestinate.module.local.chat.msgtype.TextMessage;
import com.juxin.predestinate.module.local.unread.UnreadReceiveMsgType;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.DirType;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

/**
 *
 * Created by Kind on 2017/3/28.
 */
public class ChatMgr implements ModuleBase {

    private RecMessageMgr messageMgr = new RecMessageMgr();
    private ChatSpecialMgr specialMgr = ChatSpecialMgr.getChatSpecialMgr();

    @Inject
    DBCenter dbCenter;

//    @Inject
//    DBCacheCenter dbCacheCenter;

    @Override
    public void init() {
        messageMgr.init();
        specialMgr.init();
    //    App.getCacheComponent().inject(this);
    }

    @Override
    public void release() {
        messageMgr.release();
        specialMgr.release();
    }


    public void inject(){
        ModuleMgr.getChatListMgr().getAppComponent().inject(this);
    }

    /**
     * 文字消息
     * @param whisperID
     * @param content
     */
    public void sendTextMsg(String channelID, String whisperID, String content) {
        CommonMessage commonMessage = new CommonMessage(channelID, whisperID, content);
        commonMessage.setStatus(DBConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));

        long ret = dbCenter.insertMsg(commonMessage);

        boolean b = ret != DBConstant.ERROR;
        onChatMsgUpdate(commonMessage.getChannelID(),commonMessage.getWhisperID(), b, commonMessage);

        if(b) sendMessage(commonMessage, null);
    }

    /**
     * 打招呼
     * @param whisperID
     * @param content
     * @param kf  当前发信用户为机器人  客服id
     * @param sayHelloType  当前发信用户为机器人 机器人打招呼类型(0为普通,1为向机器人一键打招呼, 3附近的人群打招呼,4为向机器人单点打招呼(包括首页和详细资料页等))
     */
    public void sendSayHelloMsg(String whisperID, String content, int kf, int sayHelloType, IMProxy.SendCallBack sendCallBack) {
        TextMessage textMessage = new TextMessage(whisperID,  content, kf, sayHelloType);
        textMessage.setStatus(DBConstant.SENDING_STATUS);
        textMessage.setJsonStr(textMessage.getJson(textMessage));

        long ret = dbCenter.insertMsg(textMessage);

        boolean b = ret != DBConstant.ERROR;
        onChatMsgUpdate(textMessage.getChannelID(),textMessage.getWhisperID(), b, textMessage);

        if(b){
            sendMessage(textMessage, sendCallBack);
        }else {
            sendCallBack.onSendFailed(null);
        }
    }

    /**
     * 关注
     * @param userID
     * @param content
     * @param kf
     * @param gz 关注状态1为关注2为取消关注
     */
    public void sendAttentionMsg(long userID, String content, int kf, int gz, IMProxy.SendCallBack sendCallBack) {
        OrdinaryMessage message = new OrdinaryMessage(userID,  content, kf, gz);
        IMProxy.getInstance().send(new NetData(App.uid, message.getType(), message.toFllowJson()), sendCallBack);
    }

    public void sendImgMsg(String channelID, String whisperID, String img_url) {
        final CommonMessage commonMessage = new CommonMessage(channelID, whisperID, img_url, null);
        commonMessage.setLocalImg(BitmapUtil.getSmallBitmapAndSave(img_url, DirType.getImageDir()));
        commonMessage.setStatus(DBConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));

        long ret = dbCenter.insertMsg(commonMessage);

        boolean b = ret != DBConstant.ERROR;
        onChatMsgUpdate(commonMessage.getChannelID(),commonMessage.getWhisperID(), b, commonMessage);

        if(!b) return;
        ModuleMgr.getMediaMgr().sendHttpFile(Constant.UPLOAD_TYPE_PHOTO, img_url, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                    commonMessage.setImg(upLoadResult.getHttpPathPic());
                    commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                    long upRet = dbCenter.updateFmessage(commonMessage);
                    if(upRet == DBConstant.ERROR){
                        onChatMsgUpdate(commonMessage.getChannelID(),commonMessage.getWhisperID(), false, commonMessage);
                        return;
                    }
                    sendMessage(commonMessage, null);
                }else {
                    updateFail(commonMessage, null);
                }
            }
        });
     }

    //语音消息
    public void sendVoiceMsg(String channelID, String whisperID, String url, int length) {
        final CommonMessage commonMessage = new CommonMessage(channelID, whisperID, url, length);
        commonMessage.setVoiceUrl(url);
        commonMessage.setStatus(DBConstant.SENDING_STATUS);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));

        long ret = dbCenter.insertMsg(commonMessage);

        boolean b = ret != DBConstant.ERROR;
        onChatMsgUpdate(commonMessage.getChannelID(),commonMessage.getWhisperID(), b, commonMessage);

        if(!b) return;
        ModuleMgr.getMediaMgr().sendHttpFile(Constant.UPLOAD_TYPE_VOICE, url, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                    commonMessage.setVoiceUrl(upLoadResult.getHttpPathPic());
                    commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
                    long upRet = dbCenter.updateFmessage(commonMessage);
                    if(upRet == DBConstant.ERROR){
                        onChatMsgUpdate(commonMessage.getChannelID(),commonMessage.getWhisperID(), false, commonMessage);
                        return;
                    }
                    sendMessage(commonMessage, null);
                }else {
                    updateFail(commonMessage, null);
                }
            }
        });
    }


    private void sendMessage(final BaseMessage message, final IMProxy.SendCallBack sendCallBack){
        PLogger.d("isMsgID=" + message.getcMsgID());
        IMProxy.getInstance().send(new NetData(App.uid, message.getType(), message.getJsonStr()), new IMProxy.SendCallBack() {
            @Override
            public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                if(sendCallBack != null){
                    sendCallBack.onResult(msgId, group, groupId, sender, contents);
                }
                MessageRet messageRet = new MessageRet();
                messageRet.parseJson(contents);
                if(!messageRet.isOk() || !messageRet.isS()){
                    updateFail(message, messageRet);
                }else {
                    updateOk(message, messageRet);
                }

                PLogger.d("isMsgOK=" + contents);
            }

            @Override
            public void onSendFailed(NetData data) {
                if(sendCallBack != null){
                    sendCallBack.onSendFailed(data);
                }
                updateFail(message, null);
                PLogger.d("isMsgError=" + message.getJsonStr());
            }
        });
    }

    // 成功后更新数据库
    private void updateOk(BaseMessage message, MessageRet messageRet) {
        message.setStatus(DBConstant.OK_STATUS);
        message.setTime(messageRet.getTm());
        message.setMsgID(messageRet.getMsgId());

        long upRet = dbCenter.updateFmessage(message);
        onChatMsgUpdate(message.getChannelID(),message.getWhisperID(), upRet != DBConstant.ERROR, message);
    }

    private void updateFail(BaseMessage message, MessageRet messageRet) {
        if (messageRet != null && messageRet.getMsgId() > 0) {
            message.setMsgID(messageRet.getMsgId());
            message.setTime(messageRet.getTm());
        } else {
            message.setMsgID(DBConstant.NumNo);
            message.setTime(getTime());
        }
        message.setStatus(DBConstant.FAIL_STATUS);
        long upRet = dbCenter.updateFmessage(message);
        onChatMsgUpdate(message.getChannelID(),message.getWhisperID(), upRet != DBConstant.ERROR, message);

    }

    /**
     * 接收消息
     * @param message
     */
    public void onReceiving(BaseMessage message) {
        message.setStatus(DBConstant.UNREAD_STATUS);
        pushMsg(dbCenter.insertMsg(message) != DBConstant.ERROR, message);
    }

    /**
     * 获取历史聊天记录
     *
     * @param channelID 频道ID
     * @param whisperID 私聊ID
     * @param page      页码
     */
    public void getHistoryChat(final String channelID, final String whisperID, int page) {
        Observable<List<BaseMessage>> observable = dbCenter.queryFmessageList(channelID, whisperID, page, 20);
        observable.subscribe(new Action1<List<BaseMessage>>() {
            @Override
            public void call(List<BaseMessage> baseMessages) {
                onChatMsgHistory(channelID, whisperID, true, baseMessages);
            }
        });
    }

    /**
     * 获取某个用户最近20条聊天记录C
     *
     * @param channelID  频道ID
     * @param whisperID  私聊ID
     * @param last_msgid 群最后一条消息ID
     */
    public void getRecentlyChat(final String channelID, final String whisperID, long last_msgid) {
        if (TextUtils.isEmpty(channelID) && !TextUtils.isEmpty(whisperID)) {// 如果是群聊去网上取二十条
            Observable<List<BaseMessage>> observable = dbCenter.queryFmessageList(channelID, whisperID, 0, 20);
            observable.subscribe(new Action1<List<BaseMessage>>() {
                @Override
                public void call(List<BaseMessage> baseMessages) {
                    onChatMsgRecently(channelID, whisperID, true,  BaseMessage.conversionListMsg(baseMessages));
                }
            });
//            DBCenter.getInstance().queryMsgListWG(channelID, whisperID, 20);
//            updateLocalReadStatus(channelID, whisperID, last_msgid);
        }
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
            observers = new LinkedHashSet<ChatMsgInterface.ChatMsgListener>();
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
     * 最近二十条聊天记录
     *
     * @param msgID0
     * @param ret
     * @param messageList
     */
    public void onChatMsgRecently(String msgID0, String msgID1, final boolean ret, final List<BaseMessage> messageList) {
        PLogger.printObject(messageList);
        final Set<ChatMsgInterface.ChatMsgListener> listeners = chatMapMsgListener.get(msgID0);
        final Set<ChatMsgInterface.ChatMsgListener> listeners2 = chatMapMsgListener.get(msgID1);
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listeners != null) {
                    for (ChatMsgInterface.ChatMsgListener imListener : listeners) {
                        imListener.onChatRecently(ret, messageList);
                    }
                }

                if (listeners2 != null) {
                    for (ChatMsgInterface.ChatMsgListener imListener : listeners2) {
                        imListener.onChatRecently(ret, messageList);
                    }
                }
                for (ChatMsgInterface.ChatMsgListener imListener : chatMsgListener) {
                    imListener.onChatRecently(ret, messageList);
                }
            }
        });
    }

    /**
     * 历史记录
     *
     * @param msgID0
     * @param ret
     * @param messageList
     */
    public void onChatMsgHistory(String msgID0, String msgID1, final boolean ret, final List<BaseMessage> messageList) {
        PLogger.printObject(messageList);
        final Set<ChatMsgInterface.ChatMsgListener> listeners = chatMapMsgListener.get(msgID0);
        final Set<ChatMsgInterface.ChatMsgListener> listeners2 = chatMapMsgListener.get(msgID1);
        MsgMgr.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (listeners != null) {
                    for (ChatMsgInterface.ChatMsgListener imListener : listeners) {
                        imListener.onChatHistory(ret, messageList);
                    }
                }

                if (listeners2 != null) {
                    for (ChatMsgInterface.ChatMsgListener imListener : listeners2) {
                        imListener.onChatHistory(ret, messageList);
                    }
                }

                for (ChatMsgInterface.ChatMsgListener imListener : chatMsgListener) {
                    imListener.onChatHistory(ret, messageList);
                }
            }
        });
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

                    // 发送的私聊消息
//                    if (App.uid == message.getSendID()) {
//                        if (message.getStatus() == OK_STATUS) {//1.发送成功2.发送失败3.发送中
//                            deleteFailMsg(message);
//                        } else if (message.getStatus() == FAIL_STATUS) {
//                            addFailMsg(message);
//                        }
//                    }
                }

//                if (TextUtils.isEmpty(message.getChannelID())) {
//                    Msg msg = new Msg();
//                    msg.setData(message);
//                    MsgMgr.getInstance().sendMsg(MsgType.MT_Chat_Whisper, msg);
//                }

                if (App.uid != message.getSendID()) {
                    //角标消息更改
                    if (UnreadReceiveMsgType.getUnreadReceiveMsgID(message.getType()) != null) {
                        specialMgr.updateUnreadMsg(message);
                    }
                }
            }
        });
    }

    /******************************
     * 个人资料存储
     ******************************/
    private Map<Long, ChatMsgInterface.InfoComplete> infoMap = new HashMap<Long, ChatMsgInterface.InfoComplete>();

    public void getUserInfoLightweight(long uid, final ChatMsgInterface.InfoComplete infoComplete) {
//        synchronized (infoMap) {
//            infoMap.put(uid, infoComplete);
//            Observable<UserInfoLightweight> observable = dbCacheCenter.queryProfile(uid);
//            observable.subscribe(new Action1<UserInfoLightweight>() {
//                @Override
//                public void call(UserInfoLightweight lightweight) {
//                    long infoTime = lightweight.getTime();
//                    if (infoTime > 0 && (infoTime + Constant.TWO_HOUR_TIME) > getTime()) {//如果有数据且是一小时内请求的就不用请求了
//                        removeInfoComplete(true, lightweight.getUid(), lightweight);
//                    } else {
//                        removeInfoComplete(false, lightweight.getUid(), lightweight);
//                        getProFile(lightweight.getUid());
//                    }
//                }
//            });
//        }
    }

    // 获取个人资料
    private void getProFile(long userID) {
        ModuleMgr.getCommonMgr().getSimpleDetail(userID, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PLogger.printObject("res=====2222===" + response.getResponseString());
            }
        });
    }

    /**
     * 更新个人资料
     * @param isRemove        是否要重回调map中移除 true是移除
     * @param infoLightweight 个人资料数据
     */
    private void removeInfoComplete(boolean isRemove, long userID, UserInfoLightweight infoLightweight) {
        PLogger.printObject(infoLightweight);
        for (Object key : infoMap.keySet()) {
            if (key.equals(userID)) {
                ChatMsgInterface.InfoComplete infoComplete = infoMap.get(key);
                infoComplete.onReqComplete(infoLightweight);
                if (isRemove) {
                    infoMap.remove(key);
                }
                return;
            }
        }
    }

    private long getTime() {
        return ModuleMgr.getAppMgr().getTime();
    }

}
