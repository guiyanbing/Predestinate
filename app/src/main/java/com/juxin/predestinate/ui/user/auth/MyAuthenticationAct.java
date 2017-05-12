package com.juxin.predestinate.ui.user.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 我的认证
 * IQQ
 */

public class MyAuthenticationAct extends BaseActivity implements View.OnClickListener {
    private TextView tv_txt_auth_phone, tv_txt_auth_video;
    private UserDetail userDetail;
    private int authResult = 103, authForVodeo = 104;
    private VideoVerifyBean videoVerifyBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_authentication_act);
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        videoVerifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
        setBackView("我的认证");
        initView();
    }


    private void initView() {
        findViewById(R.id.ll_auth_phone).setOnClickListener(this);
        findViewById(R.id.ll_auth_video).setOnClickListener(this);
        tv_txt_auth_phone = (TextView) findViewById(R.id.txt_auth_phone);
        tv_txt_auth_video = (TextView) findViewById(R.id.txt_auth_video);

        if (userDetail.isVerifyCellphone()) {
            tv_txt_auth_phone.setText("已完成");
            tv_txt_auth_phone.setTextColor(getResources().getColor(R.color.authentication_txt_bg));
        } else {
            tv_txt_auth_phone.setText("未认证");
            tv_txt_auth_phone.setTextColor(getResources().getColor(R.color.gray_text));
        }
        tv_txt_auth_video.setText("未认证");
        tv_txt_auth_video.setTextColor(getResources().getColor(R.color.gray_text));
        initVideoAuth();
        initConfig();
    }


    private void initVideoAuth() {
        tv_txt_auth_video.setTextColor(getResources().getColor(R.color.gray_text));

        switch (videoVerifyBean.getStatus()) {
            case 0:
                tv_txt_auth_video.setText("未认证");
                break;
            case 1:
                tv_txt_auth_video.setText("审核中");
                break;
            case 2:
                tv_txt_auth_video.setText("审核未通过");
                break;
            case 3:
                tv_txt_auth_video.setText("审核通过");
                tv_txt_auth_video.setTextColor(getResources().getColor(R.color.authentication_txt_bg));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_auth_phone:
                if (!userDetail.isVerifyCellphone()) {
                    UIShow.showPhoneVerify_Act(MyAuthenticationAct.this, ModuleMgr.getCenterMgr().getMyInfo().isVerifyCellphone(),authResult);
                }
                break;
            case R.id.ll_auth_video:
                UIShow.showMyAuthenticationVideoAct(this,authForVodeo);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == authResult) {
            if (userDetail.isVerifyCellphone()) {
                tv_txt_auth_phone.setText("已完成");
                tv_txt_auth_phone.setTextColor(0xff43cd67);
            }
        } else if (requestCode == authForVodeo) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            initVideoAuth();
        }
    }

    private void initConfig() {
        //已完成认证，不再请求
        if (videoVerifyBean.getStatus() == 3)
            return;

        ModuleMgr.getCommonMgr().requestVideochatConfig();
        setResult(203);
    }
}
