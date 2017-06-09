package com.juxin.predestinate.ui.user.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.ui.utils.CheckIntervalTimeUtil;

/**
 * 我的
 * Created by Kind on 2017/3/20.
 */

public class UserFragment extends BaseFragment implements PObserver {
    private UserFragmentHeadPanel headPanel;
    private UserFragmentFootPanel footPanel;
    private CheckIntervalTimeUtil checkIntervalTimeUtil;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.user_fragment);
        setTitle(getResources().getString(R.string.user_center_fragment));

        initView();
        return getContentView();
    }

    private void initView() {
        checkIntervalTimeUtil = new CheckIntervalTimeUtil();
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        headPanel = new UserFragmentHeadPanel(getActivity());
        footPanel = new UserFragmentFootPanel(getActivity());

        container.addView(headPanel.getContentView());
        container.addView(footPanel.getContentView());

        MsgMgr.getInstance().attach(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (headPanel != null && footPanel != null) {
            headPanel.refreshView();
            footPanel.refreshView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MsgMgr.getInstance().detach(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        headPanel.onActivityResult(requestCode, resultCode);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (checkIntervalTimeUtil.check(60 * 1000) || (App.uid > 0 && ModuleMgr.getCenterMgr().getMyInfo().getUid() <= 0)) {
                ModuleMgr.getCenterMgr().reqMyInfo();
            }
        }
    }


    @Override
    public void onMessage(String key, Object value) {
        switch (key) {
            case MsgType.MT_MyInfo_Change:
                headPanel.refreshView();
                footPanel.refreshView();
                break;
        }
    }
}
