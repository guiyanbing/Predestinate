package com.juxin.predestinate.module.local.chat;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.unread.UnreadMgr;
import com.juxin.predestinate.bean.db.AppComponent;
import com.juxin.predestinate.bean.db.AppModule;
import com.juxin.predestinate.bean.db.DBCallback;
import com.juxin.predestinate.bean.db.DBCenter;
import com.juxin.predestinate.bean.db.DBModule;
import com.juxin.predestinate.bean.db.DaggerAppComponent;
import com.juxin.predestinate.bean.db.OldDBModule;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.msgtype.SystemMessage;
import com.juxin.predestinate.module.local.chat.msgtype.VideoMessage;
import com.juxin.predestinate.module.local.chat.utils.MessageConstant;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.model.impl.UnreadMgrImpl;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Kind on 2017/4/13.
 */

public class ChatListMgr implements ModuleBase, PObserver {

    private int unreadNum = 0;
    private int greetNum = 0;
    private List<BaseMessage> msgList = new ArrayList<>(); //私聊列表
    private List<BaseMessage> greetList = new ArrayList<>(); //陌生人

    @Inject
    DBCenter dbCenter;

    @Override
    public void init() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void release() {
        MsgMgr.getInstance().detach(this);
    }

    public int getUnreadNumber() {
        return unreadNum;
    }


    public int getGreetNum() {
        return greetNum;
    }

    /**
     * 转换消息角标个数
     *
     * @param unreadNum
     * @return
     */
    public String getUnreadNum(int unreadNum) {
        return unreadNum <= 9 ? String.valueOf(unreadNum) : "9+";
    }

    public String getUnreadTotalNum(int unreadNum) {
        return unreadNum <= 99 ? String.valueOf(unreadNum) : "99+";
    }

    public List<BaseMessage> getMsgList() {
        List<BaseMessage> tempList = new ArrayList<>();
        synchronized (msgList) {
            tempList.addAll(msgList);
            return tempList;
        }
    }

