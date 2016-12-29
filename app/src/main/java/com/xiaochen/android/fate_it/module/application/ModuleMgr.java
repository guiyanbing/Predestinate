package com.xiaochen.android.fate_it.module.application;

import android.content.Context;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.ModuleBase;
import com.xiaochen.android.fate_it.BuildConfig;
import com.xiaochen.android.fate_it.module.login.LoginMgr;
import com.xiaochen.android.fate_it.module.model.impl.AppMgrImpl;
import com.xiaochen.android.fate_it.module.model.mgr.AppMgr;
import com.xiaochen.android.fate_it.module.request.RequestHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块初始化工具
 * Created by ZRP on 2016/12/27.
 */
public class ModuleMgr {

    private static Context context;
    private static List<ModuleBase> selfModules = new ArrayList<>();

    public ModuleMgr(Context context) {
        this.context = context;
    }

    /**
     * 调用所有逻辑模块
     */
    public static void releaseAll() {
        int moduleNum = selfModules.size();

        for (ModuleBase item : selfModules) {
            try {
                item.release();
            } catch (Throwable e) {
                // MMDebug.classicAssert(false, e);
            }

            // KwDebug.classicAssert(allModules.size() == moduleNum,
            // "模块release函数里只能释放自己占用的资源，不允许访问其它模块" + item);
        }
        selfModules.clear();
        selfModules = null;
    }

    /**
     * 按等级初始化逻辑模块
     */
    public void initModule() {
        initStatic();
        getAppMgr();
    }

    private void initStatic() {
        PLogger.init(BuildConfig.DEBUG);    //初始化日志打印
        PToast.init(context);               //初始化toast提示
        PSP.getInstance().init(context);    //初始化sharedPreferences存储
        RequestHelper.getInstance().initCookie(LoginMgr.cookie);    //初始化网络请求
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
}
