package com.juxin.predestinate.ui.user.fragment;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 用户中心功能panel
 */
public class UserFragmentFunctionPanel extends BaseViewPanel {

    public UserFragmentFunctionPanel(Context context) {
        super(context);
        setContentView(R.layout.p1_user_fragment_function_panel);

        initView();
    }

    private void initView() {

        refreshBadge();
        findViewById(R.id.ll_vip).setOnClickListener(clickListener);
        findViewById(R.id.ll_wallet).setOnClickListener(clickListener);
    }

    /**
     * UserFragmentWealthPanel刷新角标展示
     */
    public void refreshBadge() {
    }

    /**
     * 界面刷新
     */
    public void refreshView(UserDetail userDetail) {
        if (userDetail == null) return;
    }

    private final NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_vip://跳转VIP
                    break;
                case R.id.ll_wallet://跳转手机认证
                    //手机绑定
                    //UIShow.showPhoneVerify_Act(getContext(), ModuleMgr.getCenterMgr().getMyInfo().getMobileAuthStatus()==3);
                    break;
            }
        }
    };
}
