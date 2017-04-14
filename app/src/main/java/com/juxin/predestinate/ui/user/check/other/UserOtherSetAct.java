package com.juxin.predestinate.ui.user.check.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.utils.NoDoubleClickListener;

/**
 * 他人： 资料设置
 * Created by Su on 2017/4/13.
 */
public class UserOtherSetAct extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_other_set_act);
        setTitle(getString(R.string.user_other_info_set));
        setBackView();

        initView();
    }

    private void initView() {

        findViewById(R.id.rl_clear).setOnClickListener(listener);
        findViewById(R.id.rl_complain).setOnClickListener(listener);
        findViewById(R.id.ll_label).setOnClickListener(listener);
    }

    private NoDoubleClickListener listener = new NoDoubleClickListener() {
        @Override
        public void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.ll_label:     // 标签
                    UIShow.showUserOtherLabelAct(UserOtherSetAct.this);
                    break;

                case R.id.rl_clear:     // 清空聊天记录
                    clearRecord();
                    break;

                case R.id.rl_complain:  // 投诉，跳转举报
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
            }
        }, getString(R.string.user_other_set_chat_del), R.color.text_zhuyao_black, "");
    }
}
