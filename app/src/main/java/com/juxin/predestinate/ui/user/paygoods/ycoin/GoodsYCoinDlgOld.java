package com.juxin.predestinate.ui.user.paygoods.ycoin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
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
import com.juxin.predestinate.ui.user.paygoods.bean.PayGood;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGoods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 老：Y币充值弹框
 * Created by Su on 2017/5/5.
 */
public class GoodsYCoinDlgOld extends BaseActivity implements View.OnClickListener {
    private PayGoods payGoods;  // 商品信息
    private GoodsListPanel goodsPanel;
    private GoodsPayTypePanel payTypePanel; // 支付方式

    private TextView tv_tips; // 充值优惠提示信息
    private TextView tv_ycoin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_goods_ycoin_dialog_old);
        initView();
    }

    private void initView() {
        tv_tips = (TextView) findViewById(R.id.tv_ycoin_ts1);
        tv_ycoin = (TextView) findViewById(R.id.tv_dlg_ycoin_num);
        tv_ycoin.setText(ModuleMgr.getCenterMgr().getMyInfo().getYcoin() + "");
        findViewById(R.id.btn_recharge).setOnClickListener(this);
        fillGoodsPanel();
        findViewById(R.id.tv_get_tel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIShow.showActionActivity(GoodsYCoinDlgOld.this);
            }
        });
    }

    private void fillGoodsPanel() {
        LinearLayout container = (LinearLayout) findViewById(R.id.goods_container);
        goodsPanel = new GoodsListPanel(this, GoodsConstant.DLG_YCOIN_PRIVEDEG);
        container.addView(goodsPanel.getContentView());
        attachPanelListener();
        initList();

        // 支付方式
        LinearLayout payContainer = (LinearLayout) findViewById(R.id.pay_type_container);
        payTypePanel = new GoodsPayTypePanel(this, GoodsConstant.PAY_TYPE_OLD);
        payContainer.addView(payTypePanel.getContentView());

        tv_tips.setText(Html.fromHtml(getResources().getString(R.string.goods_ycoin_pay_num, payGoods.getCommodityList().get(0).getNum())
                + "<font color='#FF0000'>" + payGoods.getCommodityList().get(0).getPrivilege() + "</font>"));
    }

    private void initList() {
        if (payGoods == null)
            payGoods = new PayGoods();

        String json = FileUtil.getFromAssets(App.getActivity(), "info_goods.json");
        try {
            if (TextUtils.isEmpty(json)) return;
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("ycoin_old"))
                payGoods.parseJson(jsonObject.optString("ycoin_old"));

            goodsPanel.refresh(payGoods.getCommodityList()); // 刷新列表
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
    }

    private void attachPanelListener() {
        goodsPanel.setPanelItemClickListener(new GoodsListPanel.ListPanelItemClickListener() {
            @Override
            public void OnPanelItemClick(View convertView, int position) {
                try {
                    PayGood payGood = payGoods.getCommodityList().get(position);
                    tv_tips.setText(Html.fromHtml(getResources().getString(R.string.goods_ycoin_pay_num, payGood.getNum())
                            + "<font color='#FF0000'>" + payGood.getPrivilege() + "</font>"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:  // 充值
                UIShow.showPayAlipayt(this, payGoods.getCommodityList().get(goodsPanel.getPosition()).getId(), payTypePanel.getPayType());
                break;
        }
    }
}