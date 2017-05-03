package com.juxin.predestinate.ui.user.check.edit.info;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.area.City;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.baseui.picker.picker.AddressPicker;
import com.juxin.predestinate.module.logic.baseui.picker.picker.OptionPicker;
import com.juxin.predestinate.module.logic.config.InfoConfig;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.ui.user.edit.EditKey;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

import java.util.HashMap;

/**
 * 基本资料
 * Created by Su on 2017/5/3.
 */

public class UserEditBaseInfoPanel extends BaseViewPanel implements RequestComplete {
    private TextView name, age, home, height, income, marry,        //基本资料
            qq_info, qq, mobile_info, mobile, wechat_info, wechat;  //联系方式

    private UserDetail userDetail;

    public UserEditBaseInfoPanel(Context context) {
        super(context);
        setContentView(R.layout.f1_user_edit_base_info_panel);

        initView();
        initData();
        refreshView();
    }

    private void initView() {
        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        home = (TextView) findViewById(R.id.home);
        height = (TextView) findViewById(R.id.height);
        income = (TextView) findViewById(R.id.income);
        marry = (TextView) findViewById(R.id.marry);
        qq_info = (TextView) findViewById(R.id.qq_info);
        qq = (TextView) findViewById(R.id.qq);
        mobile_info = (TextView) findViewById(R.id.mobile_info);
        mobile = (TextView) findViewById(R.id.mobile);
        wechat_info = (TextView) findViewById(R.id.wechat_info);
        wechat = (TextView) findViewById(R.id.wechat);
    }

    private void initData() {
        findViewById(R.id.name_view).setOnClickListener(clickListener);
        findViewById(R.id.gender_view).setOnClickListener(clickListener);
        findViewById(R.id.age_view).setOnClickListener(clickListener);
        findViewById(R.id.home_view).setOnClickListener(clickListener);
        findViewById(R.id.height_view).setOnClickListener(clickListener);
        findViewById(R.id.income_view).setOnClickListener(clickListener);
        findViewById(R.id.marry_view).setOnClickListener(clickListener);
        findViewById(R.id.view_qq).setOnClickListener(clickListener);
        findViewById(R.id.view_mobile).setOnClickListener(clickListener);
        findViewById(R.id.view_wechat).setOnClickListener(clickListener);
    }


    public void refreshView() {
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        String sNotFilled = getContext().getResources().getString(R.string.str_not_filled);
        name.setText(userDetail.getNickname());
        age.setText(getContext().getString(R.string.user_info_age, userDetail.getAge()));
        home.setText(TextUtils.isEmpty(userDetail.getAddressShow()) ? sNotFilled : userDetail.getAddressShow());
        height.setText(userDetail.getHeight() + "cm");
        income.setText(TextUtils.isEmpty(userDetail.getIncome()) ? sNotFilled : userDetail.getIncome());
        marry.setText(TextUtils.isEmpty(userDetail.getMarry()) ? sNotFilled : userDetail.getMarry());
        qq_info.setText(TextUtils.isEmpty(userDetail.getQqNum())
                || userDetail.getQqNum() == "null" ? "QQ" : "QQ：" + userDetail.getQqNum());
        mobile_info.setText(TextUtils.isEmpty(userDetail.getPhone())
                || userDetail.getPhone() == "null" ? "手机" : "手机：" + userDetail.getPhone());
        wechat_info.setText(TextUtils.isEmpty(userDetail.getWechatNum())
                || userDetail.getWechatNum() == "null" ? "微信" : "微信：" + userDetail.getWechatNum());

        setAuthTip();
    }

    // 填充权限
    private void setAuthTip() {
        qq.setText(contactStatus(!TextUtils.isEmpty(userDetail.getQqNum()), userDetail.getQqNumAuth() != 2));
        mobile.setText(contactStatus(!TextUtils.isEmpty(userDetail.getPhone()), userDetail.getPhoneAuth() != 2));
        wechat.setText(contactStatus(!TextUtils.isEmpty(userDetail.getWechatNum()), userDetail.getWechatAuth() != 2));
    }

