package com.juxin.predestinate.ui.user.my;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.JsonUtil;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;
import com.juxin.predestinate.ui.recommend.DividerItemDecoration;
import com.juxin.predestinate.ui.user.my.adapter.AttentionMeAdapter;
import com.juxin.predestinate.bean.my.AttentionList;
import com.juxin.predestinate.bean.my.AttentionUserDetail;
import com.juxin.predestinate.module.util.my.AttentionUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 关注我的
 * Created by zm on 2017/4/25
 */
public class AttentionMePanel extends BaseViewPanel implements RequestComplete,XRecyclerView.LoadingListener{

    private Context mContext;
    //有关控件
    private CustomRecyclerView crvView;
    private XRecyclerView rvList;
    //数据相关
    private List<AttentionUserDetail> mUserDetails = new ArrayList<>();
    private AttentionMeAdapter mAttentionMeAdapter;
    private int count;

    public AttentionMePanel(Context context) {
        super(context);
        mContext = context;
        setContentView(R.layout.f1_my_attention_panel);
        initView();
        reqData();
        crvView.showLoading();
    }
    //请求数据（关注我的列表）
    private void reqData() {
        ModuleMgr.getCommonMgr().getFollowers(this);
    }
    //初始化ui
    private void initView(){
        crvView = (CustomRecyclerView) findViewById(R.id.my_attention_panel_crlv_list);
        rvList = crvView.getXRecyclerView();
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mAttentionMeAdapter = new AttentionMeAdapter(mContext);
        rvList.setAdapter(mAttentionMeAdapter);
        rvList.setLoadingMoreEnabled(false);//没有加载更多，所有数据一次返回
        rvList.setLoadingListener(this);
    }

    //请求数据返回
    @Override
    public void onRequestComplete(HttpResponse response) {
        rvList.refreshComplete();
        rvList.loadMoreComplete();
        crvView.showXrecyclerView();
        if (response.getUrlParam() == UrlParam.getFollowers){
            if (response.isOk()){
                mUserDetails.clear();
                AttentionList lists = new AttentionList();
//                lists.parseJson(testData());
                lists.parseJson(response.getResponseString());

                List<AttentionList.AttentionInfo> infos = lists.getArr_lists();
                mUserDetails.addAll(AttentionUtil.HandleAttentionList(infos,AttentionUtil.ATTENTIONME));//先从缓存中获取数据
                if (infos != null && !infos.isEmpty()){//缓存中处理完毕后，还有消息记录，则该记录没有获取过，去服务器请求用户详细信息
                    int size = infos.size();
                    count = size;
                    for (int i = 0 ;i < size;i++){
                        ModuleMgr.getCenterMgr().reqOtherInfo(infos.get(i).getUid(),this);//请求用户详细信息
                    }
                    return;
                }
                mAttentionMeAdapter.setList(mUserDetails);
                if (mUserDetails.size()<=0){
                    crvView.showNoData(mContext.getString(R.string.tip_data_empty), mContext.getString(R.string.tip_click_refresh), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            crvView.showLoading();
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
                        crvView.showLoading();
                        reqData();
                    }
                });
                return;
            }
            PToast.showShort(mContext.getString(R.string.net_error_check_your_net));
            return;
        }
        count--;
        if (JsonUtil.getJsonObject(response.getResponseString()).has("uid")){//用户信息请求返回成功
            AttentionUserDetail userDetail = new AttentionUserDetail();
            userDetail.parseJson(response.getResponseString());
            mUserDetails.add(userDetail);//添加到数据列表
            AttentionUtil.addUser(userDetail);//添加到缓存列表
            if (count <= 0){
                AttentionUtil.saveUserDetails();//将用户信息存入缓存
                mAttentionMeAdapter.setList(mUserDetails);
            }
        }
    }
    //刷新界面
    public void reFreshUI(){
        if (mAttentionMeAdapter != null)
            mAttentionMeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        reqData();
    }

    @Override
    public void onLoadMore() {//加载更多

    }

    //此方法只用于测试使用
    private String testData(){
        String str = "/*{\n" +
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
                "        },\n" +
                "{\n" +
                "            \"uid\": 110872194,\n" +
                "            \"time\": 1423042627\n" +
                "        }\n" +
                "    ]\n" +
                "}*/";
        return str;
    }
}