package com.juxin.predestinate.module.logic.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.juxin.predestinate.bean.db.AppComponent;
import com.juxin.predestinate.bean.db.AppModule;
import com.juxin.predestinate.bean.db.DBModule;
import com.juxin.predestinate.bean.db.DaggerAppComponent;
import com.juxin.predestinate.module.logic.notify.LockScreenMgr;

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

    /**
     * AppComponent
     */
    private static AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(new PActivityLifecycleCallbacks());

        ModuleMgr.initModule(context);
//        initAppComponent();
        LockScreenMgr.getInstance().registerReceiver();//注册锁屏弹窗
        ModuleMgr.getLoginMgr().initCookie();
    }

    /**
     * @return 获取当前展示的activity对象，如果activity为null则返回applicationContext
     */
    public static Context getActivity() {
        return activity == null ? context : activity;
    }

    /**
     * @return 获取dagger2管理的全局实例
     */
    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    // ------------------------- 私有方法 ----------------------------

    /**
     * DB初始化
     */
    private void initAppComponent() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dBModule(new DBModule(App.uid))
                .build();
    }
}
