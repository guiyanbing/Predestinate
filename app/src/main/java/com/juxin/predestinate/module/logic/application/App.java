package com.juxin.predestinate.module.logic.application;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.multidex.MultiDexApplication;

import com.bugtags.library.Bugtags;
import com.bugtags.library.BugtagsOptions;

/**
 * Application
 * Created by ZRP on 2016/9/8.
 */
public class App extends MultiDexApplication {

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

    private static PActivityLifecycleCallbacks lifecycleCallbacks;
    private static int initFlag;

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        initAppDelay();
    }

    private void initAppDelay() {
        if (initFlag > 0)
            return;

        initFlag++;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                initApp();
                initFlag++;
            }
        });
    }

    private void initApp() {
        lifecycleCallbacks = new PActivityLifecycleCallbacks();
        registerActivityLifecycleCallbacks(lifecycleCallbacks);

        ModuleMgr.initModule(context);
        initBugTags();
    }

    /**
     * App是否已初始化完成
     *
     * @return
     */
    public static boolean isAppInited() {
        return initFlag > 1;
    }

    /**
     * 初始化Bugtags
     */
    private void initBugTags() {
        BugtagsOptions options = new BugtagsOptions.Builder()
                .trackingLocation(true)//是否获取位置
                .trackingCrashLog(true)//是否收集crash
                .trackingConsoleLog(true)//是否收集console log
                .trackingUserSteps(true)//是否收集用户操作步骤
                .crashWithScreenshot(true)//crash附带图
                .versionName(ModuleMgr.getAppMgr().getVerName())//自定义版本名称
                .versionCode(ModuleMgr.getAppMgr().getVerCode())//自定义版本号
                .channel(ModuleMgr.getAppMgr().getUMChannel())//渠道标识
                .trackingNetworkURLFilter("(.*)")//自定义网络请求跟踪的 url 规则
                .build();
        Bugtags.start("882cc0b7fdb25bed47b9fa577ea684f0", this, Bugtags.BTGInvocationEventNone, options);
    }

    /**
     * @return 获取进程生命周期回调
     */
    public static PActivityLifecycleCallbacks getLifecycleCallbacks() {
        return lifecycleCallbacks;
    }

    /**
     * @return 获取当前展示的activity对象，如果activity为null则返回applicationContext
     */
    public static Context getActivity() {
        return activity == null ? context : activity;
    }

    public static Context getContext() {
        return context;
    }

    /**
     * @return 获取资源读取对象
     */
    public static Resources getResource() {
        return context.getResources();
    }

    // -----------------------------------------------------

//    private static CacheComponent cacheComponent;
//
//    public static CacheComponent getCacheComponent() {
//        return cacheComponent;
//    }
//
//    /**
//     * 缓存初始化
//     */
//    private void initAppComponent() {
//        cacheComponent = DaggerCacheComponent.builder()
//                .cacheModule(new CacheModule((Application) getContext()))
//                .dBCache(new DBCache())
//                .build();
//    }
}
