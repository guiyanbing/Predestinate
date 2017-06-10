package com.juxin.predestinate.ui.user.paygoods.diamond;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.local.statistics.DisCoverStatistics;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;
import com.juxin.predestinate.ui.user.paygoods.GoodsListPanel;
import com.juxin.predestinate.ui.user.paygoods.GoodsPayTypePanel;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGoods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 钻石充值弹框
 * Created by Su on 2017/4/12.
 */
public class GoodsDiamondDialog extends BaseActivity implements View.OnClickListener {

    private PayGoods payGoods;  // 商品信息
    private GoodsListPanel goodsPanel; // 商品列表
    private GoodsPayTypePanel payTypePanel; // 支付方式

    private int needDiamond;  // 送礼需要钻石数
    private int type = GoodsConstant.DLG_DIAMOND_NORMAL;   // 正常展示样式

    private int fromTag = -1; //打开来源
    private long touid = -1; //是否因为某个用户充值 （统计用 可选）


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_goods_diamond_dialog);

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
            if (jsonObject.has("diamond"))
                payGoods.parseJson(jsonObject.optString("diamond"));
        } catch (JSONException e) {
            PLogger.printThrowable(e);
        }
    }

    private void initView() {
        type = getIntent().getIntExtra(GoodsConstant.DLG_TYPE_KEY, GoodsConstant.DLG_DIAMOND_NORMAL);
        needDiamond = getIntent().getIntExtra(GoodsConstant.DLG_GIFT_NEED, 0);
        fromTag = getIntent().getIntExtra(GoodsConstant.DLG_OPEN_FROM, -1);
        touid = getIntent().getLongExtra(GoodsConstant.DLG_OPEN_TOUID, -1);

        LinearLayout ll_diamond_tips = (LinearLayout) findViewById(R.id.ll_diamond_tips);
        TextView tv_decdiamod = (TextView) findViewById(R.id.tv_decdiamod); // 送礼差钻石数
        findViewById(R.id.btn_recharge).setOnClickListener(this);

        if (type == GoodsConstant.DLG_DIAMOND_GIFT_SHORT && needDiamond > 0) {
            ll_diamond_tips.setVisibility(View.VISIBLE);
            tv_decdiamod.setText(getString(R.string.goods_diamond_need, needDiamond));
        }

        fillGoodsPanel();
    }

    private void fillGoodsPanel() {
        // 商品列表
        LinearLayout container = (LinearLayout) findViewById(R.id.pay_type_container);
        goodsPanel = new GoodsListPanel(this);
        container.addView(goodsPanel.getContentView());
        goodsPanel.refresh(payGoods.getCommodityList());

        // 支付方式
        payTypePanel = new GoodsPayTypePanel(this, GoodsConstant.PAY_TYPE_OLD);
        container.addView(payTypePanel.getContentView());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_recharge:  // 充值
                UIShow.showPayAlipayt(this, payGoods.getCommodityList().get(goodsPanel.getPosition()).getId(), payTypePanel.getPayType());
                //统计
                if (fromTag == Constant.OPEN_GIFT_FROM_HOT) {
                    DisCoverStatistics.onPayGift(touid, payGoods.getCommodityList().get(goodsPanel.getPosition()).getNum(),
                            payGoods.getCommodityList().get(goodsPanel.getPosition()).getPrice(), type);
                }
                break;
        }
    }
}
