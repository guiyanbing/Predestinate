package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;

/**
 * 创建日期：2017/6/21
 * 描述:邀请过期dlg
 * 作者:lc
 */
public class InvitationExpiredDlg extends BaseDialogFragment implements View.OnClickListener {
    private Context context;
    private TextView tv_call;

    public InvitationExpiredDlg() {
        settWindowAnimations(R.style.AnimDownInDownOutOverShoot);
        setGravity(Gravity.BOTTOM);
        setDialogSizeRatio(1, 0);
        setCancelable(false);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_invitation_expired_dlg);
        View view = getContentView();
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_call = (TextView) view.findViewById(R.id.tv_call);
        tv_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_call:
                //TODO 回拨
                PToast.showShort("回拨...");
                dismiss();
                break;
            default:
                break;
        }
    }
}
