package com.juxin.predestinate.ui.user.check;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.juxin.mumu.bean.message.Msg;
import com.juxin.mumu.bean.message.MsgMgr;
import com.juxin.mumu.bean.message.MsgType;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;


/**
 * 查看用户资料详情
 * Created by Su on 2016/5/30.
 */
public class UserCheckInfoAct extends BaseActivity implements View.OnClickListener, MsgMgr.IObserver {
    private LinearLayout container;

    private int channel;
    private UserDetail userProfile;
    private boolean isState = true;// 检测打招呼状态是否改变
    private boolean isBeingGreeted = true;// 是否正在打招呼

    /**********
     * panel
     ***********/
    private UserCheckInfoHeadPanel headPanel;
    private UserCheckInfoFootPanel footPanel;
    private RelativeLayout userinfo_message, add_friend;
    private View userinfo_sayhello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_info_layout);

        initData();
        initView();
        initPanel();

    }

    private void initData() {
        userProfile = ModuleMgr.getCenterMgr().getMyInfo();

    }

    private void initView() {
        initTitle();
        container = (LinearLayout) findViewById(R.id.container);

    }

    private void initTitle() {
        setTitleBackground(R.color.transparent);
        setTitleLeftImg(R.drawable.p1_back_white_btn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void initPanel() {
        headPanel = new UserCheckInfoHeadPanel(this, channel, userProfile);
        container.addView(headPanel.getContentView());

        footPanel = new UserCheckInfoFootPanel(this, channel, userProfile);
        footPanel.setSlideIgnoreView(this);
        container.addView(footPanel.getContentView());
    }

    private void initBottom() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onMessage(MsgType msgType, Msg msg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void finish() {
        super.finish();
    }
}
