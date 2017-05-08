package com.juxin.predestinate.ui.xiaoyou.wode;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.wode.adapter.WithDrawTabAdapter;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.WithdrawList;

import java.util.List;


/**
 * 提现记录
 * Created by zm on 2017/4/25
 */
public class WithDrawRecordPanel extends BaseViewPanel implements RequestComplete,XRecyclerView.LoadingListener{

    private Context mContext;
    //有关控件
    private CustomRecyclerView crvView;
    private XRecyclerView rvList;
    private TextView tvNoData;
    //数据相关
    private List<WithdrawList.WithdrawInfo> mWithdrawInfos;
    private WithDrawTabAdapter mRedBagTabAdapter;

    public WithDrawRecordPanel(Context context) {
        super(context);
        mContext = context;
        setContentView(R.layout.f1_withdraw_record_tab_panel);
        initView();
        reqData();
        crvView.showLoading();
    }
    //请求数据
    private void reqData() {
        ModuleMgr.getCommonMgr().reqWithdrawlist(this);
    }

    private void initView(){
        crvView = (CustomRecyclerView) findViewById(R.id.withdraw_record_panel_crv_list);
        tvNoData = (TextView) findViewById(R.id.withdraw_record_panel_tv_data_tip);
        tvNoData.setVisibility(View.GONE);
        rvList = crvView.getXRecyclerView();
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mRedBagTabAdapter = new WithDrawTabAdapter(mContext);
//        rvList.setEmptyView(tvNoData);
        rvList.setAdapter(mRedBagTabAdapter);
        rvList.setLoadingMoreEnabled(false);
        rvList.setLoadingListener(this);
    }

    //请求数据返回
    @Override
    public void onRequestComplete(HttpResponse response) {
        rvList.refreshComplete();
        rvList.loadMoreComplete();
//        Log.e("TTTTTTTTTTFF", response.getResponseString() + "|||" + response.isOk());
        crvView.showXrecyclerView();
        if (response.isOk()){
            WithdrawList withdrawList = new WithdrawList();

//            withdrawList.parseJson(testData());
            withdrawList.parseJson(response.getResponseString());
            mWithdrawInfos = withdrawList.getRedbagLists();
            ((RedBoxRecordAct)context).refreshView(withdrawList.getTotal());
            if (mWithdrawInfos != null && !mWithdrawInfos.isEmpty()){
                tvNoData.setVisibility(View.GONE);
                mRedBagTabAdapter.setList(mWithdrawInfos);
                return;
            }
            showNoData();
            return;
        }
        if (mWithdrawInfos != null && !mWithdrawInfos.isEmpty()){
            showNoData();
            return;
        }
        crvView.showXrecyclerView();
        PToast.showShort(mContext.getString(R.string.net_error_check_your_net));
    }

    //暂无数据
    private void showNoData(){
        tvNoData.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefresh() {//刷新
        tvNoData.setVisibility(View.GONE);
        reqData();
    }

    @Override
    public void onLoadMore() {//加载更多

    }

    private String testData(){
        String str = "/*{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"total\": \"20\",\n" +
                "  \"result\":[{\n" +
                "                    \"id\":\"1\",\n" +
                "                    \"money\":\"300\",\n" +
                "                    \"create_time\":\"2016-11-04 10:14:23\",\n" +
                "                    \"status\":\"1\"\n" +
                "             }\n" +
                ",{\n" +
                "                    \"id\":\"2\",\n" +
                "                    \"money\":\"300\",\n" +
                "                    \"create_time\":\"2016-11-04 10:14:23\",\n" +
                "                    \"status\":\"2\"\n" +
                "             },{\n" +
                "                    \"id\":\"3\",\n" +
                "                    \"money\":\"300\",\n" +
                "                    \"create_time\":\"2016-11-04 10:14:23\",\n" +
                "                    \"status\":\"3\"\n" +
                "             }]\n" +
                "}*/";
        return str;
    }
}