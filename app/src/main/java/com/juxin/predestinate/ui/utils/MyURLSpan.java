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
import com.juxin.predestinate.module.util.BaseUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.auth.MyAuthenticationAct;

/**
 * 对TextView设置显示文字及html展示点击行为
 * Created by siow on 2017/5/8.
 */
public class MyURLSpan extends ClickableSpan {

    private final static int URL_TYPE_UPLOAD_HEADPIC = 1;// 上传头像
    private final static int URL_TYPE_COMPLETE_INFO = 2;// 完善资料
    private final static int URL_TYPE_BIND_PHONE = 3;// 绑定手机
    private final static int URL_TYPE_CONNECT_QQ_SERVICE = 4;
    private final Context mContext;

    private String mUrl;

    private MyURLSpan(Context mContext, String url) {
        mUrl = url;
        this.mContext = mContext;
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
            if ("video_certification".equals(mUrl)) {
                UIShow.showMyAuthenticationVideoAct((FragmentActivity) mContext, 0);
            } else if (mUrl.startsWith("downex://") || mUrl.startsWith("downex1://") ||
                    mUrl.startsWith("downex2://") || mUrl.startsWith("downex3://")) {
                String sUrl = mUrl.replace("downex://", "http://");
                sUrl = sUrl.replace("downex1://", "http://");
                sUrl = sUrl.replace("downex2://", "ftp://");
                sUrl = sUrl.replace("downex3://", "https://");
                ModuleMgr.getHttpMgr().downloadApk(sUrl, new DownloadListener() {
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
                });
            } else if (BaseUtil.isInt(mUrl)) {
                int mIndex = Integer.parseInt(mUrl);
                switch (mIndex) {
                    case URL_TYPE_UPLOAD_HEADPIC:// 上传头像
                        UIShow.showUserRegHeadUploadAct(mContext);
                        break;
                    case URL_TYPE_COMPLETE_INFO:  // 完善资料
                        UIShow.showUserEditInfoAct(mContext);
                        break;
                    case URL_TYPE_BIND_PHONE:  // 绑定手机
                        UIShow.showPhoneVerifyAct((FragmentActivity) mContext,
                                MyAuthenticationAct.AUTHENTICSTION_REQUESTCODE); //跳手机认证页面
                        break;
                    case URL_TYPE_CONNECT_QQ_SERVICE:
                        UIShow.showQQService(mContext);
                        break;
                }
            } else {
                UIShow.showWebActivity(mContext, mUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}