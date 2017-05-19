package com.juxin.predestinate.ui.user.auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.IdCardVerifyStatusInfo;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.ui.user.my.view.AddPhotoView;

/**
 * Created by zm on 2017/5/16
 */
public class IDCardAuthenticationAct extends BaseActivity implements View.OnClickListener,RequestComplete{

    private EditText eitName;
    private EditText eitIdCard;
    private EditText eitOpenBank;
    private EditText eitBankCardId;
    private TextView tvBankCardId;
    private LinearLayout llOpenBank;
    private RadioButton rbZhi;
    private RadioButton rbYin;
    private AddPhotoView apvFrontPhoto;
    private AddPhotoView apvTailPhoto;
    private AddPhotoView apvHandPhoto;
    private LinearLayout llKeFu;

    private String cardName,cardLocal,cardIdCard,cardNum;
    private String strImgFront,strImgTail,strImgHand;

    private int paytype = 2;
    private int authIDCard = 105;
    private IdCardVerifyStatusInfo mIdCardVerifyStatusInfo;
    private LinearLayout llAudit,llCertification;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_id_card_auth_act);
        initView();
    }

    private void initView() {
        mIdCardVerifyStatusInfo = ModuleMgr.getCommonMgr().getIdCardVerifyStatusInfo();
        eitName = (EditText) findViewById(R.id.id_card_eit_name);
        eitIdCard = (EditText) findViewById(R.id.id_card_eit_id_card);
        eitOpenBank = (EditText) findViewById(R.id.id_card_eit_open_bank);
        eitBankCardId = (EditText) findViewById(R.id.id_card_eit_open_bank_id);
        llOpenBank = (LinearLayout) findViewById(R.id.id_card_ll_open_bank);
        tvBankCardId = (TextView) findViewById(R.id.id_card_tv_open_bank_id);
        llAudit = (LinearLayout) findViewById(R.id.id_card_ll_the_certification_audit);
        llCertification = (LinearLayout) findViewById(R.id.id_card_ll_certification_by);

        rbZhi = (RadioButton) findViewById(R.id.id_card_rb_zhi);
        rbYin = (RadioButton) findViewById(R.id.id_card_rb_yin);
        apvFrontPhoto = (AddPhotoView) findViewById(R.id.id_card_apv_front_photo);
        apvTailPhoto = (AddPhotoView) findViewById(R.id.id_card_apv_tail_photo);
        apvHandPhoto = (AddPhotoView) findViewById(R.id.id_card_apv_hand_photo);
        findViewById(R.id.id_card_ll_kefu).setOnClickListener(this);
        rbZhi.setOnClickListener(this);
        rbYin.setOnClickListener(this);
        rbZhi.setChecked(true);
        setBg();
        if (mIdCardVerifyStatusInfo.getStatus() == 1){
            llAudit.setVisibility(View.VISIBLE);
        }else if (mIdCardVerifyStatusInfo.getStatus() == 2){
            llCertification.setVisibility(View.VISIBLE);
        }
        if (mIdCardVerifyStatusInfo.getPaytype() == 1){
            rbYin.setChecked(true);
            rbZhi.setChecked(false);
            llOpenBank.setVisibility(View.VISIBLE);
            tvBankCardId.setText(getString(R.string.bank_id));
            eitOpenBank.setText(mIdCardVerifyStatusInfo.getBank());
            eitBankCardId.setInputType(InputType.TYPE_CLASS_NUMBER);
            eitBankCardId.setHint(R.string.input_your_bank_card_id);
            paytype = 1;
        }
        initData();
        initTitle();
    }

    private void initData() {
        eitName.setText(mIdCardVerifyStatusInfo.getAccountname());
        eitIdCard.setText(mIdCardVerifyStatusInfo.getId_num());
        eitBankCardId.setText(mIdCardVerifyStatusInfo.getAccountnum());
        if (!TextUtils.isEmpty(mIdCardVerifyStatusInfo.getId_front_img()) && !TextUtils.isEmpty(mIdCardVerifyStatusInfo.getId_back_img())
                && !TextUtils.isEmpty(mIdCardVerifyStatusInfo.getFace_img())){
            apvFrontPhoto.setImg(mIdCardVerifyStatusInfo.getId_front_img());
            apvTailPhoto.setImg(mIdCardVerifyStatusInfo.getId_back_img());
            apvHandPhoto.setImg(mIdCardVerifyStatusInfo.getFace_img());
        }
    }

    private void initTitle() {
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.txt_authtype_id));
        if (mIdCardVerifyStatusInfo.getStatus() != 1 && mIdCardVerifyStatusInfo.getStatus() != 2){
            setTitleRight(getString(R.string.title_right_submit), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardName = eitName.getText().toString();
                    cardIdCard = eitIdCard.getText().toString();
                    cardLocal = eitOpenBank.getText().toString();
                    cardNum = eitBankCardId.getText().toString();
                    if (TextUtils.isEmpty(cardNum) || TextUtils.isEmpty(cardIdCard) || TextUtils.isEmpty(cardName) || (paytype == 1 && TextUtils.isEmpty(cardLocal))) {
                        PToast.showShort(getString(R.string.please_complete_the_information));
                        return;
                    }
                    strImgFront = apvFrontPhoto.getImgStrPath();
                    strImgTail = apvTailPhoto.getImgStrPath();
                    strImgHand = apvHandPhoto.getImgStrPath();
                    if (TextUtils.isEmpty(strImgFront) || TextUtils.isEmpty(strImgTail) || TextUtils.isEmpty(strImgHand)) {
                        PToast.showShort(getString(R.string.please_upload_the_relevant_pictures));
                        return;
                    }
                    ModuleMgr.getCommonMgr().userVerify(cardIdCard, cardName, cardNum, cardLocal, cardLocal, strImgFront, strImgTail, strImgHand, paytype, IDCardAuthenticationAct.this);
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_card_ll_kefu:
                UIShow.showQQService(this);
                break;
            case R.id.id_card_rb_zhi:
                rbYin.setChecked(false);
                llOpenBank.setVisibility(View.GONE);
                tvBankCardId.setText(R.string.zhi_fu_id);
                eitBankCardId.setInputType(InputType.TYPE_CLASS_TEXT);
                eitBankCardId.setHint(R.string.input_your_zhifubao_id);
                paytype = 2;
                break;
            case R.id.id_card_rb_yin:
                rbZhi.setChecked(false);
                llOpenBank.setVisibility(View.VISIBLE);
                tvBankCardId.setText(getString(R.string.bank_id));
                eitBankCardId.setInputType(InputType.TYPE_CLASS_NUMBER);
                eitBankCardId.setHint(R.string.input_your_bank_card_id);
                paytype = 1;
                break;
            default:
                break;
        }
    }

    private void setBg(){
        Drawable drawableFirst = getResources().getDrawable(R.drawable.f1_id_card_selector);
        drawableFirst.setBounds(0, 0, UIUtil.dp2px(17), UIUtil.dp2px(17));//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        Drawable drawable = getResources().getDrawable(R.drawable.f1_id_card_selector);
        drawable.setBounds(0, 0, UIUtil.dp2px(17), UIUtil.dp2px(17));//第一0是距左右边距离，第二0是距上下边距离，第三69长度,第四宽度
        rbZhi.setCompoundDrawables(drawableFirst, null, null, null);
        rbYin.setCompoundDrawables(drawable, null, null, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == authIDCard) {
            if (data != null){
                int back = data.getIntExtra(IDCardAuthenticationSucceedAct.IDCARDBACK,0);
                //                Log.e("TTTTTTTTTTTTTPPP000","zhixing"+back);
                if (back == 1){
                    //                    Log.e("TTTTTTTTTTTTTPPP111","zhixing");
                    this.finish();
                }
            }
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()){
            UIShow.showIDCardAuthenticationSucceedAct(this,authIDCard);
            finish();
        }
//        Log.e("TTTTTTTTTTTTTTTMMM",response.getResponseString()+"|||");
        PToast.showShort(response.getMsg()+"");
    }
}
