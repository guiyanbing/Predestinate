package com.juxin.predestinate.ui.user.auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zm on 2017/5/16
 */
public class IDCardAuthenticationAct extends BaseActivity implements View.OnClickListener,RequestComplete{

    private TextView tvTitleInfo;
    private EditText eitName;
    private EditText eitIdCard;
    private EditText eitOpenBank;
    private EditText eitBankCardId;
    private EditText eitBankBranch;
    private TextView tvBankCardId;
    private LinearLayout llOpenBank,llBankBranch;
    private RadioButton rbZhi;
    private RadioButton rbYin;
    private AddPhotoView apvFrontPhoto;
    private AddPhotoView apvTailPhoto;
    private AddPhotoView apvHandPhoto;
    private LinearLayout llKeFu;

    private String cardName,cardLocal,cardLocalBranch,cardIdCard,cardNum;
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
        tvTitleInfo = (TextView) findViewById(R.id.id_card_tv_titile_info);
        eitName = (EditText) findViewById(R.id.id_card_eit_name);
        eitIdCard = (EditText) findViewById(R.id.id_card_eit_id_card);
        eitOpenBank = (EditText) findViewById(R.id.id_card_eit_open_bank);
        eitBankCardId = (EditText) findViewById(R.id.id_card_eit_open_bank_id);
        eitBankBranch = (EditText) findViewById(R.id.id_card_eit_open_bank_branch);
        llOpenBank = (LinearLayout) findViewById(R.id.id_card_ll_open_bank);
        llBankBranch = (LinearLayout) findViewById(R.id.id_card_ll_open_bank_branch);
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
            llBankBranch.setVisibility(View.VISIBLE);
            tvTitleInfo.setText(getString(R.string.id_card_tips_bank));
            tvBankCardId.setText(getString(R.string.bank_id));
            eitOpenBank.setText(mIdCardVerifyStatusInfo.getBank());
            eitBankBranch.setText(mIdCardVerifyStatusInfo.getSubbank());
            eitBankCardId.setInputType(InputType.TYPE_CLASS_NUMBER);
            eitBankCardId.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            eitBankCardId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(21)});
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
        if (!TextUtils.isEmpty(mIdCardVerifyStatusInfo.getId_front_img_small()) && !TextUtils.isEmpty(mIdCardVerifyStatusInfo.getId_back_img_small())
                && !TextUtils.isEmpty(mIdCardVerifyStatusInfo.getFace_img_small())){
            apvFrontPhoto.setImg(mIdCardVerifyStatusInfo.getId_front_img_small());
            apvTailPhoto.setImg(mIdCardVerifyStatusInfo.getId_back_img_small());
            apvHandPhoto.setImg(mIdCardVerifyStatusInfo.getFace_img_small());
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
                    cardLocalBranch = eitBankBranch.getText().toString();
                    cardNum = eitBankCardId.getText().toString();
                    if (TextUtils.isEmpty(cardName)) {
                        PToast.showShort(getString(R.string.name_cannot_be_empty));
                        return;
                    }
                    if (TextUtils.isEmpty(cardIdCard)) {
                        PToast.showShort(getString(R.string.idcard_cannot_be_empty));
                        return;
                    }
                    if (!idCardForm(cardIdCard)) {
                        PToast.showShort(getString(R.string.id_card_number_format_is_wrong));
                        return;
                    }
                    if (paytype == 1) {
                        if (TextUtils.isEmpty(cardLocal)) {
                            PToast.showShort(getString(R.string.bank_cannot_be_empty));
                            return;
                        }
                        if (TextUtils.isEmpty(cardLocalBranch)) {
                            PToast.showShort(getString(R.string.branch_cannot_be_empty));
                            return;
                        }
                    }
                    if (paytype == 1 && TextUtils.isEmpty(cardNum)) {
                        PToast.showShort(getString(R.string.bank_card_cannot_be_empty));
                        return;
                    }
                    if (paytype == 2 && TextUtils.isEmpty(cardNum)) {
                        PToast.showShort(getString(R.string.zhi_card_cannot_be_empty));
                        return;
                    }
                    if (paytype == 1 && !bankCardForm(cardNum)) {
                        PToast.showShort(getString(R.string.bank_card_number_format_is_wrong));
                        return;
                    }
                    if (paytype == 2 && !checkMobileNumber(cardNum) && !checkEmail(cardNum)) {
                        PToast.showShort(getString(R.string.zhi_card_number_format_is_wrong));
                        return;
                    }

                    //                    if (TextUtils.isEmpty(cardNum) || TextUtils.isEmpty(cardIdCard) || TextUtils.isEmpty(cardName) ||
                    //                            (paytype == 1 && (TextUtils.isEmpty(cardLocal) || TextUtils.isEmpty(cardLocalBranch)))) {
                    //                        PToast.showShort(getString(R.string.please_complete_the_information));
                    //                        return;
                    //                    }
                    strImgFront = apvFrontPhoto.getImgStrPath();
                    strImgTail = apvTailPhoto.getImgStrPath();
                    strImgHand = apvHandPhoto.getImgStrPath();
                    if (TextUtils.isEmpty(strImgFront)) {
                        PToast.showShort(getString(R.string.please_upload_the_idcard_front));
                        return;
                    }
                    if (TextUtils.isEmpty(strImgTail)) {
                        PToast.showShort(getString(R.string.please_upload_the_idcard_tail));
                        return;
                    }
                    if (TextUtils.isEmpty(strImgHand)) {
                        PToast.showShort(getString(R.string.please_upload_the_idcard_handle));
                        return;
                    }
                    ModuleMgr.getCommonMgr().userVerify(cardIdCard, cardName, cardNum, cardLocal, cardLocalBranch, strImgFront, strImgTail, strImgHand, paytype, IDCardAuthenticationAct.this);
                }
            });
        }
    }

    //身份证校验
    public boolean idCardForm(String num) {
        String reg = "^\\d{15}$|^\\d{17}[0-9Xx]$";
        if (!num.matches(reg)) {
            System.out.println("Format Error!");
            return false;
        }
        return true;
    }

    //银行卡号校验
    public boolean bankCardForm(String num) {
        String reg = "^\\d{16}$|^\\d{18,21}$";
        if (!num.matches(reg)) {
            System.out.println("Format Error!");
            return false;
        }
        return true;
    }

    // 验证手机号码
    public boolean checkMobileNumber(String mobileNumber){
//        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(14[57])|(17[0])|(17[6-8])|(18[0,0-9]))\\d{8}$");
        boolean flag = false;
        try{
            Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
            Matcher matcher = regex.matcher(mobileNumber);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    //验证邮箱
    public boolean checkEmail(String email){
//        "[a-zA-Z_]{1,}[0-9]{0,}@(([a-zA-z0-9]-*){1,}\\.){1,3}[a-zA-z\\-]{1,}";"\\w+(\\.\\w)*+[@,＠]()+\\w+(\\.\\w{2,3}){1,3}"
        boolean flag = false;
        try{
            String check = "\\w+(\\.\\w)*[@,＠]\\w+(\\.\\w{2,3}){1,3}";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
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
                llBankBranch.setVisibility(View.GONE);
                tvTitleInfo.setText(getString(R.string.id_card_tips_zhi));
                tvBankCardId.setText(R.string.zhi_fu_id);
                eitBankCardId.setInputType(InputType.TYPE_CLASS_TEXT);
//                eitBankCardId.setKeyListener(DigitsKeyListener.getInstance("0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ@.＠"));
                eitBankCardId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(26)});
                eitBankCardId.setHint(R.string.input_your_zhifubao_id);
                paytype = 2;
                break;
            case R.id.id_card_rb_yin:
                rbZhi.setChecked(false);
                llOpenBank.setVisibility(View.VISIBLE);
                llBankBranch.setVisibility(View.VISIBLE);
                tvTitleInfo.setText(getString(R.string.id_card_tips_bank));
                tvBankCardId.setText(getString(R.string.bank_id));
                eitBankCardId.setInputType(InputType.TYPE_CLASS_NUMBER);
                eitBankCardId.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                eitBankCardId.setFilters(new InputFilter[]{new InputFilter.LengthFilter(21)});
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
//                                Log.e("TTTTTTTTTTTTTPPP000", "zhixing" + back);
                if (back == 1){
                    //                    Log.e("TTTTTTTTTTTTTPPP111","zhixing");
                    data.putExtra(IDCardAuthenticationSucceedAct.IDCARDBACK, 1);
                    setResult(RESULT_OK, data);
                }
            }
        }
        this.finish();
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()){
            UIShow.showIDCardAuthenticationSucceedAct(this,authIDCard);
            ModuleMgr.getCenterMgr().getMyInfo().setIdcard_validation(1);
        }
//        Log.e("TTTTTTTTTTTTTTTMMM",response.getResponseString()+"|||");
        PToast.showShort(response.getMsg()+"");
    }
}
