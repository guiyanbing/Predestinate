package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;


/**
 * 亲密好友首页Adapter
 * Created by zhang on 2016/9/7.
 */
public class IntimacyFriendsAdapter extends BaseRecyclerViewAdapter<LabelsList.LabelInfo> {

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_xiaoyou_intimacy_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        final LabelsList.LabelInfo info = getItem(position);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {
        TextView friend_intimacy_txv_label, friend_intimacy_txv_num;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            friend_intimacy_txv_label = convertView.findViewById(R.id.friend_intimacy_txv_label);
            friend_intimacy_txv_num = convertView.findViewById(R.id.friend_intimacy_txv_num);
        }
    }
}