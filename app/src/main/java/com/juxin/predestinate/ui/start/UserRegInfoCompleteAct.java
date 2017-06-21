package com.juxin.predestinate.ui.start;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.picker.picker.OptionPicker;
import com.juxin.predestinate.module.logic.config.InfoConfig;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.UIShow;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * 用户完善信息
 *
 * @author:XY
 * @Date:2017-4-19
 */
public class UserRegInfoCompleteAct extends BaseActivity implements OnClickListener, ImgSelectUtil.OnChooseCompleteListener, PObserver {

    private final static String HEIGHT_MALE_DEFAULT = "175cm";
    private final static String HEIGHT_FEMALE_DEFAULT = "160cm";

    private ImageView img_reg_info_upload_photo;
    private LinearLayout rl_job_choose, rl_edu_choose, rl_income_choose, rl_height_choose, rl_marry_choose;
    private Button user_reg_info_complete_submit;
    private TextView tv_job, tv_edu, tv_income, tv_height, tv_marry;

    private String jobValue;
    private String eduValue;
    private String incomeValue;
    private String heightValue;
    private String marryValue;


    private HashMap<String, Object> postParams;

    private boolean ifUpHead = true;             // 是否已设置头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reg_info_complete_act);
        setBackView(R.id.base_title_back, getResources().getString(R.string.title_reginfo_complete), new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        initData();
        initView();
        initEvent();
        fillData();
    }

    private void initData() {
        PSP.getInstance().put("recommendDate", TimeUtil.getData());
        postParams = new HashMap<>();
        ifUpHead = getIntent().getIntExtra("gender", 1) == 1;
    }

    private void initView() {
        img_reg_info_upload_photo = (ImageView) findViewById(R.id.img_reg_info_upload_photo);
        ImageLoader.loadCircleAvatar(this, R.drawable.btn_reg_upload_photo_selector, img_reg_info_upload_photo);
        rl_job_choose = (LinearLayout) findViewById(R.id.layout_reg_info_job);
        rl_edu_choose = (LinearLayout) findViewById(R.id.layout_reg_info_edu);
        rl_income_choose = (LinearLayout) findViewById(R.id.layout_reg_info_income);
        rl_height_choose = (LinearLayout) findViewById(R.id.layout_reg_info_height);
        rl_marry_choose = (LinearLayout) findViewById(R.id.layout_reg_info_marry);
        tv_job = (TextView) findViewById(R.id.txt_reg_info_job);
        tv_edu = (TextView) findViewById(R.id.txt_reg_info_edu);
        tv_income = (TextView) findViewById(R.id.txt_reg_info_income);
        tv_height = (TextView) findViewById(R.id.txt_reg_info_height);
        tv_marry = (TextView) findViewById(R.id.txt_reg_info_marry);
        user_reg_info_complete_submit = (Button) findViewById(R.id.user_reg_info_complete_submit);
        String txt_tip = getIntent().getStringExtra("text");
        if (txt_tip != null)
            ((TextView) findViewById(R.id.txt_tip)).setText(txt_tip);
    }

    private void fillData() {
        eduValue = getResources().getString(R.string.txt_reg_edu_default);
        jobValue = getResources().getString(R.string.txt_reg_job_default);
        marryValue = getResources().getString(R.string.txt_reg_marry_default);
        incomeValue = getResources().getString(R.string.txt_reg_income_default);
        heightValue = ModuleMgr.getCenterMgr().getMyInfo().getGender() == 1 ? HEIGHT_MALE_DEFAULT : HEIGHT_FEMALE_DEFAULT;
        // 将从数据库获取的信息显示页面上
        tv_edu.setText(eduValue);
        tv_job.setText(jobValue);
        tv_marry.setText(marryValue);
        tv_height.setText(heightValue);
        tv_income.setText(incomeValue);
        // 将用户已经选择的信息保存到将要提交的postParams中
        if (heightValue != null)
            postParams.put("height", InfoConfig.getInstance().getHeightN().getSubmitWithShow(heightValue));
        if (eduValue != null)
            postParams.put("edu", InfoConfig.getInstance().getEduN().getSubmitWithShow(eduValue));
        if (jobValue != null)
            postParams.put("job", InfoConfig.getInstance().getJob().getSubmitWithShow(jobValue));
        if (marryValue != null)
            postParams.put("marry", InfoConfig.getInstance().getMarry().getSubmitWithShow(marryValue));
        if (incomeValue != null)
            postParams.put("income", InfoConfig.getInstance().getIncomeN().getSubmitWithShow(incomeValue));
    }

