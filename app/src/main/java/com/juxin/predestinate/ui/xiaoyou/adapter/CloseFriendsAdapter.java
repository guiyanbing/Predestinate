package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.IntimacyList;


/**
 * 亲密好友首页
 * Created by zm on 2016/9/7.
 */
public class CloseFriendsAdapter extends BaseRecyclerViewAdapter<IntimacyList.IntimacyInfo> {

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_xiaoyou_intimacy_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        IntimacyList.IntimacyInfo info = getItem(position);
        vh.tevName.setText(info.getLabelName()+"");
        vh.tevNum.setText(info.getArr_friends().size()+"人");

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