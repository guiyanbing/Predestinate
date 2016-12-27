package com.xiaochen.android.fate_it.module.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

/**
 * Application
 * Created by ZRP on 2016/9/8.
 */
public class APP extends Application {

    public static Context context;
    public static Activity activity;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        initPlugins(this);

        registerActivityLifecycleCallbacks(new PActivityLifecycleCallbacks());
    }

    private void initPlugins(Context context) {
        ModuleInitial initial = new ModuleInitial(context);
        initial.initial();
    }

    public static Context getActivity() {
        return activity == null ? context : activity;
    }
}
