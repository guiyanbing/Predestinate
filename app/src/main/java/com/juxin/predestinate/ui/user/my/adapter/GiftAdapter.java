package com.juxin.predestinate.ui.user.my.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.GiftsList;

import java.util.ArrayList;
import java.util.List;


/**
 * 礼物列表
 * Created by zm on 2017/4/13.
 */
public class GiftAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private List<GiftsList.GiftInfo> mLists;
    private int pSize = 6;

    public GiftAdapter(Context fContext, List<GiftsList.GiftInfo> lGift, int page,int pSize) {
        this.pSize = pSize;
        this.mContext = fContext;
        this.inflater = LayoutInflater.from(fContext);
        mLists = new ArrayList<>();
        int start = page * pSize;
        int end = start + pSize;
        while ((start < lGift.size()) && (start < end)) {
            mLists.add(lGift.get(start));
            start++;
        }
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder vh;
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.p1_bottom_gif_item, null);
            vh = new MyViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (MyViewHolder) convertView.getTag();
        }

        if (mLists != null && mLists.size()>0){
            final GiftsList.GiftInfo info  = mLists.get(position);
            vh.txvGifName.setText(info.getName());
            //        vh.txvIntimate.setText("亲密度+"+info.getIntimacy());
            vh.txvNeedStone.setText(info.getMoney() + mContext.getString(R.string.diamond));
            ImageLoader.loadAvatar(mContext, info.getPic(), vh.img);
            //            Log.e("TTTTTTTTHHHH",vh.txvGifName+"|||");
            if (!info.isSelect()){
                //            vh.llTop.setVisibility(View.INVISIBLE);
                vh.llTop.setBackgroundResource(R.color.white);
                //            vh.txvIntimate.setVisibility(View.INVISIBLE);
            }else {
                //            vh.llTop.setVisibility(View.VISIBLE);
                vh.llTop.setBackgroundResource(R.drawable.gift_item_bg);
                //            vh.txvIntimate.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    class MyViewHolder{

        ImageView img;
        TextView txvGifName, txvIntimate, txvNeedStone;
        LinearLayout llTop;


        public MyViewHolder(View convertView) {
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