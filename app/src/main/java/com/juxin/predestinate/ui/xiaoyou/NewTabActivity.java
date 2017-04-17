package com.juxin.predestinate.ui.xiaoyou;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;
import com.juxin.predestinate.ui.xiaoyou.view.LabelDetailsHeadView;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加标签页面
 * Created by zm on 2017/3/24
 */
public class NewTabActivity extends BaseActivity implements View.OnClickListener,XRecyclerView.LoadingListener,RequestComplete {

    //控件
    private CustomRecyclerView crlvList;
    private RecyclerView rvList;
    private LabelDetailsHeadView mLabelDetailsHeadView;
    //其他
    private ArrayList<SimpleFriendsList.SimpleFriendInfo> arrFriendinfos = new ArrayList<>();
    private TabDdetailAdapter mTabDdetailAdapter;
    private long tab ;
    private int page = 0;//当前页
    private int pageLimits = 20;//一页的条数
    private LabelsList.LabelInfo info = null ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab = getIntent().getLongExtra("tab",-1);
        setContentView(R.layout.p1_xiaoyou_newtab_activity);
        initView();
        if (tab == -1){
            creatNewTag();
        }else {
            List<Long> listIds = getFriendIds();
            for (int i = 0 ;i < listIds.size();i++){
                SimpleFriendsList.SimpleFriendInfo info = new SimpleFriendsList.SimpleFriendInfo();
                info.setUid(listIds.get(i));
                info.setUserInfoLightweight(FriendsUtils.getHandleUserInfo(info));
                arrFriendinfos.add(info);
            }
        }
    }

    private void initView() {
        mLabelDetailsHeadView = (LabelDetailsHeadView) findViewById(R.id.xiaoyou_newtab_crlv_ldhv);
        crlvList = (CustomRecyclerView) findViewById(R.id.xiaoyou_newtab_crlv_list);
        rvList = crlvList.getRecyclerView();
        rvList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        setBackView(R.id.base_title_back);
        setTitle(getString(R.string.tab_group));
        setTitleRight("添加成员", this);
        mTabDdetailAdapter = new TabDdetailAdapter(0);
        rvList.setAdapter(mTabDdetailAdapter);
        mLabelDetailsHeadView.setTab(tab);
        crlvList.showRecyclerView();
        for (int i = 0;i<TabGroupActivity.arrLabes.size();i++){
            if (tab == TabGroupActivity.arrLabes.get(i).getId()){
                info = TabGroupActivity.arrLabes.get(i);
                break;
            }
        }
        if (info != null){
            mLabelDetailsHeadView.setHeadData(info.getLabelName(),"标签成员("+info.getList().size()+")");
        }
    }

    private void creatNewTag() {
        ArrayList name = new ArrayList();
        name.add(mLabelDetailsHeadView.getName()+"");
        ArrayList list = new ArrayList();
//        list.add("60230");
        ModuleMgr.getCommonMgr().addTagGroup(name, list, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                //                Log.e("TTTTTTTTTTTTTTnewtag", response.getResponseString() + "||");
                if (response.isOk()) {//请求返回成功
                    LabelsList lists = (LabelsList) response.getBaseData();
                    List<LabelsList.LabelInfo> labelInfos = lists.getArr_labels();
                    if (labelInfos!= null){
                        TabGroupActivity.arrLabes = (ArrayList)labelInfos;
                    }

                    for(int i = 0 ;i< TabGroupActivity.arrLabes.size();i++){
                        if (mLabelDetailsHeadView.getName().equals(TabGroupActivity.arrLabes.get(i).getLabelName())){
                            tab = TabGroupActivity.arrLabes.get(i).getId();
                            break;
                        }
                    }

//                    if (labelInfos != null && !labelInfos.isEmpty()) {
//                        lvList.setLoadingMoreEnabled(labelInfos.size() >= pageLimits ? true:false);
//                        mIntimacyFriendsAdapter.setList(arrLabes);
//                        crlvList.showXrecyclerView();
//                    }else{
//                        crlvList.showNoData();
//                    }
//                    JSONArray array = response.getJsonArray("list");
//                    if (array != null) {
//                        JSONObject object = null;
//                        try {
//                            object = array.getJSONObject(0);
//                            tab = object.optLong("id");
//                            mLabelDetailsHeadView.setTab(tab);
//                            String name = object.optString("desc");
//                            LabelsList.LabelInfo info = new LabelsList.LabelInfo();
//                            info.setId(tab);
//                            info.setLabelName(name);
//                            TabGroupActivity.arrLabes.add(info);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }
        });
    }

    //设置右侧确定按钮的逻辑
    @Override
    public void onClick(View v) {
        UIShow.showSelectContactAct(tab, this);
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
//            Log.e("TTTTTT", list + "||" + arrFriendinfos);
            if (list != null){
                arrFriendinfos.addAll(list);
            }
            mTabDdetailAdapter.setList(arrFriendinfos);
            mLabelDetailsHeadView.setHeadData(info.getLabelName(), "标签成员("+arrFriendinfos.size() + ")");
//            Log.e("TTTTTTTTTT",list+"|||"+list.size()+"||");
//            ModuleMgr.getCommonMgr().getTagGroupMember(this);
        }
    }

    private List<Long> getFriendIds(){
        for (int i = 0;i < TabGroupActivity.arrLabes.size();i++ ){
            if (tab == TabGroupActivity.arrLabes.get(i).getId()){
                return TabGroupActivity.arrLabes.get(i).getList();
            }
        }
        return null;
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
                for (int i = 0;i < friendInfos.size();i++){
                    friendInfos.get(i).setUserInfoLightweight(FriendsUtils.getHandleUserInfo(friendInfos.get(i)));
                }
                mTabDdetailAdapter.setList(arrFriendinfos);
                crlvList.showRecyclerView();
            }else{
                crlvList.showNoData();
            }
        }else {//请求失败
            crlvList.showNetError();
            MMToast.showShort("请求失败，请检查您的网络");
        }
    }
}