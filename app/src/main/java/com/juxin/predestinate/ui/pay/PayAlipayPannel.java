package com.juxin.predestinate.ui.pay;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.juxin.predestinate.R;

/**
 * Created by Kind on 2017/4/19.
 */

public class PayAlipayPannel extends BasePayPannel {


    public PayAlipayPannel(FragmentActivity activity) {
        super(activity);
        setImageIcon(R.drawable.y2_paytype_alipay);
        setTextTitle("支付宝");
        setTextRemark("推荐支付宝用户使用");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
