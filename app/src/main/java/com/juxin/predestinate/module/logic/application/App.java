package com.juxin.predestinate.module.logic.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Application
 * Created by ZRP on 2016/9/8.
 */
public class App extends Application {

    public static Context context;
    public static Activity activity;
    /**
     * 登录用户的uid（供运行时程序内调用）
     */
    public static long uid = 0;//全局uid，避免重复从本地获取

    /**
     * 发送需要登录信息的Http请求使用。
     */
    public static String cookie = "";
    /**
     * 用户是否已经登录。该值暂时无效
     */
    public static boolean isLogin = false;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(new PActivityLifecycleCallbacks());

        ModuleMgr.initModule(context);
    }

    public static Context getActivity() {
        return activity == null ? context : activity;
    }
}
