package com.juxin.predestinate.module.logic.notify.view;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.utils.InputUtils;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.invoke.Invoker;
import com.juxin.predestinate.module.logic.notify.LockScreenMgr;

/**
 * 锁屏弹窗：聊天，一次只显示与一位用户的聊天信息
 */
public class LockChatPanel extends BasePanel implements OnClickListener {

    private TextView wake_lock_nickname, wake_lock_message;
    private EditText wake_lock_content;
    private long uid;

    public LockChatPanel(Context context, long uid, String avatar, String user_name, String content) {
        super(context);
        setContentView(R.layout.common_wake_lockact);

        initView();

        refresh(uid, avatar, user_name, content);
    }

    private void initView() {
        wake_lock_nickname = (TextView) findViewById(R.id.wake_lock_nickname);
        wake_lock_message = (TextView) findViewById(R.id.wake_lock_message);
        wake_lock_content = (EditText) findViewById(R.id.wake_lock_content);

        findViewById(R.id.wake_lock_send).setOnClickListener(this);
        findViewById(R.id.wake_lock_off).setOnClickListener(this);
        wake_lock_message.setOnClickListener(this);
        wake_lock_nickname.setOnClickListener(this);
    }

    /**
     * 刷新锁屏聊天信息
     *
     * @param uid       用户uid
     * @param avatar    用户头像：暂时无用
     * @param user_name 用户昵称
     * @param content   聊天内容
     */
    public void refresh(long uid, String avatar, String user_name, String content) {
        this.uid = uid;
        wake_lock_nickname.setText(TextUtils.isEmpty(user_name) ? String.valueOf(uid) : user_name);
        wake_lock_message.setText(Html.fromHtml(content));
        wake_lock_content.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wake_lock_off:
                LockScreenMgr.getInstance().closeLockNotify();
                break;
            case R.id.wake_lock_send:
                if (TextUtils.isEmpty(wake_lock_content.getText().toString())) {
                    PToast.showShort("消息不能为空");
                } else {
                    InputUtils.forceClose(wake_lock_content);
                    LockScreenMgr.getInstance().closeLockNotify();
                    Invoker.getInstance().doInApp(null, "cmd_open_chat", "{\"uid\":" + uid + ",\"reply\":" + wake_lock_content.getText().toString() + "}");
                }
                break;
            case R.id.wake_lock_message:
            case R.id.wake_lock_nickname:
                LockScreenMgr.getInstance().closeLockNotify();
                Invoker.getInstance().doInApp(null, "cmd_open_chat", "{\"uid\":" + uid + "}");
                break;
        }
    }
}
