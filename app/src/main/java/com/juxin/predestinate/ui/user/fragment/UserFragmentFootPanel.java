package com.juxin.predestinate.ui.user.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心的list布局panel
 */
public class UserFragmentFootPanel extends BaseViewPanel implements BaseRecyclerViewHolder.OnItemClickListener {

    private final RecyclerView list_user_authority;
    private UserAuthAdapter userAuthAdapter;

    public UserFragmentFootPanel(Context context, RecyclerView list_user_authority) {
        super(context);
        setContentView(R.layout.p1_user_fragment_footer);
        this.list_user_authority = list_user_authority;

        initData();
        addFooter();
    }

    // 初始化条目数据
    private void initData() {
        List<UserAuth> userAuthList = new ArrayList<>();
        Resources resources = getContext().getResources();
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
        list_user_authority.setAdapter(userAuthAdapter);
        userAuthAdapter.setOnItemClickListener(this);
    }

    /**
     * 底部添加一个margin的高度
     */
    private void addFooter() {
        LinearLayout margin_layout = new LinearLayout(getContext());
        margin_layout.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                getContext().getResources().getDimensionPixelSize(R.dimen.title_footer_height)));
        margin_layout.setBackgroundResource(R.color.bg_color);
        list_user_authority.addView(margin_layout, -1);
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
                break;

            case CenterItemID.i_Center_Dynamic_id: // 动态
                break;

            case CenterItemID.i_Center_Auth_id: // 认证
                break;

            case CenterItemID.i_Center_GoodsVip_id:// VIP充值
                break;

            case CenterItemID.i_Center_GoodsDiamond_id:// 钻石
                break;

            case CenterItemID.i_Center_Gift_id:// 礼物
                break;

            case CenterItemID.i_Center_Game_id:// 游戏
                break;

            case CenterItemID.i_Center_About_id: // 关于
                break;

            case CenterItemID.i_Center_Setting_id:// 设置
                break;
        }
    }
}
