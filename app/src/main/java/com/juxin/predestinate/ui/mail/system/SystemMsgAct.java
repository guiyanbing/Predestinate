package com.juxin.predestinate.ui.mail.system;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.mail.seen.ReadMeAdapter;

/**
 * 系统消息
 * Created by Kind on 2017/4/11.
 */

public class SystemMsgAct extends BaseActivity {

    private XRecyclerView recyclerView;
    private CustomRecyclerView recyclerholder;
    private SystemMsgAdapter systemMsgAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_mail_systemmsgact);

        initView();
    }

    private void initView(){
        recyclerholder = (CustomRecyclerView) findViewById(R.id.sys_customRecyclerView);
        recyclerView = recyclerholder.getXRecyclerView();
        systemMsgAdapter = new SystemMsgAdapter();
        recyclerView.setAdapter(systemMsgAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLoadingMoreEnabled(false);
        recyclerView.setPullRefreshEnabled(false);

        Drawable dividerDrawable = ContextCompat.getDrawable(this, R.drawable.common_divider_sample);
        recyclerView.addItemDecoration(recyclerView.new DividerItemDecoration(dividerDrawable));

        //   readMeAdapter.setList();
    }
}
