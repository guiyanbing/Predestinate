package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.RedOneKeyList;
import com.juxin.predestinate.bean.my.RedbagList;
import com.juxin.predestinate.module.local.statistics.SendPoint;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.custom.CustomStatusListView;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.user.my.adapter.RedBagTabAdapter;
import com.juxin.predestinate.ui.user.my.adapter.TreeRedBagTabAdapter;
import com.switfpass.pay.utils.Util;

import java.util.List;

/**
 * 收入详情
 * Created by zm on 2017/4/25
 */
public class TreeRedBagRecordPanel extends BasePanel implements RequestComplete, ExListView.IXListViewListener, View.OnClickListener {

    private Context mContext;
    //有关控件
    private Button butOneKey;
    private CustomStatusListView crvView;
    private ExListView rvList;
    private LinearLayout llSummary;
    private TextView tvData, tvPath, tvMoney, tvStatus;
    private TextView tvNoData,tvTopTips,tv_sum_tip;
    //数据相关
    private int mMinMoney;
    private List<RedbagList.RedbagInfo> mRedbagInfos;
    private TreeRedBagTabAdapter mTreeRedBagTabAdapter;

    public TreeRedBagRecordPanel(Context context) {
        super(context);
        mContext = context;
        setContentView(R.layout.f1_wode_redbag_record_tab_panel);
        initView();
        reqData();
        crvView.showLoading();
    }

    //请求数据
    private void reqData() {
        ModuleMgr.getCommonMgr().getTreeRedbagList(this);//请求收入详情列表
    }

    private void initView() {
        butOneKey = (Button) findViewById(R.id.wode_record_panel_btn_one_key);
        crvView = (CustomStatusListView) findViewById(R.id.wode_record_panel_crv_list);
        llSummary = (LinearLayout) findViewById(R.id.withdraw_ll_summary);
        tvData = (TextView) findViewById(R.id.withdraw_tv_date);
        tvPath = (TextView) findViewById(R.id.withdraw_tv_path);
        tvMoney = (TextView) findViewById(R.id.withdraw_tv_money);
        tvStatus = (TextView) findViewById(R.id.withdraw_tv_status);
        tvNoData = (TextView) findViewById(R.id.withdraw_record_panel_tv_data_tip);
        tvTopTips = (TextView) findViewById(R.id.wode_record_panel_tv_tips);
        tv_sum_tip = (TextView) findViewById(R.id.tv_sum_tip);


        tvNoData.setVisibility(View.GONE);
        llSummary.setVisibility(View.GONE);
        butOneKey.setOnClickListener(this);

        rvList = crvView.getExListView();
        rvList.setHeaderStr(getContext().getString(R.string.xlistview_header_hint_normal),
                getContext().getString(R.string.xlistview_header_hint_loading));
        mTreeRedBagTabAdapter = new TreeRedBagTabAdapter(mContext, this);
        rvList.setAdapter(mTreeRedBagTabAdapter);
        rvList.setPullLoadEnable(false);
        rvList.setXListViewListener(this);

        mMinMoney = ModuleMgr.getCommonMgr().getCommonConfig().getMinmoney();
        tvTopTips.setText(Html.fromHtml("<font color='#cfc19a'>红包</font>"
                + "<font color='#fb5e65'>24小时</font>"
                + "<font color='#cfc19a'>后解冻且累积</font>"
                + "<font color='#fb5e65'>满" + (int) Math.floor(mMinMoney/100) + "元</font>"
                + "<font color='#cfc19a'>可一键放入零钱</font>"));
    }

    //用于计算总钱数
    private String getSumMoney() {
        double money = 0;//收入总数
        for (int i = 0; i < mRedbagInfos.size(); i++) {
            money += mRedbagInfos.get(i).getMoney();
        }
        return Util.formatMoneyUtils(money * 100f);
    }

    //请求数据返回
    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
        tvNoData.setVisibility(View.GONE);
        crvView.showExListView();
        rvList.stopRefresh();
        rvList.stopLoadMore();
        if (response.getUrlParam() == UrlParam.reqTreeRedbagList) {
            RedbagList redbagList = new RedbagList();
            redbagList.parseJson(response.getResponseString());
            if (response.isOk()) {
                ((RedBoxRecordAct) context).refreshView(redbagList.getTotal());
                mRedbagInfos = redbagList.getRedbagLists();
                handleData(redbagList.getOnekeynum());
                return;
            }
            if (mRedbagInfos != null && !mRedbagInfos.isEmpty()) {
                return;
            }
        } else if (response.getUrlParam() == UrlParam.reqAddredonekey) {
            if (response.isOk()) {
                RedOneKeyList redOneKeyList = (RedOneKeyList) response.getBaseData();
                ((RedBoxRecordAct) context).refreshView(redOneKeyList.getSum());
                mRedbagInfos = redOneKeyList.getRedbagFailLists();
                if (redOneKeyList.getSucnum() == 0) {
                    PToast.showShort(mContext.getString(R.string.no_add_redbag));
                } else {
                    PToast.showShort(mContext.getString(R.string.succeed) + redOneKeyList.getSucnum() + mContext.getString(R.string.hour_into_the_bag));
                }
                handleData(mMinMoney);
                return;
            }
            PToast.showShort(response.getMsg());
        }
        showNoData();
    }

    //处理数据
    public void handleData(int oneKeyNum) {
        tv_sum_tip.setVisibility(View.VISIBLE);
        tv_sum_tip.setTextColor(Color.parseColor("#e3b382"));
        if (mRedbagInfos != null && !mRedbagInfos.isEmpty()) {
            mTreeRedBagTabAdapter.setList(mRedbagInfos);
            if(oneKeyNum / 100 >= mMinMoney / 100) {
                butOneKey.setEnabled(true);
                tv_sum_tip.setText("可放入零钱金额已达" + oneKeyNum / 100 + "元");
            }else {
                butOneKey.setEnabled(false);
                tv_sum_tip.setText("可放入零钱金额不足" + (int) Math.floor(mMinMoney / 100) + "元");
            }
            showCollect();
        } else {
            butOneKey.setEnabled(false);
            tv_sum_tip.setVisibility(View.VISIBLE);
            tv_sum_tip.setText("可放入零钱金额不足" + (int) Math.floor(mMinMoney / 100) + "元");
            tv_sum_tip.setTextColor(Color.parseColor("#e3b382"));
            showNoData();
        }
    }

    //暂无数据
    private void showNoData() {
        llSummary.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
    }

    //汇总ui显示设置
    public void showCollect() {
        llSummary.setVisibility(View.VISIBLE);
        tvData.setText(R.string.summary);
        tvPath.setText("--");
        tvMoney.setText(getSumMoney() + "");
        tvStatus.setText("--");
    }

    @Override
    public void onRefresh() {//刷新
        tvNoData.setVisibility(View.GONE);
        reqData();
    }

    @Override
    public void onLoadMore() {//加载更多

    }

    @Override
    public void onClick(View v) {//单击事件
        LoadingDialog.show((FragmentActivity) mContext);
        ModuleMgr.getCommonMgr().reqAddredonekey(ModuleMgr.getCenterMgr().getMyInfo().getUid(), this);//一键入袋
        Statistics.userBehavior(SendPoint.menu_me_money_onekey);
    }
}