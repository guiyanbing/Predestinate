package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.user.my.adapter.RedBagTabAdapter;
import com.juxin.predestinate.bean.my.RedOneKeyList;
import com.juxin.predestinate.bean.my.RedbagList;
import com.switfpass.pay.utils.Util;

import java.util.List;


/**
 * 收入详情
 * Created by zm on 2017/4/25
 */
public class RedBagRecordPanel extends BaseViewPanel implements RequestComplete,XRecyclerView.LoadingListener,View.OnClickListener{

    private Context mContext;
    //有关控件
    private Button butOneKey;
    private CustomRecyclerView crvView;
    private XRecyclerView rvList;
    private LinearLayout llSummary;
    private TextView tvData,tvPath,tvMoney,tvStatus;
    private TextView tvNoData;
    //数据相关
    private List<RedbagList.RedbagInfo> mRedbagInfos;
    private RedBagTabAdapter mRedBagTabAdapter;

    public RedBagRecordPanel(Context context) {
        super(context);
        mContext = context;
        setContentView(R.layout.f1_wode_redbag_record_tab_panel);
        initView();
        reqData();
        crvView.showLoading();
    }
    //请求数据
    private void reqData() {
        ModuleMgr.getCommonMgr().getRedbagList(this);//请求收入详情列表
    }

    private void initView(){
        butOneKey = (Button) findViewById(R.id.wode_record_panel_btn_one_key);
        crvView = (CustomRecyclerView) findViewById(R.id.wode_record_panel_crv_list);
        llSummary = (LinearLayout) findViewById(R.id.withdraw_ll_summary);
        tvData = (TextView) findViewById(R.id.withdraw_tv_date);
        tvPath = (TextView) findViewById(R.id.withdraw_tv_path);
        tvMoney = (TextView) findViewById(R.id.withdraw_tv_money);
        tvStatus = (TextView) findViewById(R.id.withdraw_tv_status);
        tvNoData = (TextView) findViewById(R.id.withdraw_record_panel_tv_data_tip);
        tvNoData.setVisibility(View.GONE);
        llSummary.setVisibility(View.GONE);
        butOneKey.setOnClickListener(this);
        rvList = crvView.getXRecyclerView();
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mRedBagTabAdapter = new RedBagTabAdapter(mContext,this);
        rvList.setAdapter(mRedBagTabAdapter);
        rvList.setLoadingMoreEnabled(false);
        rvList.setLoadingListener(this);
    }

    //用于计算总钱数
    private String getSumMoney() {
        double money = 0;//收入总数
        for (int i = 0; i < mRedbagInfos.size(); i++) {
            money += mRedbagInfos.get(i).getMoney();
        }
        return Util.formatMoneyUtils(money*100f);
    }

    //请求数据返回
    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
        tvNoData.setVisibility(View.GONE);
        crvView.showXrecyclerView();
        rvList.refreshComplete();
        rvList.loadMoreComplete();
        if (response.getUrlParam() == UrlParam.reqRedbagList){
//            Log.e("TTTTTTTT", response.getResponseString()+"|||"+response.isOk());
            RedbagList redbagList = new RedbagList();

//            redbagList.parseJson(testData());
            redbagList.parseJson(response.getResponseString());
            if (response.isOk()){
                ((RedBoxRecordAct)context).refreshView(redbagList.getTotal());
                mRedbagInfos = redbagList.getRedbagLists();
//                Log.e("TTTTTTTTVV",mRedbagInfos.size()+"|||");
                handleData();
                return;
            }
            if (mRedbagInfos != null && !mRedbagInfos.isEmpty()){
                return;
            }
        }else if (response.getUrlParam() == UrlParam.reqAddredonekey){
            if (response.isOk()){
                RedOneKeyList redOneKeyList = (RedOneKeyList) response.getBaseData();
                ((RedBoxRecordAct)context).refreshView(redOneKeyList.getSum());
                mRedbagInfos = redOneKeyList.getRedbagFailLists();
                if(redOneKeyList.getSucnum() == 0) {
                    PToast.showShort(mContext.getString(R.string.no_add_redbag));
                }else {
                    PToast.showShort(mContext.getString(R.string.succeed) + redOneKeyList.getSucnum() + mContext.getString(R.string.hour_into_the_bag));
                }
                handleData();
                return;
            }
            PToast.showShort(response.getMsg());
        }
        showNoData();
    }
    //处理数据
    private void handleData(){
        if (mRedbagInfos != null && !mRedbagInfos.isEmpty()){
            mRedBagTabAdapter.setList(mRedbagInfos);
            butOneKey.setEnabled(true);
            showCollect();
        }else {
            butOneKey.setEnabled(false);
            showNoData();
        }
    }
    //暂无数据
    private void showNoData(){
        llSummary.setVisibility(View.GONE);
        tvNoData.setVisibility(View.VISIBLE);
    }
    //汇总ui显示设置
    public void showCollect(){
        llSummary.setVisibility(View.VISIBLE);
        tvData.setText(R.string.summary);
        tvPath.setText("--");
        tvMoney.setText(getSumMoney()+"");
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
        LoadingDialog.show((FragmentActivity)mContext);
        ModuleMgr.getCommonMgr().reqAddredonekey(ModuleMgr.getCenterMgr().getMyInfo().getUid(),this);//一键入袋
    }

    private String testData(){
        String str = "/*{\n" +
                "  \"status\": \"ok\",\n" +
                "  \"total\": \"20\",\n" +
                "  \"result\":[{\n" +
                "        \"id\":\"2\",\n" +
                "        \"money\":\"300\",\n" +
                "        \"create_time\":\"2016-11-04 10:14:23\",\n" +
                "        \"type\":\"1\",\n" +
                "        \"rank\":0\n" +
                "  },{\n" +
                "        \"id\":\"3\",\n" +
                "        \"money\":\"300\",\n" +
                "        \"create_time\":\"2016-11-04 10:14:23\",\n" +
                "        \"type\":\"2\",\n" +
                "        \"rank\":0\n" +
                "  },{\n" +
                "        \"id\":\"4\",\n" +
                "        \"money\":\"300\",\n" +
                "        \"create_time\":\"2016-11-04 10:14:23\",\n" +
                "        \"type\":\"3\",\n" +
                "        \"rank\":0\n" +
                "  }]\n" +
                "}*/";
        return str;
    }
}