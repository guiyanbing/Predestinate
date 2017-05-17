package com.juxin.predestinate.ui.user.check.secret.dialog;

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
 * 查看视频: 钻石充值弹框
 * Created by Su on 2017/5/17.
 */

public class SecretDiamondDlg extends BaseActivity implements View.OnClickListener {

    private PayGoods payGoods;  // 商品信息
    private GoodsListPanel goodsPanel; // 商品列表
    private GoodsPayTypePanel payTypePanel; // 支付方式

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_secret_diamond_dlg);

        initList();
        initView();
    }

    private void initList() {
        if (payGoods == null)
            payGoods = new PayGoods();

        String json = FileUtil.getFromAssets(App.getActivity(), "info_goods.json");
        try {
            if (TextUtils.isEmpty(json)) return;
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has("secret_diamond"))
                payGoods.parseJson(jsonObject.optString("secret_diamond"));
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
    }

    private void initView() {
        findViewById(R.id.btn_recharge).setOnClickListener(this);

        fillGoodsPanel();
    }

    private void fillGoodsPanel() {
        // 商品列表
        LinearLayout container = (LinearLayout) findViewById(R.id.pay_container);
        goodsPanel = new GoodsListPanel(this, GoodsConstant.DLG_DIAMOND_GIFT);
        container.addView(goodsPanel.getContentView());
        goodsPanel.refresh(payGoods.getCommodityList());

        // 支付方式
        LinearLayout payContainer = (LinearLayout) findViewById(R.id.pay_type_container);
        payTypePanel = new GoodsPayTypePanel(this, GoodsConstant.PAY_TYPE_OLD);
        payContainer.addView(payTypePanel.getContentView());
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
