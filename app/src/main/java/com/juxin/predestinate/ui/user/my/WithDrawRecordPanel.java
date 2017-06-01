package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.WithdrawList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.CustomStatusListView;
import com.juxin.predestinate.module.logic.baseui.xlistview.ExListView;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.user.my.adapter.WithDrawTabAdapter;

import java.util.List;


/**
 * 提现记录
 * Created by zm on 2017/4/25
 */
public class WithDrawRecordPanel extends BasePanel implements RequestComplete,ExListView.IXListViewListener{

    private Context mContext;
    //有关控件
    private CustomStatusListView crvView;
    private ExListView rvList;
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
        crvView = (CustomStatusListView) findViewById(R.id.withdraw_record_panel_crv_list);
        tvNoData = (TextView) findViewById(R.id.withdraw_record_panel_tv_data_tip);
        tvNoData.setVisibility(View.GONE);
        rvList = crvView.getExListView();
//        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        rvList.addItemDecoration(new DividerItemDecoration(getContext(),
//                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mRedBagTabAdapter = new WithDrawTabAdapter(mContext);
//        rvList.setEmptyView(tvNoData);
        rvList.setAdapter(mRedBagTabAdapter);
        rvList.setPullLoadEnable(false);//没有加载更多，所有数据一次返回
        rvList.setXListViewListener(this);
    }

    //请求数据返回
    @Override
    public void onRequestComplete(HttpResponse response) {
        rvList.stopRefresh();
        rvList.stopLoadMore();
//        Log.e("TTTTTTTTTTFF", response.getResponseString() + "|||" + response.isOk());
        crvView.showExListView();
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
        showNoData();
        crvView.showExListView();
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
        String str = "{\n" +
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
                "}";
        return str;
    }
}