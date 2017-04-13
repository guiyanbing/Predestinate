package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;

import java.util.ArrayList;

/**
 *
 * Created by zm on 2017/3/24
 */
public class AllFriendsActivity extends BaseActivity implements View.OnClickListener,CustomSearchView.OnTextChangedListener {

    private ArrayList<SimpleFriendsList.SimpleFriendInfo> arrSimpleFriends;
    private CustomRecyclerView crlvList;
    private RecyclerView lvFriends;
    private CustomSearchView mCustomSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_xiaoyou_select_contact_activity);
        initView();
    }

    private void initView() {
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_sele_csv_search);
        mCustomSearchView.setOnTextChangedListener(this);
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.all_friends));
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_sele_crlv_list);
        lvFriends = crlvList.getRecyclerView();
        lvFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvFriends.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
//        mSelectFriendsAdapter = new SelectFriendsAdapter();
//        lvFriends.setAdapter(mSelectFriendsAdapter);
//        lvFriends = (RecyclerView) findViewById(R.id.xiaoyou_sele_lv_list);
    }

    private void changeTitleRight() {

    }

    //设置右侧确定按钮的逻辑
    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTextChanged(CharSequence str) {
        if (TextUtils.isEmpty(str)){

        }else {
            mCustomSearchView.showNoData();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCustomSearchView.onDestroy();
    }
}