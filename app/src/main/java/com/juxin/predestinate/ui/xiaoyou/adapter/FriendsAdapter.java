package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;


/**
 * xiaoyouFragment的适配器
 * Created by zm on 2017/3/25.
 */
public class FriendsAdapter extends BaseFriendsAdapter<FriendsList.FriendInfo> {

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
        } else if(info.getType() == 0){
            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                vh.friend_item_txv_title.setVisibility(View.VISIBLE);
                vh.friend_item_txv_title.setText(info.getSortKey());
            } else {
                vh.friend_item_txv_title.setVisibility(View.GONE);
            }

            vh.showLately();
            //friend_item_txv_intimacy 根据性别设置提示文字
            if (info.getGender() == 1){
                vh.friend_item_txv_intimacy.setText(R.string.intimacy);
                vh.friend_item_txv_num.setText(info.getIntimacy()+"");
            }else {
                vh.friend_item_txv_intimacy.setText(R.string.income);
                vh.friend_item_txv_num.setText(info.getIncome()+"元");
            }
            //处理list条目
            //            vh.friend_item_img_head;
            vh.friend_item_txv_name.setText(info.getNickname());
            //            vh.frien
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }
    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getItem(position).getSortKey().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = getList().get(i).getSortKey();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
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