package com.juxin.predestinate.ui.xiaoyou.wode.adapter;

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
import com.juxin.predestinate.ui.xiaoyou.wode.bean.GiftsList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2017/5/3.
 */

public class GiftGridviewAskForAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context mContext;
    private List<GiftsList.GiftInfo> mLists;
    private static final int pSize = 6;

    public GiftGridviewAskForAdapter(Context fContext, List<GiftsList.GiftInfo> lGift, int page) {
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
        ViewHolder vh;
        if (null == convertView) {
            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.f1_act_message_gift_big_items, null);
            vh.llItem = (LinearLayout) convertView.findViewById(R.id.ll_gift_item);
            vh.tvName = (TextView) convertView.findViewById(R.id.tv_gift_name);
            vh.tvMoney = (TextView) convertView.findViewById(R.id.tv_gift_money);
            vh.ivPic = (ImageView) convertView.findViewById(R.id.iv_gift_pic);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        if (null != vh) {
            GiftsList.GiftInfo item = mLists.get(position);
            //加载图片
//            PhotoUtils.loadPicFitCenter(mContext, item.pic, vh.ivPic, 0, 0);
            ImageLoader.loadAvatar(mContext,item.getPic(),vh.ivPic);
            vh.tvName.setText(item.getName());
            vh.tvMoney.setText(item.getMoney() + "");
            if(item.isSelect()){
                vh.llItem.setBackgroundResource(R.drawable.f1_view_select_askfor);
            }else{
                vh.llItem.setBackgroundResource(R.drawable.f1_view_unselect_askfor);
            }
        }
        return convertView;
    }

    private static class ViewHolder {
        LinearLayout llItem;
        TextView tvName, tvMoney;
        ImageView ivPic;
    }
}
