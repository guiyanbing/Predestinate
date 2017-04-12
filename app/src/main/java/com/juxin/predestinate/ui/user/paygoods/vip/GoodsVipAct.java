package com.juxin.predestinate.ui.user.paygoods.vip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;

/**
 * 开通VIP
 * Created by Su on 2017/4/12.
 */

public class GoodsVipAct extends BaseActivity implements View.OnClickListener{

    private TextView vipUsers;  // 开通VIP人数

    private CustomFrameLayout payWeChat, payAli, payOther; // 支付方式
    private int payType = GoodsConstant.PAY_TYPE_WECHAT;  // 默认支付方式为微信支付

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_goods_vip_act);

        initTitle();
        initView();
    }

    private void initTitle() {
        setBackView();
        setTitle(getString(R.string.goods_vip_title));
    }

    private void initView() {
        vipUsers = (TextView) findViewById(R.id.tv_vip_user);

        // 支付方式
        payWeChat = (CustomFrameLayout) findViewById(R.id.pay_type_wexin);
        payAli = (CustomFrameLayout) findViewById(R.id.pay_type_alipay);
        payOther = (CustomFrameLayout) findViewById(R.id.pay_type_other);

        payWeChat.setOnClickListener(this);
        payAli.setOnClickListener(this);
        payOther.setOnClickListener(this);
        findViewById(R.id.img_banner).setOnClickListener(this);
        findViewById(R.id.btn_dredge).setOnClickListener(this);

        payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
        payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
        payOther.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_banner:
                PToast.showShort(getString(R.string.goods_vip_banner_tip));
                break;

            case R.id.pay_type_wexin:
                payType = GoodsConstant.PAY_TYPE_WECHAT;
                payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
                payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                payOther.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                break;

            case R.id.pay_type_alipay:
                payType = GoodsConstant.PAY_TYPE_ALIPAY;
                payAli.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
                payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                payOther.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                break;

            case R.id.pay_type_other:
                payType = GoodsConstant.PAY_TYPE_OTHER;
                payOther.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
                payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                break;

            case R.id.btn_dredge:  // 立即开通
                PToast.showShort("type: " + payType);
                break;
        }
    }
}
