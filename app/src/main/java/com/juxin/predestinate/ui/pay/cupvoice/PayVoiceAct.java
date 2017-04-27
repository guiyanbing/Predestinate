package com.juxin.predestinate.ui.pay.cupvoice;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.juxin.library.controls.smarttablayout.PagerItem;
import com.juxin.library.controls.smarttablayout.SmartTabLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.PayWX;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.ViewGroupPagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2017/4/26.
 */

public class PayVoiceAct extends BaseActivity {

    private PayCupVoiceBankPanel voiceBankPanel;
    private PayCupVoiceCreditPanel voiceCreditPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_pay_cupvoice_act);
        setBackView(R.id.base_title_back, "语音支付");
        initView();
    }

    private void initView(){
        PayGood payGood = (PayGood) getIntent().getSerializableExtra("payGood");
        PayWX payWX = (PayWX) getIntent().getSerializableExtra("payWX");


        SmartTabLayout pager_indicator = (SmartTabLayout) findViewById(R.id.pay_cupvoice_pager_indicator);
        ViewPager view_pager = (ViewPager) findViewById(R.id.pay_cupvoiceview_pager);

        voiceBankPanel = new PayCupVoiceBankPanel(this, payGood);
        voiceCreditPanel = new PayCupVoiceCreditPanel(this, payGood);

        List<PagerItem> pagerItems = new ArrayList<>();
        pagerItems.add(new PagerItem("银行卡", voiceBankPanel.getContentView()));
        pagerItems.add(new PagerItem("信用卡", voiceCreditPanel.getContentView()));
        view_pager.setAdapter(new ViewGroupPagerAdapter(pagerItems));

        pager_indicator.setCustomTabView(R.layout.f1_tab_item, R.id.tab_text);
        pager_indicator.setViewPager(view_pager);//在设置SmartTabLayout.setViewPager的时候ViewPager必须先setAdapter

    }
}