    private void initEvent() {
        img_reg_info_upload_photo.setOnClickListener(this);
        rl_job_choose.setOnClickListener(this);
        rl_edu_choose.setOnClickListener(this);
        rl_income_choose.setOnClickListener(this);
        rl_height_choose.setOnClickListener(this);
        user_reg_info_complete_submit.setOnClickListener(this);
        rl_marry_choose.setOnClickListener(this);

        MsgMgr.getInstance().attach(this);
    }

    private String defValue;

    private void showChooseDlg(final InfoConfig.SimpleConfig data, final String postKey, final TextView tv_show, String title) {
        PickerDialogUtil.showOptionPickerDialog(this, new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                postParams.put(postKey, data.getSubmitWithShow(option));
                defValue = option;
                tv_show.setText(defValue);
            }
        }, data.getShow(), defValue, title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_reg_info_upload_photo:
                ImgSelectUtil.getInstance().pickPhoto(UserRegInfoCompleteAct.this, this);
                break;
            case R.id.layout_reg_info_job:
                defValue = jobValue;
                showChooseDlg(InfoConfig.getInstance().getJob(), "job", tv_job, getResources().getString(R.string.daltitle_job));
                break;
            case R.id.layout_reg_info_edu:
                defValue = eduValue;
                showChooseDlg(InfoConfig.getInstance().getEduN(), "edu", tv_edu, getResources().getString(R.string.daltitle_edu));
                break;
            case R.id.layout_reg_info_income:
                defValue = incomeValue;
                showChooseDlg(InfoConfig.getInstance().getIncomeN(), "income", tv_income, getResources().getString(R.string.daltitle_inccome));
                break;
            case R.id.layout_reg_info_height:
                defValue = heightValue;
                showChooseDlg(InfoConfig.getInstance().getHeightN(), "height", tv_height, getResources().getString(R.string.daltitle_height));
                break;
            case R.id.layout_reg_info_marry:
                defValue = marryValue;
                showChooseDlg(InfoConfig.getInstance().getMarry(), "marry", tv_marry, getResources().getString(R.string.daltitle_marry));
                break;
            case R.id.user_reg_info_complete_submit:
                if (validInput()) {
                    LoadingDialog.show(this, getResources().getString(R.string.loading_reg_update));
                    ModuleMgr.getCenterMgr().updateMyInfo(postParams, new RequestComplete() {
                        @Override
                        public void onRequestComplete(HttpResponse response) {
                            if (response.isOk()) {
                                UIShow.showMainClearTask(UserRegInfoCompleteAct.this);
                            }
                            LoadingDialog.closeLoadingDialog(300);
                        }
                    });
                }
                break;
        }
    }

    private boolean validValue(String[] values, int[] toasts) {
        for (int i = 0; i < values.length; i++) {
            if (TextUtils.isEmpty(values[i])) {
                PToast.showShort(getResources().getString(toasts[i]));
                return false;
            }
        }
        return true;
    }

    private boolean validInput() {
        String heightValue = tv_height.getText().toString();
        String checkValue[] = new String[]{jobValue, eduValue, incomeValue, heightValue, marryValue};
        int toasts[] = new int[]{R.string.toast_job_isnull, R.string.toast_edu_isnull, R.string.toast_income_isnull, R.string.toast_height_isnull, R.string.toast_marry_isnull};
        if (!validValue(checkValue, toasts)) {
            return false;
        }
        if (!ifUpHead) {
            PToast.showShort(getResources().getString(R.string.toast_head_isnull));
            return false;
        }
        return true;
    }


    @Override
    public void onComplete(final String... path) {
        if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
            return;
        }
        if (FileUtil.isExist(path[0])) {
            LoadingDialog.show(UserRegInfoCompleteAct.this, getResources().getString(R.string.user_info_avatar_upload));
            ModuleMgr.getCenterMgr().uploadAvatar(path[0], new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    if (response.isOk()) {
                        LoadingDialog.closeLoadingDialog();
                        if (response.isOk()) {
                            JSONObject jsonObject = response.getResponseJson();
                            String file_path = jsonObject.optString("file_path");
                            ImageLoader.loadCircleAvatar(UserRegInfoCompleteAct.this, file_path, img_reg_info_upload_photo);
                            MsgMgr.getInstance().sendMsg(MsgType.MT_Update_MyInfo, null);
                            ifUpHead = true;
                        } else {
                            PToast.showShort(getResources().getString(R.string.toast_head_error));
                        }
                    }
                }
            });
        } else {
            PToast.showShort(getResources().getString(R.string.toast_picpath_error));
        }
    }


    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_MyInfo_Change:
                ImageLoader.loadCircleAvatar(this, ModuleMgr.getCenterMgr().getMyInfo().getAvatar(), img_reg_info_upload_photo);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }
}
