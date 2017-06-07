package com.juxin.predestinate.ui.user.my;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.log.PSP;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;

/**
 * 创建日期：2017/6/7
 * 描述: 出场方式dlg
 * 作者:lc
 */
public class LookAtHerDlg extends BaseDialogFragment implements View.OnClickListener {
    private static final int APPEAR_TYPE_OWN = 1;//自己露脸
    private static final int APPEAR_TYPE_NO_OWN = 2;//自己不露脸

    private Context context;
    private long otherId;
    private CheckBox cb_own_agree, cb_own_disagree, cb_def_sel;

    public LookAtHerDlg() {
        settWindowAnimations(R.style.AnimDownInDownOutOverShoot);
        setGravity(Gravity.BOTTOM);
        setDialogSizeRatio(1, 0);
        setCancelable(true);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setOtherId(long otherId) {
        this.otherId = otherId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_look_at_her_dlg);
        View view = getContentView();
        initView(view);
        return view;
    }

    private void initView(View view) {
        RelativeLayout rl_own_agree = (RelativeLayout) view.findViewById(R.id.rl_own_agree);
        RelativeLayout rl_own_disagree = (RelativeLayout) view.findViewById(R.id.rl_own_disagree);
        cb_own_agree = (CheckBox) view.findViewById(R.id.cb_own_agree);
        cb_own_disagree = (CheckBox) view.findViewById(R.id.cb_own_disagree);
        cb_def_sel = (CheckBox) view.findViewById(R.id.cb_def_sel);
        TextView tv_select_ok = (TextView) view.findViewById(R.id.tv_select_ok);

        rl_own_agree.setOnClickListener(this);
        rl_own_disagree.setOnClickListener(this);
        tv_select_ok.setOnClickListener(this);

        cb_own_agree.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_own_agree:
                cb_own_agree.setChecked(true);
                cb_own_disagree.setChecked(false);
                break;
            case R.id.rl_own_disagree:
                cb_own_agree.setChecked(false);
                cb_own_disagree.setChecked(true);
                break;
            case R.id.tv_select_ok:
                if (cb_def_sel.isChecked()) {
                    if (cb_own_agree.isChecked()) {
                        PSP.getInstance().put(ModuleMgr.getCommonMgr().getPrivateKey(Constant.APPEAR_TYPE), APPEAR_TYPE_OWN);
                    } else if (cb_own_disagree.isChecked()) {
                        PSP.getInstance().put(ModuleMgr.getCommonMgr().getPrivateKey(Constant.APPEAR_TYPE), APPEAR_TYPE_NO_OWN);
                    }
                }
                VideoAudioChatHelper.getInstance().inviteVAChat((Activity) context, otherId, VideoAudioChatHelper.TYPE_VIDEO_CHAT);
                dismiss();
                break;
            default:
                break;
        }
    }
}
