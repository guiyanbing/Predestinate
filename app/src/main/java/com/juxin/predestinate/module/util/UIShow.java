package com.juxin.predestinate.module.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentActivity;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.update.AppUpdate;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.recommend.TagInfoList;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.WebActivity;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.notify.view.LockScreenActivity;
import com.juxin.predestinate.module.logic.notify.view.UserMailNotifyAct;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.main.MainActivity;
import com.juxin.predestinate.ui.recommend.RecommendAct;
import com.juxin.predestinate.ui.recommend.RecommendFilterAct;
import com.juxin.predestinate.ui.setting.FeedBackAct;
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
import com.juxin.predestinate.ui.user.update.UpdateDialog;
import com.juxin.predestinate.ui.xiaoyou.CloseFriendsActivity;
import com.juxin.predestinate.ui.xiaoyou.NewTabActivity;
import com.juxin.predestinate.ui.xiaoyou.SelectContactActivity;
import com.juxin.predestinate.ui.xiaoyou.TabGroupActivity;

import java.util.Map;

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
     * 显示activity并清空栈里其他activity
     *
     * @param activity 要启动的activity
     */
    public static void showActivityClearTask(Context context, Class activity) {
        show(context, activity, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 跳转到主页并清除栈里的其他页面
     */
    public static void showMainClearTask(Context context) {
        showActivityClearTask(context, MainActivity.class);
    }

    /**
     * 跳转到首页的指定tab，并传值，通过MainActivity的OnNewIntent接收
     *
     * @param tabType 指定的跳转tab
     * @param dataMap 跳转时通过intent传递的map值
     */
    public static void showMainWithTabData(Context context, int tabType, Map<String, String> dataMap) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(FinalKey.HOME_TAB_TYPE, tabType);
        if (dataMap != null)
            for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        context.startActivity(intent);
    }

    /**
     * 跳转到网页
     *
     * @param type 1-侧滑页面，2-全屏页面，全屏时显示loading条
     * @param url  网页地址
     */
    public static void showWebActivity(Context context, int type, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("type", type);
        show(context, intent);
    }

    /**
     * 跳转到网页
     *
     * @param url 网页地址
     */
    public static void showWebActivity(Context context, String url) {
        showWebActivity(context, 1, url);
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
     * 打开找回密码页(手机绑定)
     *
     * @param activity
     * @param openAct  要打开的activity(FindPwdAct.OPEN_FINDPWD 找回密码  FindPwdAct.OPEN_BINDPHONE 绑定手机)
     */
    public static void showFindPwdAct(FragmentActivity activity, int openAct) {
        Intent intent = new Intent(activity, FindPwdAct.class);
        intent.putExtra("openAct", openAct);
        activity.startActivity(intent);
    }

    /**
     * 打开意见反馈页面
     *
     * @param activity
     */
    public static void showFeedBackAct(FragmentActivity activity) {
        activity.startActivity(new Intent(activity, FeedBackAct.class));
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
    public static void showNewTabAct(FragmentActivity activity, long tab) {
        Intent intent = new Intent(activity, NewTabActivity.class);
        intent.putExtra("tab", tab);
        activity.startActivity(intent);
    }

    /**
     * 打开设置页
     */
    public static void showUserSetAct(final Activity context, final int resultCode) {
        context.startActivityForResult(new Intent(context, UsersSetAct.class), resultCode);
    }

    /**
     * 打开推荐的人页面
     */
    public static void showRecommendAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, RecommendAct.class);
        activity.startActivity(intent);
    }

    /**
     * 打开推荐的人筛选页面
     */
    public static void showRecommendFilterAct(final FragmentActivity activity) {
        LoadingDialog.show(activity, activity.getResources().getString(R.string.tip_is_loading));
        ModuleMgr.getCommonMgr().sysTags(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                LoadingDialog.closeLoadingDialog();
                if (response.isOk()) {
                    Intent intent = new Intent(activity, RecommendFilterAct.class);
                    intent.putExtra("tags", (TagInfoList) response.getBaseData());
                    activity.startActivityForResult(intent, 100);
                } else {
                    PToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
                }
            }
        });
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

    // -----------------------消息提示跳转 start--------------------------

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

    // -----------------------消息提示跳转 end----------------------------

    /**
     * 调起软件强制升级弹窗
     *
     * @param activity  FragmentActivity上下文
     * @param appUpdate 软件升级信息
     */
    public static void showUpdateDialog(FragmentActivity activity, AppUpdate appUpdate) {
        if (appUpdate == null) return;
        if (appUpdate.getVersion() > ModuleMgr.getAppMgr().getVerCode()) {
            UpdateDialog updateDialog = new UpdateDialog();
            updateDialog.setData(appUpdate);
            updateDialog.showDialog(activity);
        }
    }
}
