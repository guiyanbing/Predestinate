package com.juxin.predestinate.ui.user.paygoods;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * 通用商品列表adapter
 * Created by Su on 2017/3/31.
 */

public class GoodsListAdapter extends BaseRecyclerViewAdapter {
    private int selectPosition = 0;

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_goods_list_adapter};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {
        final String data = (String) getItem(position);

        RelativeLayout payItem = viewHolder.findViewById(R.id.pay_item);
        ImageView img_choose = viewHolder.findViewById(R.id.iv_choose);
        TextView tv_goods = viewHolder.findViewById(R.id.tv_goods);
        TextView tv_offers = viewHolder.findViewById(R.id.tv_offers);
        TextView tv_money = viewHolder.findViewById(R.id.tv_money);

        // 选中状态
        payItem.setSelected(selectPosition == position);
        img_choose.setVisibility(selectPosition == position ? View.VISIBLE : View.GONE);
    }

    public void updateData(int positon) {
        this.selectPosition = positon;
        notifyDataSetChanged();
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }
}
