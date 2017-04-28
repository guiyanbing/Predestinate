package com.juxin.predestinate.ui.pay.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.juxin.mumu.bean.net.BasicNameValuePair;
import com.juxin.mumu.bean.net.NameValuePair;
import com.juxin.mumu.bean.utils.MD5;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.PayWX;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import eposp.wtf_library.core.WtfPlugin;

/**
 * Created by Kind on 2017/4/24.
 */

public class PayWeixinUtils {

    private Context context;
    private IWXAPI api;

    private int iPayIdx;
    private int[] aryPayType = {0, 3, 6};

    public PayWeixinUtils(Context context) {
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, "");
    }


    public void onPayment(final PayGood payGood) {
        if (!api.isWXAppInstalled()) {
            MMToast.showShort("未安装微信!");
            return;
        }

        int payCType = -1;
        if (iPayIdx < aryPayType.length && aryPayType[iPayIdx] != 0) {
            payCType = aryPayType[iPayIdx];
        }


        ModuleMgr.getCommonMgr().reqWXMethod(payGood.getPay_name(), payGood.getPay_id(), payGood.getPay_money(), payCType, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                JSONObject jsonObject = response.getResponseJson();
                PayWX payWX = new PayWX(response.getResponseString());
                if(payWX.getPayData()!= null){
                    To_Pay(payWX);
                }else {
                    if (payWX.getPayType() == 1 && iPayIdx < aryPayType.length - 1) {
                        iPayIdx++;
                        onPayment(payGood);
                        return;
                    } else
                        MMToast.showShort(R.string.request_error);
                }

            }
        });
    }

    private void To_Pay(PayWX payWX) {
        switch (payWX.getPayType()) {
            case 1:
                To_Pay_WeiXin_SDK(payWX);
                break;
            case 2:
                To_Pay_WeiXin_Protocol(payWX.getPayData());
                break;
            case 3:
            case 6:
                To_Pay_WeiXin_Http(payWX.getPayData());
                break;
            case 4:
                To_Pay_WeiXin_Wft_Wap(payWX.getPayData());
                break;
            case 5:
                To_Pay_WeiXin_Wft_App(payWX.getPayData());
                break;
            default:
                MMToast.showShort("不支持的支付类型！");
                break;
        }
    }

    private void To_Pay_WeiXin_SDK(PayWX payWX) {
        try {
            Constant.WEIXIN_APP_ID = payWX.getApp_id();

//            Random random = new Random();
            PayReq req = new PayReq();
            req.appId = payWX.getApp_id();
            req.partnerId = payWX.getMch_id();
            req.prepayId = payWX.getPrepay_id();
//            req.nonceStr = MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
            //MD5.encode(String.valueOf(random.nextInt(10000)));
            req.nonceStr = genOutTradNo();
            req.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            req.packageValue = "Sign=WXPay";

            List<NameValuePair> signParams = new LinkedList<>();
            signParams.add(new BasicNameValuePair("appid", req.appId));
            signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
            signParams.add(new BasicNameValuePair("package", req.packageValue));
            signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
            signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
            signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

            req.sign = genAppSign(signParams);

            api.registerApp(req.appId);
            api.sendReq(req);
        } catch (Exception e) {
            e.printStackTrace();
            MMToast.showShort("订单数据出错，请重新请求");
        }
    }

    private static String genAppSign(List<NameValuePair> params) {
        StringBuilder ss = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            ss.append(params.get(i).getName());
            ss.append('=');
            ss.append(params.get(i).getValue());
            ss.append('&');
        }
        ss.append("key=");

        ss.append(Constant.WEIXIN_App_Key);

