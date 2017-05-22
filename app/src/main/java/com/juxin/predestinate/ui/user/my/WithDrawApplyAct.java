package com.juxin.predestinate.ui.user.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.juxin.predestinate.bean.my.WithdrawAddressInfo;

/**
 * 红包提现--银行卡界面
 * Created by zm on 2017/4/20
 */
public class WithDrawApplyAct extends BaseActivity implements View.OnClickListener,RequestComplete{

    private String mEidtMoney = "0";
    private boolean mIsFromEdit;

    private LinearLayout llOpenBank;
    private EditText etMoney;
    private EditText etCardName;
    private EditText etCardLocal;
    private EditText etCardLocalBranch;
    private EditText etCardNum;
    private TextView tvOpenBank;
    private Button btnNext;
    private WithdrawAddressInfo info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_wode_bank_card_act);
        mIsFromEdit = getIntent().getBooleanExtra("fromEdit",false);
        info = getIntent().getParcelableExtra("info");
        initView();
    }

    private void initView(){
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.withdrawal_page));

        llOpenBank = (LinearLayout) findViewById(R.id.bank_card_ll_open_bank);
        etMoney = (EditText) findViewById(R.id.bank_card_et_money);
        etCardName = (EditText) findViewById(R.id.bank_card_et_card_name);
        etCardLocal = (EditText) findViewById(R.id.bank_card_et_card_local);
        etCardLocalBranch = (EditText) findViewById(R.id.bank_card_et_card_local_branch);
        etCardNum = (EditText) findViewById(R.id.bank_card_et_card_num);
        btnNext = (Button) findViewById(R.id.bank_card_btn_next);
        tvOpenBank = (TextView) findViewById(R.id.bank_card_tv_card);
        mEidtMoney = PSP.getInstance().getFloat(RedBoxRecordAct.REDBOXMONEY+ModuleMgr.getCenterMgr().getMyInfo().getUid(),0)+"";
        etMoney.setText(mEidtMoney + getString(R.string.head_unit));
        btnNext.setOnClickListener(this);
        initDefaultAddress();

    }

    private void initDefaultAddress() {
        etCardName.setText(info.getAccountname());
        etCardLocal.setText(info.getBank());
        etCardNum.setText(info.getAccountnum());
        if (info.getPaytype() == 2){
            llOpenBank.setVisibility(View.GONE);
            tvOpenBank.setText(getString(R.string.zhi_fu_id));
        }
//        LoadingDialog.show(this);
//     ModuleMgr.getCommonMgr().reqWithdrawAddress(this);
    }

    @Override
    public void onClick(View v) {
        if (!(NetworkUtils.isConnected(this))) {
            PToast.showShort(getResources().getString(R.string.tip_net_error));
            return;
        }
        switch (v.getId()) {
            case R.id.bank_card_btn_next:
//                String cardName = etCardName.getText().toString().trim();
//                String cardLocal = etCardLocal.getText().toString().trim();
//                String cardLocalBranch = etCardLocalBranch.getText().toString().trim();
//                String cardNum = etCardNum.getText().toString().trim();
//                if(mIsFromEdit) {
//                    if(TextUtils.isEmpty(cardName)) {
//                        PToast.showShort(getString(R.string.name_cannot_be_empty));
//                        return;
//                    }
//                    if(TextUtils.isEmpty(cardLocal)) {
//                        PToast.showShort(getString(R.string.bank_cannot_be_empty));
//                        return;
//                    }
//
//                    if(TextUtils.isEmpty(cardNum)) {
//                        PToast.showShort(getString(R.string.bank_card_cannot_be_empty));
//                        return;
//                    }
//                    // 修改地址
//                    ModuleMgr.getCommonMgr().reqWithdrawModify(mEidtMoney,info.getPaytype(),cardName,cardNum,cardLocal,info.getSubbank(),this);
//                    break;
//                }
                //获取自己可提现金额
//                String myMoney = PSP.getInstance().getLong(RedBoxRecordAct.REDBOXMONEY+ModuleMgr.getCenterMgr().getMyInfo().getUid(),0)+"";
//                int minMoney = ModuleMgr.getCommonMgr().getCommonConfig().getMinmoney();

                // 提现请求
                if (Long.valueOf(mEidtMoney) <= 0){
                    PToast.showShort(R.string.money_cout_be_0);
                    break;
                }
                ModuleMgr.getCommonMgr().reqWithdraw(mEidtMoney,info.getPaytype(),info.getAccountname(),info.getAccountnum(), info.getBank(), info.getSubbank(), this);
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
//        if (response.getUrlParam() == UrlParam.reqWithdrawAddress){//请求默认地址结果返回
//            if (response.isOk()){
//                info = new WithdrawAddressInfo();
//                info.parseJson(response.getResponseString());
//                etCardName.setText(info.getAccountname());
//                etCardLocal.setText(info.getBank());
//                etCardNum.setText(info.getAccountnum());
//            }
//            return;
//        }
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