package com.juxin.predestinate.ui.pay.cupvoice;

import android.content.Context;

import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.baseui.custom.CustomStatusListView;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;

import java.util.ArrayList;

/**
 * 银行卡
 * Created by Kind on 2017/4/26.
 */
public class PayCupVoiceBankPanel extends BasePanel {

    public PayCupVoiceBankPanel(Context context, PayGood payGood) {
        super(context);
        setContentView(R.layout.f1_pay_cupvoice_bank);

        CustomStatusListView pay_cupvoice_bank_listview = (CustomStatusListView) findViewById(R.id.pay_cupvoice_bank_listview);
        ExListView exListView = pay_cupvoice_bank_listview.getExListView();
        pay_cupvoice_bank_listview.setPadding(0, 40, 0, 0);
        exListView.setDividerHeight(32);
        exListView.setAdapter(new PayCupVoiceBankAdapter(context, initData(), payGood));
        exListView.setPullLoadEnable(false);
        exListView.setPullRefreshEnable(false);
        pay_cupvoice_bank_listview.showExListView();
    }

    private ArrayList<String> initData() {
        ArrayList<String> list_string = new ArrayList<String>();
        list_string.add("工商银行");
        list_string.add("农业银行");
        list_string.add("建设银行");
        list_string.add("交通银行");
        list_string.add("农村信用社");
        list_string.add("光大银行");
        list_string.add("兴业银行");
        list_string.add("中信银行");
        list_string.add("浦发银行");
        list_string.add("深圳发展银行");
        list_string.add("平安银行");
        list_string.add("中国银行");
        list_string.add("广发银行");
        list_string.add("邮储银行");
        list_string.add("招商银行");
        list_string.add("华夏银行");
        list_string.add("其他银行");
        return list_string;
    }
}
