package com.juxin.predestinate.ui.user.check.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.bean.center.user.others.UserRemark;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.check.edit.custom.EditPopupWindow;
import com.juxin.predestinate.ui.user.edit.EditKey;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 他人： 资料设置
 * Created by Su on 2017/4/13.
 */
public class UserOtherSetAct extends BaseActivity implements RequestComplete {
    private TextView user_nick, user_id, user_remark;
    private ImageView user_head;
    private SeekBar videoBar, voiceBar, shieldBar;

    private UserProfile userProfile; // 他人资料
    private String tempRemark;       // 临时备注名称

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_other_set_act);
        setTitle(getString(R.string.user_other_info_set));
        setBackView();

        initData();
        initView();
    }

    private void initView() {
        user_head = (ImageView) findViewById(R.id.user_head);
        user_nick = (TextView) findViewById(R.id.user_nick);
        user_id = (TextView) findViewById(R.id.user_id);
        user_remark = (TextView) findViewById(R.id.user_remark);
        videoBar = (SeekBar) findViewById(R.id.sb_accept_video);
        voiceBar = (SeekBar) findViewById(R.id.sb_accept_voice);
        shieldBar = (SeekBar) findViewById(R.id.sb_msg_shield);

        user_remark.setOnClickListener(listener);
        findViewById(R.id.rl_clear).setOnClickListener(listener);
        findViewById(R.id.rl_complain).setOnClickListener(listener);

        if (userProfile == null) return;
        ImageLoader.loadRoundCorners(this, userProfile.getAvatar(), 8, user_head);
        user_nick.setText(userProfile.getNickname());
        user_id.setText("ID: " + userProfile.getUid());
    }

    private void initData() {
        userProfile = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_OTHER_KEY);
        if (userProfile == null) {
            PToast.showShort(getString(R.string.user_other_info_req_fail));
            return;
        }

        ModuleMgr.getCenterMgr().reqGetRemarkName(userProfile.getUid(), this);
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.user_remark:
                    EditPopupWindow popupWindow = new EditPopupWindow(UserOtherSetAct.this, EditKey.s_key_remark_name, user_remark);
                    popupWindow.showPopupWindow();
                    popupWindow.setEditPopupWindowListener(new EditPopupWindow.EditPopupWindowListener() {
                        @Override
                        public void editFinish(String text) {
                            tempRemark = "";
                            ModuleMgr.getCenterMgr().reqSetRemarkName(userProfile.getUid(), text, UserOtherSetAct.this);
                        }
                    });
                    break;

                case R.id.rl_clear:     // 清空聊天记录
                    clearRecord();
                    break;

                case R.id.rl_complain:  // 投诉，跳转举报
                    UIShow.showDefriendAct(userProfile.getUid(), UserOtherSetAct.this);
                    break;
            }
        }
    };

    /**
     * 清除聊天记录
     */
    private void clearRecord() {
        PickerDialogUtil.showSimpleAlertDialog(this, new SimpleTipDialog.ConfirmListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onSubmit() {
                PToast.showShort(getString(R.string.user_other_set_chat_del_suc));
            }
        }, getString(R.string.user_other_set_chat_del), R.color.text_zhuyao_black, "");
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.getUrlParam() == UrlParam.reqGetRemarkName) {
            if (!response.isOk()) {
                return;
            }
            UserRemark userRemark = (UserRemark) response.getBaseData();

            PLogger.d("ksjkjkjj---111: " + userRemark.getRemarkName() + ",  " + response.getResponseString());
            user_remark.setText(TextUtils.isEmpty(userRemark.getRemarkName()) ? "" : userRemark.getRemarkName());
        }

        if (response.getUrlParam() == UrlParam.reqSetRemarkName) {

            PLogger.d("ksjkjkjj---222: " + response.getResponseString());

            if (!response.isOk()) {
                PToast.showShort(getString(R.string.user_info_set_fail));
                return;
            }
            user_remark.setText(TextUtils.isEmpty(tempRemark) ? "" : tempRemark);
        }
    }
}
