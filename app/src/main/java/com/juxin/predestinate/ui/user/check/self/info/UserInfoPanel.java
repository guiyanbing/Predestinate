package com.juxin.predestinate.ui.user.check.self.info;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.juxin.library.utils.TypeConvertUtil;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.ui.user.check.bean.UserPersonInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户详细资料列表
 * Created by Su on 2016/6/6.
 */
public class UserInfoPanel extends BasePanel {

    private ListView list_info;
    private int spaceCount = 0;
    private List<UserPersonInfo> userInfoList;
    private UserDetailAdapter userDetailAdapter;

    private UserDetail userDetail;

    public UserInfoPanel(Context context, ListView list_info) {
        super(context);
        setContentView(list_info);

        initData();
        initView();
        handleUserDetailData();
    }

    private void initView() {
        userDetailAdapter = new UserDetailAdapter(getContext(), userInfoList);
        list_info = (ListView) findViewById(R.id.list_info);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_margintop, null);
        View footerView = inflater.inflate(R.layout.common_footer_distance, null);
        list_info.addHeaderView(view);
        list_info.addFooterView(footerView);
        list_info.setAdapter(userDetailAdapter);
        list_info.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }

    private void initData() {
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();

        userInfoList = new ArrayList<>();
        String[] baseDatas = getContext().getResources().getStringArray(R.array.base_data);
        String[] detailDatas = getContext().getResources().getStringArray(R.array.detail_data);
        String[] contactDatas = getContext().getResources().getStringArray(R.array.contact_data);

        for (String baseData : baseDatas) {
            userInfoList.add(new UserPersonInfo("基本信息", baseData));
        }
        for (String detailData : detailDatas) {
            userInfoList.add(new UserPersonInfo("详细资料", detailData));
        }
        for (String contactData : contactDatas) {
            userInfoList.add(new UserPersonInfo("联系方式", contactData));
        }
    }

    private void handleUserDetailData() {
        fillBaseValue();
        fillDetailValue();
        fillContactValue();
        userDetailAdapter.notifyDataSetChanged();
    }

    // 填充基本资料
    private void fillBaseValue() {
        String gender = userDetail.getGender() == 1 ? "男" : "女";
        String address = userDetail.getAddressShow();
        if (!TextUtils.isEmpty(address) && address.length() > 14) {
            address = address.substring(0, 14) + "...";
        }
        String height = userDetail.getHeight() == 0 ? "" : (userDetail.getHeight() + "cm");
        String income = userDetail.getIncome() == null ? "" : userDetail.getIncome();
        String marry = userDetail.getMarry() == null ? "" : userDetail.getMarry();
        String age = userDetail.getAge() == 0 ? "" : userDetail.getAge() + "岁";
        String nickName = userDetail.getNickname();
        String[] baseValues = new String[]{nickName, gender, age, address, height, income, marry};
        int count = spaceCount;
        for (int i = count; i < baseValues.length + count; i++) {
            userInfoList.get(i).setValue(baseValues[i - count]);
        }
    }

    // 填充详细资料
    private void fillDetailValue() {
        String edu = userDetail.getEdu() == null ? "" : userDetail.getEdu();
        String job = userDetail.getJob() == null ? "" : userDetail.getJob();
        String weight = userDetail.getWeight() == null ? "" : userDetail.getWeight();
        String star = userDetail.getStar() == null ? "" : userDetail.getStar();
        String[] detailValues = new String[]{edu, job, userDetail.getBirthday(), weight, star};
        int count = 7 + spaceCount;
        for (int i = count; i < detailValues.length + count; i++) {
            userInfoList.get(i).setValue(detailValues[i - count]);
        }
    }

    // 填充联系方式资料
    private void fillContactValue() {
        String qq = TextUtils.isEmpty(userDetail.getQqNum()) ? "" : userDetail.getQqNum();
        int qqAuth = userDetail.getQqNumAuth();
        String qqValue = qq + "-" + qqAuth;

        String mobile = TextUtils.isEmpty(userDetail.getPhone()) ? "" : userDetail.getPhone();
        int mobileAuth = userDetail.getPhoneAuth();
        String mobileValue = mobile + "-" + mobileAuth;

        String weChat = TextUtils.isEmpty(userDetail.getWechatNum()) ? "" : userDetail.getWechatNum();
        int weChatAuth = userDetail.getWechatAuth();
        String weChatValue = weChat + "-" + weChatAuth;

        String[] contactValues = new String[]{qqValue, mobileValue, weChatValue};
        int count = 12 + spaceCount;
        for (int i = count; i < contactValues.length + count; i++) {
            if (contactValues[i - count] != null) {
                int auth = TypeConvertUtil.toInt(contactValues[i - count].split("-")[1], 0);
                String value = contactValues[i - count].split("-")[0];
                userInfoList.get(i).setValue("".equals(value) ? "未填写" : limits(auth));
                userInfoList.get(i).setContact(value);
            }
        }
    }

    private String limits(int auth) {
        return auth == 2 ? "保密" : userDetail.isMan() ? "公开" : "仅对VIP公开";
    }

    //****************************************************************************************

    /**
     * 判断最终显示的字符串，默认查看的用户都为异性
     *
     * @param secret   用户资料保密级别
     * @param field    用户联系方式字段
     * @param position 用户联系方式字段 0 qq 1 手机号 2 微信号
     * @return 最终显示的字符串
     */
    private String judgeShowString(int secret, String field, int position) {
        String showString = "";
        boolean isVip = ModuleMgr.getCenterMgr().getMyInfo().isVip();
        if (secret == 2) {//保密显示
            showString = "保密";
        } else if (isVip || !userDetail.isMan() || TextUtils.isEmpty(field)) {//女或vip显示联系方式
            showString = field;
        } else if (position == 1) {//隐藏部分手机号
            if (!"".equals(field) && field.length() >= 4) {//手机号处理
                showString = field.substring(0, 4) + "***仅VIP可见";
            }
        } else {
            showString = "仅VIP可见";
        }
        return showString;
    }
}
