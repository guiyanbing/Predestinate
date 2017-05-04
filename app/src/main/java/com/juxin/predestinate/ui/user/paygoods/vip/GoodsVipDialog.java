package com.juxin.predestinate.ui.user.paygoods.vip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.FileUtil;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;
import com.juxin.predestinate.ui.user.paygoods.GoodsListPanel;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGoods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Vip充值弹框
 * Created by Su on 2017/5/4.
 */
public class GoodsVipDialog extends BaseActivity implements View.OnClickListener {

    private CustomFrameLayout payWeChat, payAli, payOther; // 支付方式
    private int payType = GoodsConstant.PAY_TYPE_WECHAT;  // 默认支付方式为微信支付

    private PayGoods payGoods;  // 商品信息
    private GoodsListPanel goodsPanel;

    private int rechargeType = GoodsConstant.DLG_VIP_PRIVEDEG; // 充值类型

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_goods_vip_dialog);

        initView();
    }

    private void initView() {
        // 支付方式
        payWeChat = (CustomFrameLayout) findViewById(R.id.pay_type_wexin);
        payAli = (CustomFrameLayout) findViewById(R.id.pay_type_alipay);
        payOther = (CustomFrameLayout) findViewById(R.id.pay_type_other);

        payWeChat.setOnClickListener(this);
        payAli.setOnClickListener(this);
        payOther.setOnClickListener(this);
        findViewById(R.id.btn_recharge).setOnClickListener(this);

        payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
        payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
        payOther.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);

        fillGoodsPanel();
    }

    private void fillGoodsPanel() {
        rechargeType = getIntent().getIntExtra(GoodsConstant.DLG_VIP_TYPE, GoodsConstant.DLG_VIP_PRIVEDEG);

        LinearLayout container = (LinearLayout) findViewById(R.id.pay_type_container);
        goodsPanel = new GoodsListPanel(this, rechargeType);
        container.addView(goodsPanel.getContentView());
        initList();
    }

    private void initList() {
        if (payGoods == null)
            payGoods = new PayGoods();

        String json = FileUtil.getFromAssets(App.getActivity(), "info_goods.json");
        try {
            if (TextUtils.isEmpty(json)) return;
            JSONObject jsonObject = new JSONObject(json);
            parseJson(jsonObject);
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
    }

    private void parseJson(JSONObject jsonObject) {
        switch (rechargeType) {
            case GoodsConstant.DLG_VIP_PRIVEDEG:
                if (jsonObject.has("vip_wakan"))
                    payGoods.parseJson(jsonObject.optString("vip_wakan"));
                break;

        }
        goodsPanel.refresh(payGoods.getCommodityList()); // 刷新列表
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

            case R.id.btn_recharge:  // 充值
                PToast.showShort("type: " + payType + "goods: " + payGoods.getCommodityList().get(goodsPanel.getPosition()).getId());
                break;
        }
    }
}
