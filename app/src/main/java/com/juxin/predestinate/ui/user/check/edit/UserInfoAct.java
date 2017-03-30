package com.juxin.predestinate.ui.user.check.edit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 个人信息页
 * Created by Su on 2017/3/28.
 */

public class UserInfoAct extends BaseActivity {

    private ImageView img_header;
    private TextView tv_name, tv_age, tv_home, tv_sign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_selfinfo_act);
        setTitle(getString(R.string.user_self_info_act));
        setBackView();

        initView();
        initData();
    }

    private void initView() {
        img_header = (ImageView) findViewById(R.id.iv_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_age = (TextView) findViewById(R.id.tv_age);
        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_sign = (TextView) findViewById(R.id.tv_sign);

        img_header.setOnClickListener(listener);
        findViewById(R.id.name_view).setOnClickListener(listener);
        findViewById(R.id.age_view).setOnClickListener(listener);
        findViewById(R.id.home_view).setOnClickListener(listener);
        findViewById(R.id.sign_view).setOnClickListener(listener);
    }

    private void initData() {
        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        ImageLoader.loadRoundCorners(this, userDetail.getAvatar(), 10, img_header);
        tv_name.setText(userDetail.getNickname());
        tv_age.setText(userDetail.getAge() + "岁");
        tv_home.setText(userDetail.getProvince());
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.iv_avatar: // 更改头像
                    break;

                case R.id.name_view: // 打开修改昵称页
                    UIShow.showEditContentAct(UserInfoAct.this, "default");
                    break;

                case R.id.age_view:  // 修改年龄
                    break;
                case R.id.home_view: // 修改地址
                    break;

                case R.id.sign_view:// 个性签名
                    UIShow.showUserEditSignAct(UserInfoAct.this, "default-----sign");
                    break;
            }
        }
    };
}
