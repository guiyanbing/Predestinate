package com.juxin.predestinate.ui.discover;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;


/**
 * 发现Adapter
 * Created by zhang on 2017/4/21.
 */

public class DiscoverAdapter extends BaseRecyclerViewAdapter<UserInfoLightweight> {

    private Context context;

    public DiscoverAdapter(Context context) {
        super();
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.f1_discover_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {
        MyViewHolder holder = new MyViewHolder(viewHolder);
        final UserInfoLightweight userInfo = getItem(position);
        ImageLoader.loadRoundCorners(context, userInfo.getAvatar(), 8, holder.iv_avatar);
        holder.tv_name.setText(userInfo.getNickname());
        holder.iv_vip.setVisibility(userInfo.isVip() ? View.VISIBLE : View.GONE);

        if (userInfo.isToper()) {
            holder.lin_ranking.setVisibility(View.VISIBLE);
            if (userInfo.isMan()) {
                holder.lin_ranking.setBackgroundResource(R.drawable.f1_ranking_bg_m);
                holder.tv_ranking_type.setText(context.getString(R.string.top_type_man));
                holder.tv_ranking_level.setText("TOP " + userInfo.getTop());
            } else {
                holder.lin_ranking.setBackgroundResource(R.drawable.f1_ranking_bg_w);
                holder.tv_ranking_type.setText(context.getString(R.string.top_type_woman));
                holder.tv_ranking_level.setText("TOP " + userInfo.getTop());
            }
        } else {
            holder.lin_ranking.setVisibility(View.GONE);
        }

        if (userInfo.getAge() == 0) {
            holder.tv_age.setVisibility(View.GONE);
        } else {
            holder.tv_age.setVisibility(View.VISIBLE);
            holder.tv_age.setText(userInfo.getAge() + "岁");
        }

        if (userInfo.getHeight() == 0) {
            holder.tv_height.setVisibility(View.GONE);
        } else {
            holder.tv_height.setVisibility(View.VISIBLE);
            holder.tv_height.setText(userInfo.getHeight() + "cm");
        }

        if (TextUtils.isEmpty(userInfo.getDistance())) {
            holder.tv_distance.setVisibility(View.GONE);
        } else {
            holder.tv_distance.setVisibility(View.VISIBLE);
            holder.tv_distance.setText(userInfo.getDistance());
        }


        if ((userInfo.getAge() != 0 || userInfo.getHeight() != 0) && !TextUtils.isEmpty(userInfo.getDistance())) {
            holder.point.setVisibility(View.VISIBLE);
        } else {
            holder.point.setVisibility(View.GONE);
        }


        if (userInfo.isVideo_available() || userInfo.isAudio_available()) {
            if (userInfo.isVideo_busy()) {
                holder.iv_calling.setVisibility(View.VISIBLE);
                holder.iv_call.setVisibility(View.GONE);
                holder.iv_video.setVisibility(View.GONE);
            } else {
                holder.iv_calling.setVisibility(View.GONE);
                holder.iv_call.setEnabled(userInfo.isAudio_available());
                holder.iv_video.setEnabled(userInfo.isVideo_available());
            }
        } else {
            holder.iv_calling.setVisibility(View.GONE);
            holder.iv_call.setEnabled(userInfo.isAudio_available());
            holder.iv_video.setEnabled(userInfo.isVideo_available());
        }

        holder.btn_sayhi.setEnabled(!userInfo.isSayHello());
        if (!userInfo.isSayHello()) {
            holder.btn_sayhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModuleMgr.getChatMgr().sendSayHelloMsg(String.valueOf(userInfo.getUid()),
                            context.getString(R.string.say_hello_txt),
                            userInfo.getKf_id(),
                            ModuleMgr.getCenterMgr().isRobot(userInfo.getKf_id()) ?
                                    Constant.SAY_HELLO_TYPE_ONLY : Constant.SAY_HELLO_TYPE_SIMPLE, new IMProxy.SendCallBack() {
                                @Override
                                public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                                    PToast.showShort(context.getString(R.string.user_info_hi_suc));
                                }

                                @Override
                                public void onSendFailed(NetData data) {
                                    PToast.showShort(context.getString(R.string.user_info_hi_fail));
                                }
                            });
                    getItem(position).setSayHello(true);
                    notifyDataSetChanged();
                }
            });
        }

        holder.rel_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIShow.showCheckOtherInfoAct(context, userInfo.getUid());
            }
        });

        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIShow.showCheckOtherInfoAct(context, userInfo.getUid());
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {
        private ImageView iv_avatar, iv_vip;
        private Button iv_video, iv_call;
        private TextView tv_name, tv_age, tv_height, tv_distance, tv_ranking_type, tv_ranking_level;
        private Button btn_sayhi;
        private LinearLayout lin_ranking, iv_calling;
        private RelativeLayout rel_item;
        private View point;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            iv_avatar = (ImageView) convertView.findViewById(R.id.discover_item_avatar);
            iv_vip = (ImageView) convertView.findViewById(R.id.discover_item_vip_state);

            iv_calling = (LinearLayout) convertView.findViewById(R.id.discover_item_calling_state);
            iv_video = (Button) convertView.findViewById(R.id.discover_item_video);
            iv_call = (Button) convertView.findViewById(R.id.discover_item_call);

            tv_name = (TextView) convertView.findViewById(R.id.discover_item_name);
            tv_age = (TextView) convertView.findViewById(R.id.discover_item_age);
            tv_height = (TextView) convertView.findViewById(R.id.discover_item_height);
            tv_distance = (TextView) convertView.findViewById(R.id.discover_item_distance);

            tv_ranking_type = (TextView) convertView.findViewById(R.id.discover_item_ranking_type);
            tv_ranking_level = (TextView) convertView.findViewById(R.id.discover_item_ranking_level);

            btn_sayhi = (Button) convertView.findViewById(R.id.discover_item_sayhi);

            lin_ranking = (LinearLayout) convertView.findViewById(R.id.discover_item_ranking_state);
            rel_item = (RelativeLayout) convertView.findViewById(R.id.discover_item);

            point = convertView.findViewById(R.id.discover_item_point);
        }

    }
}
