package com.juxin.predestinate.ui.user.fragment;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 用户中心功能panel
 */
public class UserFragmentFunctionPanel extends BasePanel {

    private ImageView iv_vip_privilege, iv_immortal_verify;
    private TextView tv_vip_title, tv_vip_status, tv_immortal_title, tv_immortal_status;

    public UserFragmentFunctionPanel(Context context) {
        super(context);
        setContentView(R.layout.p1_user_fragment_function_panel);

        initView();
    }

    private void initView() {
        tv_vip_title = (TextView) findViewById(R.id.tv_vip_title);
        iv_vip_privilege = (ImageView) findViewById(R.id.iv_vip_privilege);
        tv_vip_status = (TextView) findViewById(R.id.tv_vip_status);
        tv_immortal_title = (TextView) findViewById(R.id.tv_immortal_title);
        iv_immortal_verify = (ImageView) findViewById(R.id.iv_immortal_verify);
        tv_immortal_status = (TextView) findViewById(R.id.tv_immortal_status);

        refreshBadge();
        if (ModuleMgr.getCenterMgr().getMyInfo().isMan()) {
            findViewById(R.id.ll_vip_privilege).setOnClickListener(clickListener);
        }
        findViewById(R.id.ll_immortal_verify).setOnClickListener(clickListener);
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

        // VIP
        tv_vip_status.setText(userDetail.isVip() ?
                getContext().getResources().getString(R.string.center_vip_left_day) :
                getContext().getResources().getString(R.string.center_vip_to_open));
        tv_vip_status.setTextColor(userDetail.isVip() ? getContext().getResources().getColor(R.color.color_D4D4D4) :
                getContext().getResources().getColor(R.color.color_83A0EC));
        tv_vip_title.setTextColor(userDetail.isVip() ? getContext().getResources().getColor(R.color.color_83A0EC) :
                getContext().getResources().getColor(R.color.color_D4D4D4));
        iv_vip_privilege.setEnabled(!userDetail.isVip());

        // 真人认证
        tv_immortal_status.setText(userDetail.isVerifyAll() ?
                getContext().getResources().getString(R.string.center_phone_has_verify) :
                getContext().getResources().getString(R.string.center_video_to_verify));
        tv_immortal_status.setTextColor(userDetail.isVerifyAll() ? getContext().getResources().getColor(R.color.color_D4D4D4) :
                getContext().getResources().getColor(R.color.color_83A0EC));
        tv_immortal_title.setTextColor(userDetail.isVerifyAll() ? getContext().getResources().getColor(R.color.color_83A0EC) :
                getContext().getResources().getColor(R.color.color_D4D4D4));
        iv_immortal_verify.setEnabled(!userDetail.isVerifyAll());
    }

    private final NoDoubleClickListener clickListener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_vip_privilege: // 开通vip
                    UIShow.showOpenVipActivity(getContext());
                    break;

                case R.id.ll_immortal_verify: // 真人认证
                    UIShow.showMyAuthenticationAct((FragmentActivity) getContext(), 103);
                    break;
            }
        }
    };
}
