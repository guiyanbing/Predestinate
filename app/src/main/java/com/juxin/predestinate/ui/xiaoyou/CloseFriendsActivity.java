package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
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
    private ListView lvList;
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
        lvList = (ListView) findViewById(R.id.xiaoyou_close_friends_lv_list);
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_close_friends_csv_search);
        mCustomSearchView.setOnTextChangedListener(this);
        mCloseFriendsAdapter = new CloseFriendsAdapter(this,arrLabelinfos);

        //测试
        testData();

        lvList.setAdapter(mCloseFriendsAdapter);
    }
    private void initlistener(){
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
//        Log.e("TTTTTTTTTT", str + "|||");
//        if (TextUtils.isEmpty(str)){
//            addDataBack();
//            mFriendsAdapter.setList(arrDatas);
//        }else {
//            //查询操作
//            mCustomSearchView.showNoData();
//            arrDatas.clear();
//            mFriendsAdapter.setList(arrDatas);
//        }
    }
}