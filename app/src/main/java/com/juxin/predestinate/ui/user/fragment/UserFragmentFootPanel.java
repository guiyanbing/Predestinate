package com.juxin.predestinate.ui.user.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.start.WebEntranceActivity;

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
        int[] icons = new int[]{R.drawable.p1_user_zhuye_ico, R.drawable.p1_user_dongtai_ico, R.drawable.p1_user_renzheng_ico, R.drawable.p1_user_vip_ico, R.drawable.p1_user_zuanshi_ico,
                R.drawable.p1_user_liwu_ico, R.drawable.p1_user_youxi_ico, R.drawable.p1_user_guanyu_ico, R.drawable.p1_user_set_ico};

        for (int i = 0; i < names.length; i++) {
            UserAuth userAuth = new UserAuth(i + 1, icons[i], levels[i], names[i], isShow(i + 1));
            userAuthList.add(userAuth);
        }
        userAuthAdapter = new UserAuthAdapter();
        userAuthAdapter.setList(userAuthList);
        recyclerView.setAdapter(userAuthAdapter);
        userAuthAdapter.setOnItemClickListener(this);
    }

    /**
     * 刷新list
     */
    public void notifyAdapter() {
        userAuthAdapter.notifyDataSetChanged();
    }

    // 判断指定条目显隐状态
    private boolean isShow(int id) {
        // if (id == CenterItemID.i_Center_GoodsVip_id) { // VIP充值
        //     return false;
        // }
        return true;
    }

    @Override
    public void onItemClick(View convertView, int position) {
        switch (position) {
            case CenterItemID.i_Center_Homepage_id: // 主页
                UIShow.showUserCheckInfoAct(getContext());
                break;

            case CenterItemID.i_Center_Dynamic_id: // 动态
                break;

            case CenterItemID.i_Center_Auth_id: // 认证
                break;

            case CenterItemID.i_Center_GoodsVip_id:// VIP充值
                break;

            case CenterItemID.i_Center_GoodsDiamond_id:// 钻石
                UIShow.showGoodsDiamondAct(getContext());
                break;

            case CenterItemID.i_Center_Gift_id:// 礼物
                break;

            case CenterItemID.i_Center_Game_id:// 游戏
                break;

            case CenterItemID.i_Center_About_id: // 关于
                UIShow.show(getContext(), WebEntranceActivity.class );
                break;

            case CenterItemID.i_Center_Setting_id:// 设置
                UIShow.showUserSetAct((Activity) getContext(), 100);
                break;
        }
    }
}
