package com.juxin.predestinate.ui.user.paygoods.vip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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
 * 老：VIP充值弹框
 * Created by Su on 2017/5/5.
 */
public class GoodsVipDlgOld extends BaseActivity implements View.OnClickListener {
    private PayGoods payGoods;  // 商品信息
    private GoodsListPanel goodsPanel;
    private GoodsPayTypePanel payTypePanel; // 支付方式

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_goods_vip_dialog_old);

        initView();
    }

    private void initView() {
        findViewById(R.id.btn_recharge).setOnClickListener(this);

        fillGoodsPanel();
    }

    private void fillGoodsPanel() {
        LinearLayout container = (LinearLayout) findViewById(R.id.goods_container);
        goodsPanel = new GoodsListPanel(this, GoodsConstant.DLG_YCOIN_PRIVEDEG);
        container.addView(goodsPanel.getContentView());
        initList();

        // 支付方式
        LinearLayout payContainer = (LinearLayout) findViewById(R.id.pay_type_container);
        payTypePanel = new GoodsPayTypePanel(this, GoodsConstant.PAY_TYPE_OLD);
        payContainer.addView(payTypePanel.getContentView());
    }

    private void initList() {
        if (payGoods == null)
            payGoods = new PayGoods();

        String json = FileUtil.getFromAssets(App.getActivity(), "info_goods.json");
        try {
            if (TextUtils.isEmpty(json)) return;
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("vip_old"))
                payGoods.parseJson(jsonObject.optString("vip_old"));

            goodsPanel.refresh(payGoods.getCommodityList()); // 刷新列表
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:  // 充值
                PToast.showShort("type: " + payTypePanel.getPayType() + "goods: " + payGoods.getCommodityList().get(goodsPanel.getPosition()).getId());
                break;
        }
    }
}