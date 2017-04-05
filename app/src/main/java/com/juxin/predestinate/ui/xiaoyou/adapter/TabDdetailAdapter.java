package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;


/**
 * 标签分组详情页
 * Created by zhang on 2017/3/21.
 */
public class TabDdetailAdapter extends BaseRecyclerViewAdapter<SimpleFriendsList.SimpleFriendInfo> {

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_xiaoyou_label_detial_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        final SimpleFriendsList.SimpleFriendInfo info = getItem(position);
        //设置具体label的亲密度数据
        vh.txvName.setText(info.getNickname());
        //        vh.imgHead 设置头像
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
            txvIntimate = convertView.findViewById(R.id.friend_Label_detial_txv_intimate);
            txvNum = convertView.findViewById(R.id.friend_Label_detial_txv_num);
            txvIntimate.setVisibility(View.GONE);
            txvNum.setVisibility(View.GONE);
        }
    }

}