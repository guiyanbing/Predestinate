package com.juxin.predestinate.ui.recommend;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.view.CircularCoverView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.bean.recommend.RecommendPeople;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

import java.util.List;

/**
 * 推荐的人适配器
 * Created YAO on 2017/3/30.
 */

public class RecommendAdapter extends BaseRecyclerViewAdapter {
    private List<RecommendPeople> recommendPeopleList;

    public void setRecommendList(List<RecommendPeople> recommendPeopleList) {
        this.recommendPeopleList = recommendPeopleList;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[R.layout.p1_recommend_item];
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
//        UserInfoLightweight userinfo = (UserInfoLightweight) getItem(position);
//        final RecommendPeople recommendPeople = recommendPeopleList.get(position);
//        TextView tv_nickname = viewHolder.findViewById(R.id.tv_nickname);
//        final TextView tv_greet = viewHolder.findViewById(R.id.tv_greet);
//        TextView tv_introduction = viewHolder.findViewById(R.id.tv_introduction);
//        TextView tv_sign = viewHolder.findViewById(R.id.tv_sign);
//        TextView tv_online_time = viewHolder.findViewById(R.id.tv_online_time);
//        CircularCoverView iv_head = viewHolder.findViewById(R.id.iv_head);
//        final ImageView iv_greet = viewHolder.findViewById(R.id.iv_greet);
//        ImageView iv_identity = viewHolder.findViewById(R.id.iv_identity);
//        LinearLayout bt_send_msg = viewHolder.findViewById(R.id.bt_send_msg);
//        LinearLayout bt_greet = viewHolder.findViewById(R.id.bt_greet);
//
////        tv_nickname.setText(userinfo.getNickname());
//        bt_send_msg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PToast.showShort("打开私聊");//TODO
//            }
//        });
//        changeGreetButton(iv_greet, tv_greet, recommendPeople.is_sayhi());
//        bt_greet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (recommendPeople.is_sayhi()){
//                    PToast.showShort("已打过招呼");
//                }else{
//                    PToast.showShort("打招呼请求");//TODO
//                    changeGreetButton(iv_greet, tv_greet, false);
//                }
//
//            }
//        });
//        showIdentity(2, iv_identity, false, true);
//        setIntroduction(tv_introduction);
//        tv_sign.setText("这家伙很懒");
//        tv_online_time.setText(TimeUtil.formatBeforeTime(recommendPeople.getTm() * 1000));
//        iv_head.setBackgroundResource(R.drawable.ic_launcher);
//        iv_head.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PToast.showShort("查看资料跳转");
//            }
//        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }


//    @Override
//    public int getItemCount() {
//        return list.size();
//    }

//    class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView tv_nickname, tv_greet, tv_introduction, tv_sign, tv_online_time;
//        LinearLayout bt_send_msg, bt_greet;
//        CircularCoverView iv_head;
//        ImageView iv_greet;
//        ImageView iv_identity;
//
//        public MyViewHolder(View view) {
//            super(view);
//
//        }
//    }

    //设置基本信息
    private void setIntroduction(TextView textView) {
        String dataage = "21";
        String dataheight = "166";
        String datacity = "北京";
        String age = TextUtils.isEmpty(dataage) ? "" : dataage + "岁";
        String height = TextUtils.isEmpty(dataheight) ? "" : dataage + "cm";
        String city = datacity;
        if (!TextUtils.isEmpty(age) && !TextUtils.isEmpty(height) || !TextUtils.isEmpty(city)) {
            age += "/";
        }
        if (!TextUtils.isEmpty(height) && !TextUtils.isEmpty(city)) {
            height += "/";
        }
        textView.setText(age + height + city);
    }

    //用户身份状态
    private void showIdentity(int gender, ImageView img, boolean isVip, boolean isIdentity) {
        if (gender == 1) {//如果是女性显示认证
            img.setImageResource(isIdentity ? R.drawable.p1_certification : null);
            if (isIdentity) {
                img.setVisibility(View.VISIBLE);
                img.setImageResource(R.drawable.p1_certification);
            } else {
                img.setVisibility(View.INVISIBLE);
            }
        } else {//男生显示vip
            if (isVip) {
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
