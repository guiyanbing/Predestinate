package com.juxin.predestinate.ui.xiaoyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.xiaoyou.adapter.SelectFriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * Created by zm on 2017/3/24
 */
public class SelectContactActivity extends BaseActivity implements View.OnClickListener,CustomSearchView.OnTextChangedListener,RequestComplete,SelectFriendsAdapter.OnContactSelect {

    private ArrayList<SimpleFriendsList.SimpleFriendInfo> arrSimpleFriends = new ArrayList<>();
    private CustomRecyclerView crlvList;
    private RecyclerView lvFriends;
    private CustomSearchView mCustomSearchView;
    private SelectFriendsAdapter mSelectFriendsAdapter;
    private long tab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_xiaoyou_select_contact_activity);
        tab = getIntent().getLongExtra("tab",-1);
        initView();
        crlvList.showLoading();
        reqFriendlist();
    }

    private void initView() {
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_sele_csv_search);
        mCustomSearchView.setOnTextChangedListener(this);
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.contact));
        setTitleRight(getString(R.string.ok), this);
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_sele_crlv_list);
        lvFriends = crlvList.getRecyclerView();
        lvFriends.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        lvFriends.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mSelectFriendsAdapter = new SelectFriendsAdapter();
        mSelectFriendsAdapter.setOnContactSelectLinear(this);
        lvFriends.setAdapter(mSelectFriendsAdapter);
    }

    private void reqFriendlist(){
        if (FriendsUtils.arr_uids.size()==0){
            ModuleMgr.getCommonMgr().getFriendList(this);
        }else {
            arrSimpleFriends.addAll(FriendsUtils.arr_uids);
        }
    }

    private void changeTitleRight(int num) {
        if (num>0){
            setTitleRight(getString(R.string.ok)+"("+num+")", this);
        }else {
            setTitleRight(getString(R.string.ok), this);
        }
    }

    //设置右侧确定按钮的逻辑
    @Override
    public void onClick(View v) {
        if (mSelectFriendsAdapter.getUids().size() >0 ){
            Set<String> set = new HashSet<>();
            for (SimpleFriendsList.SimpleFriendInfo info:mSelectFriendsAdapter.getUids()){
                set.add(info.getUid()+"");
            }
            ModuleMgr.getCommonMgr().addTagGroupMember(tab, set, this);
        }
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
    public void onRequestComplete(HttpResponse response) {
//        Log.e("TTTTTTTTTTselectContact", response.getResponseString() + "|||");
        if (response.getUrlParam() == UrlParam.reqAddTagGroupMember){
            if (response.isOk()){//请求返回成功
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                ArrayList<SimpleFriendsList.SimpleFriendInfo> list = new ArrayList();
                list.addAll(mSelectFriendsAdapter.getUids());
                bundle.putParcelableArrayList("infos", list);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                SelectContactActivity.this.finish();
                SelectContactActivity.this.finish();
             }else {
                MMToast.showShort("添加失败，请重试");
            }
        }else {
            crlvList.setVisibility(View.VISIBLE);
            if (response.isOk()){//请求返回成功
                SimpleFriendsList lists = (SimpleFriendsList) response.getBaseData();
                List<SimpleFriendsList.SimpleFriendInfo> friendInfos = lists.getArr_frends();
                arrSimpleFriends.addAll(friendInfos);
                testData();
                if (arrSimpleFriends.size() > 0){
                    mSelectFriendsAdapter.setList(arrSimpleFriends);
                    crlvList.showRecyclerView();
                    crlvList.setVisibility(View.GONE);
                    mCustomSearchView.setAdapter(mSelectFriendsAdapter);
                    mCustomSearchView.setListVisibility(View.VISIBLE);
                }else {
                    crlvList.showNoData();
                }
//                Log.e("TTTTTTTTTTselectContact", response.getResponseString() + "||"+arrSimpleFriends.size());
            }else {//请求失败
                mCustomSearchView.setListVisibility(View.GONE);
                crlvList.showNetError("网络异常，点击刷新", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reqFriendlist();
                    }
                });
                //            MMToast.showShort("请求失败，请检查您的网络");
            }
        }
    }

    @Override
    public void onSelectChange(Set<SimpleFriendsList.SimpleFriendInfo> list) {
        changeTitleRight(list.size());
    }

    //测试
    private void testData(){
        if (arrSimpleFriends == null){
            arrSimpleFriends = new ArrayList<>();
        }
//        UserInfoLightweight infoLightweight = new UserInfoLightweight();
//        infoLightweight.setAlias("小小");
//        infoLightweight.setTime(100000);
//        infoLightweight.setAvatar("地址");
//        infoLightweight.setNickname("测试");
        for (int i = 0 ;i < 10;i++){
            SimpleFriendsList.SimpleFriendInfo info = new SimpleFriendsList.SimpleFriendInfo();
            info.setNickname("测试"+i);
//            info.setUserInfoLightweight(infoLightweight);
            arrSimpleFriends.add(info);
            SimpleFriendsList.SimpleFriendInfo info1 = new SimpleFriendsList.SimpleFriendInfo();
            info1.setNickname("你好"+i);
//            info1.setUserInfoLightweight(infoLightweight);
            arrSimpleFriends.add(info1);
            SimpleFriendsList.SimpleFriendInfo info2 = new SimpleFriendsList.SimpleFriendInfo();
            info2.setNickname("不是"+i);
//            info2.setUserInfoLightweight(infoLightweight);
            arrSimpleFriends.add(info2);
            SimpleFriendsList.SimpleFriendInfo info3 = new SimpleFriendsList.SimpleFriendInfo();
            info3.setNickname("wwww"+i);
//            info3.setUserInfoLightweight(infoLightweight);
            arrSimpleFriends.add(info3);
            SimpleFriendsList.SimpleFriendInfo info4 = new SimpleFriendsList.SimpleFriendInfo();
            info4.setNickname("我们"+i);
//            info4.setUserInfoLightweight(infoLightweight);
            arrSimpleFriends.add(info4);
        }
        //        mFriendsAdapter.setList(arrSearchList);
    }
}