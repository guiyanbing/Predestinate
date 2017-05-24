package com.juxin.predestinate.ui.user.my.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.view.roadlights.LMarqueeFactory;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.GiftMessageList;


/**
 * Created by zm on 2017/1/8
 */
public class GiftMessageInforView extends LMarqueeFactory<LinearLayout, GiftMessageList.GiftMessageInfo> {

    public GiftMessageInforView(Context mContext) {
        super(mContext);
    }

    @Override
    public LinearLayout generateMarqueeItemView(GiftMessageList.GiftMessageInfo data) {
        LinearLayout linearLayout = (LinearLayout) inflate(R.layout.f1_gift_message_info_view);
        TextView txt_one = (TextView) linearLayout.findViewById(R.id.txt_info_one);
        TextView txt_two = (TextView) linearLayout.findViewById(R.id.txt_info_two);
        TextView txt_three = (TextView) linearLayout.findViewById(R.id.txt_info_three);
        TextView txt_four = (TextView) linearLayout.findViewById(R.id.txt_info_four);
        txt_one.setText(data.getFrom_name()+"");
        txt_two.setText(R.string.just_now);
        txt_three.setText(data.getTo_name()+"");
        txt_four.setText(mContext.getString(R.string.sent)+data.getNum()+mContext.getString(R.string.ge)+data.getGname());
        return linearLayout;
    }
}