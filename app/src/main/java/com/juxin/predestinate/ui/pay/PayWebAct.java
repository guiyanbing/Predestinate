package com.juxin.predestinate.ui.pay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.ui.pay.utils.PayViewPanel;

/**
 * Created by Kind on 2017/4/26.
 */

public class PayWebAct extends BaseActivity{

    protected LinearLayout web_container;
    protected PayViewPanel payViewPanel;

    protected String url = "";// 网址url
    protected boolean isNeedEnc = true;// 网址是否需要加密
    protected String param = "";// 交互参数，如json字符串
    protected int payType;//支付类型

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCanBack(false);
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        payType = intent.getIntExtra("payType", -1);
        url = intent.getStringExtra("url");
        isNeedEnc = intent.getBooleanExtra("isNeedEnc", true);
        param = intent.getStringExtra("param");

        setContentView(R.layout.common_web_activity);
        initView();
    }

    private void initView() {
    //    setBackView(R.id.back_view, "手机卡支付");
        if(TextUtils.isEmpty(url)){
            MMToast.showShort("请求出错了!");
            this.finish();;
            return;
        }

        web_container = (LinearLayout) findViewById(R.id.web_container);
        payViewPanel = new PayViewPanel(this, url, isNeedEnc, param);
//        payViewPanel.setTitleListener(new PayViewPanel.TitleListener() {
//            @Override
//            public void onTitle(String title) {
//                setTitle(title);
//            }
//        });
        web_container.addView(payViewPanel.getContentView());
    }

    @Override
    public void onBackPressed() {
        if(payViewPanel != null){
//            if (payViewPanel.webView.canGoBack()) {
//                payViewPanel.webView.goBack();
//            } else {
//                finish();
//            }
        }else {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(payViewPanel != null){
            payViewPanel.onDestroy();
        }
    }
}
