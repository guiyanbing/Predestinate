package com.juxin.predestinate.ui.pay;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.goods.PayGood;

/**
 * Created by Kind on 2017/4/19.
 */

public class PayPhoneCardPannel extends BasePayPannel {


    public PayPhoneCardPannel(FragmentActivity activity, PayGood payGood) {
        super(activity, payGood);
        setImageIcon(R.drawable.y2_paytype_phonecard);
        setTextTitle("手机充值卡支付");
        setTextRemark("支持全国移动、联通、电信手机充值卡");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
