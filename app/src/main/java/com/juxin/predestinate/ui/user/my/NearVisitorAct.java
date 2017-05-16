package com.juxin.predestinate.ui.user.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

/**
 * 我的钱包页面（暂时废弃）
 * Created by zm on 2017/4/20
 */
public class NearVisitorAct extends BaseActivity implements View.OnClickListener,RequestComplete{

    private TextView tvSendNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_near_visitor_act);
        initView();
        initData();
    }

    private void initView(){
        setBackView(R.id.base_title_back);
        setTitle("最近来访");
//        findViewById(R.id.demand_red_packet_tv_askfor_gift_send).setOnClickListener(this);
//        tvSendNum = (TextView) findViewById(R.id.demand_red_packet_tv_askfor_gift_send_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.demand_red_packet_tv_askfor_gift_send:
                //// TODO: 2017/5/2 索要礼物弹框
                break;
            default:
                break;
        }
    }

    private void initData(){
//        ModuleMgr.getCommonMgr().CMDRequest("GET", true, UrlParam.qunCount.getFinalUrl(), null, NearVisitorAct.this);
        ModuleMgr.getCommonMgr().viewMeList(this);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {

    }
}