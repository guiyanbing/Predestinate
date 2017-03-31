package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.xiaoyou.adapter.FriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;

import java.util.ArrayList;

/**
 * 小友
 * Created by Kind on 2017/3/20.
 */

public class XiaoyouFragment extends BaseFragment implements CustomSearchView.OnTextChangedListener{

    //数据相关
    private ArrayList<FriendsList.FriendInfo> arrDatas;
    private ArrayList<FriendsList.FriendInfo> arrHead;//头部列表
    private ArrayList<FriendsList.FriendInfo> arrFriends;//互动列表
    //控件
    private ListView lvList;
    private CustomSearchView mCustomSearchView;//自定义的搜索控件
    private FriendsAdapter mFriendsAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.xiaoyou_fragment);
        initView();
        return getContentView();
    }

    private void initView(){
        setTitle("小友");
        lvList = (ListView) findViewById(R.id.xiaoyou_frag_lv_list);
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_frag_csv_search);
        //        mCustomSearchView = new CustomSearchView(getContext());
//        lvList.addHeaderView(mCustomSearchView);
        mCustomSearchView.setOnTextChangedListener(this);
        initDataHead();
        arrDatas = new ArrayList<>();
        arrDatas.addAll(arrHead);
        mFriendsAdapter = new FriendsAdapter(getContext(),arrDatas);
        mFriendsAdapter.setList(arrDatas);
        lvList.setAdapter(mFriendsAdapter);
        initListener();
    }

    private void initListener(){
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int handledPosition = position - lvList.getHeaderViewsCount();
                FriendsList.FriendInfo info = mFriendsAdapter.getItem(handledPosition);
                if (info != null && info.getType() == 1){
                    if (handledPosition == 0){
                        UIShow.showTabGroupAct(getActivity());
                    }else if (handledPosition == 1){
                        UIShow.showCloseFriendsAct(getActivity());
                    }
                }else if (info != null && info.getType() == 0){
                    //
                }
            }
        });
    }
    //初始化头部列表
    private void initDataHead(){
        arrHead = new ArrayList<>();
        String[] strName = getResources().getStringArray(R.array.friend_list_Label);
        String[] des = getResources().getStringArray(R.array.friend_list_describe);
        int[] icons = new int[]{R.drawable.p1_xiaoyou_xyicon01,R.drawable.p1_xiaoyou_xyicon02};
        for (int i = 0;i < strName.length && i < icons.length && i < des.length;i++){
            FriendsList.FriendInfo info = new FriendsList.FriendInfo();
            info.setType(1);
            info.setNickname(strName[i]);
            info.setDescribe(des[i]);
            info.setIcon(icons[i]);
            arrHead.add(info);
        }
    }
    //恢复搜索前的数据
    private void addDataBack(){
        if (arrDatas == null){
            arrDatas = new ArrayList<>();
        }
        arrDatas.clear();
        arrDatas.addAll(arrHead);
        if (arrFriends != null){
            arrDatas.addAll(arrFriends);
        }
    }

    @Override
    public void onTextChanged(CharSequence str) {
        Log.e("TTTTTTTTTT",str+"|||");
        if (TextUtils.isEmpty(str)){
            addDataBack();
            mFriendsAdapter.setList(arrDatas);
        }else {
            //查询操作
            mCustomSearchView.showNoData();
            arrDatas.clear();
            mFriendsAdapter.setList(arrDatas);
        }
    }
}