package com.juxin.predestinate.module.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.WindowManager;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.APKUtil;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.update.AppUpdate;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.bean.config.CommonConfig;
import com.juxin.predestinate.bean.recommend.TagInfoList;
import com.juxin.predestinate.module.local.pay.PayWX;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.WebActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.FinalKey;
import com.juxin.predestinate.module.logic.notify.view.LockScreenActivity;
import com.juxin.predestinate.module.logic.notify.view.UserMailNotifyAct;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.discover.DefriendAct;
import com.juxin.predestinate.ui.discover.MyDefriendAct;
import com.juxin.predestinate.ui.discover.MyFriendsAct;
import com.juxin.predestinate.ui.mail.chat.PrivateChatAct;
import com.juxin.predestinate.ui.main.MainActivity;
import com.juxin.predestinate.ui.pay.PayListAct;
import com.juxin.predestinate.ui.pay.PayWebAct;
import com.juxin.predestinate.ui.pay.cupvoice.PayCupVoiceDetailAct;
import com.juxin.predestinate.ui.pay.cupvoice.PayCupVoiceOkAct;
import com.juxin.predestinate.ui.pay.cupvoice.PayVoiceAct;
import com.juxin.predestinate.ui.pay.utils.PayPhoneCardAct;
import com.juxin.predestinate.ui.push.WebPushDialog;
import com.juxin.predestinate.ui.recommend.RecommendAct;
import com.juxin.predestinate.ui.recommend.RecommendFilterAct;
import com.juxin.predestinate.ui.setting.AboutAct;
import com.juxin.predestinate.ui.setting.FeedBackAct;
import com.juxin.predestinate.ui.setting.SearchTestActivity;
import com.juxin.predestinate.ui.setting.SettingAct;
import com.juxin.predestinate.ui.setting.SuggestAct;
import com.juxin.predestinate.ui.setting.UserModifyPwdAct;
import com.juxin.predestinate.ui.start.NavUserAct;
import com.juxin.predestinate.ui.start.PhoneVerifyAct;
import com.juxin.predestinate.ui.start.UserLoginExtAct;
import com.juxin.predestinate.ui.start.UserRegInfoAct;
import com.juxin.predestinate.ui.start.UserRegInfoCompleteAct;
import com.juxin.predestinate.ui.user.check.UserCheckInfoAct;
import com.juxin.predestinate.ui.user.check.edit.EditContentAct;
import com.juxin.predestinate.ui.user.check.edit.UserEditSignAct;
import com.juxin.predestinate.ui.user.check.edit.UserSecretAct;
import com.juxin.predestinate.ui.user.check.edit.info.UserEditInfoAct;
import com.juxin.predestinate.ui.user.check.other.UserOtherLabelAct;
import com.juxin.predestinate.ui.user.check.other.UserOtherSetAct;
import com.juxin.predestinate.ui.user.check.self.UserInfoAct;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;
import com.juxin.predestinate.ui.user.paygoods.diamond.GoodsDiamondAct;
import com.juxin.predestinate.ui.user.paygoods.diamond.GoodsDiamondDialog;
import com.juxin.predestinate.ui.user.paygoods.vip.GoodsVipAct;
import com.juxin.predestinate.ui.user.paygoods.vip.GoodsVipDialog;
import com.juxin.predestinate.ui.user.paygoods.vip.GoodsVipDlgOld;
import com.juxin.predestinate.ui.user.paygoods.ycoin.GoodsYCoinDialog;
import com.juxin.predestinate.ui.user.paygoods.ycoin.GoodsYCoinDlgOld;
import com.juxin.predestinate.ui.user.update.UpdateDialog;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.xiaoyou.CloseFriendsActivity;
import com.juxin.predestinate.ui.xiaoyou.IntimacyDetailActivity;
import com.juxin.predestinate.ui.xiaoyou.NewTabActivity;
import com.juxin.predestinate.ui.xiaoyou.SelectContactActivity;
import com.juxin.predestinate.ui.xiaoyou.TabGroupActivity;
import com.juxin.predestinate.ui.xiaoyou.wode.BottomGiftDialog;
import com.juxin.predestinate.ui.xiaoyou.wode.DemandRedPacketAct;
import com.juxin.predestinate.ui.xiaoyou.wode.DiamondSendGiftDlg;
import com.juxin.predestinate.ui.xiaoyou.wode.MyAttentionAct;
import com.juxin.predestinate.ui.xiaoyou.wode.MyDiamondsAct;
import com.juxin.predestinate.ui.xiaoyou.wode.MyDiamondsExplainAct;
import com.juxin.predestinate.ui.xiaoyou.wode.NearVisitorAct;
import com.juxin.predestinate.ui.xiaoyou.wode.RedBoxPhoneVerifyAct;
import com.juxin.predestinate.ui.xiaoyou.wode.RedBoxRecordAct;
import com.juxin.predestinate.ui.xiaoyou.wode.WithDrawApplyAct;
import com.juxin.predestinate.ui.xiaoyou.wode.WithDrawExplainAct;
import com.juxin.predestinate.ui.xiaoyou.wode.WithDrawSuccessAct;

