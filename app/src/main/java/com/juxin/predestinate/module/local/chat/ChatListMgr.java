package com.juxin.predestinate.module.local.chat;

import android.app.Application;
import com.juxin.library.log.PLogger;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.bean.db.AppComponent;
import com.juxin.predestinate.bean.db.AppModule;
import com.juxin.predestinate.bean.db.DBCenter;
import com.juxin.predestinate.bean.db.DBModule;
import com.juxin.predestinate.bean.db.DaggerAppComponent;
import com.juxin.predestinate.bean.db.utils.DBConstant;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
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
    public void release() {}

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

    /**
     * 批量删除消息
     * @param messageList
     */
    public void deleteBatchMessage(List<BaseMessage> messageList) {
        for (BaseMessage temp : messageList){
            deleteMessage(temp.getLWhisperID());
        }
        getWhisperList();
    }

    public long deleteMessage(long userID) {
        return dbCenter.deleteMessage(userID);
    }

    /**
     * 删除聊天记录
     * @param userID
     * @return
     */
    public long deleteFmessage(long userID) {
        long ret =dbCenter.deleteFmessage(userID);
        if(ret != DBConstant.ERROR){
            getWhisperList();
        }
        return ret;
    }


    /**
     * 更新已读
     */
    public void updateToReadAll() {
        long ret = dbCenter.updateToReadAll();
        if(ret != DBConstant.ERROR){
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
                updateListMsg(baseMessages);
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
                        MMLog.autoDebug("uid=======" + App.uid);
                        getWhisperList();

//                        dbCenter.insertUnRead("1", "11111");
//                        dbCenter.insertUnRead("2", "11111");
//                        dbCenter.insertUnRead("3", "11111");
//                        dbCenter.insertUnRead("4", "11111");
//                        dbCenter.insertUnRead("5", "11111");
//                        dbCenter.insertUnRead("6", "11111");
//                        dbCenter.insertUnRead("7", "11111");
//
//                        Observable<String> observable = dbCenter.queryUnRead("key");
//                        observable.subscribe(new Action1<String>() {
//
//                            @Override
//                            public void call(String str) {
//                            }
//                        });
//
//
//                        Observable<Map<String, String>> observable = dbCenter.queryUnReadList();
//                        observable.subscribe(new Action1<Map<String,String>>() {
//
//                            @Override
//                            public void call(Map<String, String> stringMap) {
//                                PLogger.printObject("stringMap.size()" + stringMap.size());
//                            }
//                        });

                    }
                } else {
                    logout();
                }
                break;
        }
    }

    private void logout(){
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
}
