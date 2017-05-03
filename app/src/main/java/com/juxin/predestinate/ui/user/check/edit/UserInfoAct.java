package com.juxin.predestinate.ui.user.check.edit;

import android.os.Bundle;
import android.widget.ListView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

/**
 * 个人资料查看列表页
 * Created by Su on 2017/3/28.
 */

public class UserInfoAct extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_selfinfo_act);
        setBackView(ModuleMgr.getCenterMgr().getMyInfo().getNickname());
        init();
    }

    private void init() {
        ListView infoList = (ListView) findViewById(R.id.list_info);
        new UserInfoPanel(this, infoList);
    }
}
