package com.juxin.predestinate.ui.user.check.edit.info;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.view.CircleImageView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 编辑页面头部
 * Created by Su on 2017/5/3.
 */
public class UserEditInfoHeadPanel extends BaseViewPanel {
    private UserDetail userDetail;

    private CircleImageView img_header;
    private TextView user_id, user_gender, user_age, user_height, user_province;

    public UserEditInfoHeadPanel(Context context) {
        super(context);
        setContentView(R.layout.f1_user_edit_info_head_panel);

        initView();
        initData();
    }

    public void initView() {
        img_header = (CircleImageView) findViewById(R.id.img_header);
        user_id = (TextView) findViewById(R.id.user_id);
        user_gender = (TextView) findViewById(R.id.user_gender);
        user_age = (TextView) findViewById(R.id.user_age);
        user_height = (TextView) findViewById(R.id.user_height);
        user_province = (TextView) findViewById(R.id.user_province);

        img_header.setOnClickListener(listener);
    }

    public void initData() {
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();

        ImageLoader.loadAvatar(getContext(), userDetail.getAvatar(), img_header);
        user_id.setText("ID: " + userDetail.getUid());
        user_gender.setText(userDetail.isMan() ? "男" : "女");
        user_age.setText(getContext().getString(R.string.user_info_age, userDetail.getAge()));
        user_height.setText(userDetail.getHeight() + "cm");
        user_province.setText(userDetail.getProvince());
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.img_header:
                    break;
            }
        }
    };
}
