package com.juxin.predestinate.ui.pay;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.goods.PayGood;

/**
 * Created by Kind on 2017/4/19.
 */

public class PayVoicePannel extends BasePayPannel {

    public PayVoicePannel(FragmentActivity activity, PayGood payGood) {
        super(activity, payGood);
        setImageIcon(R.drawable.y2_paytype_voice);
        setTextTitle("银联语音支付");
        setTextRemark("推荐！电话语音支付，操作简单，无需开通网银");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
