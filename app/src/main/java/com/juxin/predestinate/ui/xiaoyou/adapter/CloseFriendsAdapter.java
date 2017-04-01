package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;


/**
 * Created by zm on 2016/9/7.
 */
public class CloseFriendsAdapter extends BaseRecyclerViewAdapter<LabelsList.LabelInfo> {

    @Override
    public int[] getItemLayouts() {

        //zhushi
        return new int[]{R.layout.p1_xiaoyou_intimacy_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        LabelsList.LabelInfo info = getItem(position);
        vh.tevName.setText(info.getLabelName()+"");
        vh.tevNum.setText(info.getNum()+"人");

    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {
        TextView tevName,tevNum;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            tevName = convertView.findViewById(R.id.friend_intimacy_txv_label);
            tevNum = convertView.findViewById(R.id.friend_intimacy_txv_num);
        }
    }
}