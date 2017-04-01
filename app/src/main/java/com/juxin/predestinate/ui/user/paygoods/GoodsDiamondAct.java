package com.juxin.predestinate.ui.user.paygoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

/**
 * 钻石商品
 * Created by Su on 2017/3/31.
 */

public class GoodsDiamondAct extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_goods_diamond_act);

        initTitle();
        initView();
    }

    private void initTitle() {
        setBackView();
        setTitle(getString(R.string.goods_diamond_title));
    }

    private void initView() {
        LinearLayout container = (LinearLayout) findViewById(R.id.pay_type_container);

        GoodsListPanel goodsPanel = new GoodsListPanel(this);
        container.addView(goodsPanel.getContentView());
    }
}
