package com.juxin.predestinate.module.local.chat;

import android.app.Application;
import com.juxin.library.log.PLogger;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.mumu.bean.message.MsgMgr;
import com.juxin.mumu.bean.utils.BitmapUtil;
import com.juxin.mumu.bean.utils.TypeConvUtil;
import com.juxin.predestinate.bean.db.AppComponent;
import com.juxin.predestinate.bean.db.AppModule;
import com.juxin.predestinate.bean.db.DBCenter;
import com.juxin.predestinate.bean.db.DBModule;
import com.juxin.predestinate.bean.db.DaggerAppComponent;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.CommonMessage;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.BaseUtil;

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

public class ChatMgr implements ModuleBase, PObserver {

    private RecMessageMgr messageMgr = new RecMessageMgr();

    @Inject
    DBCenter dbCenter;

    @Override
    public void init() {
        com.juxin.library.observe.MsgMgr.getInstance().attach(this);
        messageMgr.init();
    }

    @Override
    public void release() {
        messageMgr.release();
    }


    private void inject(){
        getAppComponent().inject(this);

        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setWhisperID("1");
        baseMessage.setSendID(1);
        baseMessage.setcMsgID(1);
        baseMessage.setContent("xxxx");
        baseMessage.setStatus(1);
        onReceiving(baseMessage);

        Observable<List<BaseMessage>> listObservable = dbCenter.queryFmessageList("1","1", 1,1);
        listObservable.subscribe(new Action1<List<BaseMessage>>() {
            @Override
            public void call(List<BaseMessage> baseMessages) {

            }
        });
    }

    /**
     * 文字消息
     * @param whisperID
     * @param content
     */
    public void sendTextMsg(String channelID, String whisperID, String content) {
        CommonMessage commonMessage = new CommonMessage(channelID, whisperID, content);
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
        long ret = dbCenter.insertFmessage(commonMessage);
        if(ret == DBConstant.ERROR){
            onChatMsgUpdate(commonMessage.getChannelID(),commonMessage.getWhisperID(), false, commonMessage);
            return;
        }

        IMProxy.getInstance().send(new NetData(TypeConvUtil.toLong(whisperID), BaseMessage.BaseMessageType.common.getMsgType(), commonMessage.getJsonStr()));
    }

//    public void sendImgMsg(String whisperID, String img_url) {
//        ImgMessage message = new ImgMessage(whisperID, img_url);
//        message.setLocalImgUrl(BitmapUtil.getSmallBitmapAndSave(img_url));
//        message.setJsonStr(message.getJson(message));
//        message.setFolder(ChatListMgr.Folder.whisper.toString());//标注为是否是系统消息
//        sendMsgIsSave(null, whisperID, message);
//    }


    public void sendImgMsg(String channelID, String whisperID, String img_url) {
        CommonMessage commonMessage = new CommonMessage(channelID, whisperID, img_url, null);
        commonMessage.setLocalImg(BitmapUtil.getSmallBitmapAndSave(img_url));
        commonMessage.setJsonStr(commonMessage.getJson(commonMessage));
        long ret = dbCenter.insertFmessage(commonMessage);
        if(ret == DBConstant.ERROR){
            onChatMsgUpdate(commonMessage.getChannelID(),commonMessage.getWhisperID(), false, commonMessage);
            return;
        }

        ModuleMgr.getMediaMgr().sendHttpFile(Constant.INT_CHAT_PIC, img_url, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
//                if (response.isOk()) {
//                    response.getBaseData();
//                    UpLoadResult upLoadResult = (UpLoadResult) result.getBaseData();
//                    message.setImgUrl(upLoadResult.getHttpPathPic());
//                    message.setJsonStr(message.getJson(message));
//                    DBCenter.getInstance().updateVoiceMsgJson(message);
//                } else {
//                    updateFail(message, null);
//                }
            }
        });


        IMProxy.getInstance().send(new NetData(TypeConvUtil.toLong(whisperID), BaseMessage.BaseMessageType.common.getMsgType(), commonMessage.getJsonStr()));
    }

    /**
     * 接收消息
     * @param message
     */
    public void onReceiving(BaseMessage message) {
        pushMsg(dbCenter.insertFmessage(message) == DBConstant.ERROR, message);
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
        MMLog.autoDebug(messageList);
        final Set<ChatMsgInterface.ChatMsgListener> listeners = chatMapMsgListener.get(msgID0);
        final Set<ChatMsgInterface.ChatMsgListener> listeners2 = chatMapMsgListener.get(msgID1);
        MsgMgr.getInstance().sendMsgToUI(new Runnable() {
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
        MMLog.autoDebug(messageList);
        final Set<ChatMsgInterface.ChatMsgListener> listeners = chatMapMsgListener.get(msgID0);
        final Set<ChatMsgInterface.ChatMsgListener> listeners2 = chatMapMsgListener.get(msgID1);
        MsgMgr.getInstance().sendMsgToUI(new Runnable() {
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
        MMLog.autoDebug(message);
        final Set<ChatMsgInterface.ChatMsgListener> listeners = chatMapMsgListener.get(msgID0);
        final Set<ChatMsgInterface.ChatMsgListener> listeners2 = chatMapMsgListener.get(msgID1);
        MsgMgr.getInstance().sendMsgToUI(new Runnable() {
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
//                if (ChatListMgr.Folder.whisper.toString().equals(message.getFolder())) {
//                    if (!TextUtils.isEmpty(message.getWhisperID())) {
//                        // 私聊消息//告诉上层可以获取私聊列表了
//                        specialMgr.onWhisperMsgUpdate(message);
//
//                        // 发送的私聊消息
//                        if (App.uid == message.getSendID()) {
//                            if (message.getStatus() == OK_STATUS) {//1.发送成功2.发送失败3.发送中
//                                deleteFailMsg(message);
//                            } else if (message.getStatus() == FAIL_STATUS) {
//                                addFailMsg(message);
//                            }
//                        }
//                    }
//
//                    if (TextUtils.isEmpty(message.getChannelID())) {
//                        Msg msg = new Msg();
//                        msg.setData(message);
//                        MsgMgr.getInstance().sendMsg(MsgType.MT_Chat_Whisper, msg);
//                    }
//                }

//                if (App.uid != message.getSendID()) {
//                    //角标消息更改
//                    if (UnreadReceiveMsgType.getUnreadReceiveMsgID(message.getType()) != null) {
//                        specialMgr.updateUnreadMsg(message);
//                    }
//                }
            }
        });
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_App_Login:
                PLogger.d("---MT_App_Login--->" + value);
                if ((Boolean) value) {//登录成功
                    initAppComponent();
                    inject();
                } else {
                }
                break;
        }
    }

    /**
     * AppComponent
     */
    private static AppComponent mAppComponent;

    /**
     * @return 获取dagger2管理的全局实例
     */
    public static AppComponent getAppComponent() {
        return mAppComponent;
    }


    /**
     * DB初始化
     */
    private void initAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule((Application) App.getContext()))
                .dBModule(new DBModule(App.uid))
                .build();
    }

}
