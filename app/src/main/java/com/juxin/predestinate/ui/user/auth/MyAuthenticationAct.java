package com.juxin.predestinate.ui.user.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.bean.my.IdCardVerifyStatusInfo;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 我的认证
 * xy
 */

public class MyAuthenticationAct extends BaseActivity implements View.OnClickListener, RequestComplete {
    private TextView tv_txt_auth_phone, tv_txt_auth_video, tv_txt_auth_id;
    private UserDetail userDetail;
    private int authForVodeo = 104;
    public static final int AUTHENTICSTION_REQUESTCODE = 103;
    private int authIDCard = 105;
    private VideoVerifyBean videoVerifyBean;
    private IdCardVerifyStatusInfo mIdCardVerifyStatusInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_authentication_act);
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        videoVerifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
        mIdCardVerifyStatusInfo = ModuleMgr.getCommonMgr().getIdCardVerifyStatusInfo();
        setBackView(getResources().getString(R.string.title_auth));
        initView();
    }


    private void initView() {
        findViewById(R.id.ll_auth_phone).setOnClickListener(this);
        findViewById(R.id.ll_auth_video).setOnClickListener(this);
        findViewById(R.id.ll_auth_id).setOnClickListener(this);
        tv_txt_auth_phone = (TextView) findViewById(R.id.txt_auth_phone);
        tv_txt_auth_video = (TextView) findViewById(R.id.txt_auth_video);
        tv_txt_auth_id = (TextView) findViewById(R.id.txt_auth_id);
//TODO 身份认证状态
        if (userDetail.isVerifyCellphone()) {
            tv_txt_auth_phone.setText(getResources().getString(R.string.txt_authstatus_authok));
            tv_txt_auth_phone.setTextColor(ContextCompat.getColor(this, R.color.authentication_txt_bg));
        } else {
            tv_txt_auth_phone.setText(getResources().getString(R.string.txt_authstatus_authno));
            tv_txt_auth_phone.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
        }
        tv_txt_auth_video.setText(getResources().getString(R.string.txt_authstatus_authno));
        tv_txt_auth_video.setTextColor(ContextCompat.getColor(this, R.color.gray_text));
        initVideoAuth();
        initIdCardAuth();
        initConfig();
    }


    private void initVideoAuth() {
        tv_txt_auth_id.setTextColor(ContextCompat.getColor(this, R.color.gray_text));

        switch (mIdCardVerifyStatusInfo.getStatus()) {
            case 0:
                tv_txt_auth_id.setText(getResources().getString(R.string.txt_authstatus_authno));
                break;
            case 1:
                tv_txt_auth_id.setText(getResources().getString(R.string.txt_authstatus_authing));
                ContextCompat.getColor(this, R.color.txt_authing);
                break;
            case 2:
                tv_txt_auth_id.setText(getResources().getString(R.string.txt_authstatus_authok));
                ContextCompat.getColor(this, R.color.authentication_txt_bg);
                break;
            case 3:
            case 4:
                tv_txt_auth_id.setText(getResources().getString(R.string.txt_authstatus_autherror));
                break;
        }
    }

    private void initIdCardAuth() {
        tv_txt_auth_video.setTextColor(ContextCompat.getColor(this, R.color.gray_text));

        switch (videoVerifyBean.getStatus()) {
            case 0:
                tv_txt_auth_video.setText(getResources().getString(R.string.txt_authstatus_authno));
                break;
            case 1:
                tv_txt_auth_video.setText(getResources().getString(R.string.txt_authstatus_authing));
                ContextCompat.getColor(this, R.color.txt_authing);
                break;
            case 2:
                tv_txt_auth_video.setText(getResources().getString(R.string.txt_authstatus_autherror));
                break;
            case 3:
                tv_txt_auth_video.setText(getResources().getString(R.string.txt_authstatus_authok));
                ContextCompat.getColor(this, R.color.authentication_txt_bg);
                break;
            default:
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
            case R.id.ll_auth_phone://手机认证页
                if (!userDetail.isVerifyCellphone()) {
                    UIShow.showPhoneVerifyAct(MyAuthenticationAct.this, ModuleMgr.getCenterMgr().getMyInfo().isVerifyCellphone(), AUTHENTICSTION_REQUESTCODE);
                } else {//手机认证完成页
                    UIShow.showPhoneVerifyCompleteAct(MyAuthenticationAct.this, AUTHENTICSTION_REQUESTCODE);
                }
                break;
            case R.id.ll_auth_video://自拍认证
                UIShow.showMyAuthenticationVideoAct(this, authForVodeo);
                break;
            case R.id.ll_auth_id://身份认证
                //TODO
                if (mIdCardVerifyStatusInfo.isOk())
                    UIShow.showIDCardAuthenticationAct(this, authIDCard);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTHENTICSTION_REQUESTCODE) {
            if (userDetail.isVerifyCellphone()) {
                tv_txt_auth_phone.setText(getResources().getString(R.string.txt_authstatus_authok));
                tv_txt_auth_phone.setTextColor(ContextCompat.getColor(this, R.color.authentication_txt_bg));
            }
            if (resultCode == Constant.EXITLOGIN_RESULTCODE) {
                setResult(Constant.EXITLOGIN_RESULTCODE);
                back();
            }
        } else if (requestCode == authForVodeo) {
            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            initVideoAuth();
        } else if (requestCode == authIDCard) {
            if (data == null) {
                return;
            }
            int back = data.getIntExtra(IDCardAuthenticationSucceedAct.IDCARDBACK, 0);
            if (back == 1)
                this.finish();
//            userDetail = ModuleMgr.getCenterMgr().getMyInfo();
//            initVideoAuth();
        }
    }

    private void initConfig() {
        //已完成认证，不再请求
        if (videoVerifyBean.getStatus() == 3)
            return;

        ModuleMgr.getCommonMgr().requestVideochatConfig();
        setResult(203);
        if (mIdCardVerifyStatusInfo.getStatus() == 2)
            return;
        ModuleMgr.getCommonMgr().getVerifyStatus(null);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {

    }
}