import java.io.Serializable;
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
    public static void showUserLoginExtAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, UserLoginExtAct.class);
        activity.startActivity(intent);
    }

    /**
     * 打开注册页
     */
    public static void showUserRegInfoAct(FragmentActivity activity) {
        Intent intent = new Intent(activity, UserRegInfoAct.class);
        activity.startActivity(intent);
    }

    /**
     * 打开资料完善页
     */
    public static void showUserInfoCompleteAct(Context activity) {
        Intent intent = new Intent(activity, UserRegInfoCompleteAct.class);
        activity.startActivity(intent);
    }


    /**
     * 手机绑定
     *
     * @param activity
     * @param isVerify 是否绑定手机
     */
    public static void showPhoneVerify_Act(Context activity, boolean isVerify) {
        Intent intent = new Intent(activity, PhoneVerifyAct.class);
        intent.putExtra("isVerify", isVerify);
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

    /**
     * 打开意见反馈页面
     *
     * @param activity
     */
    public static void showSuggestAct(FragmentActivity activity) {
        activity.startActivity(new Intent(activity, SuggestAct.class));
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
    public static void showSelectContactAct(long tab, FragmentActivity activity) {
        Intent intent = new Intent(activity, SelectContactActivity.class);
        intent.putExtra("tab", tab);
        activity.startActivityForResult(intent, 0);
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
     * 打开添加亲密好友页面
     */
    public static void showIntimacyDetailAct(FragmentActivity activity, int tab) {
        Intent intent = new Intent(activity, IntimacyDetailActivity.class);
        intent.putExtra("tab", tab);
        activity.startActivity(intent);
    }

    /**
     * 打开设置页
     */
    public static void showUserSetAct(final Activity context, final int resultCode) {
        context.startActivityForResult(new Intent(context, SettingAct.class), resultCode);
    }

    /**
     * 打开关于页面
     */
    public static void showAboutAct(final Activity context) {
        context.startActivity(new Intent(context, AboutAct.class));
    }

    /**
     * 打开用户搜索测试彩蛋页面
     */
    public static void showSearchTestActivity(final Activity context) {
        context.startActivity(new Intent(context, SearchTestActivity.class));
    }

    /**
     * 打开修改密码页面
     */
    public static void showModifyAct(final Activity context) {
        context.startActivityForResult(new Intent(context, UserModifyPwdAct.class), 100);
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
     * 打开个人信息查看编辑页
     */
    public static void showUserEditInfoAct(Context context) {
        context.startActivity(new Intent(context, UserEditInfoAct.class));
    }

    /**
     * 打开TA人资料查看页
     */
    public static void showCheckOtherInfoAct(final Context context, long uid) {
        ModuleMgr.getCenterMgr().reqOtherInfo(uid, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                UserProfile userProfile = new UserProfile();
                userProfile.parseJson(response.getResponseString());

                if ("error".equals(userProfile.getResult())) {
                    PToast.showShort(context.getString(R.string.request_error));
                    return;
                }
                Intent intent = new Intent(context, UserCheckInfoAct.class);
                intent.putExtra(CenterConstant.USER_CHECK_INFO_KEY, CenterConstant.USER_CHECK_INFO_OTHER);
                intent.putExtra(CenterConstant.USER_CHECK_OTHER_KEY, userProfile);
                context.startActivity(intent);
            }
        });
    }

    /**
     * 打开自己资料查看页
     */
    public static void showCheckOwnInfoAct(Context context) {
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
     * 打开私密相册/视频
     */
    public static void showUserSecretAct(Context context) {
        context.startActivity(new Intent(context, UserSecretAct.class));
    }

    /**
     * 打开他人资料设置页
     */
    public static void showUserOtherSetAct(Context context, UserProfile userProfile) {
        Intent intent = new Intent(context, UserOtherSetAct.class);
        intent.putExtra(CenterConstant.USER_CHECK_OTHER_KEY, userProfile);
        context.startActivity(intent);
    }

    /**
     * 打开他人标签页
     */
    public static void showUserOtherLabelAct(Context context) {
        context.startActivity(new Intent(context, UserOtherLabelAct.class));
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
     * @param isShowTip 是否展示界面提示
     */
    public static void showUpdateDialog(final FragmentActivity activity, final AppUpdate appUpdate, boolean isShowTip) {
        if (appUpdate == null) return;
        // 如果不同包名且已安装升级包名的包，弹窗跳转到已安装的软件并退出当前软件，在新软件中处理升级逻辑
        if (!TextUtils.isEmpty(appUpdate.getPackage_name())
                && !ModuleMgr.getAppMgr().getPackageName().equals(appUpdate.getPackage_name())
                && APKUtil.isAppInstalled(App.context, appUpdate.getPackage_name())) {
            PickerDialogUtil.showTipDialogCancelBack(activity, new SimpleTipDialog.ConfirmListener() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onSubmit() {
                    if (APKUtil.launchApp(App.context, appUpdate.getPackage_name())) {
                        activity.moveTaskToBack(activity.isTaskRoot());
                    }
                }
            }, "检测到新版已安装，请点击跳转", "提示", "", "确定", false, false);
        }
        // 如果同包名或者包名返回为空，只要返回的版本号>当前已安装的版本号，就弹窗进行下载更新
        else if (appUpdate.getVersion() > ModuleMgr.getAppMgr().getVerCode()) {
            ModuleMgr.getCommonMgr().updateSaveUP();//更新时先保存用户信息备用
            UpdateDialog updateDialog = new UpdateDialog();
            updateDialog.setData(appUpdate);
            updateDialog.showDialog(activity);
        } else {
            if (isShowTip) PToast.showShort("您当前的版本为最新的");
        }
    }

    /**
     * 打开私信聊天内容页
     *
     * @param mContext  上下文
     * @param whisperID 私聊ID
     * @param name      名称（可有可无）
     */
    public static void showPrivateChatAct(Context mContext, long whisperID, String name) {
        showPrivateChatAct(mContext, whisperID, name, -1, null);
    }

    /**
     * 打开私信聊天内容页
     *
     * @param mContext  上下文
     * @param whisperID 私聊ID
     * @param name      名称（可有可无）
     * @param replyMsg  回复消息。一般情况是null
     */
    public static void showPrivateChatAct(Context mContext, long whisperID, String name, String replyMsg) {
        showPrivateChatAct(mContext, whisperID, name, -1, replyMsg);
    }

    /**
     * 打开私信聊天内容页
     *
     * @param mContext  上下文
     * @param whisperID 私聊ID
     * @param name      名称（可有可无）
     * @param kf_id     是否机器人（可有可无）
     */
    public static void showPrivateChatAct(Context mContext, long whisperID, String name, int kf_id) {
        showPrivateChatAct(mContext, whisperID, name, kf_id, null);
    }

    /**
     * 打开私信聊天内容页
     *
     * @param mContext  上下文
     * @param whisperID 私聊ID
     * @param name      名称（可有可无）
     * @param kf_id     是否机器人（可有可无）
     * @param replyMsg  回复消息。一般情况是null
     */
    public static void showPrivateChatAct(final Context mContext, final long whisperID, final String name, final int kf_id, final String replyMsg) {
        Intent intent = new Intent(mContext, PrivateChatAct.class);
        intent.putExtra("whisperID", whisperID);
        intent.putExtra("name", name);
        if (replyMsg != null)
            intent.putExtra("replyMsg", replyMsg);
        intent.putExtra("kf_id", kf_id);
        mContext.startActivity(intent);
    }

    /**
     * 通过配置调起的web-dialog弹框
     *
     * @param activity FragmentActivity实例
     */
    public static void showWebPushDialog(FragmentActivity activity) {
        CommonConfig commonConfig = ModuleMgr.getCommonMgr().getCommonConfig();
        if (commonConfig.canPushShow()) {
            WebPushDialog webPushAct = new WebPushDialog(activity, commonConfig.getPush_url(), commonConfig.getPushrate());
            webPushAct.showDialog(activity);
        }
    }

    /**
     * 选择支付
     *
     * @param activity
     */
    public static void showPayListAct(final FragmentActivity activity, int orderID) {
        LoadingDialog.show(activity, "生成订单中");
        ModuleMgr.getCommonMgr().reqGenerateOrders(orderID, new RequestComplete() {
            @Override
            public void onRequestComplete(final HttpResponse response) {
                MMLog.autoDebug("Re===" + response.getResponseString());
                LoadingDialog.closeLoadingDialog(800, new TimerUtil.CallBack() {
                    @Override
                    public void call() {
                        PayGood payGood = new PayGood(response.getResponseString());
                        if (payGood.isOK()) {
                            Intent intent = new Intent(activity, PayListAct.class);
                            intent.putExtra("payGood", (Serializable) payGood);
                            activity.startActivityForResult(intent, Constant.REQ_PAYLISTACT);
                        } else {
                            MMToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
                        }
                    }
                });
            }
        });

    }

    public static void showPayPhoneCardAct(final FragmentActivity activity, PayGood payGood, String orderID) {
        Intent intent = new Intent(activity, PayPhoneCardAct.class);
        intent.putExtra("payGood", payGood);
        intent.putExtra("orderID", orderID);
        activity.startActivityForResult(intent, Constant.PAYMENTACT);
    }

    public static void showPayVoiceAct(final FragmentActivity activity, PayGood payGood, PayWX payWX) {
        Intent intent = new Intent(activity, PayVoiceAct.class);
        intent.putExtra("payGood", payGood);
        if (payWX != null) {
            intent.putExtra("payWX", payWX);
        }
        activity.startActivityForResult(intent, Constant.PAYMENTACT);
    }

    /**
     * 新的语音支付详细页面
     */
    public static void shoPayCupVoiceDetailAct(Activity context, PayGood payGood, String bank_name, int resultCode) {
        Intent intent = new Intent(context, PayCupVoiceDetailAct.class);
        intent.putExtra("payGood", payGood);
        intent.putExtra("bank_name", bank_name);
        context.startActivityForResult(intent, resultCode);
    }

    /**
     * 新的语音支付详细页面
     */
    public static void showPayCupVoiceOkAct(Activity context, PayGood payGood, String phone,
                                            String nickname, String number, String bank_id, int resultCode) {
        Intent intent = new Intent(context, PayCupVoiceOkAct.class);
        intent.putExtra("payGood", payGood);
        intent.putExtra("phone", phone);
        intent.putExtra("nickname", nickname);
        if (number != null) {
            intent.putExtra("number", number);
        }
        if (bank_id != null) {
            intent.putExtra("bank_id", bank_id);
        }
        context.startActivityForResult(intent, resultCode);
    }

    public static void showPayWebAct(FragmentActivity activity, PayGood payGood) {
        Intent intent_web = new Intent(activity, PayWebAct.class);
        intent_web.putExtra("payGood", payGood);
        activity.startActivityForResult(intent_web, Constant.PAYMENTACT_TO);
    }


    /**
     * 打开QQ客服
     */
    public static void showQQServer(Context context) {
        try {
            String url = "mqqwpa://im/chat?chat_type=wpa&uin=" +
                    PSP.getInstance().getString(FinalKey.CONFIG_SERVICE_QQ, "2931837672");
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(intent);
            } else {
                PToast.showShort("QQ未安装");
            }
        } catch (Exception e) {
            PToast.showShort("QQ客服出错请使用电话客服");
        }
    }


    // -----------------------我的提示跳转 start----------------------------

    /**
     * 打开我的关注页面
     *
     * @param context
     */
    public static void showMyAttentionAct(Context context) {
        context.startActivity(new Intent(context, MyAttentionAct.class));
    }

    /**
     * 打开我的钻石页面
     *
     * @param context
     */
    public static void showMyDiamondsAct(Context context) {
        context.startActivity(new Intent(context, MyDiamondsAct.class));
    }

    /**
     * 打开最近来访页面
     *
     * @param context
     */
    public static void showNearVisitorAct(Context context) {
        context.startActivity(new Intent(context, NearVisitorAct.class));
    }

    /**
     * 打开我要赚钱页面
     *
     * @param context
     */
    public static void showDemandRedPacketAct(Context context) {
        context.startActivity(new Intent(context, DemandRedPacketAct.class));
    }

    /**
     * 打开钻石说明页面
     *
     * @param context
     */
    public static void showMyDiamondsExplainAct(Context context) {
        context.startActivity(new Intent(context, MyDiamondsExplainAct.class));
    }

    /**
     * 对话框送礼物
     *
     * @param context
     * @param giftid  要送的礼物id
     * @param to_id   统计id
     */
    public static void showDiamondSendGiftDlg(Context context, int giftid, String to_id) {
        DiamondSendGiftDlg dlg = new DiamondSendGiftDlg(context, giftid, to_id);
        dlg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dlg.show();
    }

    /**
     * 消息页面送礼物底部弹框
     *
     * @param context
     * @param to_id   他人id
     */
    public static void showBottomGiftDlg(Context context, long to_id) {
        BottomGiftDialog dialog = new BottomGiftDialog();
        dialog.setToId(to_id);
        dialog.showDialog((FragmentActivity) context);
    }

    /**
     * 打开我的钱包页面
     *
     * @param context
     */
    public static void showRedBoxRecordAct(Context context) {
        context.startActivity(new Intent(context, RedBoxRecordAct.class));
    }

    /**
     * 打开提现页面
     *
     * @param context
     */
    public static void showWithDrawApplyAct(int id, double money, boolean fromEdit, Context context) {
        Intent intent = new Intent(context, WithDrawApplyAct.class);
        intent.putExtra("id", id);
        intent.putExtra("money", money);
        intent.putExtra("fromEdit", fromEdit);
        context.startActivity(intent);
    }

    /**
     * 打开提现说明页面
     *
     * @param context
     */
    public static void showWithDrawExplainAct(Context context) {
        context.startActivity(new Intent(context, WithDrawExplainAct.class));
    }

    /**
     * 打开提现申请成功页面
     *
     * @param context
     */
    public static void showWithDrawSuccessAct(Context context) {
        context.startActivity(new Intent(context, WithDrawSuccessAct.class));
    }

    /**
     * 打开手机验证页面
     *
     * @param context
     */
    public static void showRedBoxPhoneVerifyAct(Context context) {
        context.startActivity(new Intent(context, RedBoxPhoneVerifyAct.class));
    }
    // -----------------------我的提示跳转 end----------------------------


    /**
     * 打开举报界面
     *
     * @param tuid    被举报人的uid
     * @param context 上下文
     */
    public static void showDefriendAct(long tuid, Context context) {
        Intent intent = new Intent(context, DefriendAct.class);
        intent.putExtra("tuid", tuid);
        context.startActivity(intent);
    }

    /**
     * 打开我的好友界面
     *
     * @param context
     */
    public static void showMyFriendsAct(Context context) {
        Intent intent = new Intent(context, MyFriendsAct.class);
        context.startActivity(intent);
    }

    /**
     * 打开黑名单界面
     *
     * @param context
     */
    public static void showMyDefriends(Context context) {
        Intent intent = new Intent(context, MyDefriendAct.class);
        context.startActivity(intent);
    }

    // -----------------------各种充值弹框跳转 start----------------------------

    /**
     * 钻石充值弹框
     */
    public static void showGoodsDiamondDialog(Context context) {
        context.startActivity(new Intent(context, GoodsDiamondDialog.class));
    }

    /**
     * VIP充值弹框
     *
     * @param rechargeType VIP充值类型
     */
    public static void showGoodsVipDialog(Context context, int rechargeType) {
        Intent intent = new Intent(context, GoodsVipDialog.class);
        intent.putExtra(GoodsConstant.DLG_VIP_TYPE, rechargeType);
        context.startActivity(intent);
    }

    /**
     * 老：VIP充值弹框
     */
    public static void showGoodsVipDlgOld(Context context) {
        context.startActivity(new Intent(context, GoodsVipDlgOld.class));
    }

    /**
     * Y币充值弹框
     *
     * @param remain   Y币余额
     * @param buyPower 需购买体力值
     */
    public static void showGoodsYCoinDialog(Context context, int remain, int buyPower) {
        Intent intent = new Intent(context, GoodsYCoinDialog.class);
        intent.putExtra(GoodsConstant.DLG_YCOIN_REMAIN, remain);
        intent.putExtra(GoodsConstant.DLG_YCOIN_POWER, buyPower);
        context.startActivity(intent);
    }

    /**
     * 老：Y币充值弹框
     */
    public static void showGoodsYCoinDlgOld(Context context) {
        context.startActivity(new Intent(context, GoodsYCoinDlgOld.class));
    }

    /**
     * 打开VIP开通页
     */
    public static void showGoodsVipAct(Context context) {
        context.startActivity(new Intent(context, GoodsVipAct.class));
    }

    /**
     * 打开钻石商品页
     */
    public static void showGoodsDiamondAct(Context context) {
        context.startActivity(new Intent(context, GoodsDiamondAct.class));
    }


}
