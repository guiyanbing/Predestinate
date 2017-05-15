package com.juxin.predestinate.ui.user.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.fragment.bean.UserAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心的list布局panel
 */
public class UserFragmentFootPanel extends BaseViewPanel implements BaseRecyclerViewHolder.OnItemClickListener {

    private RecyclerView recyclerView;
    private UserAuthAdapter userAuthAdapter;

    public UserFragmentFootPanel(Context context) {
        super(context);
        setContentView(R.layout.p1_user_fragment_footer);

        initData();
        reqData();
    }

    // 初始化条目数据
    private void initData() {
        List<UserAuth> userAuthList = new ArrayList<>();
        Resources resources = getContext().getResources();
        recyclerView = (RecyclerView) findViewById(R.id.user_fragment_footer);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setNestedScrollingEnabled(false); // 嵌套ScrollView滑动惯性消失
        String[] names = resources.getStringArray(R.array.user_authority_name);
        int[] levels = resources.getIntArray(R.array.user_authority_level);
        int[] icons = new int[]{R.drawable.f1_user_home_ico, R.drawable.f1_user_guanzhu_ico, R.drawable.f1_user_wallet_ico, R.drawable.f1_user_money_ico, R.drawable.f1_user_ycoin_ico,
                R.drawable.f1_user_diamonds_ico, R.drawable.f1_user_gift_ico, R.drawable.f1_user_info_ico, R.drawable.f1_user_xiangce_ico, R.drawable.f1_user_set_ico};

        for (int i = 0; i < names.length; i++) {
            UserAuth userAuth = new UserAuth(i, icons[i], levels[i], names[i], isShow(i));
            userAuthList.add(userAuth);
        }
        userAuthAdapter = new UserAuthAdapter();
        userAuthAdapter.setList(userAuthList);
        recyclerView.setAdapter(userAuthAdapter);
        userAuthAdapter.setOnItemClickListener(this);
    }

    private void reqData() {
        // 红包总额
        ModuleMgr.getCenterMgr().reqRedbagSum(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.getResponseString());
                    String status = jsonObject.optString("status");
                    if ("ok".equals(status)) {
                        ModuleMgr.getCenterMgr().getMyInfo().setRedbagsum(jsonObject.optDouble("total"));
                        refreshView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 刷新list
     */
    public void refreshView() {
        userAuthAdapter.notifyDataSetChanged();
    }

    /**
     * 判断指定条目显隐状态
     */
    private boolean isShow(int id) {
        boolean isMan = ModuleMgr.getCenterMgr().getMyInfo().isMan();
        if (isMan && id == CenterItemID.i_Center_item_4) { // 男性用户隐藏：我要赚红包
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(View convertView, int position) {
        switch (position) {
            case CenterItemID.i_Center_item_1: // 我的主页
//                UIShow.showCheckOtherInfoAct(getContext(), 110871663);
                UIShow.showCheckOwnInfoAct(getContext());
                break;

            case CenterItemID.i_Center_item_2: // 谁关注我
                UIShow.showMyAttentionAct(getContext());
                break;

            case CenterItemID.i_Center_item_3: // 我的钱包
                UIShow.showRedBoxRecordAct(getContext());
                break;

            case CenterItemID.i_Center_item_4:// 我要赚红包
                UIShow.showDemandRedPacketAct(getContext());
                break;

            case CenterItemID.i_Center_item_5:// 我的Y币
                UIShow.showBuyCoinActivity(getContext());
                break;

            case CenterItemID.i_Center_item_6:// 我的钻石
                UIShow.showMyDiamondsAct(getContext());
                break;

            case CenterItemID.i_Center_item_7:// 我的礼物
                UIShow.showMyGiftActivity(getContext());
                break;

            case CenterItemID.i_Center_item_8:// 个人资料
                UIShow.showUserEditInfoAct(getContext());
                break;

            case CenterItemID.i_Center_item_9:// 我的相册
                UIShow.showUserPhotoAct(getContext());
                break;

            case CenterItemID.i_Center_item_10:// 设置中心
                UIShow.showUserSetAct((Activity) getContext(), 100);
                break;
        }
    }
}
