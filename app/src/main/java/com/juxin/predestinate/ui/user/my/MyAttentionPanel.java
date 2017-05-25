package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PToast;
import com.juxin.library.view.BasePanel;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweightList;
import com.juxin.predestinate.bean.my.AttentionList;
import com.juxin.predestinate.bean.my.AttentionUserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.module.util.my.AttentionUtil;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.user.my.adapter.MyAttentionAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * 我关注的
 * Created by zm on 2017/4/25
 */
public class MyAttentionPanel extends BasePanel implements RequestComplete,XRecyclerView.LoadingListener{

    private Context mContext;
    //有关控件
    private CustomRecyclerView crvView;
    private XRecyclerView rvList;
    //数据相关
    private List<AttentionUserDetail> mUserDetails = new ArrayList<>();
    private MyAttentionAdapter mAttentionMeAdapter;

    public MyAttentionPanel(Context context) {
        super(context);
        mContext = context;
        setContentView(R.layout.f1_my_attention_panel);
        initView();
        reqData();
    }
    //请求数据
    private void reqData() {
        ModuleMgr.getCommonMgr().getFollowing(this);
    }

    private void initView(){
        crvView = (CustomRecyclerView) findViewById(R.id.my_attention_panel_crlv_list);
        rvList = crvView.getXRecyclerView();
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mAttentionMeAdapter = new MyAttentionAdapter(mContext);
        rvList.setAdapter(mAttentionMeAdapter);
        mAttentionMeAdapter.setOnItemClickListener(mAttentionMeAdapter);
        rvList.setLoadingMoreEnabled(false);
        rvList.setLoadingListener(this);
    }

    //请求数据返回
    @Override
    public void onRequestComplete(HttpResponse response) {
        rvList.refreshComplete();
        rvList.loadMoreComplete();
//        Log.e("TTTTTTTTTTFF", response.getResponseString() + "|||" + response.isOk());
        crvView.showXrecyclerView();
        if (response.getUrlParam() == UrlParam.getFollowing){
            if (response.isOk()){
                mUserDetails.clear();
                AttentionList lists = new AttentionList();
                lists.parseJson(response.getResponseString());
//                lists.parseJson(testData());

                List<AttentionList.AttentionInfo> infos = lists.getArr_lists();
                mUserDetails.addAll(AttentionUtil.HandleAttentionList(infos, AttentionUtil.MYATTENTION));
                for (int i = 0 ;i < mUserDetails.size();i++){
                    mUserDetails.get(i).setType(1);
                }
                if (infos != null && !infos.isEmpty()){
                    List<Long> userIds = new ArrayList<>();
                    int size = infos.size();
                    for (int i = 0;i < size;i++){
                        userIds.add(infos.get(i).getUid());
                    }
                    ModuleMgr.getCommonMgr().reqUserInfoSummary(userIds,this);//批量获取用户信息
                    return;
                }
                mAttentionMeAdapter.setList(mUserDetails);
                if (mUserDetails.size()<=0){
                    crvView.showNoData(mContext.getString(R.string.tip_data_empty), mContext.getString(R.string.tip_click_refresh), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            reqData();
                        }
                    });
                }
                return;
            }
            if (mUserDetails.size()<=0){
                crvView.showNetError(mContext.getString(R.string.tip_click_refresh), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reqData();
                    }
                });
                return;
            }
            PToast.showShort(mContext.getString(R.string.net_error_check_your_net));
            return;
        }
        UserInfoLightweightList userInfos = new UserInfoLightweightList();
        userInfos.parseJsonSummary(JsonUtil.getJsonObject(response.getResponseString()));
        List<UserInfoLightweight> userList = userInfos.getLightweightLists();
        int size = userList.size();
//        Log.e("TTTTTTTTTTTGG", response.getResponseString() + "|||" + size);
        for (int i = 0;i < size;i++){
            AttentionUserDetail userDetail = new AttentionUserDetail();
            userDetail.parseJs(userList.get(i));
            userDetail.setType(1);
            mUserDetails.add(userDetail);//添加到数据列表
            AttentionUtil.addUser(userDetail);//添加到缓存列表
            if (i == size-1){
//                Log.e("TTTTTTTTTTTTT000",count+"|||");
                AttentionUtil.saveUserDetails();//将用户信息存入缓存
                mAttentionMeAdapter.setList(mUserDetails);
            }
        }
    }
    //刷新界面
    public void reFreshUI(){
        mAttentionMeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        reqData();
    }

    @Override
    public void onLoadMore() {//加载更多

    }

    private String testData(){
        String str = "{\n" +
                "    \"result\": \"success\",\n" +
                "    \"item\": [\n" +
                "        {\n" +
                "            \"uid\": 333245,\n" +
                "            \"time\": 1423042627\n" +
                "        },\n" +
                "{\n" +
                "            \"uid\": 122821207,\n" +
                "            \"time\": 1423042627\n" +
                "        },\n" +
                "{\n" +
                "            \"uid\": 123950396,\n" +
                "            \"time\": 1423042627\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        return str;
    }

}