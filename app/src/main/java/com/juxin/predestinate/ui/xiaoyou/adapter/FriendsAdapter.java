package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;

import java.util.List;


/**
 * xiaoyouFragment的适配器
 * Created by zm on 2017/3/25.
 */
public class FriendsAdapter extends ExBaseAdapter<FriendsList.FriendInfo> {
    public FriendsAdapter(Context context, List<FriendsList.FriendInfo> datas) {
        super(context, datas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.p1_xiaoyou_fragment_item);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
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
            //处理list条目
            //            vh.friend_item_img_head;
            //            vh.friend_item_txv_name;
            //            vh.frien
        }


        //        if (position < 3) {
        //            vh.showSpecialItem();
        //            vh.special_item_icon.setImageResource(info.getSpecial_icon());
        //            vh.special_item_title.setText(info.getSpecial_name());
        //            vh.special_item_content.setText(info.getSpecial_content());
        //            if (info.getUnread_type() == 0) {//通讯录好友
        //                ModuleMgr.getUnreadMgr().registerBadge(vh.special_item_unread, false, UnreadHelper.CONTACTS_FRIENDS);
        //            } else if (info.getUnread_type() == 1) {//好友申请
        //                ModuleMgr.getUnreadMgr().registerBadge(vh.special_item_unread, false, UnreadHelper.CONTACTS_APPLICANT);
        //            }
        //
        //            convertView.setOnClickListener(new View.OnClickListener() {
        //                @Override
        //                public void onClick(View v) {
        //                    switch (position) {
        //                        case 0: //通讯录好友
        //                            UIShow.showFriendsPhone((FragmentActivity) getContext());
        //                            break;
        //                        case 1: //好友申请
        //                            UIShow.showFriensdAddMe((FragmentActivity) getContext());
        //                            break;
        //                        case 2: //添加好友
        //                            UIShow.showFriendsMeAdd((FragmentActivity) getContext());
        //                            break;
        //                    }
        //                }
        //            });
        //
        //        } else if (position == 3) {
        //            vh.showFirstFriend();
        //            setFriendInfo(vh, info, convertView);
        //        } else {
        //            vh.showFriendItem();
        //            setFriendInfo(vh, info, convertView);
        //        }
        return convertView;
    }

    class ViewHolder {
        ImageView friend_item_img_head, friend_item_img_vip, friend_item_img_dynamic, friend_item_img_photo, friend_item_img_right;
        TextView friend_item_txv_name, friend_item_txv_intimacy, friend_item_txv_describe, friend_item_txv_num,friend_item_txv_title;


        //        CircleImageView special_item_icon, info_item_icon;
        //        ImageView info_item_vip_state;
        //        TextView special_item_title, special_item_content;
        //        BadgeView special_item_unread;
        //
        //        TextView info_item_nickname, info_item_height, info_item_city, info_item_time;
        //        GenderAgeView info_item_age;
        //
        //        View friend_special_item, friend_item_driver, friend_info_item, info_item_isOnline;


        public ViewHolder(View convertView) {
            initView(convertView);
        }

        private void initView(View convertView) {
            friend_item_img_head = (ImageView) convertView.findViewById(R.id.friend_item_img_head);
            friend_item_img_vip = (ImageView) convertView.findViewById(R.id.friend_item_img_vip);
            friend_item_img_dynamic = (ImageView) convertView.findViewById(R.id.friend_item_img_dynamic);
            friend_item_img_photo = (ImageView) convertView.findViewById(R.id.friend_item_img_photo);
            friend_item_img_right = (ImageView) convertView.findViewById(R.id.friend_item_img_right);

            friend_item_txv_name = (TextView) convertView.findViewById(R.id.friend_item_txv_name);
            friend_item_txv_intimacy = (TextView) convertView.findViewById(R.id.friend_item_txv_intimacy);
            friend_item_txv_describe = (TextView) convertView.findViewById(R.id.friend_item_txv_describe);
            friend_item_txv_num = (TextView) convertView.findViewById(R.id.friend_item_txv_num);
            friend_item_txv_title = (TextView) convertView.findViewById(R.id.friend_item_txv_title);
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
