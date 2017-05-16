package com.juxin.predestinate.ui.pay;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 支付选择方式
 * Created by Kind on 2017/4/19.
 */

public class PayListAct extends BaseActivity implements View.OnClickListener {

    public static boolean bPayOkFlag;

    private PayGood payGood;
    private BasePayPannel payAlipayPannel, payWXPannel, payCupPannel, payPhonecardPannel, payAlipayWebPannel, payVoicePannel;

    private TextView paylist_help_txt;
    private boolean help_txt = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_paylistact);
        setBackView(R.id.base_title_back, "支付订单");

        initView();
    }

    private void initView() {
        payGood = (PayGood) getIntent().getSerializableExtra("payGood");
        if (payGood == null) {
            MMToast.showShort(R.string.request_error);
            this.finish();
            return;
        }

        TextView paylist_title = (TextView) findViewById(R.id.paylist_title);

        paylist_title.setText(Html.fromHtml("订单信息：" + payGood.getPay_name() + " " + "<font color='#fd6c8e'>" + payGood.getPay_money() + "</font>" + " 元"));

        LinearLayout pay_listView = (LinearLayout) findViewById(R.id.paytype_list);
        payAlipayPannel = new PayAlipayPannel(this, payGood);
        payWXPannel = new PayWXPannel(this, payGood);
        payVoicePannel = new PayVoicePannel(this, payGood);
        payCupPannel = new PayCUPPannel(this, payGood);
        payAlipayWebPannel = new PayAlipayWebPannel(this, payGood);
        payPhonecardPannel = new PayPhoneCardPannel(this, payGood);

        pay_listView.addView(payAlipayPannel.getContentView());
        pay_listView.addView(payWXPannel.getContentView());
        pay_listView.addView(payVoicePannel.getContentView());
        pay_listView.addView(payCupPannel.getContentView());
        pay_listView.addView(payAlipayWebPannel.getContentView());
        pay_listView.addView(payPhonecardPannel.getContentView());

        findViewById(R.id.paylist_qq).setOnClickListener(this);
        paylist_help_txt = (TextView) findViewById(R.id.paylist_help_txt);
        findViewById(R.id.paylist_help).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.paylist_qq:{
                UIShow.showQQService(this);
            }
            case R.id.paylist_help:{
                if (help_txt) {
                    paylist_help_txt.setVisibility(View.VISIBLE);
                    help_txt = false;
                } else {
                    paylist_help_txt.setVisibility(View.GONE);
                    help_txt = true;
                }
                break;
            }
        }
    }
}
