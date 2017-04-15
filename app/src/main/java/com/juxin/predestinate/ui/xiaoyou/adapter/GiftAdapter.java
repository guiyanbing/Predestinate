package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.GiftsList;


/**
 * 礼物列表
 * Created by zm on 2017/4/13.
 */
public class GiftAdapter extends BaseRecyclerViewAdapter<GiftsList.GiftInfo> {

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_bottom_gif_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        final GiftsList.GiftInfo info = getItem(position);
        vh.txvGifName.setText(info.getName());
        vh.txvIntimate.setText(info.getIntimacy()+"");
        vh.txvNeedStone.setText(info.getStone()+"");
        vh.img.setBackgroundResource(info.getIcon());
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {

        ImageView img;
        TextView txvGifName, txvIntimate, txvNeedStone;
        LinearLayout llTop;


        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            img =  convertView.findViewById(R.id.bottom_gif_item_img);
            txvGifName =  convertView.findViewById(R.id.bottom_gif_item_txvgifname);
            txvIntimate =  convertView.findViewById(R.id.bottom_gif_item_txvintimacy);
            txvNeedStone =  convertView.findViewById(R.id.bottom_gif_item_txvneedstone);
            llTop = convertView.findViewById(R.id.bottom_gif_item_ll_top);
        }
    }

}