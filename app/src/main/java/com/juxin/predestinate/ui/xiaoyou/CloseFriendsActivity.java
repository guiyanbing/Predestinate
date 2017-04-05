package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.adapter.CloseFriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;

import java.util.ArrayList;

/**
 * 亲密好友页面
 * Created by zm on 2017/3/24
 */
public class CloseFriendsActivity extends BaseActivity implements View.OnClickListener,CustomSearchView.OnTextChangedListener {

    //控件
    //控件
    private CustomRecyclerView crlvList;
    private XRecyclerView lvList;
    private CustomSearchView mCustomSearchView;//自定义的搜索控件

    private ArrayList<LabelsList.LabelInfo> arrLabelinfos;
    private CloseFriendsAdapter mCloseFriendsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_xiaoyou_close_friends_activity);
        initView();
    }

    private void initView() {
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.good_friend));
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_close_friends_lv_list);
        lvList = crlvList.getXRecyclerView();
        lvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_close_friends_csv_search);
        mCustomSearchView.setOnTextChangedListener(this);
        mCloseFriendsAdapter = new CloseFriendsAdapter();

        //测试
        testData();

        lvList.setAdapter(mCloseFriendsAdapter);
        initListener();
    }
    private void initListener(){
        mCloseFriendsAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                LabelsList.LabelInfo info = mCloseFriendsAdapter.getItem(position);
                UIShow.showNewTabAct(CloseFriendsActivity.this,info.getId());
            }
        });
    }

    //设置右侧确定按钮的逻辑
    @Override
    public void onClick(View v) {

    }

    //测试
    private void testData(){
        arrLabelinfos = new ArrayList<>();
        for (int i = 0 ;i < 10;i++){
            LabelsList.LabelInfo info = new LabelsList.LabelInfo();
            info.setId(i);
            info.setLabelName("标签" + i);
            info.setNum(i+i);
            arrLabelinfos.add(info);
        }
        mCloseFriendsAdapter.setList(arrLabelinfos);
    }
    @Override
    public void onTextChanged(CharSequence str) {
        if (TextUtils.isEmpty(str)){
            lvList.setVisibility(View.VISIBLE);
        }else {
            lvList.setVisibility(View.GONE);
            mCustomSearchView.showNoData();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCustomSearchView.onDestroy();
    }
}