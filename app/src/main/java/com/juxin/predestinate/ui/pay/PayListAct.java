package com.juxin.predestinate.ui.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

/**
 * 支付选择方式
 * Created by Kind on 2017/4/19.
 */

public class PayListAct extends BaseActivity {

    private BasePayPannel payAlipayPannel, payWXPannel, payCupPannel, payPhonecardPannel, payAlipayWebPannel, payVoicePannel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.y2_paylistact);
        setBackView(R.id.base_title_back, "支付订单");


        LinearLayout pay_listView = (LinearLayout) findViewById(R.id.paytype_list);


        payAlipayPannel = new PayAlipayPannel(this);
        payWXPannel = new PayWXPannel(this);
        payVoicePannel = new PayVoicePannel(this);
        payCupPannel = new PayCUPPannel(this);
        payAlipayWebPannel = new PayAlipayWebPannel(this);
        payPhonecardPannel = new PayPhoneCardPannel(this);


        pay_listView.addView(payAlipayPannel.getContentView());
        pay_listView.addView(payWXPannel.getContentView());
        pay_listView.addView(payVoicePannel.getContentView());
        pay_listView.addView(payCupPannel.getContentView());
        pay_listView.addView(payAlipayWebPannel.getContentView());
        pay_listView.addView(payPhonecardPannel.getContentView());
    }
}
