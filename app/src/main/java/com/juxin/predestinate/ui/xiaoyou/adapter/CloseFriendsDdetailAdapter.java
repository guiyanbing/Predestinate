package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;


/**
 * 亲密好友详情页
 * Created by zhang on 2017/3/21.
 */
public class CloseFriendsDdetailAdapter extends BaseRecyclerViewAdapter<FriendsList.FriendInfo> {

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_xiaoyou_label_detial_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        final FriendsList.FriendInfo info = getItem(position);
        //设置具体label的亲密度数据
        //        vh.imgHead
        vh.txvName.setText(info.getNickname());
        vh.txvNum.setText(info.getIntimacy() + "");
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {

        ImageView imgHead;
        TextView txvName, txvIntimate, txvNum;


        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            imgHead =  convertView.findViewById(R.id.friend_Label_detial_img_head);
            txvName =  convertView.findViewById(R.id.friend_Label_detial_txv_name);
            //            txvIntimate = (TextView) convertView.findViewById(R.id.friend_Label_detial_txv_intimate);
            txvNum = convertView.findViewById(R.id.friend_Label_detial_txv_num);
        }
    }

}