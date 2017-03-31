package com.juxin.predestinate.module.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.baseui.WebActivity;
import com.juxin.predestinate.module.logic.notify.view.LockScreenActivity;
import com.juxin.predestinate.module.logic.notify.view.UserMailNotifyAct;
import com.juxin.predestinate.ui.main.MainActivity;
import com.juxin.predestinate.ui.recommend.RecommendAct;
import com.juxin.predestinate.ui.setting.UsersSetAct;
import com.juxin.predestinate.ui.start.FindPwdAct;
import com.juxin.predestinate.ui.start.LoginAct;
import com.juxin.predestinate.ui.start.NavUserAct;
import com.juxin.predestinate.ui.start.RegInfoAct;
import com.juxin.predestinate.ui.user.check.UserCheckInfoAct;
import com.juxin.predestinate.ui.user.check.edit.EditContentAct;
import com.juxin.predestinate.ui.user.check.edit.UserEditSignAct;
import com.juxin.predestinate.ui.user.check.edit.UserInfoAct;
import com.juxin.predestinate.ui.user.check.edit.UserSecretAct;
import com.juxin.predestinate.ui.user.paygoods.GoodsDiamondAct;
import com.juxin.predestinate.ui.xiaoyou.CloseFriendsActivity;
import com.juxin.predestinate.ui.xiaoyou.NewTabActivity;
import com.juxin.predestinate.ui.xiaoyou.SelectContactActivity;
import com.juxin.predestinate.ui.xiaoyou.TabGroupActivity;

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
    //============================== 小友模块相关跳转 =============================
    /**
     * 打开标签分组页面
     */
    public static void showTabGroupAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, TabGroupActivity.class);
        activity.startActivity(intent);
    }
    /**
     * 打开亲密好友页面
     */
    public static void showCloseFriendsAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, CloseFriendsActivity.class);
        activity.startActivity(intent);
    }
    /**
     * 打开添加联系人页面
     */
    public static void showSelectContactAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, SelectContactActivity.class);
        activity.startActivity(intent);
    }
    /**
     * 打开添加标签页面
     */
    public static void showNewTabAct(FragmentActivity activity,long tab) {
        Intent intent = new Intent(activity, NewTabActivity.class);
        intent.putExtra("tab",tab);
        activity.startActivity(intent);
    }
    /**
     * 打开设置页
     */
    public static void showUserSetAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, UsersSetAct.class);
        activity.startActivity(intent);
    }
    /**
     * 打开推荐的人页面
     */
    public static void showRecommendAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, RecommendAct.class);
        activity.startActivity(intent);
    }

    /**
     * 打开个人信息页
     */
    public static void showUserInfoAct(Context context) {
        context.startActivity(new Intent(context, UserInfoAct.class));
    }

    /**
     * 打开主页
     */
    public static void showUserCheckInfoAct(Context context) {
        context.startActivity(new Intent(context, UserCheckInfoAct.class));
    }

    /**
     * 打开编辑昵称页
     */
    public static void showEditContentAct(FragmentActivity context) {
        Intent intent = new Intent(context, EditContentAct.class);
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

    /**
     * 打开钻石商品页
     */
    public static void showGoodsDiamondAct(Context context) {
        context.startActivity(new Intent(context, GoodsDiamondAct.class));
    }

    /**
     * 打开私密相册/视频
     */
    public static void showUserSecretAct(Context context) {
        context.startActivity(new Intent(context, UserSecretAct.class));
    }

    /**
     * 打开锁屏弹窗弹出的activity
     */
    public static void showLockScreenActivity() {
        Intent intent = new Intent(App.context, LockScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.context.startActivity(intent);
    }

    /**
     * 跳转到解锁后顶部消息提示
     *
     * @param type       消息类型
     * @param simpleData 简单的用户个人资料
     * @param content    聊天内容
     */
    public static void showUserMailNotifyAct(int type, UserInfoLightweight simpleData, String content) {
        int flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        if (Build.VERSION.SDK_INT >= 11) flags = flags | Intent.FLAG_ACTIVITY_CLEAR_TASK;

        Intent intent = new Intent(App.context, UserMailNotifyAct.class);
        intent.addFlags(flags);
        intent.putExtra("type", type);
        intent.putExtra("simple_data", simpleData);
        intent.putExtra("content", content);
        App.context.startActivity(intent);
    }
}
