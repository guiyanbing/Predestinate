package com.juxin.predestinate.module.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.juxin.predestinate.module.logic.base.WebActivity;
import com.juxin.predestinate.ui.main.MainActivity;
import com.juxin.predestinate.ui.start.LoginAct;
import com.juxin.predestinate.ui.start.NavUserAct;
import com.juxin.predestinate.ui.start.RegInfoAct;

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
     * 打开导航页
     */
    public static void showNavUserAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, NavUserAct.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }
    /**
     * 打开登录页
     */
    public static void showLoginAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, LoginAct.class);
        activity.startActivity(intent);
    }
    /**
     * 打开注册页
     */
    public static void showRegInfoAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, RegInfoAct.class);
        activity.startActivity(intent);
    }
}
