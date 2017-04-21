package com.juxin.predestinate.ui.start;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.BaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Thread.State;
import java.lang.ref.WeakReference;

/**
 * 手机验证页面
 *
 * @author dengxiaohong add fare by IQQ
 */
public class PhoneVerify_Act extends BaseActivity implements OnClickListener, RequestComplete {

    private LinearLayout llyfirst;
    private LinearLayout llynext;
    private LinearLayout llyok;
    private EditText edtPhone;
    private EditText et_code;
    private EditText edtfouce;
    private Button bt_send_code;
    private Button btnok;
    private TextView txtPhone;
    private TextView txtDesc;

    private boolean isok = false;

    // byIQQ phone fare
    private String phone, code;
    private final MyHandler m_Handler = new MyHandler(this);
    private SendEnableThread sendthread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_activity_phoneverify);
        setBackView(getResources().getString(R.string.title_phone_certification));
        initView();
        // 手机验证
//		AppCfg.getAppCfg().onSava(this, 31);
    }

    private static class MyHandler extends Handler {
        private WeakReference<PhoneVerify_Act> mActivity;

        MyHandler(PhoneVerify_Act act) {
            this.mActivity = new WeakReference<>(act);
        }

        @Override
        public void handleMessage(Message msg) {
            PhoneVerify_Act act = mActivity.get();
            if (act != null) {
                if (msg.what > 0) {
                    act.bt_send_code.setText(String.format(act.getString(R.string.bt_send_code_press), msg.what));
                } else {
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

    private void initView() {
        llyfirst = (LinearLayout) this.findViewById(R.id.lly_phoneverify_first);
        llynext = (LinearLayout) this.findViewById(R.id.lly_phoneverify_next);
        edtPhone = (EditText) this.findViewById(R.id.edt_phoneverify_phone);
        et_code = (EditText) this.findViewById(R.id.edt_phoneverify_note);
        edtfouce = (EditText) this.findViewById(R.id.edt_phoneverify_fouce);
        bt_send_code = (Button) this.findViewById(R.id.btn_phoneverify_begin);
        btnok = (Button) this.findViewById(R.id.btn_phoneverify_ok);
        txtPhone = (TextView) this.findViewById(R.id.txt_phoneverify_phone);
        txtDesc = (TextView) this.findViewById(R.id.txt_phoneverify_desc1);
        llynext.setVisibility(View.GONE);
        bt_send_code.setOnClickListener(this);
        btnok.setOnClickListener(this);
        isok = this.getIntent().getBooleanExtra("isVerify", false);
        if (!isok) {
            llyfirst.setVisibility(View.VISIBLE);
            llynext.setVisibility(View.GONE);
            btnok.setEnabled(false);
        } else {
            llyfirst.setVisibility(View.GONE);
            llynext.setVisibility(View.VISIBLE);
            setFinishedState();
        }
        edtfouce.setFocusable(true);
        edtfouce.requestFocus();
        btnok.setText("立即验证");
    }

    private void setFinishedState() {
        txtDesc.setText("您已经验证成功，登录密码为：");
        UserDetail user = ModuleMgr.getCenterMgr().getMyInfo();
        if (user != null) {
            String password = ModuleMgr.getLoginMgr().getUserList().get(0).getPw();
            txtPhone.setText(password);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_phoneverify_begin:
                if (validPhone()) {
                    ModuleMgr.getCenterMgr().reqVerifyCodeEx(phone, this);
                    LoadingDialog.show(this, getResources().getString(R.string.tip_loading_submit));
                }
                break;
            case R.id.btn_phoneverify_ok:
                if (isok) {
                    this.finish();
                } else {
                    if (validInput()) {
                        ModuleMgr.getCenterMgr().mobileAuthEx(phone, code, this);
                        LoadingDialog.show(this, getResources().getString(R.string.tip_loading_submit));
                    }
                }
                break;
        }

    }

    private boolean validPhone() {
        phone = edtPhone.getText().toString();
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

    private boolean validInput() {
        code = et_code.getText().toString();
        if (TextUtils.isEmpty(code)) {
            PToast.showShort(getResources().getString(R.string.toast_code_isnull));
            return false;
        }
        return true;
    }


    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
        if (response.getUrlParam() == UrlParam.reqReqVerifyCode) {
            switch (getCodeResult(response.getResponseString())) {
                case 0:
                    PToast.showLong("系统忙，请稍候再试。");
                    bt_send_code.setEnabled(true);
                    break;
                case 1:
                    et_code.requestFocus();
                    bt_send_code.setEnabled(false);
                    btnok.setEnabled(true);
                    if (sendthread == null || sendthread.getState() == State.TERMINATED) {
                        sendthread = new SendEnableThread();
                        sendthread.start();
                    }
                    break;
                case 2:
                    PToast.showLong("您的帐号已与手机绑定，不能再绑定。");
                    this.finish();
                    break;
                case 3:
                    PToast.showLong("该手机已经被验证过了，请输入其他的手机号。");
                    bt_send_code.setEnabled(true);
                    break;
            }
        } else if (response.getUrlParam() == UrlParam.mobileAuth) {

            switch (getCodeResult(response.getResponseString())) {
                case 0:
                    PToast.showLong("验证码错误。");
                    bt_send_code.setEnabled(true);
                    break;
                case 1:
                    PToast.showShort(getResources().getString(R.string.toast_mobile_authok));
//                    AppModel.getInstance().getUserDetail().setVerifyCellphone(true);
//                    AppModel.getInstance().getUserDetail().setPhone(edtPhone.getText().toString());
                    isok = true;
                    this.setResult(102);
                    llyfirst.setVisibility(View.GONE);
                    llynext.setVisibility(View.VISIBLE);
                    setFinishedState();
                    back();
                    break;
            }
        }

    }

    /**
     * 手机验证提交手机解析 result 0 失败,1 成功,2 当前帐号已绑定手机,3 当前手机号已被别人绑定
     *
     * @author dengxiaohong
     */
    private int getCodeResult(String str) {
        try {
            JSONObject jsonObject = new JSONObject(str);
            String resCode = jsonObject.optString("respCode");
            int errno = jsonObject.optInt("errNo");
            if (resCode == null) {
                return 0;
            }
            if (resCode.equals("success")) {
                return 1;
            }
            if (errno == 1) {
                return 2;
            } else if (errno == 2) {
                return 3;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
