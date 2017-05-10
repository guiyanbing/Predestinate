package com.juxin.predestinate.ui.user.paygoods.vip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * Vip充值弹框
 * Created by Su on 2017/5/4.
 */
public class GoodsVipDialog extends BaseActivity implements View.OnClickListener {
    private PayGoods payGoods;              // 商品信息
    private GoodsListPanel goodsPanel;      // 商品列表
    private GoodsPayTypePanel payTypePanel; // 支付方式

    // 提示文字
    private LinearLayout ll_vip_privilege, ll_low_power, ll_low_host, ll_high_arean, ll_get_dog;
    private LinearLayout ll_more_privilege, ll_goods_preference;
    private TextView tv_title;
    private Button btn_recharge;   // 充值按钮

    private int rechargeType = GoodsConstant.DLG_VIP_PRIVEDEG; // 充值类型

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_goods_vip_dialog);

        initView();
    }

    private void initView() {
        ll_vip_privilege = (LinearLayout) findViewById(R.id.ll_vip_privilege);
        ll_low_power = (LinearLayout) findViewById(R.id.ll_low_power);
        ll_low_host = (LinearLayout) findViewById(R.id.ll_low_host);
        ll_more_privilege = (LinearLayout) findViewById(R.id.ll_more_privilege);
        ll_goods_preference = (LinearLayout) findViewById(R.id.ll_goods_preference);
        ll_high_arean = (LinearLayout) findViewById(R.id.ll_high_arean);
        ll_get_dog = (LinearLayout) findViewById(R.id.ll_get_dog);

        tv_title = (TextView) findViewById(R.id.tv_title);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);

        btn_recharge.setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);

        fillGoodsPanel();
    }

    private void fillGoodsPanel() {
        rechargeType = getIntent().getIntExtra(GoodsConstant.DLG_VIP_TYPE, GoodsConstant.DLG_VIP_PRIVEDEG);

        // 商品列表
        LinearLayout container = (LinearLayout) findViewById(R.id.goods_container);
        goodsPanel = new GoodsListPanel(this, rechargeType);
        container.addView(goodsPanel.getContentView());
        attachPanelListener();
        initList();

        // 支付方式
        LinearLayout payContainer = (LinearLayout) findViewById(R.id.pay_type_container);
        payTypePanel = new GoodsPayTypePanel(this, GoodsConstant.PAY_TYPE_NEW);
        payContainer.addView(payTypePanel.getContentView());

        btn_recharge.setText(getString(R.string.goods_ycoin_pay, (int) (payGoods.getCommodityList().get(0).getDoublePrice())));
        initUI();  // 展示UI
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

    /**
     * 根据rechargeType更新UI
     */
    private void initUI() {
        switch (rechargeType) {
            case GoodsConstant.DLG_VIP_PRIVEDEG: // 开通VIP特权
                showVipPrivedge();
                break;

            case GoodsConstant.DLG_VIP_LOW_POWER: // 体力不足
                showLowPower();
                break;

            case GoodsConstant.DLG_VIP_LOW_WAKAN: // 灵力不足
                showLowhost();
                break;

            case GoodsConstant.DLG_VIP_HEIGH_AREANA:// 高级擂台
                showHighAreana();
                break;

            case GoodsConstant.DLG_VIP_GET_DOG:     // 获取斗牛犬
                showGetDog();
                break;
        }
    }

    private void showVipPrivedge() {
        ll_vip_privilege.setVisibility(View.VISIBLE);
        ll_goods_preference.setVisibility(View.VISIBLE);
        ll_more_privilege.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.goods_vip_privilege_title));
    }

    private void showLowPower() {
        ll_low_power.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.goods_vip_dlg_low_power));
    }

    private void showLowhost() {
        ll_low_host.setVisibility(View.VISIBLE);
        ll_more_privilege.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.goods_vip_dlg_host));
    }

    private void showHighAreana() {
        ll_high_arean.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.goods_vip_dlg_high_arean_title));
    }

    private void showGetDog() {
        ll_get_dog.setVisibility(View.VISIBLE);
        tv_title.setText(getString(R.string.goods_vip_dlg_get_dog));
    }

    /**
     * 商品选择
     */
    private void attachPanelListener() {
        goodsPanel.setPanelItemClickListener(new GoodsListPanel.ListPanelItemClickListener() {
            @Override
            public void OnPanelItemClick(View convertView, int position) {
                btn_recharge.setText(getString(R.string.goods_ycoin_pay, (int) (payGoods.getCommodityList().get(position).getDoublePrice())));
            }
        });
    }

    private void parseJson(JSONObject jsonObject) {
        switch (rechargeType) {
            case GoodsConstant.DLG_VIP_PRIVEDEG:
                if (jsonObject.has("vip_host"))
                    payGoods.parseJson(jsonObject.optString("vip_host"));
                break;

            case GoodsConstant.DLG_VIP_LOW_POWER:
            case GoodsConstant.DLG_VIP_LOW_WAKAN:
                if (jsonObject.has("vip_power"))
                    payGoods.parseJson(jsonObject.optString("vip_power"));
                break;

            case GoodsConstant.DLG_VIP_GET_DOG:
            case GoodsConstant.DLG_VIP_HEIGH_AREANA:
                if (jsonObject.has("vip_dog"))
                    payGoods.parseJson(jsonObject.optString("vip_dog"));
                break;

        }
        goodsPanel.refresh(payGoods.getCommodityList()); // 刷新列表
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                finish();
                break;

            case R.id.btn_recharge:  // 充值
                PToast.showShort("type: " + payTypePanel.getPayType() + "goods: " + payGoods.getCommodityList().get(goodsPanel.getPosition()).getId());
                break;
        }
    }
}
