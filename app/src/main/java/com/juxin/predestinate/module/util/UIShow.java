package com.juxin.predestinate.module.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.juxin.predestinate.module.logic.base.WebActivity;
import com.juxin.predestinate.ui.main.MainActivity;

/**
 * 应用内页面跳转工具
 * Created by ZRP on 2016/12/9.
 */
public class UIShow {

    public static void show(Context context, Intent intent) {
        context.startActivity(intent);
    }

    public static void show(Context context, Class clz, int flag) {
        Intent intent = new Intent(context, clz);
        if (flag != -1) intent.addFlags(flag);
        show(context, intent);
    }

    public static void show(Context context, Class clz) {
        show(context, clz, -1);
    }

    /**
     * 跳转到主页并清除栈里的其他页面
     */
    public static void showMainClearTask(Context context) {
        show(context, MainActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 跳转到网页
     *
     * @param url 网页地址
     */
    public static void showWebActivity(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        show(context, intent);
    }

    /**
     * 跳转到系统设置面面
     *
     * @param context
     */
    public static void showNetworkSettings(Context context) {
        Intent intent = null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        context.startActivity(intent);
    }

}
