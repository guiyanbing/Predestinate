package com.juxin.predestinate.ui.setting;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.settting.ContactBean;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.start.UserLoginExtAct;

/**
 * 封禁弹框
 * Created by xy on 2017/6/20.
 */

public class BottomBannedDialog extends BaseDialogFragment {
    private Context mContext;
    private boolean isLogin;

    public BottomBannedDialog() {
        settWindowAnimations(R.style.AnimDownInDownOutOverShoot);
        setGravity(Gravity.BOTTOM);
        setDialogSizeRatio(-2, -2);
        setCancelable(true);
    }

    public void setCtx(Context context, boolean isLogin) {
        this.mContext = context;
        this.isLogin = isLogin;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_bottom_banned_dialog);
        initView();
        return getContentView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_bottom_banned_time)).setText("封停时间:永久");
        findViewById(R.id.iv_bottom_banned_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().cancel();
                ModuleMgr.getLoginMgr().logout();
                if (isLogin)
                    UIShow.showActivityClearTask(mContext, UserLoginExtAct.class);
            }
        });
        ContactBean contactBean = ModuleMgr.getCommonMgr().getContactBean();
        if (contactBean == null) {
            return;
        }
        ((TextView) findViewById(R.id.bottom_banned_csphone)).setText(contactBean.getTel());
    }
}