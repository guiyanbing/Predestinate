package com.juxin.predestinate.ui.recommend;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.library.view.CircularCoverView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.recommend.RecommendPeople;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

import java.util.List;

/**
 * 推荐的人适配器
 * Created YAO on 2017/3/30.
 */

class RecommendAdapter extends BaseRecyclerViewAdapter {
    private List<RecommendPeople> recommendPeopleList;
    private Context context;

    public void setRecommendList(Context context, List<RecommendPeople> recommendPeopleList) {
        this.context = context;
        this.recommendPeopleList = recommendPeopleList;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_recommend_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        UserInfoLightweight userinfo = (UserInfoLightweight) getItem(position);
        final RecommendPeople recommendPeople = recommendPeopleList.get(position);
        TextView tv_nickname = viewHolder.findViewById(R.id.tv_nickname);
        final TextView tv_greet = viewHolder.findViewById(R.id.tv_greet);
        TextView tv_introduction = viewHolder.findViewById(R.id.tv_introduction);
        TextView tv_sign = viewHolder.findViewById(R.id.tv_sign);
        TextView tv_online_time = viewHolder.findViewById(R.id.tv_online_time);
        ImageView iv_head = viewHolder.findViewById(R.id.iv_head);
        final ImageView iv_greet = viewHolder.findViewById(R.id.iv_greet);
        ImageView iv_identity = viewHolder.findViewById(R.id.iv_identity);
        LinearLayout bt_send_msg = viewHolder.findViewById(R.id.bt_send_msg);
        LinearLayout bt_greet = viewHolder.findViewById(R.id.bt_greet);

        tv_nickname.setText(userinfo.getNickname());
        bt_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PToast.showShort("打开私聊");//TODO
            }
        });
        changeGreetButton(iv_greet, tv_greet, recommendPeople.is_sayhi());
        bt_greet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recommendPeople.is_sayhi()) {
                    PToast.showShort("已打过招呼");
                } else {
                    PToast.showShort("打招呼请求");//TODO
                    changeGreetButton(iv_greet, tv_greet, false);
                }

            }
        });
        showIdentity(userinfo, iv_identity);
        setIntroduction(tv_introduction, userinfo);
        tv_sign.setText(userinfo.getSignname());
        tv_online_time.setText(TimeUtil.formatBeforeTime(recommendPeople.getTm() * 1000));
        ImageLoader.loadRoundCorners(context, userinfo.getAvatar(),10, iv_head);
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PToast.showShort("查看资料跳转");
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }


    //设置基本信息
    private void setIntroduction(TextView textView, UserInfoLightweight userinfo) {
        int dataage = userinfo.getAge();
        int dataheight = userinfo.getHeight();
        String age = dataage == 0 ? "" : dataage + "岁";
        String height = dataheight == 0 ? "" : dataheight + "cm";
        String city = userinfo.getCityName();
        if (!TextUtils.isEmpty(age) &&(!TextUtils.isEmpty(height) || !TextUtils.isEmpty(city))) {
            age += "/";
        }
        if (!TextUtils.isEmpty(height) && !TextUtils.isEmpty(city)) {
            height += "/";
        }
        textView.setText(age + height + city);
    }

    //用户身份状态
    private void showIdentity(UserInfoLightweight userinfo, ImageView img) {
        if (userinfo.getGender() == 1) {//如果是女性显示认证
            img.setImageResource(userinfo.isAuth() ? R.drawable.p1_certification : null);
            if (userinfo.isAuth()) {
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.p1_certification);
            } else {
                img.setVisibility(View.INVISIBLE);
            }
        } else {//男生显示vip
            if (userinfo.isVip()) {
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.p1_vip_ico);
            } else {
                img.setVisibility(View.INVISIBLE);
            }

        }
    }

    //打招呼按钮状态
    private void changeGreetButton(ImageView img, TextView txt, boolean enable) {
        if (enable) {
            img.setImageResource(R.drawable.p1_greet_normal);
            txt.setText("打个招呼");
        } else {
            img.setImageResource(R.drawable.p1_greet_press);
            txt.setText("已打招呼");
        }
    }
}
