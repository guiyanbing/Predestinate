package com.juxin.predestinate.ui.pay;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.goods.PayGood;

/**
 * Created by Kind on 2017/4/19.
 */

public class PayCUPPannel extends BasePayPannel {


    public PayCUPPannel(FragmentActivity activity, PayGood payGood) {
        super(activity, payGood);
        setImageIcon(R.drawable.y2_paytype_unionpay);
        setTextTitle("银联手机支付");
        setTextRemark("银行最多，需验证预留手机号码，免网银");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
    }
}
