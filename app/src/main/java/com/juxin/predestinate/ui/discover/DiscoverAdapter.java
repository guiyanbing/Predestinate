package com.juxin.predestinate.ui.discover;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.light.UserInfoLightweight;
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
        UserInfoLightweight userInfo = getItem(position);
        ImageLoader.loadRoundCorners(context, userInfo.getAvatar(), 8, holder.iv_avatar);
        holder.tv_name.setText(userInfo.getNickname());
        holder.iv_vip.setVisibility(userInfo.isVip() ? View.VISIBLE : View.GONE);

        holder.lin_ranking.setVisibility(View.GONE);

        holder.tv_age.setText(userInfo.getAge() + "岁");

        holder.tv_height.setText(userInfo.getHeight() + "cm");

        holder.tv_distance.setText(userInfo.getDistance());

        if (userInfo.isVideo_available() || userInfo.isAudio_available()) {
            if (userInfo.isVideo_busy()) {
                holder.iv_calling.setVisibility(View.VISIBLE);
                holder.iv_call.setVisibility(View.GONE);
                holder.iv_video.setVisibility(View.GONE);
            } else {
                holder.iv_calling.setVisibility(View.GONE);
                holder.iv_call.setVisibility(userInfo.isAudio_available() ? View.VISIBLE : View.GONE);
                holder.iv_video.setVisibility(userInfo.isVideo_available() ? View.VISIBLE : View.GONE);
            }
        } else {
            holder.iv_calling.setVisibility(View.GONE);
            holder.iv_call.setVisibility(View.GONE);
            holder.iv_video.setVisibility(View.GONE);
        }

        holder.btn_sayhi.setEnabled(!userInfo.isSayHello());
        if (!userInfo.isSayHello()) {
            holder.btn_sayhi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MMToast.showShort("打招呼");
                    getItem(position).setSayHello(true);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {
        private ImageView iv_avatar, iv_calling, iv_video, iv_call, iv_vip;
        private TextView tv_name, tv_age, tv_height, tv_distance, tv_ranking_type, tv_ranking_level;
        private Button btn_sayhi;
        private LinearLayout lin_ranking;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            iv_avatar = (ImageView) convertView.findViewById(R.id.discover_item_avatar);
            iv_calling = (ImageView) convertView.findViewById(R.id.discover_item_calling_state);
            iv_video = (ImageView) convertView.findViewById(R.id.discover_item_video);
            iv_call = (ImageView) convertView.findViewById(R.id.discover_item_call);
            iv_vip = (ImageView) convertView.findViewById(R.id.discover_item_vip_state);

            tv_name = (TextView) convertView.findViewById(R.id.discover_item_name);
            tv_age = (TextView) convertView.findViewById(R.id.discover_item_age);
            tv_height = (TextView) convertView.findViewById(R.id.discover_item_height);
            tv_distance = (TextView) convertView.findViewById(R.id.discover_item_distance);

            tv_ranking_type = (TextView) convertView.findViewById(R.id.discover_item_ranking_type);
            tv_ranking_level = (TextView) convertView.findViewById(R.id.discover_item_ranking_level);

            btn_sayhi = (Button) convertView.findViewById(R.id.discover_item_sayhi);

            lin_ranking = (LinearLayout) convertView.findViewById(R.id.discover_item_ranking_state);
        }

    }
}
