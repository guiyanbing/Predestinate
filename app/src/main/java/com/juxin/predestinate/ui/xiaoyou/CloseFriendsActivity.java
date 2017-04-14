package com.juxin.predestinate.ui.xiaoyou;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.adapter.CloseFriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.IntimacyList;
import com.juxin.predestinate.ui.xiaoyou.view.CustomSearchView;

import java.util.ArrayList;

/**
 * 亲密好友页面
 * Created by zm on 2017/3/24
 */
public class CloseFriendsActivity extends BaseActivity implements View.OnClickListener,CustomSearchView.OnTextChangedListener {

    //控件
    private CustomRecyclerView crlvList;
    private RecyclerView lvList;
    private CustomSearchView mCustomSearchView;//自定义的搜索控件

    private ArrayList<IntimacyList.IntimacyInfo> arrLabelinfos;
    private CloseFriendsAdapter mCloseFriendsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_xiaoyou_close_friends_activity);
        arrLabelinfos = FriendsUtils.intimacyInfos;
        Log.e("TTTTTTTclose",arrLabelinfos.size()+"||");
        initView();
    }

    private void initView() {
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.good_friend));
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_close_friends_lv_list);
        lvList = crlvList.getRecyclerView();
        lvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        lvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mCustomSearchView = (CustomSearchView) findViewById(R.id.xiaoyou_close_friends_csv_search);
        mCustomSearchView.setOnTextChangedListener(this);
        mCloseFriendsAdapter = new CloseFriendsAdapter();

        //测试
//        testData();

        lvList.setAdapter(mCloseFriendsAdapter);
        crlvList.showRecyclerView();
        mCloseFriendsAdapter.setList(arrLabelinfos);
        initListener();
    }
    private void initListener(){
        mCloseFriendsAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                IntimacyList.IntimacyInfo info = mCloseFriendsAdapter.getItem(position);
                // TODO: 2017/4/13 待处理
                UIShow.showIntimacyDetailAct(CloseFriendsActivity.this,position);
            }
        });
//        lvList.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                //判断是当前layoutManager是否为LinearLayoutManager
//                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//                if (layoutManager instanceof LinearLayoutManager) {
//                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//                    //获取最后一个可见view的位置
//                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
//                    //获取第一个可见view的位置
//                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    Log.e("TTTTTTTTTTTT", lvList + "||||" + recyclerView);
//                    Log.e("TTTTTTTTTTTT", "dx=" + dx + ";dy=" + dy + "||||" + lvList.getTop()+"|||"+firstItemPosition);
//                    //
//                    //                    if (foodsArrayList.get(firstItemPosition) instanceof Foods) {
//                    //                        int foodTypePosion = ((Foods) foodsArrayList.get(firstItemPosition)).getFood_stc_posion();
//                    //                        FoodsTypeListview.getChildAt(foodTypePosion).setBackgroundResource(R.drawable.choose_item_selected);
//                    //                    }
//
//                    if (firstItemPosition > 0)
//                        firstItemPosition--;
//                    int section = getSectionForPosition(firstItemPosition);
//                    int nextSection = getSectionForPosition(firstItemPosition + 1);
//                    int nextSecPosition = getPositionForSection(+nextSection);
//
//                    firstItemPosition++;
//                    if (firstItemPosition != lastItemPosition) {
//                        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) invite_title_layout.getLayoutParams();
//                        params.topMargin = 0;
//                        invite_title_layout.setLayoutParams(params);
//                        if (listMembers.size() == 1) {
//                            invite_title_layout.setVisibility(View.GONE);
//                        }
//
//                        if (getPositionForSection(section) != -1) {
//                            invite_title.setText(listMembers.size() == 0 ? "A" : listMembers.get(getPositionForSection(section)).getSortKey());
//                        }
//                    }
//                    if (nextSecPosition == firstVisibleItem + 1) {
//                        View childView = view.getChildAt(0);
//                        if (childView != null) {
//                            int titleHeight = invite_title_layout.getHeight();
//                            int bottom = childView.getBottom();
//                            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) invite_title_layout.getLayoutParams();
//                            if (bottom < titleHeight) {
//                                float pushedDistance = bottom - titleHeight;
//                                params.topMargin = (int) pushedDistance;
//                                invite_title_layout.setLayoutParams(params);
//                            } else {
//                                if (params.topMargin != 0) {
//                                    params.topMargin = 0;
//                                    invite_title_layout.setLayoutParams(params);
//                                }
//                            }
//                        }
//                    }
//                    lastFirstVisibleItem = firstVisibleItem;
//
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
//        FoodsNameRecycle.setOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
//                //判断是当前layoutManager是否为LinearLayoutManager
//                // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
//                if (layoutManager instanceof LinearLayoutManager) {//                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;

        //                    //获取最后一个可见view的位置
//                    int lastItemPosition = linearManager.findLastVisibleItemPosition();
//                    //获取第一个可见view的位置
//                    int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//                    if (foodsArrayList.get(firstItemPosition) instanceof Foods) {
//                        int foodTypePosion = ((Foods) foodsArrayList.get(firstItemPosition)).getFood_stc_posion();
//                        FoodsTypeListview.getChildAt(foodTypePosion).setBackgroundResource(R.drawable.choose_item_selected);
//                    }
//                    System.out.println(lastItemPosition + "   " + firstItemPosition);
//                }
//            }
//        });
    }

    //设置右侧确定按钮的逻辑
    @Override
    public void onClick(View v) {

    }

    //测试
    private void testData(){
//        arrLabelinfos = new ArrayList<>();
//        for (int i = 0 ;i < 20;i++){
//            LabelsList.LabelInfo info = new LabelsList.LabelInfo();
//            info.setId(i);
//            info.setLabelName("标签" + i);
//            info.setNum(i+i);
//            arrLabelinfos.add(info);
//        }
//        mCloseFriendsAdapter.setList(arrLabelinfos);
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

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
//    public int getSectionForPosition(int position) {
//        if ((arrLabelinfos.size() - 1) > position && arrLabelinfos.get(position).getSortKey().length() != 0)
//            return listMembers.get(position).getSortKey().charAt(0);
//        else
//            return 0;
//    }
    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
//    public int getPositionForSection(int section) {
//        for (int i = 0; i < listMembers.size(); i++) {
//            String sortStr = listMembers.get(i).getSortKey();
//            char firstChar = sortStr.toUpperCase().charAt(0);
//            if (firstChar == section) {
//                return i;
//            }
//        }
//        return -1;
//    }
}