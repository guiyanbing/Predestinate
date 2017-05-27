package com.juxin.predestinate.ui.pay;

import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.PayWX;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

/**
 * 银联支付
 * Created by Kind on 2017/4/19.
 */

public class PayCUPPannel extends BasePayPannel {

    /******************************
     * 银联支付*********************************** mMode参数解释： "00" - 启动银联正式环境 "01" -
     * 连接银联测试环境
     *****************************************************************/
    private final String mMode = "00";

    public PayCUPPannel(FragmentActivity activity, PayGood payGood) {
        super(activity, payGood);
        setImageIcon(R.drawable.y2_paytype_unionpay);
        setTextTitle("银联手机支付");
        setTextRemark("银行最多，需验证预留手机号码，免网银");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        ModuleMgr.getCommonMgr().reqCUPOrAlipayMethod(UrlParam.reqUnionPay, getOutTradeNo(), getPayGood().getPay_name(),
                getPayGood().getPay_id(), getPayGood().getPay_money(), new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        PayWX payWX = new PayWX(response.getResponseString());
                        if (!payWX.isOK()) {
                            PToast.showShort("请求失败，请稍后再试");
                            return;
                        }
                        UPPayAssistEx.startPayByJAR(getActivity(), PayActivity.class, null, null, payWX.getPayID(), mMode);
                    }
                });
    }
}
