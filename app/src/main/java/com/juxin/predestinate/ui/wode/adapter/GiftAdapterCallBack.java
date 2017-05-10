package com.juxin.predestinate.ui.wode.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.third.pagerecyeler.PageRecyclerView;
import com.juxin.predestinate.ui.wode.BottomGiftDialog;
import com.juxin.predestinate.ui.wode.bean.GiftsList;

import java.util.List;


/**
 * 礼物列表
 * Created by zm on 2017/4/13.
 */
public class GiftAdapterCallBack implements PageRecyclerView.CallBack{

    private Context mContext;
    private List<GiftsList.GiftInfo> listData;
    private PageRecyclerView.PageAdapter mPageAdapter;
    private BottomGiftDialog mBottomGiftDialog;
    private int oldPosition = -1;
    private int sum;

    public GiftAdapterCallBack(Context context,BottomGiftDialog mBottomGiftDialog, List<GiftsList.GiftInfo> listData) {
        mContext = context;
        this.listData = listData;
        this.mBottomGiftDialog = mBottomGiftDialog;
    }

    public void setPageAdapter(PageRecyclerView.PageAdapter pageAdapter){
        mPageAdapter = pageAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.p1_bottom_gif_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (listData != null && listData.size()>0){
            MyViewHolder vh = (MyViewHolder) holder;
            final GiftsList.GiftInfo info  = listData.get(position);
            vh.txvGifName.setText(info.getName());
            //        vh.txvIntimate.setText("亲密度+"+info.getIntimacy());
            vh.txvNeedStone.setText(info.getMoney() + "");
            ImageLoader.loadAvatar(mContext, info.getPic(), vh.img);
//            Log.e("TTTTTTTTHHHH",vh.txvGifName+"|||");
            if (!info.isShow()){
                //            vh.llTop.setVisibility(View.INVISIBLE);
                vh.llTop.setBackgroundResource(R.color.white);
                //            vh.txvIntimate.setVisibility(View.INVISIBLE);
            }else {
                //            vh.llTop.setVisibility(View.VISIBLE);
                vh.llTop.setBackgroundResource(R.drawable.gift_item_bg);
                //            vh.txvIntimate.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemClickListener(View view, int position) {
        for (int i = 0;i<listData.size();i++){
            listData.get(i).setIsShow(false);
        }
        listData.get(position).setIsShow(true);
        //                mGiftAdapter.setList(mGiftAdapter.getList());
        if (oldPosition != -1)
            mPageAdapter.notifyItemChanged(oldPosition);//刷新界面
        mPageAdapter.notifyItemChanged(position);
        if (oldPosition == position){
            sum++;
        }else {
            sum = 1;
        }
        mBottomGiftDialog.onSelectNumChanged(sum,sum*listData.get(position).getMoney(),position);
        oldPosition = position;
    }

    @Override
    public void onItemLongClickListener(View view, int position) {

    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView txvGifName, txvIntimate, txvNeedStone;
        LinearLayout llTop;


        public MyViewHolder(View convertView) {
            super(convertView);
            initView(convertView);
        }

        private void initView(View convertView) {
            img = (ImageView) convertView.findViewById(R.id.bottom_gif_item_img);
            txvGifName = (TextView) convertView.findViewById(R.id.bottom_gif_item_txvgifname);
//            txvIntimate = (TextView) convertView.findViewById(R.id.bottom_gif_item_txvintimacy);
            txvNeedStone = (TextView) convertView.findViewById(R.id.bottom_gif_item_txvneedstone);
            llTop = (LinearLayout) convertView.findViewById(R.id.bottom_gif_item_ll_top);
        }
    }

}