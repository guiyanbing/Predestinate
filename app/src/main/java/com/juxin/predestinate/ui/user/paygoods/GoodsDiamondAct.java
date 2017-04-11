package com.juxin.predestinate.ui.user.paygoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

    // 支付方式
    private RelativeLayout payWeChat, payAli, payOther;
    private ImageView payChooseWeChat, payChooseAli, payChooseOther;

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
        payWeChat = (RelativeLayout) findViewById(R.id.pay_type_wexin);
        payAli = (RelativeLayout) findViewById(R.id.pay_type_alipay);
        payOther = (RelativeLayout) findViewById(R.id.pay_type_other);
        payChooseWeChat = (ImageView) findViewById(R.id.pay_choose_wexin);
        payChooseAli = (ImageView) findViewById(R.id.pay_choose_alipay);
        payChooseOther = (ImageView) findViewById(R.id.pay_choose_other);

        // 商品列表
        LinearLayout container = (LinearLayout) findViewById(R.id.pay_type_container);
        GoodsListPanel goodsPanel = new GoodsListPanel(this);
        container.addView(goodsPanel.getContentView());

        payWeChat.setOnClickListener(this);
        payAli.setOnClickListener(this);
        payOther.setOnClickListener(this);

        payWeChat.setSelected(true);
        payChooseWeChat.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_type_wexin:
                payType = PAY_TYPE_WECHAT;
                payWeChat.setSelected(true);
                payAli.setSelected(false);
                payChooseWeChat.setVisibility(View.VISIBLE);
                break;

            case R.id.pay_type_alipay:
                payType = PAY_TYPE_ALIPAY;
                payAli.setSelected(true);
                payChooseAli.setVisibility(View.VISIBLE);
                break;

            case R.id.pay_type_other:
                payType = PAY_TYPE_OTHER;
                payOther.setSelected(true);
                payChooseAli.setVisibility(View.VISIBLE);
                break;

        }
    }
}
