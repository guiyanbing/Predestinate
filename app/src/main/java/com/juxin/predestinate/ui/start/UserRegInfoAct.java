package com.juxin.predestinate.ui.start;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.picker.picker.OptionPicker;
import com.juxin.predestinate.module.logic.config.InfoConfig;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.util.PickerDialogUtil;

/**
 * 注册页面
 * Created YAO on 2017/4/19.
 */

public class UserRegInfoAct extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private EditText et_nickname;
    private TextView txt_reg_info_age;
    private RadioGroup rg_gender;
    private Button bt_submit;


    private String nickname;
    private int age;
    private String ageVlaue;
    private int gender = 1;


    private UrlParam urlParam = UrlParam.reqRegister;  // 默认为常规注册

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p2_user_reg_info_act);
        setBackView(getResources().getString(R.string.title_reg));
        initView();
        initEvent();
    }

    private void initView() {
        et_nickname = (EditText) findViewById(R.id.edtTxt_reg_info_nickname);
        txt_reg_info_age = (TextView) findViewById(R.id.txt_reg_info_age);
        rg_gender = (RadioGroup) findViewById(R.id.rg_reg_info_gender);
        bt_submit = (Button) findViewById(R.id.btn_reg_info_submit);
    }

    private void initEvent() {
        txt_reg_info_age.setOnClickListener(this);
        bt_submit.setOnClickListener(this);
        rg_gender.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_reg_info_age:
                PickerDialogUtil.showOptionPickerDialog(this, new OptionPicker.OnOptionPickListener() {
                    @Override
                    public void onOptionPicked(String option) {
                        age = InfoConfig.getInstance().getAgeN().getSubmitWithShow(option);
                        ageVlaue = option;
                        txt_reg_info_age.setText(ageVlaue);
                    }
                }, InfoConfig.getInstance().getAgeN().getShow(), "18岁", "年龄");
                break;
            case R.id.btn_reg_info_submit:
                if (validInput()) {
                    LoadingDialog.show(this,getResources().getString(R.string.loading_reg));
                    ModuleMgr.getCenterMgr().getMyInfo().setGender(gender);
                    ModuleMgr.getLoginMgr().onRegister(this,urlParam, nickname, age, gender);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 检查用户输入的信息是否有误
     *
     * @return
     */
    private boolean validInput() {
        nickname = et_nickname.getText().toString();
        if (TextUtils.isEmpty(nickname)) {
            PToast.showShort(getResources().getString(R.string.toast_nickname_isnull));
            return false;
        }
        if (age == 0 || ageVlaue == null) {
            PToast.showShort(getResources().getString(R.string.toast_age_isnull));
            return false;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radio_reg_info_male:
                gender = 1; // 性别男
                break;
            case R.id.radio_reg_info_female:
                gender = 2; // 性别女
                break;
        }
    }
}
