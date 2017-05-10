package com.juxin.predestinate.ui.wode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.wode.bean.QunCountInfo;

/**
 * 我的钱包页面
 * Created by zm on 2017/4/20
 */
public class DemandRedPacketAct extends BaseActivity implements View.OnClickListener,RequestComplete{

    private TextView tvSendNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_demand_red_packet_act);
        initView();
        initData();
    }

    private void initView(){
        setBackView(R.id.base_title_back);
        setTitle("我要赚红包");
        findViewById(R.id.demand_red_packet_tv_askfor_gift_send).setOnClickListener(this);
        tvSendNum = (TextView) findViewById(R.id.demand_red_packet_tv_askfor_gift_send_num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.demand_red_packet_tv_askfor_gift_send://索要礼物弹框
                AskforGiftDialog dialog = new AskforGiftDialog(DemandRedPacketAct.this,"","");
                dialog.show();
                break;
            default:
                break;
        }
    }

    private void initData(){
        ModuleMgr.getCommonMgr().CMDRequest("GET", true, UrlParam.qunCount.getFinalUrl(), null, DemandRedPacketAct.this);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        QunCountInfo info = new QunCountInfo();
        info.parseJson(response.getResponseString());
        if (info.isOk()){
//            Log.e("TTTTTTRRRR", response.getResponseString() + "   ||   " + jsonResult+"|||"+response.isOk()+"|||"+response.getResponseJson());
            tvSendNum.setText(info.getCount()+"");
            return;
        }
        PToast.showShort(getString(R.string.net_error));
    }
}