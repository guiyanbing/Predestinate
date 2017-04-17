package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.view.View;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.GiftsNumInfo;
import com.juxin.predestinate.ui.xiaoyou.zanshi.view.GiftNumPopup;


/**
 * 礼物列表
 * Created by zm on 2017/4/13.
 */
public class GiftNumAdapter extends BaseRecyclerViewAdapter<GiftsNumInfo> {

    private GiftNumPopup mGiftNumPopup;
    private GiftNumPopup.OnSelectNumChangedListener mOnSelectNumChangedListener;
    public GiftNumAdapter(GiftNumPopup mGiftNumPopup){
        this.mGiftNumPopup = mGiftNumPopup;
    }
    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_bottom_gif_num_pop_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
        final GiftsNumInfo info = getItem(position);
        vh.txvNum.setText(info.getNum()+"");
        vh.txvMoral.setText(info.getMoral() + "");
        this.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
//                Log.e("TTTTTTTTTGG",mOnSelectNumChangedListener+"|||");
                if (mOnSelectNumChangedListener != null){
                    mOnSelectNumChangedListener.onSelectNumChanged(GiftNumAdapter.this.getItem(position).getNum());
                }
                mGiftNumPopup.dismiss();
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setOnSelectNumChangedListener(GiftNumPopup.OnSelectNumChangedListener mOnSelectNumChangedListener){
        this.mOnSelectNumChangedListener = mOnSelectNumChangedListener;
    }

    class MyViewHolder {

        TextView txvNum, txvMoral;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            txvNum =  convertView.findViewById(R.id.bottom_gif_num_pop_item_num);
            txvMoral =  convertView.findViewById(R.id.bottom_gif_num_pop_item_moral);
        }
    }

}