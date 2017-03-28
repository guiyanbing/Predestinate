package com.juxin.predestinate.ui.user.fragment;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;

/**
 * 个人中心条目头部
 */
public class UserFragmentHeadPanel extends BaseViewPanel implements View.OnClickListener, ImgSelectUtil.OnChooseCompleteListener {

    private ImageView user_head, vip_status;
    private TextView user_nick, user_id, vip_end;
    private UserFragmentFunctionPanel functionPanel;

    private UserDetail myInfo;

    public UserFragmentHeadPanel(Context context) {
        super(context);
        setContentView(R.layout.p1_user_fragment_header);

        initView();
        refreshView();
    }

    private void initView() {
        user_head = (ImageView) findViewById(R.id.user_head);
        user_nick = (TextView) findViewById(R.id.user_nick);
        vip_status = (ImageView) findViewById(R.id.user_vip_status);
        user_id = (TextView) findViewById(R.id.user_id);
        vip_end = (TextView) findViewById(R.id.user_vip_end);

        //根据屏幕分辨率设置最大显示长度
        user_nick.setMaxEms(App.context.getResources().getDisplayMetrics().density <= 1.5 ? 5 : 7);

        user_head.setOnClickListener(this);
        user_nick.setOnClickListener(this);
        findViewById(R.id.ll_edit).setOnClickListener(this);

        LinearLayout function_container = (LinearLayout) findViewById(R.id.function_container);
        functionPanel = new UserFragmentFunctionPanel(getContext());
        function_container.addView(functionPanel.getContentView());
    }

    /**
     * 界面刷新
     */
    public void refreshView() {
        myInfo = ModuleMgr.getCenterMgr().getMyInfo();
        vip_status.setVisibility(myInfo.isVip() ? View.VISIBLE : View.GONE);
        user_id.setText("ID:" + myInfo.getUid());
        user_nick.setText(myInfo.getNickname());
        functionPanel.refreshView(myInfo);
    }

    /**
     * 角标刷新
     */
    public void refreshBadge() {
        functionPanel.refreshBadge();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head://上传头像
                ImgSelectUtil.getInstance().pickPhoto(getContext(), this);
                break;

            case R.id.ll_edit://跳转到个人信息页面
                break;
        }
    }

    //处理其他界面返回结果
    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == 101) {
            refreshView();
        }
    }

    @Override
    public void onComplete(final String... path) {
        if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
            return;
        }
        PLogger.d("path=== " + path[0]);
    }
}