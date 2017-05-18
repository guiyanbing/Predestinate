package com.juxin.predestinate.ui.user.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.controls.smarttablayout.PagerItem;
import com.juxin.library.controls.smarttablayout.SmartTabLayout;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.user.auth.IDCardAuthenticationSucceedAct;
import com.juxin.predestinate.ui.user.my.adapter.ViewGroupPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的钱包页面
 * Created by zm on 2017/4/20
 */
public class RedBoxRecordAct extends BaseActivity implements View.OnClickListener{

    public static String REDBOXMONEY = "REDBOXMONEY";//键
    private int authResult = 103, authForVodeo = 104,authIDCard = 105;

    private CustomRecyclerView crlList;
    private RecyclerView rlvList;
    private TextView tvMoney;
    private TextView tvWithdraw;
    private TextView tvTips;
    private SmartTabLayout stlTitles;
    private ViewPager vpViewChange;

    private List<PagerItem> listViews;//pagerItem集合
    private List<BaseViewPanel> panls = new ArrayList<>(); // Tab页面列表

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
        tvTips.setVisibility(View.GONE);
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
                float minMoney = ModuleMgr.getCommonMgr().getCommonConfig().getMinmoney()/100f;
//                Log.e("TTTTTTTTTT",ModuleMgr.getCommonMgr().getCommonConfig().getMinmoney()+"|||");
                if (Float.valueOf(money) <= minMoney) {
                    PToast.showShort(getString(R.string.withdraw_tips) +minMoney + getString(R.string.head_unit));
                    return;
                }

                if (!ModuleMgr.getCenterMgr().getMyInfo().isVerifyCellphone()){//是否绑定了手机号
                    UIShow.showRedBoxPhoneVerifyAct(RedBoxRecordAct.this);
                    //                    UIShow.showPhoneVerify_Act(RedBoxRecordAct.this, ModuleMgr.getCenterMgr().getMyInfo().isVerifyCellphone(), authResult);//验证手机
                } else if (!ModuleMgr.getCommonMgr().getIdCardVerifyStatusInfo().getIsVerifyIdCard()){//是否进行了身份认证
                    UIShow.showIDCardAuthenticationAct(this,authIDCard);
                }else {
                    UIShow.showWithDrawApplyAct(0,0,false,this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == authResult) {//手机号认证

        } else if (requestCode == authIDCard) {//身份认证
            if (data != null){
                int back = data.getIntExtra(IDCardAuthenticationSucceedAct.IDCARDBACK,0);
                if (back == 2){
                    this.finish();
                }
            }
        }
    }
}