package com.juxin.predestinate.module.logic.notify.view;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.notify.FloatingMgr;

/**
 * 私聊信息提示panel
 */
public class CustomFloatingPanel extends FloatingBasePanel {

    private ImageView header;
    private TextView userName, userContent, btnIgnore, btnReply;
    private View main_btn, reply_content;

    public CustomFloatingPanel(Context context) {
        super(context);
        setContentView(R.layout.common_floating_layout);
    }

    @Override
    public void initView() {
        header = (ImageView) findViewById(R.id.user_img);
        main_btn = findViewById(R.id.main_btn);
        userName = (TextView) findViewById(R.id.user_name);
        userContent = (TextView) findViewById(R.id.user_content);

        reply_content = findViewById(R.id.reply_content);
        btnIgnore = (TextView) findViewById(R.id.btn_ignore);
        btnReply = (TextView) findViewById(R.id.btn_reply);

        btnIgnore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingMgr.getInstance().removePanel(CustomFloatingPanel.this);
            }
        });
    }

    /**
     * 双按钮样式【忽略 回复】数据初始化
     *
     * @param title        标题，用户名
     * @param content      显示内容
     * @param img          用户头像
     * @param btn1         左按钮显示文字
     * @param btn1listener 左按钮点击事件
     * @param btn2         右按钮显示文字
     * @param btn2listener 右按钮点击事件
     */
    public void init(String title, String content, String img, String btn1,
                     OnClickListener btn1listener, String btn2, OnClickListener btn2listener) {
        reply_content.setVisibility(View.VISIBLE);

        ImageLoader.loadAvatar(getContext(), img, header);
        userName.setText(title);
        userContent.setText(Html.fromHtml(content));
        if (!TextUtils.isEmpty(btn1)) {
            btnIgnore.setText(btn1);
            btnIgnore.setOnClickListener(btn1listener);
        } else {
            btnIgnore.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(btn2)) {
            btnReply.setText(btn2);
            btnReply.setOnClickListener(btn2listener);
        } else {
            btnReply.setVisibility(View.GONE);
        }
        main_btn.setOnClickListener(btn2listener);
    }

    /**
     * 无按钮样式 数据初始化
     *
     * @param title    标题，用户名
     * @param content  显示内容
     * @param img      用户头像
     * @param listener 联系她点击事件
     */
    public void init(String title, String content, String img, OnClickListener listener) {
        reply_content.setVisibility(View.GONE);

        ImageLoader.loadAvatar(getContext(), img, header);
        userName.setText(title);
        userContent.setText(Html.fromHtml(content));
        main_btn.setOnClickListener(listener);
    }

    @Override
    public void reset() {
    }
}
