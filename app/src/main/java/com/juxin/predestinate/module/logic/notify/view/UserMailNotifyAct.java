package com.juxin.predestinate.module.logic.notify.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.invoke.Invoker;
import com.juxin.predestinate.module.util.BaseUtil;

/**
 * 应用非锁屏状态下消息弹框
 */
public class UserMailNotifyAct extends BaseActivity implements View.OnClickListener {

    private UserInfoLightweight simpleData;//简单的用户资料
    private String content;//消息内容
    private int iClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_user_mail_notify);

        initData();
        initView();
        setData();
    }

    private void initData() {
        simpleData = getIntent().getParcelableExtra("simple_data");
        content = getIntent().getStringExtra("content");
    }

    private void initView() {
        findViewById(R.id.user_mail_notify_main).setOnClickListener(this);
        findViewById(R.id.floating_tip).setOnClickListener(this);
        findViewById(R.id.btn_ignore).setOnClickListener(this);
        findViewById(R.id.btn_reply).setOnClickListener(this);
    }

    private void setData() {
        if (simpleData == null || TextUtils.isEmpty(content)) {
            finish();
        } else {
            ImageView avatar = (ImageView) findViewById(R.id.user_img);
            if (TextUtils.isEmpty(simpleData.getAvatar())) {
                avatar.setImageResource(R.drawable.ic_launcher);
            } else {
                ImageLoader.loadAvatar(this, simpleData.getAvatar(), avatar);
            }

            TextView user_name = (TextView) findViewById(R.id.user_name);
            user_name.setText(TextUtils.isEmpty(simpleData.getNickname()) ? String.valueOf(simpleData.getUid()) :
                    simpleData.getNickname());

            TextView notify_content = (TextView) findViewById(R.id.user_content);
            notify_content.setText(Html.fromHtml(content));

            HideMainMessageForThreeSec();
        }
    }

    public void HideMainMessageForThreeSec() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        overridePendingTransition(R.anim.top_in, 0);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.top_out);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (BaseUtil.isOutOfBounds(this, event)) {
            if (event.getAction() == MotionEvent.ACTION_DOWN ||
                    event.getAction() == MotionEvent.ACTION_UP ||
                    event.getAction() == MotionEvent.ACTION_MOVE) {
                finish();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_mail_notify_main:
                if (iClick < 1) {
                    iClick++;
                    return;
                }
                finish();
                break;
            case R.id.btn_ignore:
                finish();
                break;
            case R.id.floating_tip:
            case R.id.btn_reply:
                finish();
                Invoker.getInstance().doInApp(null, "cmd_open_chat", "{\"uid\":" + simpleData.getUid() + ",\"nickname\":" + simpleData.getNickname() + "}");
                break;
        }
    }
}
