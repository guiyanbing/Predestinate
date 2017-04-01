package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.adapter.CloseFriendsDdetailAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;

import java.util.ArrayList;

/**
 * 添加标签页面
 * Created by zm on 2017/3/24
 */
public class NewTabActivity extends BaseActivity implements View.OnClickListener {

    //控件
    private RecyclerView rvList;
    //其他
    private ArrayList<FriendsList.FriendInfo> arrFriendinfos;
    private CloseFriendsDdetailAdapter mCloseFriendsDdetailAdapter;
    private long tab ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab = getIntent().getLongExtra("tab",-1);
        setContentView(R.layout.p1_xiaoyou_newtab_activity);
        initView();
    }

    private void initView() {
        rvList = (RecyclerView) findViewById(R.id.xiaoyou_newtab_rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
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