    public boolean isContain(long userID) {
        synchronized (msgList) {
            for (BaseMessage temp : msgList) {
                if (userID == temp.getLWhisperID()) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 获取角标数
     *
     * @param userID
     * @return
     */
    public int getNoReadNum(long userID) {
        synchronized (msgList) {
            for (BaseMessage temp : msgList) {
                if (userID == temp.getLWhisperID()) {
                    return temp.getNum();
                }
            }
            return 0;
        }
    }

    /**
     * 陌生人列表
     *
     * @return
     */
    public List<BaseMessage> getGeetList() {
        List<BaseMessage> tempList = new ArrayList<>();
        synchronized (greetList) {
            tempList.addAll(greetList);
            return tempList;
        }
    }

    public synchronized void updateListMsg(List<BaseMessage> messages, long tm) {
        PLogger.printObject(messages);
        unreadNum = 0;
        msgList.clear();
        greetNum = 0;
        greetList.clear();
        if (messages != null && messages.size() > 0) {
            for (BaseMessage tmp : messages) {
                if (tmp.isRu() || MailSpecialID.getMailSpecialID(tmp.getLWhisperID())) {
                    msgList.add(tmp);
                } else {
                    greetList.add(tmp);
                    greetNum += tmp.getNum();
                }
                unreadNum += tmp.getNum();
            }
        }
        unreadNum += getFollowNum();//关注
        PLogger.d("unreadNum=" + unreadNum);

        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MsgMgr.getInstance().sendMsg(MsgType.MT_User_List_Msg_Change, null);
        }
    };

    /**
     * 截取语音
     *
     * @param url
     * @return
     */
    public String subStringAmr(String url) {
        return url.contains(".amr") ? (url.replace(".amr", "")) : url;
    }

    /**
     * 拼接
     *
     * @param url
     * @return
     */
    public String spliceStringAmr(String url) {
        return url.contains(".amr") ? url : (url + ".amr");
    }

    /**
     * 关注
     *
     * @return
     */
    public int getFollowNum() {
        return ModuleMgr.getUnreadMgr().getUnreadNumByKey(UnreadMgrImpl.FOLLOW_ME);
    }

    public void updateFollow() {
        ModuleMgr.getUnreadMgr().resetUnreadByKey(UnreadMgrImpl.FOLLOW_ME);
    }

    //是否能聊天
    private String getIsTodayChatKey() {//是否显示问题反馈第一句KEY
        return "isTodayChat" + App.uid;
    }

    public void setTodayChatShow() {//隐藏，显示私聊列表
        PSP.getInstance().put(getIsTodayChatKey(), TimeUtil.getCurrentData());
    }

    /**
     * 当前时否可以聊天
     *
     * @return true是可以发信的，false不可以发信
     */
    public boolean getTodayChatShow() {
        String currentData = TimeUtil.getCurrentData();
        String isTodayChat = PSP.getInstance().getString(getIsTodayChatKey(), "");
        return isTodayChat.equals("") || !isTodayChat.equals(currentData);
    }

    /**
     * 批量删除消息
     *
     * @param messageList
     */
    public void deleteBatchMessage(final List<BaseMessage> messageList) {
        List<Long> idList = new ArrayList<Long>();

        for (BaseMessage temp : messageList) {
            idList.add(temp.getLWhisperID() );
        }

        dbCenter.deleteMessageList(idList);
    }

    public void deleteMessage(long userID) {
        dbCenter.deleteMessage(userID);
    }

    /**
     * 删除聊天记录
     *
     * @param userID
     * @return
     */
    public long deleteFmessage(final long userID) {
        dbCenter.getCenterFLetter().updateContent(String.valueOf(userID), new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                if (result != MessageConstant.OK) {
                    return;
                }

                dbCenter.getCenterFMessage().delete(userID, null);
            }
        });

        //// TODO: 2017/6/15 yuchenl: status
        return MessageConstant.OK;
    }

    /**
     * 更新已读
     */
    public void updateToReadAll() {
        MsgMgr.getInstance().runOnChildThread(new Runnable() {
            @Override
            public void run() {
                dbCenter.updateToReadAll(new DBCallback() {
                    @Override
                    public void OnDBExecuted(long result) {
                        getWhisperListUnSubscribe();
                    }
                });
            }
        });
    }

