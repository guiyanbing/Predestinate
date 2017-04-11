package com.juxin.predestinate.ui.mail.seen;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

/**
 * 谁看过我
 * Created by Kind on 2017/4/10.
 */

public class ReadMeAct extends BaseActivity {

    private XRecyclerView recyclerView;
    private CustomRecyclerView customRecyclerView;
    private ReadMeAdapter readMeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_mail_readmeact);

        initView();
    }

    private void initView(){
        customRecyclerView = (CustomRecyclerView) findViewById(R.id.customRecyclerView);
        recyclerView = customRecyclerView.getXRecyclerView();
        readMeAdapter = new ReadMeAdapter();
        recyclerView.setAdapter(readMeAdapter);

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
