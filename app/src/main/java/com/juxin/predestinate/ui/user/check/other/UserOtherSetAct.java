package com.juxin.predestinate.ui.user.check.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.others.UserProfile;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.user.util.CenterConstant;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 他人： 资料设置
 * Created by Su on 2017/4/13.
 */
public class UserOtherSetAct extends BaseActivity {
    private TextView user_nick, user_id, user_remark;
    private ImageView user_head;
    private SeekBar videoBar, voiceBar, shieldBar;

    private UserProfile userProfile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_other_set_act);
        setTitle(getString(R.string.user_other_info_set));
        setBackView();

        initView();
        initData();
    }

    private void initView() {
        user_head = (ImageView) findViewById(R.id.user_head);
        user_nick = (TextView) findViewById(R.id.user_nick);
        user_id = (TextView) findViewById(R.id.user_id);
        user_remark = (TextView) findViewById(R.id.user_remark);
        videoBar = (SeekBar) findViewById(R.id.sb_accept_video);
        voiceBar = (SeekBar) findViewById(R.id.sb_accept_voice);
        shieldBar = (SeekBar) findViewById(R.id.sb_msg_shield);

        findViewById(R.id.rl_clear).setOnClickListener(listener);
        findViewById(R.id.rl_complain).setOnClickListener(listener);
    }

    private void initData() {
        userProfile = getIntent().getParcelableExtra(CenterConstant.USER_CHECK_OTHER_KEY);
        if (userProfile == null) {
            PToast.showShort(getString(R.string.user_other_info_req_fail));
            return;
        }

        ImageLoader.loadRoundCorners(this, userProfile.getAvatar(), 8, user_head);
        user_nick.setText(userProfile.getNickname());
        user_id.setText("ID: " + userProfile.getUid());
        user_remark.setText(userProfile.getNickname());
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
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
}
