package com.juxin.predestinate.ui.user.my;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.log.PSP;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;
import com.juxin.predestinate.ui.mail.chat.PrivateChatAct;

/**
 * 创建日期：2017/6/7
 * 描述: 出场方式dlg
 * 作者:lc
 */
public class LookAtHerDlg extends BaseDialogFragment implements View.OnClickListener {
    private Context context;
    private long otherId;
    private String channel_uid;
    private int selectVal;
    private boolean isMan = true;
    private boolean isInvate = false;
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

    public void setOtherId(long otherId, String channel_uid) {
        this.otherId = otherId;
        this.channel_uid = channel_uid;
    }

    public void setIsInvate(boolean isInvate) {
        this.isInvate = isInvate;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_look_at_her_dlg);
        View view = getContentView();
        isMan = ModuleMgr.getCenterMgr().getMyInfo().isMan();
        initView(view);
        return view;
    }

    private void initView(View view) {
        RelativeLayout rl_own_agree = (RelativeLayout) view.findViewById(R.id.rl_own_agree);
        RelativeLayout rl_own_disagree = (RelativeLayout) view.findViewById(R.id.rl_own_disagree);
        LinearLayout ll_def_select = (LinearLayout) view.findViewById(R.id.ll_def_select);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        TextView tv_first = (TextView) view.findViewById(R.id.tv_first);
        TextView tv_second = (TextView) view.findViewById(R.id.tv_second);
        cb_own_agree = (CheckBox) view.findViewById(R.id.cb_own_agree);
        cb_own_disagree = (CheckBox) view.findViewById(R.id.cb_own_disagree);
        cb_def_sel = (CheckBox) view.findViewById(R.id.cb_def_sel);
        TextView tv_select_ok = (TextView) view.findViewById(R.id.tv_select_ok);

        rl_own_agree.setOnClickListener(this);
        rl_own_disagree.setOnClickListener(this);
        tv_select_ok.setOnClickListener(this);

        if(!isMan && isInvate) {//女号--邀请他
            VideoVerifyBean bean = ModuleMgr.getCommonMgr().getVideoVerify();
            tv_title.setText(getString(R.string.invitation_he_type));
            rl_own_agree.setVisibility(View.GONE);
            rl_own_disagree.setVisibility(View.GONE);
            cb_own_agree.setVisibility(View.GONE);
            cb_own_disagree.setVisibility(View.GONE);
            ll_def_select.setVisibility(View.GONE);

            if(bean.getBooleanVideochat()) {
                tv_first.setText(getString(R.string.invitation_he_video));
                rl_own_agree.setVisibility(View.VISIBLE);
            }
            if(bean.getBooleanAudiochat()) {
                tv_second.setText(getString(R.string.invitation_he_audio));
                rl_own_agree.setVisibility(View.VISIBLE);
            }
            tv_select_ok.setText(getString(R.string.cancel));
        }
        cb_own_agree.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_own_agree:
                if(!isInvate) {// 男、女 来自非邀请他按钮
                    cb_own_agree.setChecked(true);
                    cb_own_disagree.setChecked(false);
                }else {// 女性--邀请他(邀请视频)
                    VideoAudioChatHelper.getInstance().girlSingleInvite((Activity)context,otherId,VideoAudioChatHelper.TYPE_VIDEO_CHAT);
                }

                break;
            case R.id.rl_own_disagree:
                if(!isInvate) {// 男、女 来自非邀请他按钮
                    cb_own_agree.setChecked(false);
                    cb_own_disagree.setChecked(true);
                }else {// 女性--邀请他(邀请语音)
                    VideoAudioChatHelper.getInstance().girlSingleInvite((Activity)context,otherId,VideoAudioChatHelper.TYPE_AUDIO_CHAT);
                }
                break;
            case R.id.tv_select_ok:
                if(!isInvate) {// 男、女 来自非邀请他按钮
                    if (cb_own_agree.isChecked()) {
                        selectVal = Constant.APPEAR_TYPE_OWN;
                        if (cb_def_sel.isChecked()) {
                            saveType(Constant.APPEAR_FOREVER_TYPE, Constant.APPEAR_TYPE_OWN);
                        }
                    } else if (cb_own_disagree.isChecked()) {
                        selectVal = Constant.APPEAR_TYPE_NO_OWN;
                        if (cb_def_sel.isChecked()) {
                            saveType(Constant.APPEAR_FOREVER_TYPE, Constant.APPEAR_TYPE_NO_OWN);
                        }
                    }
                    VideoAudioChatHelper.getInstance().inviteVAChat((Activity) context, otherId, VideoAudioChatHelper.TYPE_VIDEO_CHAT,
                            false, selectVal, channel_uid, false);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    private void saveType(String type, int val) {
        PSP.getInstance().put(ModuleMgr.getCommonMgr().getPrivateKey(type), val);
    }
}
