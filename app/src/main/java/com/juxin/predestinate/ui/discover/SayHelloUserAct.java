package com.juxin.predestinate.ui.discover;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.swipemenu.SwipeListView;
import com.juxin.predestinate.module.logic.swipemenu.SwipeMenu;
import com.juxin.predestinate.module.logic.swipemenu.SwipeMenuCreator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/5/22.
 */

public class SayHelloUserAct extends BaseActivity implements SwipeListView.OnSwipeItemClickedListener, AdapterView.OnItemClickListener {

    private SwipeListView listView;

    private List<String> data = new ArrayList<>();

    private SayHelloUserAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_say_hello_user_act);
        initData();
        initView();
    }

    private void initView() {
        setTitle("左滑删除");
        setBackView();
        listView = (SwipeListView) findViewById(R.id.say_hello_user_list);
        adapter = new SayHelloUserAdapter(this, data);
        listView.setAdapter(adapter);

        listView.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                menu.setTitle("删除");
                menu.setTitleSize(18);
                menu.setTitleColor(Color.WHITE);
                menu.setViewHeight(adapter.getItemHeight());
            }
        });

        listView.setPullLoadEnable(false);
        listView.setPullRefreshEnable(false);
        listView.setOnItemClickListener(this);
        listView.setSwipeItemClickedListener(this);
    }

    private void initData() {
        String mm = "测试--------》";
        for (int i = 0; i < 30; i++) {
            String a = mm + i;
            data.add(a);
        }
    }


    @Override
    public void onSwipeChooseOpened() {

    }

    @Override
    public void onSwipeChooseClosed() {

    }

    @Override
    public void onSwipeChooseChecked(int position, boolean isChecked) {

    }

    @Override
    public void onSwipeMenuClick(int position, SwipeMenu swipeMenu, View contentView) {
        data.remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
