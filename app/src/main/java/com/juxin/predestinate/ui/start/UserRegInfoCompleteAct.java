package com.juxin.predestinate.ui.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.BitmapUtil;
import com.juxin.library.utils.FileUtil;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.file.UpLoadResult;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.picker.picker.OptionPicker;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.InfoConfig;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.UIShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 用户完善信息
 *
 * @author:XY
 * @Date:2017-4-19
 */
public class UserRegInfoCompleteAct extends BaseActivity implements OnClickListener, ImgSelectUtil.OnChooseCompleteListener {
    private final static int TASK_TYPE_HEADUPLOAD = 0;
    private final static int TASK_TYPE_MODIFYDATA = 1;

    public final static int REQUEST_TYPE_REGISTER = 2;
    public final static int REQUEST_TYPE_LOGIN = 3;

    private final static String HEIGHT_MALE_DEFAULT = "175cm";
    private final static String HEIGHT_FEMALE_DEFAULT = "160cm";

    private ImageView img_reg_info_upload_photo;
    private RelativeLayout rl_job_choose, rl_edu_choose, rl_income_choose, rl_height_choose, rl_marry_choose;
    private Button user_reg_info_complete_submit;
    private TextView tv_job, tv_edu, tv_income, tv_height, tv_marry;

    private String jobValue;
    private String eduValue;
    private String incomeValue;
    private String heightValue;
    private String marryValue;

    private String headPicPath;

    private HashMap<String, Object> postParams;

    private int requestType;

