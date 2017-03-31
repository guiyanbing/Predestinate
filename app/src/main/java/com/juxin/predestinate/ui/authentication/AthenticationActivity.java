package com.juxin.predestinate.ui.authentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.ui.xiaoyou.adapter.AthenticationAdapter;

import java.util.ArrayList;

/**
 * 身份认证主页面
 * Created by zm on 2017/3/24
 */
public class AthenticationActivity extends BaseActivity implements View.OnClickListener {

    //控件
    private ListView lv_list;
    //适配器
    private AthenticationAdapter mAthenticationAdapter;
    //数据
    private ArrayList<String> arrNames;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_athentication_act);
        initView();
        initArrData();
    }

    private void initView() {
        setBackView(R.id.base_title_tab);
        setTitle(getString(R.string.athentication));
    }

    private void initArrData(){
        arrNames = new ArrayList<>();
        String[] auth = getResources().getStringArray(R.array.auth_arr);
        for (int i = 0;i < auth.length;i++){
            arrNames.add(auth[i]);
        }
        mAthenticationAdapter = new AthenticationAdapter(this,arrNames);
        lv_list.setAdapter(mAthenticationAdapter);
    }

    //list的item的单击事件
    @Override
    public void onClick(View v) {

    }
}