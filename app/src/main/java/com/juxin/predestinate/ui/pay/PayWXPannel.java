package com.juxin.predestinate.ui.pay;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.ui.pay.utils.PayWeixinUtils;

/**
 * Created by Kind on 2017/4/19.
 */

public class PayWXPannel extends BasePayPannel {

    private PayWeixinUtils payWeixinUtils;

    public PayWXPannel(FragmentActivity activity, PayGood payGood) {
        super(activity, payGood);
        setImageIcon(R.drawable.y2_paytype_weixin);
        setTextTitle("微信支付");
        setTextRemark("推荐开通了微信支付功能的用户使用");
        payWeixinUtils = new PayWeixinUtils(activity);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        payWeixinUtils.onPayment(getPayGood());
    }
}