    private boolean ifUpHead=true;             // 是否已设置头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_reg_info_complete_act);
        setBackView(R.id.base_title_back, "完善资料", new OnClickListener() {
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
        if (ModuleMgr.getCenterMgr().getMyInfo().getGender() == 1) {
            ifUpHead = true;
        }
    }

    private void initView() {
        img_reg_info_upload_photo = (ImageView) findViewById(R.id.img_reg_info_upload_photo);
        rl_job_choose = (RelativeLayout) findViewById(R.id.layout_reg_info_job);
        rl_edu_choose = (RelativeLayout) findViewById(R.id.layout_reg_info_edu);
        rl_income_choose = (RelativeLayout) findViewById(R.id.layout_reg_info_income);
        rl_height_choose = (RelativeLayout) findViewById(R.id.layout_reg_info_height);
        rl_marry_choose = (RelativeLayout) findViewById(R.id.layout_reg_info_marry);
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
        requestType = getIntent().getIntExtra("requestType", REQUEST_TYPE_REGISTER);
        if (requestType == REQUEST_TYPE_REGISTER) {
            eduValue = "大专";
            jobValue = "企业职工";
            marryValue = "未婚";
            incomeValue = "2000-5000";
            int gender = getIntent().getIntExtra("gender", 1);
            heightValue = gender == 1 ? HEIGHT_MALE_DEFAULT : HEIGHT_FEMALE_DEFAULT;
        } else {
            // 从数据库获取用户信息
            eduValue = "大专";
            jobValue = "企业职工";
            marryValue = "未婚";
            incomeValue = "2000-5000";
            heightValue = ModuleMgr.getCenterMgr().getMyInfo().getGender() == 1 ? HEIGHT_MALE_DEFAULT : HEIGHT_FEMALE_DEFAULT;
        }
        // 将从数据库获取的信息显示页面上
        tv_edu.setText(eduValue);
        tv_job.setText(jobValue);
        tv_marry.setText(marryValue);
        tv_height.setText(heightValue);
        tv_income.setText(incomeValue);
        // 将用户已经选择的信息保存到将要提交的postParams中
        if (heightValue != null) {
            postParams.put("height", InfoConfig.getInstance().getHeightN().getSubmitWithShow(heightValue));
        }
        if (eduValue != null) {
            postParams.put("edu", InfoConfig.getInstance().getEduN().getSubmitWithShow(eduValue));
        }
        if (jobValue != null) {
            postParams.put("job", InfoConfig.getInstance().getJob().getSubmitWithShow(jobValue));
        }
        if (marryValue != null) {
            postParams.put("marry", InfoConfig.getInstance().getMarry().getSubmitWithShow(marryValue));
        }
        if (incomeValue != null) {
            postParams.put("income", InfoConfig.getInstance().getIncomeN().getSubmitWithShow(incomeValue));
        }
    }

    private void initEvent() {
        img_reg_info_upload_photo.setOnClickListener(this);
        rl_job_choose.setOnClickListener(this);
        rl_edu_choose.setOnClickListener(this);
        rl_income_choose.setOnClickListener(this);
        rl_height_choose.setOnClickListener(this);
        user_reg_info_complete_submit.setOnClickListener(this);
        rl_marry_choose.setOnClickListener(this);
    }

    private String defValue;

    private void showChooseDlg(final InfoConfig.SimpleConfig data,final String postKey, final TextView tv_show, String title) {
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
                defValue=jobValue;
                showChooseDlg(InfoConfig.getInstance().getJob(),"job",tv_job,"工作情况");
//                PickerDialogUtil.showOptionPickerDialog(this, new OptionPicker.OnOptionPickListener() {
//                    @Override
//                    public void onOptionPicked(String option) {
//                        postParams.put("job", InfoConfig.getInstance().getJob().getSubmitWithShow(option));
//                        jobValue = option;
//                        tv_job.setText(jobValue);
//                    }
//                }, InfoConfig.getInstance().getJob().getShow(), jobValue, "工作情况");
                break;
            case R.id.layout_reg_info_edu:
                defValue=eduValue;
                showChooseDlg(InfoConfig.getInstance().getEduN(),"edu",tv_edu,"学历");
//                PickerDialogUtil.showOptionPickerDialog(this, new OptionPicker.OnOptionPickListener() {
//                    @Override
//                    public void onOptionPicked(String option) {
//                        postParams.put("edu", InfoConfig.getInstance().getEduN().getSubmitWithShow(option));
//                        eduValue = option;
//                        tv_edu.setText(eduValue);
//                    }
//                }, InfoConfig.getInstance().getEduN().getShow(), eduValue, "学历");
                break;
            case R.id.layout_reg_info_income:
                defValue=incomeValue;
                showChooseDlg(InfoConfig.getInstance().getIncomeN(),"income",tv_income,"月收入");
//                PickerDialogUtil.showOptionPickerDialog(this, new OptionPicker.OnOptionPickListener() {
//                    @Override
//                    public void onOptionPicked(String option) {
//                        postParams.put("income", InfoConfig.getInstance().getIncomeN().getSubmitWithShow(option));
//                        incomeValue = option;
//                        tv_income.setText(incomeValue);
//                    }
//                }, InfoConfig.getInstance().getIncomeN().getShow(), incomeValue, "月收入");
                break;
            case R.id.layout_reg_info_height:
                defValue=heightValue;
                showChooseDlg(InfoConfig.getInstance().getHeightN(),"height",tv_height,"身高");
//                PickerDialogUtil.showOptionPickerDialog(this, new OptionPicker.OnOptionPickListener() {
//                    @Override
//                    public void onOptionPicked(String option) {
//                        postParams.put("height", InfoConfig.getInstance().getHeightN().getSubmitWithShow(option));
//                        heightValue = option;
//                        tv_height.setText(heightValue);
//                    }
//                }, InfoConfig.getInstance().getHeightN().getShow(), heightValue, "身高");
                break;
            case R.id.layout_reg_info_marry:
                defValue=marryValue;
                showChooseDlg(InfoConfig.getInstance().getMarry(),"marry",tv_marry,"婚姻状态");
//                PickerDialogUtil.showOptionPickerDialog(this, new OptionPicker.OnOptionPickListener() {
//                    @Override
//                    public void onOptionPicked(String option) {
//                        postParams.put("marry", InfoConfig.getInstance().getMarry().getSubmitWithShow(option));
//                        marryValue = option;
//                        tv_marry.setText(marryValue);
//                    }
//                }, InfoConfig.getInstance().getMarry().getShow(), marryValue, "婚姻状态");
                break;
            case R.id.user_reg_info_complete_submit:
                if (!checkInputInfo(true)) {
                    ModuleMgr.getLoginMgr().modifyUserData(postParams, new RequestComplete() {
                        @Override
                        public void onRequestComplete(HttpResponse response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.getResponseString());
                                if ("success".equals(jsonObject.optString("result"))) {
                                    UIShow.showMainClearTask(UserRegInfoCompleteAct.this);
                                } else {
                                    PToast.showShort("注册失败，请稍候再试");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            LoadingDialog.closeLoadingDialog(300);
                        }
                    });
                }
                break;
        }
    }

//    private void handleUploadHeadTaskResult(ResultInfo result) {
//        if ("success".equals(result.getResult())) {
//            // 完成头像
//            AppCfg.getAppCfg().onSava(this, 11);
//            // 将用户选择的头像显示到页面上
//            PhotoUtils.loadSmallBitmap(this, headPicPath, img_reg_info_upload_photo, true);
//            // 将上传成功的头像链接保存到内存当中
//            AppModel.getInstance().setAvatarToDataBase(result.getContent());
//        } else {
//            // 删除保存的头像
//            SDCardUtil.delFile(headPicPath);
//        }
//        String message = "success".equals(result.getResult()) ? "头像上传成功" : "头像上传失败";
//        ifUpHead = "success".equals(result.getResult());
//        T.showShort(this, message);
//    }


