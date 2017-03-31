package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;

import java.util.List;


/**
 * Created by zhang on 2016/9/7.
 */
public class IntimacyFriendsAdapter extends ExBaseAdapter<LabelsList.LabelInfo> {
    public IntimacyFriendsAdapter(Context context, List<LabelsList.LabelInfo> datas) {
        super(context, datas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.p1_xiaoyou_intimacy_item);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final LabelsList.LabelInfo info = getItem(position);
        return convertView;
    }

    class ViewHolder {
        TextView friend_intimacy_txv_label, friend_intimacy_txv_num;


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
            friend_intimacy_txv_label = (TextView) convertView.findViewById(R.id.friend_intimacy_txv_label);
            friend_intimacy_txv_num = (TextView) convertView.findViewById(R.id.friend_intimacy_txv_num);
            //            special_item_icon = (CircleImageView) convertView.findViewById(R.id.special_item_icon);
            //            info_item_icon = (CircleImageView) convertView.findViewById(R.id.info_item_icon);
            //            info_item_vip_state = (ImageView) convertView.findViewById(R.id.info_item_vip_state);
            //            special_item_title = (TextView) convertView.findViewById(R.id.special_item_title);
            //            special_item_content = (TextView) convertView.findViewById(R.id.special_item_content);
            //            special_item_unread = (BadgeView) convertView.findViewById(R.id.special_item_unread);
            //            info_item_nickname = (TextView) convertView.findViewById(R.id.info_item_nickname);
            //            info_item_height = (TextView) convertView.findViewById(R.id.info_item_height);
            //            info_item_city = (TextView) convertView.findViewById(R.id.info_item_city);
            //            info_item_time = (TextView) convertView.findViewById(R.id.info_item_time);
            //            info_item_age = (GenderAgeView) convertView.findViewById(R.id.info_item_age);
            //            info_item_isOnline = convertView.findViewById(R.id.info_item_isOnline);
            //
            //            friend_special_item = convertView.findViewById(R.id.friend_special_item);
            //            friend_item_driver = convertView.findViewById(R.id.friend_item_driver);
            //            friend_info_item = convertView.findViewById(R.id.friend_info_item);
        }
    }

}
