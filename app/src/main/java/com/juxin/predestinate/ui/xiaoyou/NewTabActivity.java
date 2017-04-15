package com.juxin.predestinate.ui.xiaoyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.mumu.bean.utils.MMToast;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加标签页面
 * Created by zm on 2017/3/24
 */
public class NewTabActivity extends BaseActivity implements View.OnClickListener,XRecyclerView.LoadingListener,RequestComplete {

    //控件
    private CustomRecyclerView crlvList;
    private XRecyclerView rvList;
    //其他
    private ArrayList<SimpleFriendsList.SimpleFriendInfo> arrFriendinfos;
    private TabDdetailAdapter mTabDdetailAdapter;
    private long tab ;
    private int page = 0;//当前页
    private int pageLimits = 20;//一页的条数

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab = getIntent().getLongExtra("tab",-1);
        if (tab == -1){
            creatNewTag();
        }
        setContentView(R.layout.p1_xiaoyou_newtab_activity);
        initView();
    }

    private void initView() {
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_newtab_crlv_list);
        rvList = crlvList.getXRecyclerView();
        rvList.setLoadingListener(this);
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.tab_group));
        setTitleRight("添加成员", this);
        mTabDdetailAdapter = new TabDdetailAdapter(0);
        rvList.setAdapter(mTabDdetailAdapter);
        if (tab == -1) {

        } else {

        }
    }

    private void creatNewTag() {
        ArrayList name = new ArrayList();
        name.add("好友");
        ArrayList list = new ArrayList();
        list.add("60230");
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
                            tab = object.optLong("id");
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
            ArrayList<SimpleFriendsList.SimpleFriendInfo> list;
            list = data.getExtras().getParcelableArrayList("infos");
            Log.e("TTTTTTTTTT",list+"|||"+list.size()+"||");
            ModuleMgr.getCommonMgr().getTagGroupMember(this);
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()){//请求返回成功
            SimpleFriendsList lists = (SimpleFriendsList) response.getBaseData();
            List<SimpleFriendsList.SimpleFriendInfo> friendInfos = lists.getArr_frends();
            if (page == 0){
                arrFriendinfos.clear();
            }
            arrFriendinfos.addAll(friendInfos);
            if (friendInfos != null && !friendInfos.isEmpty()) {
                rvList.setLoadingMoreEnabled(friendInfos.size() >= pageLimits ? true:false);
                mTabDdetailAdapter.setList(arrFriendinfos);
                crlvList.showXrecyclerView();
            }else{
                crlvList.showNoData();
            }
        }else {//请求失败
            crlvList.showNetError();
            MMToast.showShort("请求失败，请检查您的网络");
        }
    }
}