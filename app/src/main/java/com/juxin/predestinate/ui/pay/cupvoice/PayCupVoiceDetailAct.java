package com.juxin.predestinate.ui.pay.cupvoice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.PayWX;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.UIShow;

/**
 * 语音支付详细页面
 *
 * Created by Kind on 2017/4/27.
 */

public class PayCupVoiceDetailAct  extends BaseActivity implements View.OnClickListener {

    private TextView pay_voice_bank_name, pay_voice_price, pay_voice_service, pay_voice_ok;
    private EditText pay_voice_bank_number, pay_voice_bank_phone, pay_voice_bank_nickname, pay_voice_bank_id;

    private PayGood payGood = null;
    private PayWX payWX = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_pay_cupvoice_detail_act);
        setBackView(R.id.base_title_back, "语音支付");
        initView();
    }

    private void initView() {
        payGood = (PayGood) getIntent().getSerializableExtra("payGood");

        payWX = (PayWX) getIntent().getSerializableExtra("payWX");
        String bank_name = getIntent().getStringExtra("bank_name");
        pay_voice_bank_name = (TextView) findViewById(R.id.pay_voice_bank_name);
        pay_voice_price = (TextView) findViewById(R.id.pay_voice_price);
        pay_voice_service = (TextView) findViewById(R.id.pay_voice_service);
        pay_voice_bank_number = (EditText) findViewById(R.id.pay_voice_bank_number);
        pay_voice_bank_phone = (EditText) findViewById(R.id.pay_voice_bank_phone);
        pay_voice_bank_nickname = (EditText) findViewById(R.id.pay_voice_bank_nickname);
        pay_voice_bank_id = (EditText) findViewById(R.id.pay_voice_bank_id);
        pay_voice_ok = (TextView) findViewById(R.id.pay_voice_ok);

        pay_voice_bank_id.setKeyListener(new NumberKeyListener() {

            @Override
            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_TEXT;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] mychar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'X', 'x'};
                return mychar;
            }
        });

        pay_voice_ok.setOnClickListener(this);

        pay_voice_bank_name.setText(bank_name);
        pay_voice_price.setText("支付金额:" + payGood.getPay_money());
        pay_voice_service.setText(payGood.getPay_money()+"");

        if (payWX == null) {
            pay_voice_bank_name.setVisibility(View.VISIBLE);
        } else {
            pay_voice_bank_number.setText(payWX.getPan());
            pay_voice_bank_phone.setText(payWX.getMobile());
            pay_voice_bank_nickname.setText(payWX.getRealName());
            pay_voice_bank_id.setText(payWX.getIdcard());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_voice_ok:
                String number = pay_voice_bank_number.getText().toString().trim();
                String phone = pay_voice_bank_phone.getText().toString().trim();
                String nickname = pay_voice_bank_nickname.getText().toString().trim();
                String bank_id = pay_voice_bank_id.getText().toString().trim();

                if (payWX != null) {
                    if (number.equals(payWX.getPan()) && phone.equals(payWX.getMobile()) && nickname.equals(payWX.getRealName()) && bank_id.equals(payWX.getIdcard())) {
                        UIShow.showPayCupVoiceOkAct(this, payGood, phone, nickname, null, null, Constant.PAY_VOICE_DETAIL);
                    } else {
                        getData(number, phone, nickname, bank_id);
                    }
                } else {
                    getData(number, phone, nickname, bank_id);
                }
                break;

            default:
                break;
        }
    }

    private void getData(String number, String phone, String nickname, String bank_id) {
        if(TextUtils.isEmpty(number)){
            PToast.showShort("请输入正确的卡号");
            return;
        }

        if(TextUtils.isEmpty(phone)){ // || !BaseUtil.isMobileNO(phone)
            PToast.showShort("请输入正确的手机号");
            return;
        }

        if(TextUtils.isEmpty(nickname)){
            PToast.showShort("请输入正确的名称");
            return;
        }

        if(TextUtils.isEmpty(bank_id)){
            PToast.showShort("请输入正确的身份证号");
            return;
        }

        UIShow.showPayCupVoiceOkAct(this, payGood, phone, nickname, number, bank_id, Constant.PAY_VOICE_DETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.PAY_VOICE_DETAIL:
                if (resultCode == Constant.PAY_VOICE_OK) {
                    setResult(Constant.PAY_VOICE_DETAIL);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
