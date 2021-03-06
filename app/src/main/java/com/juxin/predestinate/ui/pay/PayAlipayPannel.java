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
import com.juxin.predestinate.ui.pay.utils.PayAlipayUtils;

/**
 * Created by Kind on 2017/4/19.
 */

public class PayAlipayPannel extends BasePayPannel {


    public PayAlipayPannel(FragmentActivity activity, PayGood payGood) {
        super(activity, payGood);
        setImageIcon(R.drawable.y2_paytype_alipay);
        setTextTitle("支付宝");
        setTextRemark("推荐支付宝用户使用");
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        ModuleMgr.getCommonMgr().reqCUPOrAlipayMethod(UrlParam.reqAlipay, getOutTradeNo(), getPayGood().getPay_name(),
                getPayGood().getPay_id(), getPayGood().getPay_money(), new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        PayWX payWX = new PayWX(response.getResponseString(),true);
                        if(!payWX.isOK()){
                            PToast.showShort("请求失败，请稍后再试");
                            return;
                        }

                        PayAlipayUtils alipayUtils = new PayAlipayUtils(getActivity());
                        alipayUtils.pay(payWX.getCupPayType(), payWX.getParam());
                    }
                });
    }
}
