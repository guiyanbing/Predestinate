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

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(new PActivityLifecycleCallbacks());

        initPlugins();
    }

    private void initPlugins() {
        ModuleMgr initial = new ModuleMgr(context);
        initial.initModule();
    }

    public static Context getActivity() {
        return activity == null ? context : activity;
    }
}
