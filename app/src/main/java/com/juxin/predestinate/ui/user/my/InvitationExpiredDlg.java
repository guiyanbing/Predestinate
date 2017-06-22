package com.juxin.predestinate.ui.user.my;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.utils.TypeConvertUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.local.statistics.SendPoint;
import com.juxin.predestinate.module.local.statistics.Statistics;
import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;

/**
 * 创建日期：2017/6/21
 * 描述:邀请过期dlg
 * 作者:lc
 */
public class InvitationExpiredDlg extends BaseDialogFragment implements View.OnClickListener {

    private long otherId;
    private String channel_uid;
    private int type;//类型: 1 视频、2 语音
    private int price;

    private Context context;
    private TextView tv_call;

    public InvitationExpiredDlg() {
        settWindowAnimations(R.style.AnimDownInDownOutOverShoot);
        setGravity(Gravity.BOTTOM);
        setDialogSizeRatio(1, 0);
        setCancelable(false);
    }

    public void setData(Context context, long otherId, String channel_uid, int type, int price) {
        this.context = context;
        this.otherId = otherId;
        this.channel_uid = channel_uid;
        this.type = type;
        this.price = price;
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
        tv_call.setText(getString(R.string.back_call_bt, price));
        tv_call.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_call:
                if(type == 1) {
                    VideoAudioChatHelper.getInstance().inviteVAChat((Activity) context, otherId, VideoAudioChatHelper.TYPE_VIDEO_CHAT,
                            true, Constant.APPEAR_TYPE_NO, channel_uid, false);
                }else if(type == 2) {
                    VideoAudioChatHelper.getInstance().inviteVAChat((Activity) context, otherId, VideoAudioChatHelper.TYPE_AUDIO_CHAT,
                            channel_uid);
                }
                dismiss();
                break;
            default:
                break;
        }
    }
}