    // 权限展示逻辑
    private String contactStatus(boolean haveContact, boolean showVip) {
        return haveContact ? (showVip ? (userDetail.isMan() ? "公开" : "仅对VIP开放") : "保密") : "未填写";
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            final UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            switch (v.getId()) {
                case R.id.name_view:
                    String defaultValut = userDetail.getNickname();
                    break;

                case R.id.gender_view:
                    PToast.showShort("性别无法修改");
                    break;

                case R.id.age_view:
                    PToast.showShort("年龄无法修改");
                    break;

                case R.id.home_view:
                    final String province = userDetail.getProvinceName() == null ? "" : userDetail.getProvinceName();
                    final String city = userDetail.getCityName() == null ? "" : userDetail.getCityName();
                    PickerDialogUtil.showAddressPickerDialog2((FragmentActivity) getContext(), new AddressPicker.OnAddressPickListener() {
                        @Override
                        public void onAddressPicked(City city) {
                            if (!province.equals(city.getProvinceName()) || !city.equals(city.getCityName())) {
                                LoadingDialog.show((FragmentActivity) getContext(), "正在修改，请稍候...");
                                HashMap<String, Object> postParams = new HashMap<>();
                                postParams.put(EditKey.s_key_pro, city.getProvinceID());
                                postParams.put(EditKey.s_key_city, city.getCityID());
                                ModuleMgr.getCenterMgr().updateMyInfo(postParams, UserEditBaseInfoPanel.this);
                            }
                        }
                    }, province, city);
                    break;

                case R.id.height_view:
                    InfoConfig.SimpleConfig simpleConfig = InfoConfig.getInstance().getHeightN();
                    showOptionPickerDialog(userDetail.getHeight() + "cm", simpleConfig, EditKey.s_key_height, "身高");
                    break;

                case R.id.income_view:
                    InfoConfig.SimpleConfig incomeConfig = InfoConfig.getInstance().getIncomeN();
                    showOptionPickerDialog(userDetail.getIncome(), incomeConfig, EditKey.s_key_income, "月收入");
                    break;

                case R.id.marry_view:
                    InfoConfig.SimpleConfig marryConfig = InfoConfig.getInstance().getMarry();
                    showOptionPickerDialog(userDetail.getMarry(), marryConfig, EditKey.s_key_marry, "情感状态");
                    break;

                case R.id.view_qq:
                    String qqValue = TextUtils.isEmpty(userDetail.getQqNum()) ? "" : userDetail.getQqNum();
                    break;

                case R.id.view_mobile:
                    if (userDetail.isVerifyCellphone()) {
                        MMToast.showShort("手机号已绑定，无法修改");
                        break;
                    }
                    String moblieValue = TextUtils.isEmpty(userDetail.getPhone()) ? "" : userDetail.getPhone();
                    break;

                case R.id.view_wechat:
                    String weChatValue = TextUtils.isEmpty(userDetail.getWechatNum()) ? "" : userDetail.getWechatNum();
                    break;
            }
        }
    };

    // 展示单项选择Dialog
    private void showOptionPickerDialog(final String value, final InfoConfig.SimpleConfig config, final String postKey, String title) {
        PickerDialogUtil.showOptionPickerDialog((FragmentActivity) getContext(), new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(String option) {
                if (value == null || !value.equals(option)) {
                    LoadingDialog.show((FragmentActivity) getContext(), "正在修改，请稍候...");
                    HashMap<String, Object> postParams = new HashMap<>();
                    postParams.put(postKey, config.getSubmitWithShow(option));
                    ModuleMgr.getCenterMgr().updateMyInfo(postParams, UserEditBaseInfoPanel.this);
                }
            }
        }, config.getShow(), value, title);
    }

    @Override
    public void onRequestComplete(final HttpResponse response) {
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
