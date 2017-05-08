package com.juxin.predestinate.ui.xiaoyou.wode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.controls.smarttablayout.PagerItem;
import com.juxin.library.controls.smarttablayout.SmartTabLayout;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.xiaoyou.wode.adapter.ViewGroupPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的钱包页面
 * Created by zm on 2017/4/20
 */
public class RedBoxRecordAct extends BaseActivity implements View.OnClickListener{

    public static String REDBOXMONEY = "REDBOXMONEY";//键

    private CustomRecyclerView crlList;
    private RecyclerView rlvList;
    private TextView tvMoney;
    private TextView tvWithdraw;
    private TextView tvTips;
    private SmartTabLayout stlTitles;
    private ViewPager vpViewChange;

    private List<PagerItem> listViews;//pagerItem集合
    private List<BaseViewPanel> panls = new ArrayList<>(); // Tab页面列表

    //list间隔样式
//    android:divider="@color/transparent"
//    android:dividerHeight="1dip"
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_wode_red_bag_record_act);
        initView();
    }

    private void initView(){
        setBackView(R.id.base_title_back);
        tvMoney = (TextView) findViewById(R.id.wode_wallet_tv_money);
        tvWithdraw = (TextView) findViewById(R.id.wode_wallet_tv_draw);
        stlTitles = (SmartTabLayout) findViewById(R.id.wode_wallet_stl_titles);
        vpViewChange = (ViewPager) findViewById(R.id.wode_wallet_vp_view_change);
        tvTips = (TextView) findViewById(R.id.wode_wallet_tv_tips);
        tvTips.setText(Html.fromHtml(getString(R.string.withdraw_tip)));
        tvWithdraw.setOnClickListener(this);
        initViewsList();
        initViewPager();
        ((LinearLayout)stlTitles.getTabStrip()).setGravity(Gravity.CENTER_HORIZONTAL);//标题居中
        stlTitles.setCustomTabView(R.layout.f1_custom_table_view, R.id.tv_left_tab);//设置自定义标题
        stlTitles.setViewPager(vpViewChange);//设置viewpager
        setTitle(getString(R.string.my_red));
        setTitleRight(getString(R.string.explain), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到提现说明页
                UIShow.showWithDrawExplainAct(RedBoxRecordAct.this);
            }
        });
    }
    //添加两个panel
    private void initViewsList() {
        panls.add(new RedBagRecordPanel(this));
        panls.add(new WithDrawRecordPanel(this));
        listViews = new ArrayList<>();
        listViews.add(new PagerItem(getString(R.string.income_details), panls.get(0).getContentView()));
        listViews.add(new PagerItem(getString(R.string.withdraw_record), panls.get(1).getContentView()));
    }

    private void initViewPager() {
        vpViewChange.setAdapter(new ViewGroupPagerAdapter(listViews));
    }

    public void refreshView(double money){
        PSP.getInstance().put(REDBOXMONEY + ModuleMgr.getCenterMgr().getMyInfo().getUid(), (float) money);//存储可提现金额
        tvMoney.setText(money + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.wode_wallet_tv_draw:
                String money = tvMoney.getText().toString().trim();
                int minMoney = ModuleMgr.getCommonMgr().getCommonConfig().getMinmoney();
                Log.e("TTTTTTTTTT",ModuleMgr.getCommonMgr().getCommonConfig().getMinmoney()+"|||");
                if (Float.valueOf(money) <= Float.valueOf(minMoney)) {
                    PToast.showShort(getString(R.string.withdraw_tips) +minMoney + getString(R.string.head_unit));
                    return;
                }

                UserDetail userDetail1 = ModuleMgr.getCenterMgr().getMyInfo();
                final boolean isVerify = userDetail1.isVerifyCellphone();//是否绑定了手机号
//                Intent intent = null;
                if (isVerify) {
                    UIShow.showWithDrawApplyAct(0,0,false,this);
//                    intent = new Intent(this, WithDrawApplyAct.class);
//                    startActivity(intent);
                } else {
                    UIShow.showRedBoxPhoneVerifyAct(RedBoxRecordAct.this);//验证手机
                }
                break;
            default:
                break;
        }
    }
}