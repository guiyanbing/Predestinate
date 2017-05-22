package com.juxin.predestinate.ui.user.paygoods.ycoin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
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

    private Button btn_recharge;        // 充值按钮
    private ImageView img_select_vip;
    private boolean selectVip = true;   // 默认开通VIP
    private int rechargeNum;            // 支付金额

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_goods_ycoin_dialog);

        initView();
    }

    private void initView() {
        // Y币余额
        int remain = getIntent().getIntExtra(GoodsConstant.DLG_YCOIN_REMAIN, 0);
        TextView tv_remain = (TextView) findViewById(R.id.tv_remain);
        tv_remain.setText(String.valueOf(remain));

        // 购买体力值
        int power = getIntent().getIntExtra(GoodsConstant.DLG_YCOIN_POWER, 0);
        LinearLayout header = (LinearLayout) findViewById(R.id.ll_header_tips);
        TextView tv_power = (TextView) findViewById(R.id.tv_buy_power);
        if (power > 0) {
            header.setVisibility(View.VISIBLE);
            tv_power.setText(String.valueOf(power));
        }

        // 开通vip面板
        LinearLayout ll_vip = (LinearLayout) findViewById(R.id.ll_pay_vip);
        if (ModuleMgr.getCenterMgr().getMyInfo().isVip()) {
            ll_vip.setVisibility(View.GONE);
            selectVip = false;
        }

        img_select_vip = (ImageView) findViewById(R.id.iv_pay_select_vip);
        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        img_select_vip.setOnClickListener(this);
        btn_recharge.setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);

        fillGoodsPanel();
    }

    private void fillGoodsPanel() {
        LinearLayout container = (LinearLayout) findViewById(R.id.goods_container);
        goodsPanel = new GoodsListPanel(this, GoodsConstant.DLG_YCOIN_NEW);
        container.addView(goodsPanel.getContentView());
        attachPanelListener();
        initList();

        // 支付方式
        LinearLayout payContainer = (LinearLayout) findViewById(R.id.pay_type_container);
        payTypePanel = new GoodsPayTypePanel(this, GoodsConstant.PAY_TYPE_NEW);
        payContainer.addView(payTypePanel.getContentView());

        // 支付按钮
        if (selectVip) {
            rechargeNum = 50;
        }
        rechargeNum += (int) (payGoods.getCommodityList().get(0).getDoublePrice());
        btn_recharge.setText(getString(R.string.goods_ycoin_pay, rechargeNum));
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
     * 商品选择
     */
    private void attachPanelListener() {
        goodsPanel.setPanelItemClickListener(new GoodsListPanel.ListPanelItemClickListener() {
            @Override
            public void OnPanelItemClick(View convertView, int position) {
                rechargeNum = 0;
                if (selectVip) {
                    rechargeNum = 50;
                }
                rechargeNum += (int) (payGoods.getCommodityList().get(position).getDoublePrice());
                btn_recharge.setText(getString(R.string.goods_ycoin_pay, rechargeNum));
            }
        });
    }

    /**
     * vip选择状态切换
     */
    private void switchSelVip() {
        if (selectVip) {
            img_select_vip.setBackgroundResource(R.drawable.ic_radio_nor);
            selectVip = false;
            rechargeNum -= 50;
        } else {
            img_select_vip.setBackgroundResource(R.drawable.ic_radio_male_sel);
            selectVip = true;
            rechargeNum += 50;
        }
        btn_recharge.setText(getString(R.string.goods_ycoin_pay, rechargeNum));
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
                UIShow.showPayAlipayt(this, getPayid(), payTypePanel.getPayType());
                break;
        }
    }
}
