package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;


/**
 * Created by zm on 2016/9/7.
 */
public class AthenticationAdapter extends BaseRecyclerViewAdapter<String> {
    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_user_athentication_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        String str = getItem(position);
        vh.tevName.setText(str+"");
        //设置认证状态
        //        vh.tevState
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder{
        TextView tevName,tevState;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            tevName = convertView.findViewById(R.id.user_txv_name);
            tevState = convertView.findViewById(R.id.user_tev_state);
        }
    }
}