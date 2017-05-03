package com.juxin.predestinate.ui.xiaoyou.wode.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.config.Diamond;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;


/**
 * 礼物列表
 * Created by zm on 2017/4/13.
 */
public class MyDiamondAdapter extends BaseRecyclerViewAdapter<Diamond> {

    private Context mContext;

    public MyDiamondAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.f1_wode_my_diamond_item};
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder= super.onCreateViewHolder(parent, viewType);
        return viewHolder;
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {

        MyViewHolder vh = new MyViewHolder(viewHolder);
        final Diamond info = getItem(position);
        vh.tv_num.setText(info.getNum()+"钻石");
        vh.tv_price.setText("￥" + info.getCost() + "");
        vh.tv_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/4/20 跳转到支付页面的具体逻辑(和position有关)
                UIShow.showPayListAct((FragmentActivity) mContext, 56);
                //                ((MyDiamondsAct) mContext).handler.sendEmptyMessage(info.getPid());
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {

        ImageView img_diamond;
        TextView tv_num;
        TextView tv_price;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            img_diamond = convertView.findViewById(R.id.wode_my_diamond_item_img_diamond);
            tv_num = convertView.findViewById(R.id.wode_my_diamond_item_tv_num);
            tv_price = convertView.findViewById(R.id.wode_my_diamond_item_tv_price);
        }
    }
}