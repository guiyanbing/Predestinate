package com.juxin.predestinate.ui.user.check.edit.info;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.observe.PObserver;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.mumu.bean.utils.TypeConvUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.picker.picker.DatePicker;
import com.juxin.predestinate.module.logic.baseui.picker.picker.OptionPicker;
import com.juxin.predestinate.module.logic.config.InfoConfig;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.CommonUtil;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.ui.user.edit.EditKey;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.util.HashMap;

/**
 * 详细资料
 * Created by Su on 2017/5/3.
 */

public class UserEditDetailInfoPanel extends BaseViewPanel implements RequestComplete{
    private TextView edu, job, birth, weight, star; //详细资料
    private String starValue;                       // 修改星座展示值
    private InfoConfig.SimpleConfig starConfig;

    public UserEditDetailInfoPanel(Context context) {
        super(context);
        setContentView(R.layout.f1_user_edit_detail_info_panel);

        initView();
        initEvent();
        refreshView();
    }

    private void initView() {
        edu = (TextView) findViewById(R.id.edu);
        job = (TextView) findViewById(R.id.job);
        birth = (TextView) findViewById(R.id.birth);
        weight = (TextView) findViewById(R.id.weight);
        star = (TextView) findViewById(R.id.star);
        starConfig = InfoConfig.getInstance().getStar();
    }

    private void initEvent() {
        findViewById(R.id.edu_view).setOnClickListener(clickListener);
        findViewById(R.id.job_view).setOnClickListener(clickListener);
        findViewById(R.id.birth_view).setOnClickListener(clickListener);
        findViewById(R.id.weight_view).setOnClickListener(clickListener);
        findViewById(R.id.star_view).setOnClickListener(clickListener);
    }

    public void refreshView() {
        UserDetail userdetail = ModuleMgr.getCenterMgr().getMyInfo();

        String sNotFilled = getContext().getResources().getString(R.string.str_not_filled);
        starValue = userdetail.getStar();
        edu.setText(TextUtils.isEmpty(userdetail.getEdu()) ? sNotFilled : userdetail.getEdu());
        job.setText(TextUtils.isEmpty(userdetail.getJob()) ? sNotFilled : userdetail.getJob());
        birth.setText(TextUtils.isEmpty(userdetail.getBirthday()) ? sNotFilled : userdetail.getBirthday());
        weight.setText(TextUtils.isEmpty(userdetail.getWeight()) ? sNotFilled : userdetail.getWeight());
        star.setText(TextUtils.isEmpty(starValue) ? sNotFilled : starValue);
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            UserDetail userdetail = ModuleMgr.getCenterMgr().getMyInfo();
            switch (v.getId()) {
                case R.id.edu_view:
                    final InfoConfig.SimpleConfig eduConfig = InfoConfig.getInstance().getEduN();
                    showOptionPickerDialog(userdetail.getEdu(), eduConfig, EditKey.s_key_edu, "学历");
                    break;

                case R.id.job_view:
                    final InfoConfig.SimpleConfig jobConfig = InfoConfig.getInstance().getJob();
                    showOptionPickerDialog(userdetail.getJob(), jobConfig, EditKey.s_key_job, "工作情况");
                    break;

                case R.id.birth_view:
                    final String[] dateValues = TextUtils.isEmpty(userdetail.getBirthday()) ? new String[]{"", "", ""} : userdetail.getBirthday().split("-");
                    PickerDialogUtil.showDatePickerDialog((FragmentActivity)getContext(), new DatePicker.OnYearMonthDayPickListener() {
                        @Override
                        public void onDatePicked(String year, String month, String day) {
                            if (!year.equals(dateValues[0]) || !month.equals(dateValues[1]) || !day.equals(dateValues[2])) {
                                LoadingDialog.show((FragmentActivity)getContext(), "正在修改，请稍候...");
                                String value = year + "-" + month + "-" + day;
                                HashMap<String, Object> postParams = new HashMap<>();
                                postParams.put(EditKey.s_key_birth, value);
                                String starValueTmp = CommonUtil.getStar(TypeConvUtil.toInt(month), TypeConvUtil.toInt(day));
                                if (!starValue.equals(starValueTmp) || starValue != starValueTmp) {
                                    postParams.put(EditKey.s_key_star, starConfig.getSubmitWithShow(starValueTmp));
                                }
                                ModuleMgr.getCenterMgr().updateMyInfo(postParams, UserEditDetailInfoPanel.this);
                            }
                        }
                    }, Integer.valueOf(dateValues[0]), Integer.valueOf(dateValues[1]), Integer.valueOf(dateValues[2]));
                    break;

                case R.id.weight_view:
                    final InfoConfig.SimpleConfig weightConfig = InfoConfig.getInstance().getWeight();
                    showOptionPickerDialog(userdetail.getWeight(), weightConfig, EditKey.s_key_weight, "体重");
                    break;

                case R.id.star_view:
                    showOptionPickerDialog(starValue, starConfig, EditKey.s_key_star, "星座");
                    break;
            }
        }
    };

    /**
     * 单项选择Dialog
     * <p/>
     * 资料修改
     */
    private void showOptionPickerDialog(final String value, final InfoConfig.SimpleConfig config, final String postKey, String title) {
        PickerDialogUtil.showOptionPickerDialog((FragmentActivity)getContext(), new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                if (value == null || !value.equals(option)) {
                    LoadingDialog.show((FragmentActivity) getContext(), "正在修改，请稍候...");
                    HashMap<String, Object> postParams = new HashMap<String, Object>();
                    postParams.put(postKey, config.getSubmitWithShow(option));
                    ModuleMgr.getCenterMgr().updateMyInfo(postParams, UserEditDetailInfoPanel.this);
                }
            }
        }, config.getShow(), value, title);
    }

    @Override
    public void onRequestComplete(final HttpResponse response) {
        //修改个人资料返回结果
        if (response.getUrlParam() == UrlParam.updateMyInfo) {
            LoadingDialog.closeLoadingDialog(200, new TimerUtil.CallBack() {
                @Override
                public void call() {
                    if (response.isOk()) {
                        PToast.showShort("修改成功");
                    } else {
                        PToast.showShort("修改失败，请重试");
                    }
                }
            });
        }
    }
}
