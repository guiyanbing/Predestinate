package com.juxin.predestinate.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.request.DownloadListener;
import com.juxin.predestinate.module.local.mail.MailSpecialID;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.ApkUnit;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;
import com.juxin.predestinate.ui.user.auth.MyAuthenticationAct;

/**
 * 对TextView设置显示文字及html展示点击行为
 * Created by siow on 2017/5/8.
 */
public class MyURLSpan extends ClickableSpan {
    private final static String URL_TYPE_UPLOAD_HEAD_PIC = "1";                         //上传头像
    private final static String URL_TYPE_COMPLETE_INFO = "2";                           //完善资料
    private final static String URL_TYPE_BIND_PHONE = "3";                              //绑定手机
    private final static String URL_TYPE_CONNECT_QQ_SERVICE = "4";                      //QQ客服
    private final static String URL_TYPE_VIDEO_CERTIFICATION = "video_certification";   //视频认证
    private final static String URL_TYPE_RECHARGE_YB = "recharge_yb";                   //充值Y币
    private final static String URL_TYPE_RECHARGE_VIP = "recharge_vip";                 //充值VIP
    private final static String URL_TYPE_CHECK_UPDATE = "check_update";                 //检查升级

    private final static String URL_TYPE_CERTIFY_REAL_NAME = "certify_real_name";                 //实名认证
    private final static String URL_TYPE_CERTIFY_PHONE = "certify_phone";                 //手机认证
    private final static String URL_TYPE_INVITE_VIDEO = "invite_video";                 //发起视频聊天
    private final static String URL_TYPE_SEND_GIFT = "send_gift";                 //送礼提示
    private final static String URL_TYPE_JUMP_KF = "jump_kf";                 //跳转到小秘书聊天框

    private Context mContext;
    private String mUrl;
    private long otherID;
    private String channel_uid;

    private MyURLSpan(Context mContext, String url, long otherID, String channel_uid) {
        this.mContext = mContext;
        this.mUrl = url;
        this.otherID = otherID;
        this.channel_uid = channel_uid;
    }

    public static void addClickToTextViewLink(Context mContext, TextView tv, String content) {
        addClickToTextViewLink(mContext, tv, content, -1, null);
    }

    /**
     * 设置TextView文字展示及其html格式内容点击行为
     *
     * @param mContext 上下文
     * @param tv       需要设置html点击效果展示的TextView
     * @param content  TextView展示的文字
     */
    public static void addClickToTextViewLink(Context mContext, TextView tv, String content, long otherID, String channel_uid) {
        tv.setText(Html.fromHtml(content + ""));
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence linkContent = tv.getText();
        if (linkContent instanceof Spannable) {
            int end = linkContent.length();
            Spannable sp = (Spannable) linkContent;
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(linkContent);
            for (URLSpan url : urls) {
                style.removeSpan(url);
                MyURLSpan myURLSpan = new MyURLSpan(mContext, url.getURL(), otherID, channel_uid);
                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv.setText(style);
        }
    }

    @Override
    public void onClick(View widget) {
        try {
            if (TextUtils.isEmpty(mUrl))
                return;

            switch (mUrl) {
                //上传头像
                case URL_TYPE_UPLOAD_HEAD_PIC:
                    UIShow.showUserRegHeadUploadAct(mContext);
                    break;
                //完善资料
                case URL_TYPE_COMPLETE_INFO:
                    UIShow.showUserEditInfoAct(mContext);
                    break;
                //绑定手机
                case URL_TYPE_BIND_PHONE:
                    UIShow.showPhoneVerifyAct((FragmentActivity) mContext, MyAuthenticationAct.AUTHENTICSTION_REQUESTCODE);
                    break;
                //QQ客服
                case URL_TYPE_CONNECT_QQ_SERVICE:
                    UIShow.showQQService(mContext);
                    break;
                //视频认证
                case URL_TYPE_VIDEO_CERTIFICATION:
                    UIShow.showMyAuthenticationVideoAct((FragmentActivity) mContext, 0);
                    break;
                //充值Y币
                case URL_TYPE_RECHARGE_YB:
                    UIShow.showBuyCoinActivity(mContext);
                    break;
                //充值VIP
                case URL_TYPE_RECHARGE_VIP:
                    UIShow.showOpenVipActivity(mContext);
                    break;
                //检查升级
                case URL_TYPE_CHECK_UPDATE:
                    ModuleMgr.getCommonMgr().checkUpdate((FragmentActivity) App.getActivity(), true);
                    break;
                //实名认证
                case URL_TYPE_CERTIFY_REAL_NAME:
                    UIShow.showIDCardAuthenticationAct((FragmentActivity) App.getActivity(), 0);
                    break;
                //手机认证
                case URL_TYPE_CERTIFY_PHONE:
                    UIShow.showPhoneVerifyAct((FragmentActivity) App.getActivity(), MyAuthenticationAct.AUTHENTICSTION_REQUESTCODE);
                    break;
                //发起视频聊天
                case URL_TYPE_INVITE_VIDEO:
                    VideoAudioChatHelper.getInstance().inviteVAChat((Activity) App.getActivity(),
                            otherID, VideoAudioChatHelper.TYPE_VIDEO_CHAT, true, Constant.APPEAR_TYPE_NO, channel_uid);
                    break;
                //送礼提示
                case URL_TYPE_SEND_GIFT:
                    UIShow.showBottomGiftDlg(App.getActivity(), otherID, Constant.OPEN_FROM_CHAT_FRAME, channel_uid);
                    break;
                //跳转到小秘书聊天框
                case URL_TYPE_JUMP_KF:
                    UIShow.showPrivateChatAct(App.getActivity(), MailSpecialID.customerService.getSpecialID(), null);
                    break;
                default:
                    int i = checkDownExUrl(mUrl);
                    //是否自定义下载协议
                    if (i >= 0) {
                        String sUrl = mUrl.replace(DownExUrlProtocol[i], DownExUrlReplace[i]);
                        ModuleMgr.getHttpMgr().downloadApk(sUrl, downloadListener);
                    } else
                        UIShow.showWebActivity(mContext, mUrl);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final static String[] DownExUrlProtocol = {"downex://", "downex1://", "downex2://", "downex3://"};
    private final static String[] DownExUrlReplace = {"http://", "http://", "ftp://", "https://"};

    /**
     * 检测URL是否自定义下载协议URL
     * @param url
     * @return -1 不是自定义下载协议URL  >= 0 自定义协议头在DownExUrlProtocol数组中的下标索引值
     */
    private int checkDownExUrl(String url) {
        for (int i = DownExUrlProtocol.length - 1; i >= 0; i--) {
            if (url.startsWith(DownExUrlProtocol[i]))
                return i;
        }
        return -1;
    }

    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onStart(String url, String filePath) {
        }

        @Override
        public void onProcess(String url, int process, long size) {
        }

        @Override
        public void onSuccess(String url, String filePath) {
            ApkUnit.ExecApkFile(mContext, filePath);
        }

        @Override
        public void onFail(String url, Throwable throwable) {
        }
    };
}