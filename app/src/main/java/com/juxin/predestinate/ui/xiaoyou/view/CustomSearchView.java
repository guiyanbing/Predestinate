package com.juxin.predestinate.ui.xiaoyou.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.request.HTCallBack;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.xiaoyou.adapter.FriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.BaseFriendInfo;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;

import java.text.Collator;
import java.util.Comparator;
import java.util.List;


/**
 * 标签详情页头部
 * Created by zm on 2016/8/23
 */
public class CustomSearchView extends LinearLayout implements RequestComplete {
    //相关控件
    private ClearEditText editText;
    private TextView txvNoFriend;
    private LinearLayout llSeach;
    private TextView txvTile;
    private RecyclerView mRecyclerView;
    private FriendsAdapter mFriendsAdapter;

    private List<FriendsList.FriendInfo> arrSearchList;//数据
    private HTCallBack mHTCallBack;
    private OnTextChangedListener mOnTextChangedListener;//监听

    private boolean isOpenList = true;//是否开启搜索列表

    public CustomSearchView(Context context) {
        this(context, null);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.p1_xiaoyou_search,this);
        this.setOrientation(VERTICAL);
        mFriendsAdapter = new FriendsAdapter();
        editText = (ClearEditText) findViewById(R.id.search_edt_search);
        txvNoFriend = (TextView) findViewById(R.id.search_txv_title_no_friends);
        llSeach = (LinearLayout) findViewById(R.id.search_title_layout);
        txvTile = (TextView) findViewById(R.id.search_txv_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_rl_list);
        initSearchView();
    }
    /**
     * 初始化搜索框
     */
    private void initSearchView() {
        editText.setOnSearchClickListener(new ClearEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {

            }
        });
        // 根据输入框输入值的改变来过滤搜索
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 这个时候不需要挤压效果 就把他隐藏掉
                txvNoFriend.setVisibility(View.GONE);
                llSeach.setVisibility(View.GONE);
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                //                filterData(s.toString());
                if (isOpenList == true && !TextUtils.isEmpty(s.toString())) {
                    reqSearch(s.toString());
                }
                if (mOnTextChangedListener != null) {
                    mOnTextChangedListener.onTextChanged(s);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    // 通讯社按中文拼音排序
    public class Mycomparator implements Comparator<BaseFriendInfo> {
        public int compare(BaseFriendInfo lhs, BaseFriendInfo rhs) {
            Comparator cmp = Collator.getInstance(java.util.Locale.ENGLISH);
            return cmp.compare(lhs.getSortKey(), rhs.getSortKey());
        }
    }

    public void showNoData(){
        txvNoFriend.setVisibility(View.VISIBLE);
        llSeach.setVisibility(View.GONE);
    }

    public void showTitleLayout(String title){
        txvNoFriend.setVisibility(View.GONE);
        llSeach.setVisibility(View.VISIBLE);
        txvTile.setText(title + "");
    }

    /**
     * 设置文字改变监听
     * @param onTextChangedListener
     */
    public void setOnTextChangedListener(OnTextChangedListener onTextChangedListener){
        this.mOnTextChangedListener = onTextChangedListener;
    }
    /**
     * 文字改变监听
     */
    public interface OnTextChangedListener{
        void onTextChanged(CharSequence str);
    }
    /**
     * 发送查询
     * @param str
     */
    public void reqSearch(String str){
        //网络请求
        if (mHTCallBack != null){
            mHTCallBack.cancel();//取消之前的网络请求
        }
        //建立新的请求
    }
    //搜索请求返回结果处理
    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()){//请求返回成功
            FriendsList lists = (FriendsList) response.getBaseData();
            List<FriendsList.FriendInfo> friendInfos = lists.getArr_frends();
            if (friendInfos != null && !friendInfos.isEmpty()) {
                mFriendsAdapter.setList(friendInfos);
            }else{
                showNoData();//显示没有搜索到该用户
            }
        }else {//请求失败
            MMToast.showShort("请求失败，请检查您的网络");
        }
    }
    //页面退出时调用
    public void onDestroy() {
        if (mHTCallBack != null){
            mHTCallBack.cancel();
        }
    }
}