    public void updateToBatchRead(List<BaseMessage> greetList) {
        if (greetList == null || greetList.size() <= 0) {
            return;
        }
        dbCenter.getCenterFMessage().updateToRead(greetList, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                getWhisperListUnSubscribe();
            }
        });
    }

    /**
     * 更新私聊列表状态
     *
     * @param userID
     * @return
     */
    public void updateToReadPrivate(long userID) {
        dbCenter.getCenterFLetter().updateStatus(userID, new DBCallback() {
            @Override
            public void OnDBExecuted(long result) {
                if (result != MessageConstant.OK) {
                    return;
                }

                getWhisperListUnSubscribe();
            }
        });
    }

    /**
     * 获取消息列表，初始化时进行调用，不取消订阅，外部请勿使用
     */
    public void getWhisperList() {
        PLogger.d("getWhisperList====1");
        dbCenter.getCenterFLetter().queryLetterList()
                .subscribeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<BaseMessage>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(List<BaseMessage> baseMessages) {
                        PLogger.d("getWhisperList====" + baseMessages.size());
                        updateListMsg(baseMessages, ModuleMgr.getAppMgr().getTime());
                    }
                });
    }

    /**
     * 获取消息列表，外部使用，查询完成后取消订阅
     */
    public void getWhisperListUnSubscribe() {
        PLogger.d("getWhisperList====2");
        final Observable<List<BaseMessage>> observable = dbCenter.getCenterFLetter().queryLetterList();
        observable.subscribeOn(Schedulers.io());
        observable.subscribeOn(AndroidSchedulers.mainThread());
        observable.subscribe(new Subscriber<List<BaseMessage>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<BaseMessage> baseMessages) {
                this.unsubscribe();
                PLogger.d("getWhisperList=un===2" + baseMessages.size());
                updateListMsg(baseMessages, ModuleMgr.getAppMgr().getTime());
            }
        });
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_Unread_change:
                if (App.uid <= 0) return;
                Map<String, Object> msgMap = (Map<String, Object>) value;
                String Msg_Name_Key = (String) msgMap.get(UnreadMgr.Msg_Name_Key);
                if (Msg_Name_Key.equals(UnreadMgrImpl.FOLLOW_ME)) {
                    ModuleMgr.getCenterMgr().reqMyInfo(new RequestComplete() {
                        @Override
                        public void onRequestComplete(HttpResponse response) {
                            getWhisperListUnSubscribe();
                        }
                    });
                }
                break;
            case MsgType.MT_App_Login:
                PLogger.d("---ChatList_MT_App_Login--->" + value);
                if ((Boolean) value) {//登录成功
                    login();
                } else {
                    logout();
                }
                break;
        }
    }

    private void login() {
        if (App.uid > 0) {
            initAppComponent();
            //升级数据库
            new OldDBModule().updateDB(App.uid);
            getAppComponent().inject(this);
            ModuleMgr.getChatMgr().inject();
            PLogger.d("uid=======" + App.uid);
            getWhisperList();
            ModuleMgr.getChatMgr().deleteMessageKFIDHour(48);
        }
    }

    private void logout() {
        mAppComponent = null;
        msgList.clear();
        greetList.clear();
        greetNum = 0;
        unreadNum = 0;
    }

    /**
     * AppComponent
     */
    private AppComponent mAppComponent;

    /**
     * @return 获取dagger2管理的全局实例
     */
    public AppComponent getAppComponent() {
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

        MsgMgr.getInstance().sendMsg(MsgType.MT_DB_Init_Ok, null);
    }

    /**
     * 处理特殊消息
     * 例如 系统消息，心动消息
     *
     * @param message
     */
    public void setSpecialMsg(BaseMessage message) {
        switch (message.getType()) {
            case BaseMessage.TalkRed_MsgType://红包消息
                setTalkMsg(message);
                break;
            case BaseMessage.video_MsgType://视频消息
                setVideoMsg(message);
                break;
            case BaseMessage.System_MsgType://系统消息
                setSystemMsg(message);
                break;
            default:
                break;
        }
    }

    /**
     * 视频消息
     *
     * @param message
     */
    private void setVideoMsg(BaseMessage message) {
        if (message == null) return;
        VideoMessage videoMessage = (VideoMessage) message;
        if (videoMessage.getVideoTp() == 1) {
            VideoAudioChatHelper.getInstance().openInvitedActivity((Activity) App.getActivity(),
                    videoMessage.getVideoID(), videoMessage.getLWhisperID(), videoMessage.getVideoMediaTp());
        } else {
            UIShow.sendBroadcast(App.getActivity(), videoMessage.getVideoTp(), videoMessage.getVc_channel_key());
        }
    }

    /**
     * 红包消息
     *
     * @param message
     */
    private void setTalkMsg(BaseMessage message) {
        if (message == null) return;

        JSONObject jsonObject = message.getJsonObj();
        int red_log_id = jsonObject.optInt("red_log_id");
        String content = jsonObject.optString("mct");
        UIShow.showChatRedBoxDialog((Activity) App.getActivity(), red_log_id, content);
    }

    private void setSystemMsg(BaseMessage message) {
        if (message != null && !(message instanceof SystemMessage)) return;
        SystemMessage mess = (SystemMessage) message;
        if (mess != null) {
            switch (mess.getXtType()) {
                case 3:
                    ModuleMgr.getChatMgr().updateOtherRead(null, mess.getFid() + "", mess.getTid(), mess);
                    break;
            }
        }
    }
}