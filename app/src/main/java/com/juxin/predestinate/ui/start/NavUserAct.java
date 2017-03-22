package com.juxin.predestinate.ui.start;

import android.os.Bundle;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.login.LoginMgr;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.base.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;


/**
 * 用户导航页面
 * <p/>
 * Created by XY on 2017/3/21.
 */
public class NavUserAct extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isCanBack(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.r1_nav_act);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_reg).setOnClickListener(clickListener);
        findViewById(R.id.btn_login).setOnClickListener(clickListener);
    }

    private NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:   // 跳转到用户登录页面
                    UIShow.showLoginAct(NavUserAct.this);
                    break;

                case R.id.btn_reg:   // 跳转到用户注册页面
                    UIShow.showRegInfoAct(NavUserAct.this);  // 跳新版注册
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        ModuleMgr.getLoginMgr().clearCookie();
        super.onBackPressed();
    }
}
