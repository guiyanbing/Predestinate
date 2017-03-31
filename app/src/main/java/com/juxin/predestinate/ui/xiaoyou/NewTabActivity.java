package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;

import java.util.ArrayList;

/**
 * 添加标签页面
 * Created by zm on 2017/3/24
 */
public class NewTabActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<SimpleFriendsList.SimpleFriendInfo> arrSimpleFriends;
    private long tab ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab = getIntent().getLongExtra("tab",-1);
        setContentView(R.layout.p1_xiaoyou_newtab_activity);
        initView();
    }

    private void initView() {
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.tab_group));
        setTitleRight("添加成员", this);
        if (tab == -1){

        }else {

        }
    }

    //设置右侧确定按钮的逻辑
    @Override
    public void onClick(View v) {
        UIShow.showSelectContactAct(this);
    }
}