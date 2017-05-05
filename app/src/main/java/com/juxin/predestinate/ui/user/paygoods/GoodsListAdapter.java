package com.juxin.predestinate.ui.user.paygoods;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGood;

/**
 * 通用商品列表adapter
 * Created by Su on 2017/3/31.
 */

public class GoodsListAdapter extends BaseRecyclerViewAdapter {
    private int selectPosition = 0;
    private int itemType = 0;       // 展示布局
    private int chargeType = 0;     // 充值种类

    public GoodsListAdapter() {
    }

    public GoodsListAdapter(int itemType) {
        this.chargeType = itemType;

        if (itemType > 0)       // 非0状态下都引用第二种布局
            this.itemType = 1;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_goods_list_adapter, R.layout.f1_goods_list_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {
        final PayGood data = (PayGood) getItem(position);

        RelativeLayout payItem = viewHolder.findViewById(R.id.pay_item);
        CustomFrameLayout payBg = viewHolder.findViewById(R.id.pay_bg);
        ImageView img_choose = viewHolder.findViewById(R.id.iv_choose);
        ImageView goods_ico = viewHolder.findViewById(R.id.goods_ico);
        TextView tv_name = viewHolder.findViewById(R.id.tv_name);       // 商品名称
        TextView tv_money = viewHolder.findViewById(R.id.tv_money);     // 商品价格
        TextView tv_desc = viewHolder.findViewById(R.id.tv_desc);       // 商品活动

        // 选中状态
        payItem.setSelected(selectPosition == position);
        payBg.showOfIndex(selectPosition == position ? 1 : 0);

        // 设置数据
        if (!TextUtils.isEmpty(data.getName())) {
            tv_name.setVisibility(View.VISIBLE);
            tv_name.setText(data.getName());
        }

        if (itemType == 1) {
            tv_money.setText(Html.fromHtml("&#165;").toString() + data.getPrice());
        } else {
            tv_money.setText(data.getPrice() + "元");
        }

        if (!TextUtils.isEmpty(data.getDesc())) {
            tv_desc.setVisibility(View.VISIBLE);
            tv_desc.setText(data.getDesc());
        }
    }

    public void updateData(int positon) {
        this.selectPosition = positon;
        notifyDataSetChanged();
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return itemType;
    }
}
