package com.juxin.predestinate.ui.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PToast;
import com.juxin.library.view.CircularCoverView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.recommend.RecommendPeople;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.util.TimeUtil;

import java.util.List;

/**
 * 推荐的人适配器
 * Created YAO on 2017/3/30.
 */

public class RecommendAdapter extends XRecyclerView.Adapter<RecommendAdapter.MyViewHolder> {
    private Context context;
    private List<RecommendPeople> list;

    public RecommendAdapter(Context context, List<RecommendPeople> list) {
        this.context = context;
        this.list = list;
    }
    public void setList(List<RecommendPeople> datas) {
        this.list = datas;
        notifyDataSetChanged();
    }

    public List<RecommendPeople> getList() {
        return list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.p1_recommend_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
//        holder.tv_nickname.setText(list.get(position));
        holder.bt_send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PToast.showShort("打开私聊");//TODO
            }
        });
        holder.bt_greet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PToast.showShort("打招呼请求");//TODO
                changeGreetButton(holder.iv_greet, holder.tv_greet, false);
            }
        });
        showIdentity(2, holder.iv_identity, false, true);
        setIntroduction(holder.tv_introduction);
        holder.tv_sign.setText("这家伙很懒");
        long time = 11111111;//TODO 时间
        holder.tv_online_time.setText(TimeUtil.formatBeforeTime(time * 1000));
        holder.iv_head.setBackgroundResource(R.drawable.ic_launcher);
        holder.iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PToast.showShort("查看资料跳转");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nickname, tv_greet, tv_introduction, tv_sign, tv_online_time;
        LinearLayout bt_send_msg, bt_greet;
        CircularCoverView iv_head;
        ImageView iv_greet;
        ImageView iv_identity;

        public MyViewHolder(View view) {
            super(view);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            tv_greet = (TextView) view.findViewById(R.id.tv_greet);
            tv_introduction = (TextView) view.findViewById(R.id.tv_introduction);
            tv_sign = (TextView) view.findViewById(R.id.tv_sign);
            tv_online_time = (TextView) view.findViewById(R.id.tv_online_time);
            iv_head = (CircularCoverView) view.findViewById(R.id.iv_head);
            iv_greet = (ImageView) view.findViewById(R.id.iv_greet);
            iv_identity = (ImageView) view.findViewById(R.id.iv_identity);
            bt_send_msg = (LinearLayout) view.findViewById(R.id.bt_send_msg);
            bt_greet = (LinearLayout) view.findViewById(R.id.bt_greet);
        }
    }

    //设置基本信息
    private void setIntroduction(TextView textView) {
        String dataage = "21";
        String dataheight = "166";
        String datacity = "北京";
        String age = TextUtils.isEmpty(dataage) ? "" : dataage + "岁";
        String height = TextUtils.isEmpty(dataheight) ? "" : dataage + "cm";
        String city = datacity;
        if (!TextUtils.isEmpty(age) && !TextUtils.isEmpty(height) || !TextUtils.isEmpty(city)) {
            age+="/";
        }
        if (!TextUtils.isEmpty(height) && !TextUtils.isEmpty(city)) {
            height+="/";
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
