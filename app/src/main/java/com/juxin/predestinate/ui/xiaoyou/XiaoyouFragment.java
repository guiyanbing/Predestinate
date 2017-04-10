package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.adapter.FriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;
import com.juxin.predestinate.ui.xiaoyou.zanshi.ClosenessUpgradeDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 小友
 * Created by Kind on 2017/3/20.
 */

public class XiaoyouFragment extends BaseFragment implements CustomSearchView.OnTextChangedListener,RequestComplete,XRecyclerView.LoadingListener {

    //数据相关
    private ArrayList<FriendsList.FriendInfo> arrDatas;
    private ArrayList<FriendsList.FriendInfo> arrHead;//头部列表
    private ArrayList<FriendsList.FriendInfo> arrFriends;//互动列表
    //控件
    private CustomRecyclerView crlvList;
    private XRecyclerView lvList;
    private CustomSearchView mCustomSearchView;//自定义的搜索控件
    private FriendsAdapter mFriendsAdapter;
    private int page = 0;//当前页
    private int pageLimits = 20;//一页的条数
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
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_frag_crlv_list);
        lvList = crlvList.getXRecyclerView();
        lvList.setLoadingListener(this);
        lvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        lvList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_frag_csv_search);
        //        mCustomSearchView = new CustomSearchView(getContext());
//        lvList.addHeaderView(mCustomSearchView);
        mCustomSearchView.setOnTextChangedListener(this);
        initDataHead();
        arrDatas = new ArrayList<>();
        arrDatas.addAll(arrHead);
        mFriendsAdapter = new FriendsAdapter();
        mFriendsAdapter.setList(arrDatas);
        lvList.setAdapter(mFriendsAdapter);
        initListener();
    }

    private void initListener(){
        mFriendsAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                //                int handledPosition = position - lvList.getHeaderViewsCount();
                FriendsList.FriendInfo info = mFriendsAdapter.getItem(position);
                if (info != null && info.getType() == 1){
                    if (position == 0){
                        UIShow.showTabGroupAct(getActivity());
//                        RedPacketDialog dialog = new RedPacketDialog();
//                        dialog.showDialog(getActivity());

                        //测试
                        ClosenessUpgradeDialog dialog = new ClosenessUpgradeDialog();
                        dialog.showDialog(getActivity());
                    }else if (position == 1){
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
        crlvList.showXrecyclerView();
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

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()){//请求返回成功
            FriendsList lists = (FriendsList) response.getBaseData();
            List<FriendsList.FriendInfo> friendInfos = lists.getArr_frends();
            if (page == 0){
                arrFriends.clear();
            }
            arrFriends.addAll(friendInfos);
            if (friendInfos != null && !friendInfos.isEmpty()) {
                lvList.setLoadingMoreEnabled(friendInfos.size() >= pageLimits ? true:false);
                mFriendsAdapter.setList(arrFriends);
                crlvList.showXrecyclerView();
            }else{
                crlvList.showNoData();
            }
        }else {//请求失败
            crlvList.showNetError();
            MMToast.showShort("请求失败，请检查您的网络");
        }
    }

    @Override
    public void onRefresh() {//刷新
        page = 0;
    }

    @Override
    public void onLoadMore() {//加载更多
        page++;
    }
}