package com.juxin.predestinate.ui.start;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PSP;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.PickerDialogUtil;

/**
 * 手机认证完成页面
 * Created by xy on 2017/5/17.
 */

public class PhoneVerifyCompleteAct extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_phoneverify_complete_act);
        setBackView(getResources().getString(R.string.title_phone_certification));
        initView();

    }

    private void initView() {
        UserDetail user = ModuleMgr.getCenterMgr().getMyInfo();
        if (user != null) {
            String password = ModuleMgr.getLoginMgr().getUserList().get(0).getPw();
            ((TextView) findViewById(R.id.txt_phoneverify_complete_pwd)).setText(password);
        }
        findViewById(R.id.bt_return_authact).setOnClickListener(this);
        findViewById(R.id.bt_return_loginact).setOnClickListener(this);
    }


    public void exitLogin() {
        clearUserInfo();
        setResult(Constant.EXITLOGIN_RESULTCODE);
        finish();
    }

    public static void clearUserInfo() {
        // 清除当前登录的用户信息
        ModuleMgr.getLoginMgr().logout();
        PSP.getInstance().put("addMsgToUserDate", null);
        PSP.getInstance().put("recommendDate", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_return_authact:
                setResult(203);
                finish();
                break;
            case R.id.bt_return_loginact:
                PickerDialogUtil.showSimpleTipDialog(this, new SimpleTipDialog.ConfirmListener() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onSubmit() {
                        exitLogin();
                    }
                }, getResources().getString(R.string.dal_exit_content), getResources().getString(R.string.dal_exit_title), getResources().getString(R.string.dal_cancle), getResources().getString(R.string.dal_submit), true);
                break;
        }
    }
}
