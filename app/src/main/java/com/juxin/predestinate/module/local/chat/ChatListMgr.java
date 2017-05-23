package com.juxin.predestinate.module.local.chat;

import android.app.Activity;
import android.app.Application;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.bean.db.AppComponent;
import com.juxin.predestinate.bean.db.AppModule;
import com.juxin.predestinate.bean.db.DBCenter;
import com.juxin.predestinate.bean.db.DBModule;
import com.juxin.predestinate.bean.db.DaggerAppComponent;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.UIShow;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by Kind on 2017/4/13.
 */

public class ChatListMgr implements ModuleBase, PObserver {

    private int unreadNum = 0;
    private List<BaseMessage> msgList = new ArrayList<>(); //私聊列表

    @Inject
    DBCenter dbCenter;

    @Override
    public void init() {
        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void release() {
    }

    public int getUnreadNumber() {
        return unreadNum;
    }

    /**
     * 转换消息角标个数
     *
     * @param unreadNum
     * @return
     */
    public String getUnreadNum(int unreadNum) {
        return unreadNum <= 99 ? String.valueOf(unreadNum) : "99+";
    }

    public List<BaseMessage> getMsgList() {
        List<BaseMessage> tempList = new ArrayList<>();
        synchronized (msgList) {
            tempList.addAll(msgList);
            return tempList;
        }
    }

    public void updateListMsg(List<BaseMessage> messages) {
        unreadNum = 0;
        msgList.clear();
        if (messages != null && messages.size() > 0) {
            msgList.addAll(messages);
            for (BaseMessage tmp : messages) {
                unreadNum += tmp.getNum();
            }
        }
//            unreadNum += getVisitNum();//最近访客
//            List<FriendInfo> friendInfos = ModuleMgr.getMsgCommonMgr().getFriendsData().getFriendData();
//            if (messages != null) {
//                for (BaseMessage tmp : messages) {
//                    boolean isB = MailSpecialID.getMailSpecialID(tmp.getLWhisperID());
//                    if (isB) {
//                        friendsList.add(tmp);
//                    } else {
//                        MMLog.autoDebug("friendInfos=" + friendInfos.size());
//                        if (ModuleMgr.getMsgCommonMgr().getFriendsData().isContains(tmp.getLWhisperID())) {
//                            friendsList.add(tmp);
//                        } else {
//                            newFriendListUnreadNum += tmp.getNum();
//                            newFriendList.add(tmp);
//                        }
//                    }
//                }
//            }
        MsgMgr.getInstance().sendMsg(MsgType.MT_User_List_Msg_Change, null);
        // updateBasicUserInfo();
    }

    //是否能聊天
    private String getIsTodayChatKey() {//是否显示问题反馈第一句KEY
        return "isTodayChat" + App.uid;
    }

    public void setTodayChatShow(boolean b) {//隐藏，显示私聊列表
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
    public void deleteBatchMessage(List<BaseMessage> messageList) {
        for (BaseMessage temp : messageList) {
            deleteMessage(temp.getLWhisperID());
        }
        getWhisperList();
    }

    public long deleteMessage(long userID) {
        return dbCenter.deleteMessage(userID);
    }

    /**
     * 删除聊天记录
     *
     * @param userID
     * @return
     */
    public long deleteFmessage(long userID) {
        long ret = dbCenter.deleteFmessage(userID);
        if (ret != DBConstant.ERROR) {
            getWhisperList();
        }
        return ret;
    }


    /**
     * 更新已读
     */
    public void updateToReadAll() {
        long ret = dbCenter.updateToReadAll();
        if (ret != DBConstant.ERROR) {
            getWhisperList();
        }
    }

    public void updateToRead(String channelID, String userID) {
        dbCenter.updateToRead(channelID, userID);
    }

    public void getWhisperList() {
        Observable<List<BaseMessage>> listObservable = dbCenter.queryLetterList();
        listObservable.subscribe(new Action1<List<BaseMessage>>() {
            @Override
            public void call(List<BaseMessage> baseMessages) {
                PLogger.printObject("xxxxxxxxxxx" + baseMessages.size());
                List<BaseMessage> messageList = BaseMessage.conversionListMsg(baseMessages);
                updateListMsg(messageList);
            }
        });
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_App_Login:
                PLogger.d("---ChatList_MT_App_Login--->" + value);
                if ((Boolean) value) {//登录成功
                    if (App.uid > 0) {
                        initAppComponent();
                        getAppComponent().inject(this);
                        ModuleMgr.getChatMgr().inject();
                        PLogger.d("uid=======" + App.uid);
                        getWhisperList();
                    }
                } else {
                    logout();
                }
                break;
        }
    }

    private void logout() {
        mAppComponent = null;
        msgList.clear();
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
            default:
                break;
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
        if (jsonObject == null) return;

        String content = jsonObject.optString("mct");
        UIShow.showChatRedBoxDialog((Activity) App.getActivity(), content);

    }
}