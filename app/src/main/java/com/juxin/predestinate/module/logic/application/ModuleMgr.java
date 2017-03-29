package com.juxin.predestinate.module.logic.application;

import android.content.Context;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.ModuleBase;
import com.juxin.library.observe.MsgMgr;
import com.juxin.predestinate.BuildConfig;
import com.juxin.predestinate.module.local.center.CenterMgr;
import com.juxin.predestinate.module.local.chat.ChatMgr;
import com.juxin.predestinate.module.local.common.CommonMgr;
import com.juxin.predestinate.module.local.login.LoginMgr;
import com.juxin.predestinate.module.logic.model.impl.AppMgrImpl;
import com.juxin.predestinate.module.logic.model.impl.HttpMgrImpl;
import com.juxin.predestinate.module.logic.model.mgr.AppMgr;
import com.juxin.predestinate.module.logic.model.mgr.HttpMgr;
import com.juxin.predestinate.module.logic.request.RequestHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块初始化工具
 * Created by ZRP on 2016/12/27.
 */
public final class ModuleMgr {

    private static List<ModuleBase> selfModules = new ArrayList<>();

    /**
     * 调用所有逻辑模块
     */
    public static void releaseAll() {
        for (ModuleBase item : selfModules) {
            try {
                item.release();
            } catch (Throwable e) {
                PLogger.printThrowable(e);
            }
        }
        selfModules.clear();
        selfModules = null;
    }

    /**
     * 按等级初始化逻辑模块
     */
    public static void initModule(Context context) {
        initStatic(context);
        getAppMgr();
        getHttpMgr();
    }

    private static void initStatic(Context context) {
        PLogger.init(BuildConfig.DEBUG);    //初始化日志打印
        PToast.init(context);               //初始化toast提示
        PSP.getInstance().init(context);    //初始化sharedPreferences存储
        MsgMgr.getInstance().initUiThread();//初始化主线程消息监听
        RequestHelper.getInstance().init(context);    //初始化网络请求
    }

    /**
     * 将一个新模块添加的管理器中，并调用其init方法
     *
     * @param module 一个新的模块
     */
    private static void addModule(final ModuleBase module) {
        module.init();
        selfModules.add(module);
    }

    /**
     * 应用模块。
     */
    private static AppMgr appMgr = null;

    /**
     * 取得应用信息管理的实例的唯一方法，统一从此处获得，全局唯一
     *
     * @return 返回应用管理器的实例
     */
    public static AppMgr getAppMgr() {
        if (appMgr == null) {
            appMgr = new AppMgrImpl();
            addModule(appMgr);
        }
        return appMgr;
    }

    /**
     * 网络请求模块
     */
    private static HttpMgr httpMgr = null;

    /**
     * 取得应用信息管理的实例的唯一方法，统一从此处获得，全局唯一
     *
     * @return 返回应用管理器的实例
     */
    public static HttpMgr getHttpMgr() {
        if (httpMgr == null) {
            httpMgr = new HttpMgrImpl();
            addModule(httpMgr);
        }
        return httpMgr;
    }

    /**
     * 个人中心管理
     */
    private static CenterMgr centerMgr = null;

    public static CenterMgr getCenterMgr() {
        if (centerMgr == null) {
            centerMgr = new CenterMgr();
            addModule(centerMgr);
        }
        return centerMgr;
    }
    /**
     * 登录管理
     */
    private static LoginMgr loginMgr = null;

    public static LoginMgr getLoginMgr() {
        if (loginMgr == null) {
            loginMgr = new LoginMgr();
            addModule(loginMgr);
        }
        return loginMgr;
    }

    /**
     * 通用
     */
    private static CommonMgr commonMgr = null;

    public static CommonMgr getCommonMgr() {
        if (commonMgr == null) {
            commonMgr = new CommonMgr();
            addModule(commonMgr);
        }
        return commonMgr;
    }

    private static ChatMgr chatMgr = null;

    public static ChatMgr getChatMgr() {
        if (chatMgr == null) {
            chatMgr = new ChatMgr();
            addModule(chatMgr);
        }
        return chatMgr;
    }

}
