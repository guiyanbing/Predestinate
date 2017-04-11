package com.juxin.predestinate.ui.xiaoyou.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.request.HTCallBack;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.pinyin.Pinyin;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.adapter.FriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.BaseFriendInfo;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
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
    /**
     * 上次第一个可见元素，用于滚动时记录标识。
     */
    private int lastFirstVisibleItem = -1;

    public CustomSearchView(Context context) {
        this(context, null);
    }

    public CustomSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        inflate(context, R.layout.p1_xiaoyou_search, this);
        this.setOrientation(VERTICAL);
        mFriendsAdapter = new FriendsAdapter();

        testData();
        toPinYin();

        editText = (ClearEditText) findViewById(R.id.search_edt_search);
        txvNoFriend = (TextView) findViewById(R.id.search_txv_title_no_friends);
        llSeach = (LinearLayout) findViewById(R.id.search_title_layout);
        txvTile = (TextView) findViewById(R.id.search_txv_title);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_rl_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mRecyclerView.setAdapter(mFriendsAdapter);
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
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                //判断是当前layoutManager是否为LinearLayoutManager
                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    //获取最后一个可见view的位置
//                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
                    //获取第一个可见view的位置
                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    Log.e("TTTTTTTTTTTT", "dx=" + dx + ";dy=" + dy + "||||" + mRecyclerView.getTop() + "|||" + firstItemPosition);

                    int section = getSectionForPosition(firstItemPosition);
                    int nextSection = getSectionForPosition(firstItemPosition + 1);
                    int nextSecPosition = getPositionForSection(+nextSection);

                    if (firstItemPosition != lastFirstVisibleItem) {
                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) llSeach.getLayoutParams();
                        params.topMargin = 0;
                        llSeach.setLayoutParams(params);
                        if (arrSearchList.size() == 1) {
                            llSeach.setVisibility(View.GONE);
                        }

                        if (getPositionForSection(section) != -1) {
                            showTitleLayout(arrSearchList.size() == 0 ? "A" : arrSearchList.get(getPositionForSection(section)).getSortKey());
                        }
                    }
                    if (nextSecPosition == firstItemPosition + 1) {
                        View childView = recyclerView.getChildAt(0);
                        if (childView != null) {
                            int titleHeight = llSeach.getHeight();
                            int bottom = childView.getBottom();
                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) llSeach.getLayoutParams();
                            if (bottom < titleHeight) {
                                float pushedDistance = bottom - titleHeight;
                                params.topMargin = (int) pushedDistance;
                                llSeach.setLayoutParams(params);
                            } else {
                                if (params.topMargin != 0) {
                                    params.topMargin = 0;
                                    llSeach.setLayoutParams(params);
                                }
                            }
                        }
                    }
                    lastFirstVisibleItem = firstItemPosition;
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
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

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        if ((arrSearchList.size() - 1) > position && arrSearchList.get(position).getSortKey().length() != 0)
            return arrSearchList.get(position).getSortKey().charAt(0);
        else
        return 0;
    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < arrSearchList.size(); i++) {
            String sortStr = arrSearchList.get(i).getSortKey();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    //测试
    private void testData(){
        arrSearchList = new ArrayList<>();
        for (int i = 0 ;i < 10;i++){
            FriendsList.FriendInfo info = new FriendsList.FriendInfo();
            info.setNickname("测试"+i);
            arrSearchList.add(info);
            FriendsList.FriendInfo info1 = new FriendsList.FriendInfo();
            info1.setNickname("你好"+i);
            arrSearchList.add(info1);
            FriendsList.FriendInfo info2 = new FriendsList.FriendInfo();
            info2.setNickname("不是"+i);
            arrSearchList.add(info2);
        }
        mFriendsAdapter.setList(arrSearchList);
    }

    public void toPinYin(){
        for (int i = 0 ;i<arrSearchList.size();i++){
            String contactSort = "";
            // 汉字转换成拼音
            String pinyin = Pinyin.toPinyin(arrSearchList.get(i).getNickname().toCharArray()[0]);
            //                Log.d("pinyin_phone", "name = " + contactName.toCharArray()[0] + "    pinyin = " + pinyin);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                contactSort = sortString.toUpperCase();
            } else {
                contactSort = "#";
            }
            arrSearchList.get(i).setSortKey(contactSort);
        }

        // 根据a-z进行排序
        Comparator comp = new Mycomparator();
        Collections.sort(arrSearchList, comp);

    }

    //页面退出时调用
    public void onDestroy() {
        if (mHTCallBack != null){
            mHTCallBack.cancel();
        }
    }
}