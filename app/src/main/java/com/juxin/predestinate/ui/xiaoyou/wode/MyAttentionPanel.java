package com.juxin.predestinate.ui.xiaoyou.wode;

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
import com.juxin.predestinate.ui.xiaoyou.wode.adapter.MyAttentionAdapter;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.AttentionList;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.AttentionUserDetail;

import java.util.ArrayList;
import java.util.List;


/**
 * 我关注的
 * Created by zm on 2017/4/25
 */
public class MyAttentionPanel extends BaseViewPanel implements RequestComplete,XRecyclerView.LoadingListener{

    private Context mContext;
    //有关控件
    private CustomRecyclerView crvView;
    private XRecyclerView rvList;
    //数据相关
    private List<AttentionUserDetail> mUserDetails = new ArrayList<>();
    private MyAttentionAdapter mAttentionMeAdapter;
    private int count;

    public MyAttentionPanel(Context context) {
        super(context);
        mContext = context;
        setContentView(R.layout.f1_my_attention_panel);
        initView();
        reqData();
    }
    //请求数据
    private void reqData() {
        crvView.showLoading();
        ModuleMgr.getCommonMgr().getFollowing(this);
    }

    private void initView(){
        crvView = (CustomRecyclerView) findViewById(R.id.my_attention_panel_crlv_list);
        rvList = crvView.getXRecyclerView();
        rvList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvList.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST, R.drawable.p1_decoration_px1));
        mAttentionMeAdapter = new MyAttentionAdapter(mContext);
//        rvList.setEmptyView(tvNoData);
        rvList.setAdapter(mAttentionMeAdapter);
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
//                lists.parseJson(response.getResponseString());
                lists.parseJson(testData());

                List<AttentionList.AttentionInfo> infos = lists.getArr_lists();
                mUserDetails.addAll(AttentionUtil.HandleAttentionList(infos));
//                Log.e("TTTTTTTMMM",infos.size()+"|||");
                if (infos != null && !infos.isEmpty()){
                    int size = infos.size();
                    count = size;
                    for (int i = 0 ;i < size;i++){
                        ModuleMgr.getCenterMgr().reqOtherInfo(infos.get(i).getUid(),this);
                    }
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
        count--;
        if (JsonUtil.getJsonObject(response.getResponseString()).has("uid")){
            AttentionUserDetail userDetail = new AttentionUserDetail();
            userDetail.parseJson(response.getResponseString());
            userDetail.setType(1);
            mUserDetails.add(userDetail);
            AttentionUtil.addUser(userDetail);
            if (count <= 0){
                AttentionUtil.saveUserDetails();
                mAttentionMeAdapter.setList(mUserDetails);
            }
        }
//        if (mWithdrawInfos != null && !mWithdrawInfos.isEmpty()){
//            return;
//        }
//        crvView.showXrecyclerView();
//        PToast.showShort(mContext.getString(R.string.net_error_check_your_net));
    }

    @Override
    public void onRefresh() {
        reqData();
    }

    @Override
    public void onLoadMore() {//加载更多

    }

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
                "        }\n" +
                "    ]\n" +
                "}*/";
        return str;
    }
}