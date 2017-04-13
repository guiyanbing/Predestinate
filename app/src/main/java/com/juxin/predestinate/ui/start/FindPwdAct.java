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

import java.lang.ref.WeakReference;

/**
 * 找回密码(绑定手机)
 * Created YAO on 2017/3/24.
 */

public class FindPwdAct extends BaseActivity implements View.OnClickListener, RequestComplete {
    private EditText et_phone, et_code, et_pwd;
    private Button bt_send_code;
    private String phone, code, pwd;
    private boolean timerSwitch = true;
    private SendEnableThread sendthread = null;
    public static final int OPEN_FINDPWD = 0x1;
    public static final int OPEN_BINDPHONE = 0x2;
    private int openAct;
    private int getCodetag;
    private final MyHandler m_Handler = new MyHandler(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_findpwd_act);
        initAct();
        setBackView(OPEN_FINDPWD == openAct ? getResources().getString(R.string.title_findpwdact) : getResources().getString(R.string.title_phone_certification));
        initView();
    }

    private void initAct() {
        openAct = getIntent().getIntExtra("openAct", OPEN_FINDPWD);
        if (OPEN_FINDPWD == openAct) {
            getCodetag = 301;
        } else if (OPEN_BINDPHONE == openAct) {
            getCodetag = 302;
        }
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        bt_send_code = (Button) findViewById(R.id.bt_send_code);
        findViewById(R.id.bt_submit).setOnClickListener(this);
        bt_send_code.setOnClickListener(this);
//        m_Handler = new Handler() {
//            public final void handleMessage(Message msg) {
//
//            }
//        };
        bt_send_code.setEnabled(timerSwitch);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send_code:
                if (checkPhone()) {
                    ModuleMgr.getCenterMgr().reqVerifyCode(phone, this, getCodetag);
                    LoadingDialog.show(this, getResources().getString(R.string.tip_loading_submit));
                }
                break;
            case R.id.bt_submit:
                if (checkPhone() && checkSubmitInfo()) {
                    if (OPEN_FINDPWD == openAct) {
                        ModuleMgr.getCenterMgr().resetPassword(phone, pwd, code, this);
                    } else {
                        ModuleMgr.getCenterMgr().mobileAuth(ModuleMgr.getCenterMgr().getMyInfo().getUid(), phone, pwd, code, this);
                    }
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
        if ("".equals(phone)) {
            PToast.showShort(getResources().getString(R.string.toast_phone_isnull));
            return false;
        }
        if (!BaseUtil.isNumeric(phone) || phone.length() != 11) {
            PToast.showShort(getResources().getString(R.string.toast_phone_iserror));
            return false;
        }
        return true;
    }


    private static class MyHandler extends Handler {
        private WeakReference<FindPwdAct> mActivity;

        MyHandler(FindPwdAct act) {
            this.mActivity = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            FindPwdAct act = mActivity.get();
            if (act != null) {
                if (msg.what > 0) {
                    act.bt_send_code.setText(String.format(act.getString(R.string.bt_send_code_press), msg.what));

                } else {
                    act.timerSwitch = true;
                    act.bt_send_code.setText(act.getResources().getString(R.string.bt_send_code_normal));
                    act.bt_send_code.setEnabled(true);
                }
            }
        }
    }

    private class SendEnableThread extends Thread {
        public void run() {
            int count = 60;
            while (count >= 0) {
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
        } else if (response.getUrlParam() == UrlParam.resetPassword) {
            if (response.isOk()) {
                PToast.showShort(getResources().getString(R.string.toast_resetpwd_ok));
                back();
            } else {
                PToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
            }
        } else if (response.getUrlParam() == UrlParam.mobileAuth) {
            if (response.isOk()) {
                PToast.showShort(getResources().getString(R.string.toast_mobile_authok));
                back();
            } else {
                PToast.showShort(CommonUtil.getErrorMsg(response.getMsg()));
            }
        }
    }
}
