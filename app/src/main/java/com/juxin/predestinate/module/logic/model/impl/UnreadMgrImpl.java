package com.juxin.predestinate.module.logic.model.impl;

import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.unread.UnreadMgr;
import com.juxin.predestinate.bean.db.DBCenter;
import com.juxin.predestinate.module.local.chat.ChatSpecialMgr;
import com.juxin.predestinate.module.local.chat.inter.ChatMsgInterface;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;

/**
 * 未读角标实现类
 * Created by ZRP on 2017/5/17.
 */
public class UnreadMgrImpl implements ModuleBase, ChatMsgInterface.UnreadReceiveMsgListener, PObserver {

    // =========================未读角标配置（添加新的角标类型后在此进行配置）============================

    /* --------一级角标：最顶层父级-------- */
    public static final String MAIL = "MAIL";                           //信箱
    public static final String CENTER = "CENTER";                       //个人中心
    /* --------二级角标-------- */
    public static final String FOLLOW_ME = "FOLLOW_ME";                 //谁关注我
    public static final String MY_WALLET = "MY_WALLET";                 //我的钱包
    /* --------三级角标-------- */
    // 暂无
    /* --------游离角标-------- */
    // 暂无

    /* --------未读消息的级联关系，每次添加新的层级角标之后在此进行配置-------- */
    private static final Map<String, String[]> parentMap = new HashMap<String, String[]>() {
        {
            put(FOLLOW_ME, new String[]{CENTER});
            put(MY_WALLET, new String[]{CENTER});
        }
    };

    // ====================================未读角标配置 end============================================

    @Inject
    DBCenter dbCenter;

    @Override
    public void init() {
        MsgMgr.getInstance().attach(this);//监听登录消息
        ChatSpecialMgr.getChatSpecialMgr().attachUnreadMsgListener(this);//监听抛出的角标消息
    }

    @Override
    public void release() {
        ChatSpecialMgr.getChatSpecialMgr().detachUnreadMsgListener(this);
    }

    /**
     * @return 获取未读角标管理类
     */
    public UnreadMgr getUnreadMgr() {
        return UnreadMgr.getInstance();
    }

    @Override
    public void onUpdateUnread(BaseMessage message) {
        switch (message.getType()) {//比对抛出的未读类型消息，进行角标的添加
            //TODO 聊天红包消息
        }
    }

    /**
     * 获取角标数据存储的key，以uid进行存储，便于用户切换帐号处理
     *
     * @return 获取最终存储用户角标信息的标签
     */
    private String getStoreTag() {
        return "unread_" + String.valueOf(ModuleMgr.getLoginMgr().getUid());
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_App_Login:
                if ((Boolean) value) {// 登录成功
                    // 注入dagger组件
                    ModuleMgr.getChatListMgr().getAppComponent().inject(this);

                    // 初始化角标数据
                    Observable<String> observable = dbCenter.queryUnRead(getStoreTag());
                    observable.subscribe(new Action1<String>() {
                        @Override
                        public void call(String storeString) {
                            getUnreadMgr().init(storeString, parentMap);
                        }
                    });
                    // 角标变更监听，每次变更之后更新数据库
                    getUnreadMgr().setUnreadListener(new UnreadMgr.UnreadListener() {
                        @Override
                        public void onUnreadChange(String key, boolean isAdd, String storeString) {
                            dbCenter.insertUnRead(getStoreTag(), storeString);
                        }
                    });
                }
                break;
        }
    }
}