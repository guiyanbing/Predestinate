package com.juxin.predestinate.ui.user.paygoods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

/**
 * 钻石充值弹框
 * Created by Su on 2017/4/12.
 */

public class GoodsDiamondDialog extends BaseActivity {

    private CustomFrameLayout payWeChat, payAli, payOther; // 支付方式

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_goods_diamond_dialog);

        initView();
    }

    private void initView() {

        // 支付方式
        payWeChat = (CustomFrameLayout) findViewById(R.id.pay_type_wexin);
        payAli = (CustomFrameLayout) findViewById(R.id.pay_type_alipay);
        payOther = (CustomFrameLayout) findViewById(R.id.pay_type_other);

        payOther.setVisibility(View.GONE);
    }
}
