package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;

import java.util.ArrayList;

/**
 * 标签分组页面
 * Created by zm on 2017/3/24
 */
public class TabGroupActivity extends BaseActivity implements View.OnClickListener,CustomSearchView.OnTextChangedListener {

    private ArrayList<SimpleFriendsList.SimpleFriendInfo> arrSimpleFriends;
    private CustomSearchView mCustomSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_xiaoyou_tabgroup_activity);
        initView();
    }

    private void initView() {
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_tabgroup_csv_search);
        mCustomSearchView.setOnTextChangedListener(this);
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.tab_group));
        setTitleRight("新建标签", this);
    }

    private void changeTitleRight() {

    }

    //设置右侧确定按钮的逻辑
    @Override
    public void onClick(View v) {
        UIShow.showNewTabAct(this,-1);
    }

    @Override
    public void onTextChanged(CharSequence str) {
        if (TextUtils.isEmpty(str)){

        }else {
            mCustomSearchView.showNoData();
        }
    }
}