package com.juxin.predestinate.module.logic.application;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.LinkedList;

/**
 * Application中activity生命周期监测回调，>=API14
 * Created by ZRP on 2016/9/8.
 */
public class PActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    // 本地模拟的activity栈链表，记录activity跳转行为
    private LinkedList<Activity> activities = new LinkedList<>();

    /**
     * @return 获取本地模拟的activity栈链表
     */
    public LinkedList<Activity> getActivities() {
        return activities;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        App.activity = activity;
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        App.activity = activity;

        // 本地模拟一个只存储10条记录的activity栈
        activities.add(activity);
        if (activities.size() > 10) activities.remove(0);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
