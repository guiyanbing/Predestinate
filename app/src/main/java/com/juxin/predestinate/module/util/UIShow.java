package com.juxin.predestinate.module.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.juxin.predestinate.module.logic.baseui.WebActivity;
import com.juxin.predestinate.ui.main.MainActivity;
import com.juxin.predestinate.ui.start.FindPwdAct;
import com.juxin.predestinate.ui.start.LoginAct;
import com.juxin.predestinate.ui.start.NavUserAct;
import com.juxin.predestinate.ui.start.RegInfoAct;
import com.juxin.predestinate.ui.user.information.EditContentAct;
import com.juxin.predestinate.ui.user.information.UserEditSignAct;
import com.juxin.predestinate.ui.user.information.UserInfoAct;

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

    /**
     * 打开找回密码页
     */
    public static void showFindPwdAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, FindPwdAct.class);
        activity.startActivity(intent);
    }

    /**
     * 打开个人信息页
     */
    public static void showUserInfoAct(Context context) {
        context.startActivity(new Intent(context, UserInfoAct.class));
    }

    /**
     * 打开编辑昵称页
     */
    public static void showEditContentAct(FragmentActivity context, String defaultValue) {
        Intent intent = new Intent(context, EditContentAct.class);
        intent.putExtra("defaultValue", defaultValue);
        context.startActivity(intent);
    }

    /**
     * 打开编辑个性签名页
     */
    public static void showUserEditSignAct(FragmentActivity context, String sign) {
        Intent intent = new Intent(context, UserEditSignAct.class);
        intent.putExtra("sign", sign);
        context.startActivity(intent);
    }
}
