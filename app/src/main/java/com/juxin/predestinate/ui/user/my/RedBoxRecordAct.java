package com.juxin.predestinate.ui.user.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.controls.smarttablayout.PagerItem;
import com.juxin.library.controls.smarttablayout.SmartTabLayout;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.statistics.SendPoint;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.model.impl.UnreadMgrImpl;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.auth.IDCardAuthenticationSucceedAct;
import com.juxin.predestinate.ui.user.my.adapter.ViewGroupPagerAdapter;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的钱包页面
 * Created by zm on 2017/4/20
 */
public class RedBoxRecordAct extends BaseActivity implements PObserver {

    public static String REDBOXMONEY = "REDBOXMONEY";//键
    private int authResult = 103, authForVodeo = 104, authIDCard = 105;

    private TextView tvMoney;
    private TextView tvWithdraw;
    private TextView tvTips;
    private SmartTabLayout stlTitles;
    private ViewPager vpViewChange;

    private List<PagerItem> listViews;//pagerItem集合
    private List<BasePanel> panls = new ArrayList<>(); // Tab页面列表

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_wode_red_bag_record_act);
        MsgMgr.getInstance().attach(this);
        if (!ModuleMgr.getCommonMgr().getIdCardVerifyStatusInfo().getIsVerifyIdCard()) {
            ModuleMgr.getCommonMgr().getVerifyStatus(null);
        }
        initView();
        ModuleMgr.getUnreadMgr().resetUnreadByKey(UnreadMgrImpl.MY_WALLET);
    }

    private void initView() {
        setBackView(R.id.base_title_back);
        tvMoney = (TextView) findViewById(R.id.wode_wallet_tv_money);
        tvWithdraw = (TextView) findViewById(R.id.wode_wallet_tv_draw);
        stlTitles = (SmartTabLayout) findViewById(R.id.wode_wallet_stl_titles);
        vpViewChange = (ViewPager) findViewById(R.id.wode_wallet_vp_view_change);
        tvTips = (TextView) findViewById(R.id.wode_wallet_tv_tips);
        tvTips.setText(Html.fromHtml(getString(R.string.withdraw_tip)));
        tvTips.setVisibility(View.GONE);
        tvWithdraw.setOnClickListener(clickListener);
        refreshView(ModuleMgr.getCenterMgr().getMyInfo().getRedbagsum() / 100f);
        initViewsList();
        initViewPager();
        ((LinearLayout) stlTitles.getTabStrip()).setGravity(Gravity.CENTER_HORIZONTAL);//标题居中
        stlTitles.setCustomTabView(R.layout.f1_custom_table_view, R.id.tv_left_tab);//设置自定义标题
        stlTitles.setViewPager(vpViewChange);//设置viewpager
        setTitle(getString(R.string.my_red));
        setTitleRight(getString(R.string.explain), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到提现说明页
                UIShow.showWithDrawExplainAct(RedBoxRecordAct.this);
                Statistics.userBehavior(SendPoint.menu_me_money_explain);
            }
        });
    }

    //添加两个panel
    private void initViewsList() {
        panls.add(new TreeRedBagRecordPanel(this));
        panls.add(new RedBagRecordPanel(this));
        panls.add(new WithDrawRecordPanel(this));
        listViews = new ArrayList<>();
        listViews.add(new PagerItem(getString(R.string.tree_income_details), panls.get(0).getContentView()));
        listViews.add(new PagerItem(getString(R.string.income_details), panls.get(1).getContentView()));
        listViews.add(new PagerItem(getString(R.string.withdraw_record), panls.get(2).getContentView()));
    }

    private void initViewPager() {
        vpViewChange.setAdapter(new ViewGroupPagerAdapter(listViews));
    }

    public void refreshView(double money) {
        PSP.getInstance().put(REDBOXMONEY + ModuleMgr.getCenterMgr().getMyInfo().getUid(), (float) money);//存储可提现金额
        tvMoney.setText(money + "");
        ModuleMgr.getCenterMgr().getMyInfo().setRedbagsum(money*100);
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.wode_wallet_tv_draw:
                    Statistics.userBehavior(SendPoint.menu_me_money_withdraw);
                    String money = tvMoney.getText().toString().trim();
                    float minMoney = ModuleMgr.getCommonMgr().getCommonConfig().getMinmoney() / 100f;
                    if (Float.valueOf(money) <= minMoney) {
                        PToast.showShort(getString(R.string.withdraw_tips) + minMoney + getString(R.string.head_unit));
                        return;
                    }

                    int status = ModuleMgr.getCommonMgr().getIdCardVerifyStatusInfo().getStatus();
                    if (!ModuleMgr.getCenterMgr().getMyInfo().isVerifyCellphone()) {//是否绑定了手机号
                        UIShow.showRedBoxPhoneVerifyAct(RedBoxRecordAct.this);
                        break;
                    }
                    if (status <= 0) {//是否进行了身份认证
                        UIShow.showIDCardAuthenticationAct(RedBoxRecordAct.this, authIDCard);
                        break;
                    }
                    if (status != 2) {//身份认证还未通过
                        PToast.showShort(R.string.the_identity_certification_audit);
                        break;
                    }
                    UIShow.showWithDrawApplyAct(0, 0, false, RedBoxRecordAct.this);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == authResult) {//手机号认证

        } else if (requestCode == authIDCard) {//身份认证
            if (data != null) {
                int back = data.getIntExtra(IDCardAuthenticationSucceedAct.IDCARDBACK, 0);
                if (back == 2) {
                    this.finish();
                }
            }
        }
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_GET_MONEY_Notice:
                refreshView(0.0); //提现成功更新可提现金额
                //主动刷新提现记录
                if (panls.size() != 0) {
                    if (panls.get(2) != null) {
                        ((WithDrawRecordPanel) panls.get(2)).onRefresh();
                    }
                }
                break;
            default:
                break;
        }
    }
}