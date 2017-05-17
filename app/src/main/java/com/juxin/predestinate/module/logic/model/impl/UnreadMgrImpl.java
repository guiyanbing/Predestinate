package com.juxin.predestinate.module.logic.model.impl;

import com.juxin.library.observe.ModuleBase;
import com.juxin.library.unread.UnreadMgr;
import com.juxin.mumu.bean.message.Msg;
import com.juxin.mumu.bean.message.MsgMgr;
import com.juxin.mumu.bean.message.MsgType;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.cache.PCache;

import java.util.HashMap;
import java.util.Map;

/**
 * 未读角标实现类
 * Created by ZRP on 2017/5/17.
 */
public class UnreadMgrImpl implements ModuleBase, MsgMgr.IObserver {

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

    @Override
    public void init() {
        MsgMgr.getInstance().attach(MsgType.MT_App_Login, this);//监听登录消息
    }

    @Override
    public void release() {
        MsgMgr.getInstance().detach(this);
    }

    /**
     * @return 获取未读角标管理类
     */
    public UnreadMgr getUnreadMgr() {
        return UnreadMgr.getInstance();
    }

    @Override
    public void onMessage(MsgType msgType, Msg msg) {
        switch (msgType) {
            case MT_App_Login:
                if ((Boolean) msg.getData()) {//如果是登录消息，重新初始化角标map
                    getUnreadMgr().init(PCache.getInstance().getCache(getStoreTag()), parentMap);
                    getUnreadMgr().setUnreadListener(new UnreadMgr.UnreadListener() {
                        @Override
                        public void onUnreadChange(String key, boolean isAdd, String storeString) {
                            PCache.getInstance().cacheString(getStoreTag(), storeString);
                        }
                    });
                }
                break;
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
}