    @Override
    public void onBackPressed() {
        updateDataToLocal();
        if (requestType == REQUEST_TYPE_REGISTER) {
            Intent intent = new Intent(this, NavUserAct.class);
            startActivity(intent);
        }
        overridePendingTransition(R.anim.slide_fragment_right_in, R.anim.slide_fragment_right_out);
        finish();
    }

    private boolean checkInputInfo(boolean isShowToast) {
        if (TextUtils.isEmpty(jobValue)) {
            if (isShowToast) {
                PToast.showShort("请选择您的职业");
            }
            return true;
        }
        if (TextUtils.isEmpty(eduValue)) {
            if (isShowToast) {
                PToast.showShort("请选择您的学历");
            }
            return true;
        }
        if (TextUtils.isEmpty(incomeValue)) {
            if (isShowToast) {
                PToast.showShort("请选择您的收入状况");
            }
            return true;
        }
        String heightValue = tv_height.getText().toString();
        if (TextUtils.isEmpty(heightValue)) {
            if (isShowToast) {
                PToast.showShort("请选择您的身高");
            }
            return true;
        }
        if (TextUtils.isEmpty(marryValue)) {
            if (isShowToast) {
                PToast.showShort("请选择您的婚姻状态");
            }
            return true;
        }

        if (!ifUpHead) {
            PToast.showShort("请您上传真实照片做为头像");
            return true;
        }

        return false;
    }

    private void updateDataToLocal() {
//        int height = !postParams.containsKey("height") ? -1 : Integer.parseInt(postParams.get("height").toString());
//        DataCenter.getInstance().update_userinfo_item(AppCtx.getPreference(AppCtx.UserName), AppCtx.getPreference(AppCtx.SUid),
//                User.DEFAULT_STRING_VALUE, User.DEFAULT_INT_VALUE, User.DEFAULT_STRING_VALUE, User.DEFAULT_INT_VALUE, User.DEFAULT_INT_VALUE,
//                User.DEFAULT_INT_VALUE, User.DEFAULT_INT_VALUE, User.DEFAULT_INT_VALUE, User.DEFAULT_INT_VALUE, User.DEFAULT_INT_VALUE,
//                User.DEFAULT_INT_VALUE, jobValue, eduValue, height, incomeValue, marryValue, User.DEFAULT_STRING_VALUE, User.DEFAULT_STRING_VALUE
//                , User.DEFAULT_STRING_VALUE);
    }

    @Override
    public void onComplete(final String... path) {
        if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
            return;
        }
        if (FileUtil.isExist(path[0])) {
            LoadingDialog.show(UserRegInfoCompleteAct.this, "头像上传，请稍侯");
            ModuleMgr.getMediaMgr().sendHttpFile(Constant.INT_AVATAR, path[0], new RequestComplete() {
                @Override
                public void onRequestComplete(HttpResponse response) {
                    LoadingDialog.closeLoadingDialog();
                    if (response.isOk()) {
//                        UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
//                        String pic = upLoadResult.getHttpPathPic();
//                        if (!TextUtils.isEmpty(pic)) {
//                            isCompleteHead = true;
//                            headPicBitmap = BitmapUtil.getSmallBitmap(path[0]);
//                            img_header.setImageBitmap(headPicBitmap);
//                            _photoUrl = ModuleMgr.getCenterMgr().getInterceptUrl(pic);
//                            resetSubmit();
//                        }
                    } else {
                        MMToast.showShort("头像上传失败，请重试");
                    }
                    FileUtil.deleteFile(path[0]);   // 上传完成后清除临时裁切文件
                }
            });
        } else {
            MMToast.showShort("图片地址无效");
        }
//        headPicPath
    }
}
