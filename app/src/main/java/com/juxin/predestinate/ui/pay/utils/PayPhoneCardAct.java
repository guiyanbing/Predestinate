package com.juxin.predestinate.ui.pay.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.utils.InputUtils;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.pay.PayWX;
import com.juxin.predestinate.module.local.pay.goods.PayGood;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

/**
 * 手机卡充值
 * Created by Kind on 2017/4/26.
 */

public class PayPhoneCardAct extends BaseActivity implements View.OnClickListener {

    private TextView pay_phonecard_title, pay_phonecard_cardMoney, pay_phonecard_tips, pay_phonecard_tips_txt;
    private EditText pay_phonecard_sn, pay_phonecard_pw;
    private Button pay_phonecard_ok;

    private boolean pay_tips = true;
    private Button operator_mobile, operator_unicom, operator_telecom;
    private Button credits_ten, credits_thirty, credits_fifty, credits_hundred;

    private View[] operators = new View[3];
    private View[] creditss = new View[4];

    private int pay_Operators = 0;// 运营商类1是联通，2是电信，0移动
    private int pay_Credits = 50; // 面额

    private String orderID;//订单号
    private PayGood payGood;//商品

    private int Is_PaySearchTask = 0;//请求次数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_pay_phonecardact);

        initView();
    }

    public void initView() {
        payGood = (PayGood) getIntent().getSerializableExtra("payGood");
        orderID = getIntent().getStringExtra("orderID");

        pay_phonecard_title = (TextView) findViewById(R.id.pay_phonecard_title);
        pay_phonecard_cardMoney = (TextView) findViewById(R.id.pay_phonecard_cardMoney);
        pay_phonecard_sn = (EditText) findViewById(R.id.pay_phonecard_sn);
        pay_phonecard_pw = (EditText) findViewById(R.id.pay_phonecard_pw);
        pay_phonecard_ok = (Button) findViewById(R.id.pay_phonecard_ok);
        pay_phonecard_tips = (TextView) findViewById(R.id.pay_phonecard_tips);
        pay_phonecard_tips_txt = (TextView) findViewById(R.id.pay_phonecard_tips_txt);

        operator_mobile = (Button) findViewById(R.id.operator_mobile);
        operator_unicom = (Button) findViewById(R.id.operator_unicom);
        operator_telecom = (Button) findViewById(R.id.operator_telecom);

        credits_ten = (Button) findViewById(R.id.credits_ten);
        credits_thirty = (Button) findViewById(R.id.credits_thirty);
        credits_fifty = (Button) findViewById(R.id.credits_fifty);
        credits_hundred = (Button) findViewById(R.id.credits_hundred);

        pay_phonecard_ok.setOnClickListener(this);
        pay_phonecard_tips.setOnClickListener(this);

        operator_mobile.setOnClickListener(this);
        operator_unicom.setOnClickListener(this);
        operator_telecom.setOnClickListener(this);
        operators[0] = operator_mobile;
        operators[1] = operator_unicom;
        operators[2] = operator_telecom;
        setBtnOperators(operator_mobile);

        credits_ten.setOnClickListener(this);
        credits_thirty.setOnClickListener(this);
        credits_fifty.setOnClickListener(this);
        credits_hundred.setOnClickListener(this);

        creditss[0] = credits_ten;
        creditss[1] = credits_thirty;
        creditss[2] = credits_fifty;
        creditss[3] = credits_hundred;
        setBtnCreditss(credits_hundred);

        pay_phonecard_title.setText(payGood.getPay_name());
        pay_phonecard_cardMoney.setText(payGood.getPay_money() + "元");
    }

    /**
     * @param sn 充值卡号
     * @param password 充值卡密码
     */
    private void onPayment(String sn, String password) {
        LoadingDialog.show(this, "充值中...");
        ModuleMgr.getCommonMgr().reqPhoneCardMethod(payGood.getPay_id(), orderID,
                payGood.getPay_money(), pay_Credits, pay_Operators, sn, password, new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        PayWX payWX = new PayWX();
                        payWX.PayPhoneCard(response.getResponseString());
                        if(!"1".equals(payWX.getResult())){
                            MMToast.showShort("请求失败，请稍后再试");
                            LoadingDialog.closeLoadingDialog();
                            return;
                        }
                        try {
                            Thread.sleep(6000);
                            onPaySearch();
                        }catch (Exception e){
                            e.printStackTrace();
                            LoadingDialog.closeLoadingDialog();
                        }

                    }
                });
    }

    private void onPaySearch(){
        ModuleMgr.getCommonMgr().reqSearchPhoneCardMethod(orderID, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                PayWX payWX = new PayWX();
                payWX.PayPhoneCard(response.getResponseString());
                try {
                    if ("1".equals(payWX.getResult())) {
                        LoadingDialog.closeLoadingDialog();
                        MMToast.showShort(payWX.getPayContent());
                        // 更新信息
                        MsgMgr.getInstance().sendMsg(MsgType.MT_Update_MyInfo, null);
                    } else if("error".equals(payWX.getResult()) && "订单没有同步，再重试".equals(payWX.getPayContent())){
                        if(Is_PaySearchTask < 2){
                            Is_PaySearchTask++;
                            Thread.sleep(12000);
                            onPaySearch();
                        }else{
                            LoadingDialog.closeLoadingDialog();
                            if("订单没有同步，再重试".equals(payWX.getPayContent())){
                                MMToast.showShort("支付出错，请联系客服");
                            }else{
                                MMToast.showShort(payWX.getPayContent());
                            }
                        }
                    }else{
                        LoadingDialog.closeLoadingDialog();
                        MMToast.showShort(payWX.getPayContent());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    LoadingDialog.closeLoadingDialog();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pay_phonecard_ok: {
                InputUtils.forceClose(pay_phonecard_sn);
                InputUtils.forceClose(pay_phonecard_pw);

                String sn = pay_phonecard_sn.getText().toString().trim();
                String pw = pay_phonecard_pw.getText().toString().trim();

                if (TextUtils.isEmpty(sn) || TextUtils.isEmpty(pw)) {
                    MMToast.showShort("您输入的卡或密码有问题，请重输");
                    return;
                }

                onPayment(sn, pw);
                break;
            }
            case R.id.pay_phonecard_tips: {
                if (pay_tips) {
                    pay_phonecard_tips_txt.setVisibility(View.VISIBLE);
                    pay_tips = false;
                } else {
                    pay_phonecard_tips_txt.setVisibility(View.GONE);
                    pay_tips = true;
                }
                break;
            }
            // 运营商
            case R.id.operator_mobile:
            case R.id.operator_unicom:
            case R.id.operator_telecom:
                setBtnOperators(v);
                break;
            // 面额
            case R.id.credits_ten:
            case R.id.credits_thirty:
            case R.id.credits_fifty:
            case R.id.credits_hundred:
                setBtnCreditss(v);
                break;
            default:
                break;
        }
    }

    // 运营商类1是联通，2是电信，0移动
    private void setBtnOperators(View v) {
        switch (v.getId()) {
            case R.id.operator_mobile:
                pay_Operators = 0;
                break;
            case R.id.operator_unicom:
                pay_Operators = 1;
                break;
            case R.id.operator_telecom:
                pay_Operators = 2;
                break;
            default:
                break;
        }
        updateOperatorsSelecter(v);
    }

    // 面额
    private void setBtnCreditss(View v) {
        switch (v.getId()) {
            case R.id.credits_ten:
                pay_Credits = 10;
                break;
            case R.id.credits_thirty:
                pay_Credits = 30;
                break;
            case R.id.credits_fifty:
                pay_Credits = 50;
                break;
            case R.id.credits_hundred:
                pay_Credits = 100;
                break;
            default:
                break;
        }
        updateCreditsSelecter(v);
    }

    private void updateOperatorsSelecter(View v) {
        for (View relativeLayout : operators) {
            if (relativeLayout.equals(v)) {
                relativeLayout.setSelected(true);
            } else {
                relativeLayout.setSelected(false);
            }
        }
    }

    private void updateCreditsSelecter(View v) {
        for (View relativeLayout : creditss) {
            if (relativeLayout.equals(v)) {
                relativeLayout.setSelected(true);
            } else {
                relativeLayout.setSelected(false);
            }
        }
    }
}
