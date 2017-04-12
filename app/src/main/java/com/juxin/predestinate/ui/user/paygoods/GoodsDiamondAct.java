package com.juxin.predestinate.ui.user.paygoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.juxin.library.log.PToast;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

/**
 * 钻石商品
 * Created by Su on 2017/3/31.
 */
public class GoodsDiamondAct extends BaseActivity implements View.OnClickListener {
    private static final int PAY_TYPE_WECHAT = 0;  // 微信支付
    private static final int PAY_TYPE_ALIPAY = 1;  // 支付宝支付
    private static final int PAY_TYPE_OTHER = 2;   // 其他支付
    private static final int PAY_STATUS_UNCHOOSE = 0; // 未选中支付
    private static final int PAY_STATUS_CHOOSE = 1;   // 选中支付

    private GoodsListPanel goodsPanel;
    private CustomFrameLayout payWeChat, payAli, payOther; // 支付方式

    private int payType = PAY_TYPE_WECHAT;  // 默认支付方式为微信支付

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_goods_diamond_act);

        initTitle();
        initView();
    }

    private void initTitle() {
        setBackView();
        setTitle(getString(R.string.goods_diamond_title));
    }

    private void initView() {

        // 支付方式
        payWeChat = (CustomFrameLayout) findViewById(R.id.pay_type_wexin);
        payAli = (CustomFrameLayout) findViewById(R.id.pay_type_alipay);
        payOther = (CustomFrameLayout) findViewById(R.id.pay_type_other);

        // 商品列表
        LinearLayout container = (LinearLayout) findViewById(R.id.pay_type_container);
        goodsPanel = new GoodsListPanel(this);
        container.addView(goodsPanel.getContentView());

        payWeChat.setOnClickListener(this);
        payAli.setOnClickListener(this);
        payOther.setOnClickListener(this);
        findViewById(R.id.btn_recharge).setOnClickListener(this);
        findViewById(R.id.btn_contact).setOnClickListener(this);

        payWeChat.showOfIndex(PAY_STATUS_CHOOSE);
        payAli.showOfIndex(PAY_STATUS_UNCHOOSE);
        payOther.showOfIndex(PAY_STATUS_UNCHOOSE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_type_wexin:
                payType = PAY_TYPE_WECHAT;
                payWeChat.showOfIndex(PAY_STATUS_CHOOSE);
                payAli.showOfIndex(PAY_STATUS_UNCHOOSE);
                payOther.showOfIndex(PAY_STATUS_UNCHOOSE);
                break;

            case R.id.pay_type_alipay:
                payType = PAY_TYPE_ALIPAY;
                payAli.showOfIndex(PAY_STATUS_CHOOSE);
                payWeChat.showOfIndex(PAY_STATUS_UNCHOOSE);
                payOther.showOfIndex(PAY_STATUS_UNCHOOSE);
                break;

            case R.id.pay_type_other:
                payType = PAY_TYPE_OTHER;
                payOther.showOfIndex(PAY_STATUS_CHOOSE);
                payWeChat.showOfIndex(PAY_STATUS_UNCHOOSE);
                payAli.showOfIndex(PAY_STATUS_UNCHOOSE);
                break;

            case R.id.btn_recharge: // 立即充值

                PToast.showShort("position: " + goodsPanel.getPosition() + ",  type: " + payType);

                break;

            case R.id.btn_contact:  // 联系客服
                break;
        }
    }
}
