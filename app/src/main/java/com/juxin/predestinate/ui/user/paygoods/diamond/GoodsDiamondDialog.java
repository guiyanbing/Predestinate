package com.juxin.predestinate.ui.user.paygoods.diamond;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.juxin.library.log.PToast;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;

/**
 * 钻石充值弹框
 * Created by Su on 2017/4/12.
 */

public class GoodsDiamondDialog extends BaseActivity implements View.OnClickListener {

    private CustomFrameLayout payWeChat, payAli, payOther; // 支付方式
    private int payType = GoodsConstant.PAY_TYPE_WECHAT;  // 默认支付方式为微信支付

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_goods_diamond_dialog);

        initView();
    }

    private void initView() {

        // 支付方式
        payWeChat = (CustomFrameLayout) findViewById(R.id.pay_type_wexin);
        payAli = (CustomFrameLayout) findViewById(R.id.pay_type_alipay);
        payOther = (CustomFrameLayout) findViewById(R.id.pay_type_other);

        payOther.setVisibility(View.GONE);
        payWeChat.setOnClickListener(this);
        payAli.setOnClickListener(this);
        findViewById(R.id.btn_recharge).setOnClickListener(this);

        payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
        payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_type_wexin:
                payType = GoodsConstant.PAY_TYPE_WECHAT;
                payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
                payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                break;

            case R.id.pay_type_alipay:
                payType = GoodsConstant.PAY_TYPE_ALIPAY;
                payAli.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
                payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                break;

            case R.id.btn_recharge:  // 充值
                PToast.showShort("type: " + payType);
                break;

        }
    }
}
