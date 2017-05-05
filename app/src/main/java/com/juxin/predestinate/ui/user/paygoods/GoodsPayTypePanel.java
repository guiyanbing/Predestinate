package com.juxin.predestinate.ui.user.paygoods;

import android.content.Context;
import android.view.View;

import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;

/**
 * 支付方式通用panel
 * <p>
 * Created by Su on 2017/5/4.
 */
public class GoodsPayTypePanel extends BaseViewPanel implements View.OnClickListener {
    private int payType = GoodsConstant.PAY_TYPE_WECHAT;  // 充值类型，默认微信充值

    private CustomFrameLayout payWeChat, payAli, payOther; // 支付方式

    /**
     * @param itemType 加载布局样式
     */
    public GoodsPayTypePanel(Context context, int itemType) {
        super(context);
        if (itemType == GoodsConstant.PAY_TYPE_NEW) {
            setContentView(R.layout.p1_goods_of_payment);
        } else {
            setContentView(R.layout.f1_goods_pay_type);
        }

        initView();
    }

    private void initView() {
        payWeChat = (CustomFrameLayout) findViewById(R.id.pay_type_wexin);
        payAli = (CustomFrameLayout) findViewById(R.id.pay_type_alipay);
        payOther = (CustomFrameLayout) findViewById(R.id.pay_type_other);

        payWeChat.setOnClickListener(this);
        payAli.setOnClickListener(this);
        payOther.setOnClickListener(this);

        payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
        payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
        payOther.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
    }

    /**
     * 获取支付类型
     */
    public int getPayType() {
        return payType;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                break;
        }
    }
}
