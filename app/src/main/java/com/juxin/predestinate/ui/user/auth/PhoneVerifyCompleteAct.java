package com.juxin.predestinate.ui.user.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;

import org.json.JSONObject;

/**
 * 手机认证完成页面
 * Created by xy on 2017/5/17.
 */

public class PhoneVerifyCompleteAct extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_phoneverify_complete_act);
        setBackView(getResources().getString(R.string.title_phone_certification));
        initView();

    }

    private void initView() {
        findViewById(R.id.bt_return_authact).setOnClickListener(this);
        findViewById(R.id.ll_open_qq_btn).setOnClickListener(this);
        ((TextView) findViewById(R.id.tv_customerservice_desc)).setText(getResources().getString(R.string.txt_customerservice_complete_desc));
        ((TextView) findViewById(R.id.tv_customerservice_phone)).setText("0731-1231124444");//TODO 获取客服手机
    }

    public static void clearUserInfo() {
        // 清除当前登录的用户信息
        ModuleMgr.getLoginMgr().logout();
        PSP.getInstance().put("addMsgToUserDate", null);
        PSP.getInstance().put("recommendDate", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_open_qq_btn://在线客服qq交流
                LoadingDialog.show(PhoneVerifyCompleteAct.this);
                ModuleMgr.getCommonMgr().getCustomerserviceQQ(new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        LoadingDialog.closeLoadingDialog();
                        if (!response.isOk()) {
                            PToast.showShort(response.getMsg());
                            return;
                        }
                        JSONObject jsonObject = response.getResponseJson();
                        String qq = jsonObject.optString("qq");
                        UIShow.showQQService(PhoneVerifyCompleteAct.this, qq);
                    }
                });
                break;
            case R.id.bt_return_authact:
                setResult(203);
                finish();
                break;
            default:
                break;
        }
    }
}
