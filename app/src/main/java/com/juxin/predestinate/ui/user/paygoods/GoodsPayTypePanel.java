package com.juxin.predestinate.ui.user.paygoods;

import android.content.Context;
import android.view.View;

import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.config.PayTypeList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;

/**
 * 支付方式通用panel
 * <p>
 * Created by Su on 2017/5/4.
 */
public class GoodsPayTypePanel extends BaseViewPanel implements View.OnClickListener {
    private int payType = GoodsConstant.PAY_TYPE_WECHAT;  // 充值类型，默认微信充值

    private PayTypeList payTypeList;            // 在线配置控制支付类型
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
        payTypeList = ModuleMgr.getCommonMgr().getCommonConfig().getPayTypeList();

        payWeChat = (CustomFrameLayout) findViewById(R.id.pay_type_wexin);
        payAli = (CustomFrameLayout) findViewById(R.id.pay_type_alipay);
        payOther = (CustomFrameLayout) findViewById(R.id.pay_type_other);

        initPayType();
        payWeChat.setOnClickListener(this);
        payAli.setOnClickListener(this);
        payOther.setOnClickListener(this);

        initDefaultType();
    }

    /**
     * 支付方式显隐
     */
    private void initPayType() {
        if (payTypeList == null
                || payTypeList.getPayTypes() == null
                || payTypeList.getPayTypes().size() < 3) return;

        if (!payTypeList.getPayTypes().get(0).isEnable()) {
            disable(payTypeList.getPayTypes().get(0).getName());
        }

        if (!payTypeList.getPayTypes().get(1).isEnable()) {
            disable(payTypeList.getPayTypes().get(1).getName());
        }

        if (!payTypeList.getPayTypes().get(2).isEnable()) {
            disable(payTypeList.getPayTypes().get(2).getName());
        }
    }

    private void disable(String defaultType) {
        // 微信支付
        if (defaultType.equals(GoodsConstant.PAY_TYPE_WECHAT_NAME)) {
            payWeChat.setVisibility(View.GONE);
            return;
        }

        // 支付宝支付
        if (defaultType.equals(GoodsConstant.PAY_TYPE_ALIPAY_NAME)) {
            payAli.setVisibility(View.GONE);
            return;
        }

        // 其他支付
        if (defaultType.equals(GoodsConstant.PAY_TYPE_OTHER_NAME)) {
            payOther.setVisibility(View.GONE);
        }
    }

    /**
     * 默认支付方式
     */
    private void initDefaultType() {
        if (payTypeList == null
                || payTypeList.getPayTypes() == null
                || payTypeList.getPayTypes().size() < 3) return;


        String defaultType = payTypeList.getPayTypes().get(0).getName();

        // 微信支付
        if (defaultType.equals(GoodsConstant.PAY_TYPE_WECHAT_NAME)) {
            payType = GoodsConstant.PAY_TYPE_WECHAT;
            payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
            payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
            payOther.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
            return;
        }

        // 支付宝支付
        if (defaultType.equals(GoodsConstant.PAY_TYPE_ALIPAY_NAME)) {
            payType = GoodsConstant.PAY_TYPE_ALIPAY;
            payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
            payAli.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
            payOther.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
            return;
        }

        // 其他支付
        if (defaultType.equals(GoodsConstant.PAY_TYPE_OTHER_NAME)) {
            payType = GoodsConstant.PAY_TYPE_OTHER;
            payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
            payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
            payOther.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
        }
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
