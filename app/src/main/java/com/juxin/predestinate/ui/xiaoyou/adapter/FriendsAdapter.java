package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;


/**
 * xiaoyouFragment的适配器
 * Created by zm on 2017/3/25.
 */
public class FriendsAdapter extends BaseRecyclerViewAdapter<FriendsList.FriendInfo> {

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_xiaoyou_fragment_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        final FriendsList.FriendInfo info = getItem(position);
        if (info.getType() == 1) {
            vh.showImgRight();
            vh.friend_item_img_head.setImageResource(info.getIcon());
            vh.friend_item_txv_name.setText(info.getNickname() + "");
            vh.friend_item_txv_describe.setText(info.getDescribe() + "");
            vh.friend_item_txv_title.setVisibility(View.GONE);
            //            Log.e("TTTTTTT",info.getNickname()+"||");
        } else if(info.getType() == 0){
            if ((position > 0 && getItem(position-1).getType() == 1) || position == 0){
                vh.friend_item_txv_title.setVisibility(View.VISIBLE);
            }else {
                vh.friend_item_txv_title.setVisibility(View.GONE);
            }
            vh.showLately();
            //friend_item_txv_intimacy 根据性别设置提示文字
            if (info.getGender() == 1){
                vh.friend_item_txv_intimacy.setText(R.string.intimacy);
                vh.friend_item_txv_num.setText(info.getIntimacy()+"");
            }else {
                vh.friend_item_txv_intimacy.setText(R.string.income);
                vh.friend_item_txv_num.setText(info.getIntimacy()+"元");
            }
            //处理list条目
            //            vh.friend_item_img_head;
            //            vh.friend_item_txv_name;
            //            vh.frien
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {
        ImageView friend_item_img_head, friend_item_img_vip, friend_item_img_dynamic, friend_item_img_photo, friend_item_img_right;
        TextView friend_item_txv_name, friend_item_txv_intimacy, friend_item_txv_describe, friend_item_txv_num,friend_item_txv_title;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            friend_item_img_head =  convertView.findViewById(R.id.friend_item_img_head);
            friend_item_img_vip =  convertView.findViewById(R.id.friend_item_img_vip);
            friend_item_img_dynamic =  convertView.findViewById(R.id.friend_item_img_dynamic);
            friend_item_img_photo =  convertView.findViewById(R.id.friend_item_img_photo);
            friend_item_img_right =  convertView.findViewById(R.id.friend_item_img_right);

            friend_item_txv_name =  convertView.findViewById(R.id.friend_item_txv_name);
            friend_item_txv_intimacy =  convertView.findViewById(R.id.friend_item_txv_intimacy);
            friend_item_txv_describe =  convertView.findViewById(R.id.friend_item_txv_describe);
            friend_item_txv_num =  convertView.findViewById(R.id.friend_item_txv_num);
            friend_item_txv_title =  convertView.findViewById(R.id.friend_item_txv_title);
        }

        public void showImgRight() {
            friend_item_img_right.setVisibility(View.VISIBLE);
            friend_item_img_dynamic.setVisibility(View.GONE);
            friend_item_img_photo.setVisibility(View.GONE);
            friend_item_img_vip.setVisibility(View.GONE);
            friend_item_txv_num.setVisibility(View.GONE);
        }

        public void showLately() {
            friend_item_txv_num.setVisibility(View.VISIBLE);
            friend_item_img_right.setVisibility(View.GONE);
            friend_item_img_dynamic.setVisibility(View.GONE);
            friend_item_img_photo.setVisibility(View.GONE);
            friend_item_img_vip.setVisibility(View.GONE);
        }
    }
}