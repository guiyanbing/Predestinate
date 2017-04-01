package com.juxin.predestinate.ui.user.paygoods;

import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * 通用商品列表adapter
 * Created by Su on 2017/3/31.
 */

public class GoodsListAdapter extends BaseRecyclerViewAdapter {
    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_goods_list_adapter};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {

        ImageView img_choose = viewHolder.findViewById(R.id.iv_choose);
        TextView tv_goods = viewHolder.findViewById(R.id.tv_goods);
        TextView tv_offers = viewHolder.findViewById(R.id.tv_offers);
        TextView tv_money = viewHolder.findViewById(R.id.tv_money);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }
}
