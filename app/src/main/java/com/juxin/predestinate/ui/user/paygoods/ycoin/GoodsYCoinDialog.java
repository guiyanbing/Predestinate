package com.juxin.predestinate.ui.user.paygoods.ycoin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;
import com.juxin.predestinate.ui.user.paygoods.GoodsListPanel;
import com.juxin.predestinate.ui.user.paygoods.GoodsPayTypePanel;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGoods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * y币充值弹框
 * Created by Su on 2017/5/4.
 */
public class GoodsYCoinDialog extends BaseActivity implements View.OnClickListener {
    private PayGoods payGoods;  // 商品信息
    private GoodsListPanel goodsPanel;
    private GoodsPayTypePanel payTypePanel; // 支付方式

    private ImageView img_select_vip;
    private boolean selectVip = true;    // 默认开通VIP

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_goods_ycoin_dialog);

        initView();
    }

    private void initView() {
        img_select_vip = (ImageView) findViewById(R.id.iv_pay_select_vip);
        img_select_vip.setOnClickListener(this);
        findViewById(R.id.btn_recharge).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);

        fillGoodsPanel();
    }

    private void fillGoodsPanel() {
        LinearLayout container = (LinearLayout) findViewById(R.id.goods_container);
        goodsPanel = new GoodsListPanel(this, GoodsConstant.DLG_YCOIN_PRIVEDEG);
        container.addView(goodsPanel.getContentView());
        initList();

        // 支付方式
        LinearLayout payContainer = (LinearLayout) findViewById(R.id.pay_type_container);
        payTypePanel = new GoodsPayTypePanel(this, GoodsConstant.PAY_TYPE_NEW);
        payContainer.addView(payTypePanel.getContentView());
    }

    private void initList() {
        if (payGoods == null)
            payGoods = new PayGoods();

        String json = FileUtil.getFromAssets(App.getActivity(), "info_goods.json");
        try {
            if (TextUtils.isEmpty(json)) return;
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("ycoin"))
                payGoods.parseJson(jsonObject.optString("ycoin"));

            goodsPanel.refresh(payGoods.getCommodityList()); // 刷新列表
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
    }

    /**
     * vip选择状态切换
     */
    private void switchSelVip() {
        if (selectVip) {
            img_select_vip.setBackgroundResource(R.drawable.ic_radio_nor);
            selectVip = false;
        } else {
            img_select_vip.setBackgroundResource(R.drawable.ic_radio_male_sel);
            selectVip = true;
        }
    }

    /**
     * 获取支付商品ID
     */
    private int getPayid() {
        if (!selectVip) {
            return payGoods.getCommodityList().get(goodsPanel.getPosition()).getId();
        }
        return payGoods.getCommodityList().get(goodsPanel.getPosition()).getSub_id();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_close:
                finish();
                break;

            case R.id.iv_pay_select_vip: // 开通Vip
                switchSelVip();
                break;

            case R.id.btn_recharge:  // 充值
                PToast.showShort("type: " + payTypePanel.getPayType() + "goods: " + getPayid());
                break;
        }
    }
}
