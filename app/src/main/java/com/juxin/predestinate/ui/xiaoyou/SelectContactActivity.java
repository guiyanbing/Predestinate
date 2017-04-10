package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;

import java.util.ArrayList;

/**
 *
 * Created by zm on 2017/3/24
 */
public class SelectContactActivity extends BaseActivity implements View.OnClickListener,CustomSearchView.OnTextChangedListener {

    private ArrayList<SimpleFriendsList.SimpleFriendInfo> arrSimpleFriends;
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
        setTitle(getString(R.string.contact));
        setTitleRight(getString(R.string.ok), this);
        lvFriends = (RecyclerView) findViewById(R.id.xiaoyou_sele_lv_list);
    }

    private void changeTitleRight() {
        if (arrSimpleFriends != null){
            if (arrSimpleFriends.size()>0){
                setTitleRight(getString(R.string.ok)+"("+arrSimpleFriends.size()+")", this);
            }else {
                setTitleRight(getString(R.string.ok), this);
            }
        }
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