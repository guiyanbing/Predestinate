package com.juxin.predestinate.ui.xiaoyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.xiaoyou.adapter.TabDdetailAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;
import com.juxin.predestinate.ui.xiaoyou.view.IntimacyDetailsHeadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加标签页面
 * Created by zm on 2017/3/24
 */
public class IntimacyDetailActivity extends BaseActivity implements View.OnClickListener,XRecyclerView.LoadingListener,RequestComplete {

    //控件
    private CustomRecyclerView crlvList;
    private RecyclerView rvList;
    private IntimacyDetailsHeadView mIntimacyDetailsHeadView;
    //其他
    private List<SimpleFriendsList.SimpleFriendInfo> arrFriendinfos;
    private TabDdetailAdapter mTabDdetailAdapter;
    private int tab ;
    private int page = 0;//当前页
    private int pageLimits = 20;//一页的条数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab = getIntent().getIntExtra("tab", -1);
        arrFriendinfos = FriendsUtils.intimacyInfos.get(tab).getArr_friends();
//        Log.e("TTTTTTTTTTTYYY",tab+"||"+arrFriendinfos.size());
        setContentView(R.layout.p1_xiaoyou_intimacy_detail_activity);
        initView();
    }

    private void initView() {
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_newtab_crlv_list);
        mIntimacyDetailsHeadView = (IntimacyDetailsHeadView) findViewById(R.id.xiaoyou_intimacy_head);
        rvList = crlvList.getRecyclerView();
        crlvList.showRecyclerView();
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        setBackView(R.id.base_title_back);
        setTitle("亲密好友");
        mIntimacyDetailsHeadView.setHeadData(FriendsUtils.intimacyInfos.get(tab).getLabelName(),arrFriendinfos.size()+"");
        mTabDdetailAdapter = new TabDdetailAdapter(1);
        mTabDdetailAdapter.setList(arrFriendinfos);
        rvList.setAdapter(mTabDdetailAdapter);
    }

    private void creatNewTag() {
        ArrayList name = new ArrayList();
        name.add("name");
        ArrayList list = new ArrayList();
        ModuleMgr.getCommonMgr().addTagGroup(name, list, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
//                Log.e("TTTTTTTTTTTTTTnewtag", response.getResponseString() + "||");
                if (response.isOk()) {//请求返回成功
                    JSONArray array = response.getJsonArray("list");
                    if (array != null) {
                        JSONObject object = null;
                        try {
                            object = array.getJSONObject(0);
//                            tab = object.optLong("id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    //设置右侧确定按钮的逻辑
    @Override
    public void onClick(View v) {
        UIShow.showSelectContactAct(tab,this);
    }

    @Override
    public void onRefresh() {
        page = 0;
    }

    @Override
    public void onLoadMore() {
        page++;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK&&data != null){
            ModuleMgr.getCommonMgr().getTagGroupMember(this);
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {

    }
}