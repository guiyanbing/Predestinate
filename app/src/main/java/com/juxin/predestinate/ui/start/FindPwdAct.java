package com.juxin.predestinate.ui.start;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.BaseUtil;
import com.juxin.predestinate.module.util.CommonUtil;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 找回密码
 * Created YAO on 2017/3/24.
 */

public class FindPwdAct extends BaseActivity implements View.OnClickListener, RequestComplete {
    private EditText et_phone, et_code, et_pwd;
    private Button bt_send_code, bt_submit;
    private String phone, code,pwd;
    private boolean timerSwitch=true;
    private SendEnableThread sendthread = null;
    private Handler m_Handler = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r1_findpwd_act);
        setBackView(getResources().getString(R.string.title_findpwdact));
        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        bt_send_code = (Button) findViewById(R.id.bt_send_code);
        bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_send_code.setOnClickListener(this);
        m_Handler = new Handler() {
            public final void handleMessage(Message msg) {
                if (msg.what > 0) {
                    bt_send_code.setText(String.format(getString(R.string.bt_send_code_press), msg.what));

                } else {
                    timerSwitch = true;
                    bt_send_code.setText(getResources().getString(R.string.bt_send_code_normal));
                    bt_send_code.setEnabled(true);
                }
            }
        };
        bt_send_code.setEnabled(timerSwitch ? true : false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_code:
                if (checkPhone()) {
                    ModuleMgr.getCenterMgr().reqVerifyCode(phone, this);
                    LoadingDialog.show(this, getResources().getString(R.string.tip_loading_submit));
                }
                break;
            case R.id.bt_submit:
                if (checkPhone()&&checkSubmitInfo()) {
                    ModuleMgr.getCenterMgr().resetPassword(phone, pwd, code, this);
                    LoadingDialog.show(this, getResources().getString(R.string.tip_loading_submit));
                }
                break;

        }
    }

    private boolean checkSubmitInfo() {
            code = et_code.getText().toString();
            if (TextUtils.isEmpty(code)) {
                PToast.showShort(getResources().getString(R.string.toast_code_isnull));
                return false;
            }
            pwd = et_pwd.getText().toString();
            if (TextUtils.isEmpty(pwd)) {
                PToast.showShort(getResources().getString(R.string.toast_setpwd_isnull));
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

    public class SendEnableThread extends Thread {
        private boolean isStop = false;

        public void setStop(boolean isStop) {
            this.isStop = isStop;
        }

        public void run() {
            int count = 60;
            while (!isStop && count >= 0) {
                try {
                    Thread.sleep(1000);
                    m_Handler.sendEmptyMessage(count);
                } catch (Exception e) {
                    break;
                }
                count--;
            }
        }
    }
    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
        if (response.getUrlParam() == UrlParam.reqReqVerifyCode) {
            if (response.isOk()) {
                PToast.showShort("验证码发送成功");
                bt_send_code.setEnabled(false);
                et_code.requestFocus();
                if (sendthread == null || sendthread.getState() == Thread.State.TERMINATED) {
                    sendthread = new SendEnableThread();
                    sendthread.start();
                    timerSwitch = false;
                }

            } else {
                bt_send_code.setEnabled(true);
                PToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
            }
        }else if(response.getUrlParam()== UrlParam.resetPassword){
            if (response.isOk()){
                PToast.showShort("找回密码成功");
                back();
            }else{
                PToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
            }

        }
    }
}
