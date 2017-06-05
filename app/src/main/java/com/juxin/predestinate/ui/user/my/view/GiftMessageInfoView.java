package com.juxin.predestinate.ui.user.my.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.view.roadlights.LMarqueeFactory;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.GiftMessageList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

/**
 * Created by zm on 2017/1/8
 */
public class GiftMessageInfoView extends LMarqueeFactory<LinearLayout, GiftMessageList.GiftMessageInfo> {

    private int mGender = 1;

    public GiftMessageInfoView(Context mContext) {
        super(mContext);
        this.mGender = ModuleMgr.getCenterMgr().getMyInfo().getGender();
    }

    @Override
    public LinearLayout generateMarqueeItemView(GiftMessageList.GiftMessageInfo data) {
        LinearLayout linearLayout = (LinearLayout) inflate(R.layout.f1_gift_message_info_view);
        LinearLayout content = (LinearLayout) linearLayout.findViewById(R.id.content);
        TextView txt_one = (TextView) linearLayout.findViewById(R.id.txt_info_one);
        TextView txt_two = (TextView) linearLayout.findViewById(R.id.txt_info_two);
        TextView txt_three = (TextView) linearLayout.findViewById(R.id.txt_info_three);
        TextView txt_four = (TextView) linearLayout.findViewById(R.id.txt_info_four);
        TextView txt_five = (TextView) linearLayout.findViewById(R.id.txt_info_five);
        if (mGender == 1) {
            content.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lmarquee_boy_bg));
            txt_one.setTextColor(ContextCompat.getColor(mContext, R.color.lmarquee_boy_nick));
            txt_two.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            txt_three.setTextColor(ContextCompat.getColor(mContext, R.color.lmarquee_boy_nick));
            txt_four.setTextColor(ContextCompat.getColor(mContext, R.color.lmarquee_boy_gift));
            txt_five.setTextColor(ContextCompat.getColor(mContext, R.color.white));

            txt_one.setText(data.getFrom_name() + "");
            txt_two.setText(R.string.just_now);
            txt_three.setText(data.getTo_name() + "");
            txt_four.setText(data.getGname());
            txt_five.setText(R.string.very_rich);
        } else if (mGender == 2) {
            content.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lmarquee_girl_bg));
            txt_one.setTextColor(ContextCompat.getColor(mContext, R.color.lmarquee_girl_nick_gift));
            txt_two.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            txt_three.setTextColor(ContextCompat.getColor(mContext, R.color.lmarquee_girl_nick_gift));
            txt_four.setTextColor(ContextCompat.getColor(mContext, R.color.black));

            txt_one.setText(data.getTo_name() + "");
            txt_two.setText(R.string.just_now_get);
            txt_three.setText(data.getGname() + "");
            txt_four.setText(R.string.very_greater);
        }
        return linearLayout;
    }
}