package com.juxin.predestinate.ui.pay.cupvoice;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.PayWX;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import org.json.JSONObject;

/**
 * 支付确认页面
 * Created by Kind on 2017/4/27.
 */

public class PayCupVoiceOkAct extends BaseActivity implements View.OnClickListener {

    private TextView pay_voice_ok;
    private PayGood payGood;
    private String phone;
    private String nickname;
    private String number;
    private String bank_id;

    private boolean IsPay = true;
    private boolean isSuccess = false;
    private boolean isButton = false;//是否是点完成按钮过来的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_pay_cupvoice_ok_act);
        setPagerTitle();
        initView();
    }

    private void setPagerTitle() {
        setBackView(R.id.base_title_back, "语音支付", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsPay) {
                    MMToast.showShort("支付中，请等待");
                } else if (isSuccess) {
                    PayCupVoiceOkAct.this.setResult(Constant.PAY_VOICE_OK);
                    back();
                } else {
                    back();
                }
            }
        });
    }

    private void initView() {
        payGood = (PayGood) getIntent().getSerializableExtra("payGood");
        phone = getIntent().getStringExtra("phone");
        nickname = getIntent().getStringExtra("nickname");
        number = getIntent().getStringExtra("number");
        pay_voice_ok = (TextView) findViewById(R.id.pay_voice_ok);
        pay_voice_ok.setOnClickListener(this);
        if (number != null && !"".equals(number)) {
            bank_id = getIntent().getStringExtra("bank_id");
            executeVoice(number, phone, nickname, bank_id);
        } else {
            executeVoice(phone, nickname);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_voice_ok:
                if (isSuccess) {
                    // 更新信息
                    updateInfo();
                } else {
                    executeQueryVoice(true);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!IsPay) {
            MMToast.showShort("支付中，请等待");
        } else if (isSuccess) {
            this.setResult(Constant.PAY_VOICE_OK);
            this.finish();
        } else {
            this.finish();
        }
    }

    public void executeVoice(String number, String phone, String nickname, String bank_id) {
        IsPay = false;
        ModuleMgr.getCommonMgr().reqAngelPay(payGood.getPay_name(), payGood.getPay_id(), payGood.getPay_money(), number, phone, bank_id, nickname, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PayWX payWX = new PayWX();
                payWX.onPayPhoneCard(response.getResponseString());

                if ("1".equals(payWX.getResult())) {
                    isSuccess = true;
                }else {
                    isSuccess = false;
                }
                IsPay = true;
                MMToast.showShort(payWX.getPayContent());
            }
        });
    }

    public void executeVoice(String phone, String nickname) {
        ModuleMgr.getCommonMgr().reqAngelPayB(payGood.getPay_name(), payGood.getPay_id(), payGood.getPay_money(), phone, nickname, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PayWX payWX = new PayWX();
                payWX.onPayPhoneCard(response.getResponseString());

                if ("1".equals(payWX.getResult())) {
                    isSuccess = true;
                }else {
                    isSuccess = false;
                }
                IsPay = true;
                MMToast.showShort(payWX.getPayContent());
            }
        });
    }

    public void executeQueryVoice(boolean isOK) {

        if(isOK){
            isButton = true;
        }
        LoadingDialog.show(this, "验证中...");
        ModuleMgr.getCommonMgr().reqAnglePayQuery(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                LoadingDialog.closeLoadingDialog();
                PayWX payWX = new PayWX();
                payWX.onPayPhoneCard(response.getResponseString());
                if(!response.isServerResponse()){
                    executeQueryVoice(false);
                    return;
                }

                if ("1".equals(payWX.getResult())) {
                    MMToast.showShort(payWX.getPayContent());
                    if(isButton){
                        updateInfo();// 更新信息
                    }
                    isButton = false;
                    isSuccess = true;
                }else {
                    isButton = false;
                    isSuccess = false;
                    MMToast.showShort(payWX.getPayContent());
                }
                IsPay = true;
            }
        });
    }

    private void updateInfo(){
        LoadingDialog.show(PayCupVoiceOkAct.this,"更新中...");
        ModuleMgr.getCenterMgr().reqMyInfo(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                LoadingDialog.closeLoadingDialog();
                JSONObject jsonObject = response.getResponseJson();
                if(!jsonObject.isNull("userDetail")){
                    MMToast.showShort("更新失败!");
                }
                PayCupVoiceOkAct.this.setResult(Constant.PAY_VOICE_OK);
                PayCupVoiceOkAct.this.finish();
            }
        });
    }
}