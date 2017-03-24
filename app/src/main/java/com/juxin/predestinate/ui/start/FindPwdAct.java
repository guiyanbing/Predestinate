package com.juxin.predestinate.ui.start;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.util.BaseUtil;

/**
 * 找回密码
 * Created YAO on 2017/3/24.
 */

public class FindPwdAct extends BaseActivity implements View.OnClickListener {
    private EditText et_phone, et_code, et_pwd;
    private Button bt_send_code, bt_submit;
    private String phone, code;
    private boolean timerSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r1_findpwd_act);
        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        bt_send_code = (Button) findViewById(R.id.bt_send_code);
        bt_submit = (Button) findViewById(R.id.bt_submit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_code:
                if (checkPhone()) {
                    //TODO 发送验证码
//                    ModuleMgr.getCenterMgr().sendSMS(phone, Constant.BIND_PHONE, this);
                    LoadingDialog.show(this, getResources().getString(R.string.tip_loading_submit));
                }
                break;
            case R.id.bt_submit:
                if (checkPhone() && checkCode()) {
                    //TODO 提交
//                    ModuleMgr.getCenterMgr().bindChellPhone(phoneNum, Constant.BIND_PHONE, keys, this);
                    LoadingDialog.show(this, getResources().getString(R.string.tip_loading_submit));
                }
                break;

        }
    }

    private boolean checkCode() {
        code = et_code.getText().toString();
        if (TextUtils.isEmpty(code)) {
            PToast.showShort(getResources().getString(R.string.toast_code_isnull));
            return false;
        }
        return true;
    }

    private boolean checkPhone() {
        phone = et_phone.getText().toString();
        if (phone == null || "".equals(phone)) {
            PToast.showShort(getResources().getString(R.string.toast_phone_isnull));
            return false;
        }
        if (!BaseUtil.isNumeric(phone) || phone.length() != 11) {
            PToast.showShort(getResources().getString(R.string.toast_phone_iserror));
            return false;
        }
        return true;
    }

}