//		sb.append("sign str\n" + ss.toString() + "\n\n");
        //String appSign = MD5.getMessageDigest(ss.toString().getBytes()).toUpperCase();
        String appSign = MD5.encode(ss.toString()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String genOutTradNo() {
        Random random = new Random();
        return MD5.encode(String.valueOf(random.nextInt(10000)));
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private void To_Pay_WeiXin_Protocol(String payData) {
        if (!payData.startsWith("weixin://")) {
            MMToast.showShort(R.string.request_error);
            return;
        }
        Uri uri = Uri.parse(payData);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            if (context instanceof Activity) {
                ((Activity) context).startActivityForResult(intent, Constant.PAY_WEIXIN);
            } else {
                context.startActivity(intent);
            }
        } else {
            MMToast.showShort("微信未安装");
        }
    }

    private void To_Pay_WeiXin_Http(String payData) {
        if (!payData.startsWith("http://") &&
                !payData.startsWith("https://")) {
            MMToast.showShort(R.string.request_error);
            return;
        }
        LoadingDialog.show((FragmentActivity) context, "");

        WebView webView = new WebView(context);
        // 设置WebView上的支持效果
        WebSettings webSettings = webView.getSettings();
        // 多窗口
        webSettings.supportMultipleWindows();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("https") || url.startsWith("http")) {
                    view.loadUrl(url);
                } else {
                    To_Pay_WeiXin_Protocol(url);
                    LoadingDialog.closeLoadingDialog();
                }
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                LoadingDialog.closeLoadingDialog();
                MMToast.showShort("支付失败");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LoadingDialog.show((FragmentActivity) context, "");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LoadingDialog.closeLoadingDialog();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            dealJavascriptLeak(webView);
        }

        webView.loadUrl(payData);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void dealJavascriptLeak(WebView webView) {
        webView.removeJavascriptInterface("searchBoxJavaBridge_");
        webView.removeJavascriptInterface("accessibility");
        webView.removeJavascriptInterface("accessibilityTraversal");
    }


    private void To_Pay_WeiXin_Wft_Wap(String payData) {
        try {
            if (TextUtils.isEmpty(payData)) {
                MMToast.showShort("订单数据出错，请重新请求");
                return;
            }

//            payData = "{\"tradeType\":\"pay.weixin.app\",\"productDesc\":\"钻石VIP月付\",\"callbackUrl\":\"\",\"orderNo\":\"2016102717573088224\",\"wx_key\":\"wx59b3a81e268f96a6\",\"privateKey\":\"8a4d7ee09f2db1dc793dcd0f9aeafc1f\",\"remark\":\"2016102717573088224\",\"orderAmount\":1,\"payType\":\"1\",\"merchantNo\":\"898875454113076\",\"notifyUrl\":\"http:\\/\\/p.app.yuanfenba.net\\/Pay\\/shzyAppPayNotify\",\"productName\":\"钻石VIP月付\"}";
            JSONObject jso = new JSONObject(payData);

            Constant.WEIXIN_APP_ID = jso.optString("wx_key");

            Map<String, String> data = new HashMap<>();
            data.put("merchantNo", jso.optString("merchantNo")); // 微通付商户号
            data.put("orderAmount", jso.optString("orderAmount")); //交易金额，分为单位
            data.put("orderNo", jso.optString("orderNo")); //第三方平台订单号
            data.put("notifyUrl", jso.optString("notifyUrl")); //
            data.put("callbackUrl", jso.optString("callbackUrl")); //
            data.put("payType", jso.optString("payType")); //交易方式，1：app支付；
            data.put("productName", jso.optString("productName")); //产品名
            data.put("productDesc", jso.optString("productDesc")); //产品描述
            data.put("remark", jso.optString("remark")); //备注
            data.put("privateKey", jso.optString("privateKey")); //微通付商户密钥
            data.put("wx_key", jso.optString("wx_key")); //微保平台
            data.put("tradeType", jso.optString("tradeType")); //交易类型，微信支付
            new WtfPlugin().getOrderForPay((Activity) context, data);
        } catch (Exception e) {
            e.printStackTrace();
            MMToast.showShort("订单数据出错，请重新请求");
        }
    }

    private void To_Pay_WeiXin_Wft_App(String payData) {
        try {
            if (TextUtils.isEmpty(payData)) {
                MMToast.showShort("订单数据出错，请重新请求");
                return;
            }

            JSONObject jso = new JSONObject(payData);

            Constant.WEIXIN_APP_ID = jso.optString("appid");

            RequestMsg msg = new RequestMsg();
            msg.setTokenId(jso.optString("token_id"));
            msg.setTradeType(MainApplication.WX_APP_TYPE);
            msg.setAppId(jso.optString("appid"));

            PayPlugin.unifiedAppPay((Activity) context, msg);
        } catch (Exception e) {
            e.printStackTrace();
            MMToast.showShort("订单数据出错，请重新请求");
        }
    }
}
