package com.juxin.predestinate.ui.user.paygoods.diamond;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.user.paygoods.GoodsConstant;
import com.juxin.predestinate.ui.user.paygoods.GoodsListPanel;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGood;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGoods;

import java.util.ArrayList;

/**
 * 钻石商品
 * Created by Su on 2017/3/31.
 */
public class GoodsDiamondAct extends BaseActivity implements View.OnClickListener, RequestComplete {

    private GoodsListPanel goodsPanel;
    private CustomFrameLayout payWeChat, payAli, payOther; // 支付方式
    private int payType = GoodsConstant.PAY_TYPE_WECHAT;  // 默认支付方式为微信支付

    private ArrayList<PayGood> payGoodList; // 列表数据
    private CustomRecyclerView cv_common;   // 状态布局
    private ScrollView payLayout;           // 支付布局
    private TextView remainDiamond;         // 钻石余额

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_goods_diamond_act);

        initTitle();
        initView();
        reqData();
    }

    private void initTitle() {
        setBackView();
        setTitle(getString(R.string.goods_diamond_title));
    }

    private void initView() {
        cv_common = (CustomRecyclerView) findViewById(R.id.cv_common);
        payLayout = (ScrollView) findViewById(R.id.pay_layout);
        remainDiamond = (TextView) findViewById(R.id.remain_diamond);

        // 支付方式
        payWeChat = (CustomFrameLayout) findViewById(R.id.pay_type_wexin);
        payAli = (CustomFrameLayout) findViewById(R.id.pay_type_alipay);
        payOther = (CustomFrameLayout) findViewById(R.id.pay_type_other);

        // 商品列表
        LinearLayout container = (LinearLayout) findViewById(R.id.pay_type_container);
        goodsPanel = new GoodsListPanel(this);
        container.addView(goodsPanel.getContentView());

        payWeChat.setOnClickListener(this);
        payAli.setOnClickListener(this);
        payOther.setOnClickListener(this);
        findViewById(R.id.btn_recharge).setOnClickListener(this);
        findViewById(R.id.btn_contact).setOnClickListener(this);

        payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_CHOOSE);
        payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
        payOther.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
    }

    private void reqData() {
        cv_common.showLoading();
        ModuleMgr.getCommonMgr().reqCommodityList(Constant.GOOD_DIAMOND, this);
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
                payWeChat.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                payAli.showOfIndex(GoodsConstant.PAY_STATUS_UNCHOOSE);
                break;

            case R.id.btn_recharge: // 立即充值
                recharge();
                break;

            case R.id.btn_contact:  // 联系客服
                startActivity(new Intent(this, GoodsDiamondDialog.class));
                break;
        }
    }

    private void recharge() {
        PToast.showShort("goods id: " + payGoodList.get(goodsPanel.getPosition()) + ",  type: " + payType);
    }

    /**
     * 获取数据失败
     */
    private void showError() {
        if (null != payGoodList && payGoodList.size() > 0) {
            return;
        }
        cv_common.showNetError("点击刷新", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv_common.showLoading();
                reqData();
            }
        });
    }

    /**
     * 获取数据成功
     */
    private void initData() {
        cv_common.setVisibility(View.GONE);
        payLayout.setVisibility(View.VISIBLE);
        goodsPanel.refresh(payGoodList);
    }

    /**
     * 数据展示
     */
    private void showData() {
        if (null != payGoodList && payGoodList.size() > 0) {
            initData();
            return;
        }
        showError();
    }

    private void clear() {
        goodsPanel.refresh(null);
        if (null != payGoodList)
            payGoodList.clear();
    }

    /**
     * 支付列表请求结果
     */
    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()) {
            clear();
            PayGoods payGoods = (PayGoods) response.getBaseData();
            if (payGoods != null && payGoods.getCommodityList() != null && payGoods.getCommodityList().size() > 0) {
                payGoodList = payGoods.getCommodityList();
            }
            showData();
        } else {
            showError();
        }
    }
}
