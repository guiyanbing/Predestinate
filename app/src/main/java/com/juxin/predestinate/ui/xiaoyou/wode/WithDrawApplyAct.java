package com.juxin.predestinate.ui.xiaoyou.wode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.WithdrawAddressInfo;

/**
 * 红包提现--银行卡界面
 * Created by zm on 2017/4/20
 */
public class WithDrawApplyAct extends BaseActivity implements View.OnClickListener,RequestComplete{

    private int mId;
    private String mEidtMoney = "0";
    private boolean mIsFromEdit;

    private EditText etMoney;
    private EditText etCardName;
    private EditText etCardLocal;
    private EditText etCardLocalBranch;
    private EditText etCardNum;
    private Button btnNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_wode_bank_card_act);
        mIsFromEdit = getIntent().getBooleanExtra("fromEdit",false);
        initView();
    }

    private void initView(){
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.withdrawal_page));

        etMoney = (EditText) findViewById(R.id.bank_card_et_money);
        etCardName = (EditText) findViewById(R.id.bank_card_et_card_name);
        etCardLocal = (EditText) findViewById(R.id.bank_card_et_card_local);
        etCardLocalBranch = (EditText) findViewById(R.id.bank_card_et_card_local_branch);
        etCardNum = (EditText) findViewById(R.id.bank_card_et_card_num);
        btnNext = (Button) findViewById(R.id.bank_card_btn_next);
        if(mIsFromEdit) {
            mEidtMoney =  getIntent().getDoubleExtra("money",0)+"";
            mId = getIntent().getIntExtra("id", 0);
        }else {
            mEidtMoney = PSP.getInstance().getLong(RedBoxRecordAct.REDBOXMONEY+ModuleMgr.getCenterMgr().getMyInfo().getUid(),0)+"";
        }
        etMoney.setText(mEidtMoney + getString(R.string.head_unit));
        btnNext.setOnClickListener(this);
        initDefaultAddress();

    }

    private void initDefaultAddress() {
        LoadingDialog.show(this);
     ModuleMgr.getCommonMgr().reqWithdrawAddress(this);
    }

    @Override
    public void onClick(View v) {
        if (!(NetworkUtils.isConnected(this))) {
            PToast.showShort(getResources().getString(R.string.tip_net_error));
            return;
        }
        switch (v.getId()) {
            case R.id.bank_card_btn_next:
                String cardName = etCardName.getText().toString().trim();
                String cardLocal = etCardLocal.getText().toString().trim();
                String cardLocalBranch = etCardLocalBranch.getText().toString().trim();
                String cardNum = etCardNum.getText().toString().trim();
                if(mIsFromEdit) {
                    if(TextUtils.isEmpty(cardName)) {
                        PToast.showShort(getString(R.string.name_cannot_be_empty));
                        return;
                    }
                    if(TextUtils.isEmpty(cardLocal)) {
                        PToast.showShort(getString(R.string.bank_cannot_be_empty));
                        return;
                    }

                    if(TextUtils.isEmpty(cardLocalBranch)) {
                        PToast.showShort(getString(R.string.branch_cannot_be_empty));
                        return;
                    }

                    if(TextUtils.isEmpty(cardNum)) {
                        PToast.showShort(getString(R.string.bank_card_cannot_be_empty));
                        return;
                    }
                    // 修改地址
                    ModuleMgr.getCommonMgr().reqWithdrawModify(mId,cardName,cardNum,cardLocal,cardLocalBranch,this);
                    break;
                }
                //获取自己可提现金额
                String myMoney = PSP.getInstance().getLong(RedBoxRecordAct.REDBOXMONEY+ModuleMgr.getCenterMgr().getMyInfo().getUid(),0)+"";
                int minMoney = ModuleMgr.getCommonMgr().getCommonConfig().getMinmoney();

                if(TextUtils.isEmpty(mEidtMoney)) {
                    PToast.showShort(getString(R.string.money_cannot_be_empty));
                    return;
                }

                if(Float.parseFloat(mEidtMoney) > Float.parseFloat(myMoney)) {
                    PToast.showShort(getString(R.string.not_beyond));
                    return;
                }

                if(Float.valueOf(mEidtMoney) <= minMoney) {
                    PToast.showShort(getString(R.string.withdraw_tips) + minMoney + getString(R.string.head_unit));
                    return;
                }
                if(TextUtils.isEmpty(cardName)) {
                    PToast.showShort(getString(R.string.name_cannot_be_empty));
                    return;
                }
                if(TextUtils.isEmpty(cardLocal)) {
                    PToast.showShort(getString(R.string.bank_cannot_be_empty));
                    return;
                }

                if(TextUtils.isEmpty(cardLocalBranch)) {
                    PToast.showShort(getString(R.string.branch_cannot_be_empty));
                    return;
                }

                if(TextUtils.isEmpty(cardNum)) {
                    PToast.showShort(getString(R.string.bank_card_cannot_be_empty));
                    return;
                }
                // 提现请求
                ModuleMgr.getCommonMgr().reqWithdraw(mEidtMoney, cardName, cardNum, cardLocal, cardLocalBranch, this);
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
        if (response.getUrlParam() == UrlParam.reqWithdrawAddress){//请求默认地址结果返回
            if (response.isOk()){
                WithdrawAddressInfo info = new WithdrawAddressInfo();
                info.parseJson(response.getResponseString());
                etCardName.setText(info.getAccountname());
                etCardLocal.setText(info.getBank());
                etCardLocalBranch.setText(info.getSubbank());
                etCardNum.setText(info.getAccountnum());
            }
            return;
        }
        if (response.getUrlParam() == UrlParam.reqWithdraw || response.getUrlParam() == UrlParam.reqWithdrawModify){//申请提现结果返回，修改地址结果返回

            if (response.isOk()){
                PToast.showShort(TextUtils.isEmpty(response.getMsg()) ? getString(R.string.submit_succeed) : response.getMsg());
                UIShow.showWithDrawSuccessAct(WithDrawApplyAct.this);
                this.finish();
                return;
            }
            PToast.showShort(TextUtils.isEmpty(response.getMsg()) ? getString(R.string.submit_failure) : response.getMsg());
            return;
        }
    }
}