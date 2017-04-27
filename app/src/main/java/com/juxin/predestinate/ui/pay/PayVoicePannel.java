package com.juxin.predestinate.ui.pay;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.PayWX;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.UIShow;

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
        LoadingDialog.show(getActivity(), "");
        ModuleMgr.getCommonMgr().reqangelPayF(new RequestComplete() {
            @Override
            public void onRequestComplete(final HttpResponse response) {
                LoadingDialog.closeLoadingDialog(800, new TimerUtil.CallBack() {
                    @Override
                    public void call() {
                        PayWX payWX = new PayWX();
                        payWX.onPayAngelPayF(response.getResponseString());
                        UIShow.showPayVoiceAct(getActivity(), getPayGood(), payWX.isOK()? payWX : null);
                    }
                });
            }
        });
    }
}
