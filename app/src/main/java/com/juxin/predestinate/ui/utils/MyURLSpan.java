package com.juxin.predestinate.ui.utils;

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
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.ApkUnit;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.auth.MyAuthenticationAct;

/**
 * 对TextView设置显示文字及html展示点击行为
 * Created by siow on 2017/5/8.
 */
public class MyURLSpan extends ClickableSpan {
    private final Context mContext;

    private final static String URL_TYPE_UPLOAD_HEAD_PIC = "1";//上传头像
    private final static String URL_TYPE_COMPLETE_INFO = "2";//完善资料
    private final static String URL_TYPE_BIND_PHONE = "3";//绑定手机
    private final static String URL_TYPE_CONNECT_QQ_SERVICE = "4";//QQ客服
    private final static String URL_TYPE_VIDEO_CERTIFICATION = "video_certification";//视频认证
    private final static String URL_TYPE_RECHARGE_YB = "recharge_yb";//充值Y币
    private final static String URL_TYPE_RECHARGE_VIP = "recharge_vip";//充值VIP
    private String mUrl;

    private MyURLSpan(Context mContext, String url) {
        this.mContext = mContext;
        mUrl = url;
    }

    /**
     * 设置TextView文字展示及其html格式内容点击行为
     *
     * @param mContext 上下文
     * @param tv       需要设置html点击效果展示的TextView
     * @param content  TextView展示的文字
     */
    public static void addClickToTextViewLink(Context mContext, TextView tv, String content) {
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
                MyURLSpan myURLSpan = new MyURLSpan(mContext, url.getURL());
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

            switch (mUrl){
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
                default:
                    int i = testDownExUrl(mUrl);
                    //是否自定义下载协议
                    if (i >= 0) {
                        String sUrl = mUrl.replace(DownExUrlHead[i], DownExUrlReplace[i]);
                        ModuleMgr.getHttpMgr().downloadApk(sUrl, downloadListener);
                    }
                    else
                        UIShow.showWebActivity(mContext, mUrl);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final static String[] DownExUrlHead = {"downex://", "downex1://", "downex2://", "downex3://"};
    private final static String[] DownExUrlReplace = {"http://", "http://", "ftp://", "https://"};

    private int testDownExUrl(String url){
        for (int i = DownExUrlHead.length - 1; i >= 0; i--) {
            if (url.startsWith(DownExUrlHead[i]))
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