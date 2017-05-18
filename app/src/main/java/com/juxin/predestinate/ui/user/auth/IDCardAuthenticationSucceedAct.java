package com.juxin.predestinate.ui.user.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.my.RedBoxRecordAct;

/**
 * Created by zm on 2017/5/16
 */
public class IDCardAuthenticationSucceedAct extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_id_card_auth_succeed_act);
        initView();
    }

    private void initView() {
        initTitle();
        findViewById(R.id.id_card_succeed_btn_red).setOnClickListener(this);
        findViewById(R.id.id_card_succeed_btn_main).setOnClickListener(this);
    }

    private void initTitle() {
        setBackView(R.id.base_title_back);
        setTitle("身份认证");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_card_succeed_btn_red:
                //返回钱包
                finish();
                UIShow.showActivityClearTask(this, RedBoxRecordAct.class);
                break;
            case R.id.id_card_succeed_btn_main:
                //返回首页
                finish();
                break;
            default:
                break;
        }
    }
}
