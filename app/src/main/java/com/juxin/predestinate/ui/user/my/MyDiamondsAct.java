package com.juxin.predestinate.ui.user.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.config.Diamond;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.user.my.adapter.MyDiamondAdapter;

import java.util.List;

/**
 * 我的钻石页面
 * Created by zm on 2017/4/19
 */
public class MyDiamondsAct extends BaseActivity{

    private TextView tvDiamondSum;
    private CustomRecyclerView crlList;
    private RecyclerView rlvList;
    private MyDiamondAdapter mMyDiamondAdapter;

    //list间隔样式
//    android:divider="@color/transparent"
//    android:dividerHeight="1dip"
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_wode_my_diamonds_act);
        initView();
    }

    private void initView(){
        setBackView(R.id.base_title_back);
        crlList = (CustomRecyclerView) findViewById(R.id.wode_diamond_crl_list);
        rlvList = crlList.getRecyclerView();
        rlvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rlvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        setTitle(getString(R.string.my_diamonds_explain));
        setTitleRight(getString(R.string.explain), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到钻石说明页
                UIShow.showMyDiamondsExplainAct(MyDiamondsAct.this);
            }
        });
        initData();
    }

    private void initData() {
        //逻辑待完善
        mMyDiamondAdapter = new MyDiamondAdapter(this);
        List<Diamond> dataList = ModuleMgr.getCommonMgr().getCommonConfig().getDiamondList();
        rlvList.setAdapter(mMyDiamondAdapter);
        mMyDiamondAdapter.setList(dataList);
        crlList.showRecyclerView();
    }
}