package com.juxin.predestinate.ui.user.check.edit.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

/**
 * 个人资料页
 * Created by Su on 2017/5/3.
 */

public class UserEditInfoAct extends BaseActivity implements PObserver{
    private UserEditInfoHeadPanel headPanel;
    private UserEditBaseInfoPanel basePanel;
    private UserEditDetailInfoPanel detailPanel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_user_edit_info_layout);

        initTitle();
        initPanel();
    }

    private void initTitle() {
        findViewById(R.id.cut_line).setVisibility(View.GONE);
        setBackView(ModuleMgr.getCenterMgr().getMyInfo().getNickname());
    }

    private void initPanel() {
        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        headPanel = new UserEditInfoHeadPanel(this);
        basePanel = new UserEditBaseInfoPanel(this);
        detailPanel = new UserEditDetailInfoPanel(this);

        container.addView(headPanel.getContentView());
        container.addView(basePanel.getContentView());
        container.addView(detailPanel.getContentView());

        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_MyInfo_Change:
                basePanel.refreshView();
                detailPanel.refreshView();
                break;
        }
    }
}
