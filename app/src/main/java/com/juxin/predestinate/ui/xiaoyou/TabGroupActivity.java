package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.adapter.IntimacyFriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签分组页面
 * Created by zm on 2017/3/24
 */
public class TabGroupActivity extends BaseActivity implements View.OnClickListener,CustomSearchView.OnTextChangedListener,RequestComplete,XRecyclerView.LoadingListener {
    //控件
    private CustomRecyclerView crlvList;
    private XRecyclerView lvList;
    private CustomSearchView mCustomSearchView;

    public static ArrayList<LabelsList.LabelInfo> arrLabes = new ArrayList<>();
    private IntimacyFriendsAdapter mIntimacyFriendsAdapter;
    private int page = 0;//当前页
    private int pageLimits = 20;//一页的条数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_xiaoyou_tabgroup_activity);
        initView();
        ModuleMgr.getCommonMgr().getTagGroupList(this);
    }

    private void initView() {
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_tabgroup_crlv_list);
        lvList = crlvList.getXRecyclerView();
        lvList.setLoadingListener(this);
        lvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_tabgroup_csv_search);
        mCustomSearchView.setOnTextChangedListener(this);
        mIntimacyFriendsAdapter = new IntimacyFriendsAdapter();
        lvList.setAdapter(mIntimacyFriendsAdapter);
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.tab_group));
        setTitleRight("新建标签", this);
        initListener();
    }
    private void initListener(){
        mIntimacyFriendsAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                LabelsList.LabelInfo info = arrLabes.get(position);
//                Log.e("TTTTTTTTT",info.getId()+"||"+info.getLabelName());
                UIShow.showNewTabAct(TabGroupActivity.this,info.getId());
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCustomSearchView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mIntimacyFriendsAdapter.setList(arrLabes);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
//        Log.e("TTTTTTTTTTTTTTh",response.getResponseString()+"||");
        if (response.isOk()){//请求返回成功
            LabelsList lists = (LabelsList) response.getBaseData();
            List<LabelsList.LabelInfo> labelInfos = lists.getArr_labels();
            if (page == 0){
                arrLabes.clear();
            }
            arrLabes.addAll(labelInfos);

            //// TODO: 2017/4/12 用于测试
            testData();
            lvList.setLoadingMoreEnabled(arrLabes.size() >= pageLimits ? true:false);

            if (labelInfos != null && !labelInfos.isEmpty()) {
                lvList.setLoadingMoreEnabled(labelInfos.size() >= pageLimits ? true:false);
                mIntimacyFriendsAdapter.setList(arrLabes);
                crlvList.showXrecyclerView();
            }else{
                crlvList.showNoData();
            }
        }else {//请求失败
            crlvList.showNetError();
            MMToast.showShort("请求失败，请检查您的网络");
        }

        //测试使用
        crlvList.showXrecyclerView();
    }

    @Override
    public void onRefresh() {
        page = 0;
    }

    @Override
    public void onLoadMore() {
        page++;
    }


    //测试
    private void testData(){
//        if (arrLabes == null){
//            arrLabes = new ArrayList<>();
//        }
//        for (int i = 0;i < 10 ;i++){
//            LabelsList.LabelInfo info = new LabelsList.LabelInfo();
//            info.setLabelName("测试");
//            info.setNum(i);
//            arrLabes.add(info);
//        }
//        mIntimacyFriendsAdapter.setList(arrLabes);
    }
}