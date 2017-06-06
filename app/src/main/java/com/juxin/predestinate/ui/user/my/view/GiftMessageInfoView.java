package com.juxin.predestinate.ui.user.my.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.view.roadlights.LMarqueeFactory;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.GiftMessageList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

/**
 * 跑马灯item项
 * Created by zm on 2017/1/8
 */
public class GiftMessageInfoView extends LMarqueeFactory<LinearLayout, GiftMessageList.GiftMessageInfo> {

    private Context mContext;
    private int mGender = 1;

    public GiftMessageInfoView(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        this.mGender = ModuleMgr.getCenterMgr().getMyInfo().getGender();
    }

    @Override
    public LinearLayout generateMarqueeItemView(GiftMessageList.GiftMessageInfo data) {
        LinearLayout linearLayout = (LinearLayout) inflate(R.layout.f1_gift_message_info_view);
        TextView txt_one = (TextView) linearLayout.findViewById(R.id.txt_info_one);
        TextView txt_two = (TextView) linearLayout.findViewById(R.id.txt_info_two);
        TextView txt_three = (TextView) linearLayout.findViewById(R.id.txt_info_three);
        TextView txt_four = (TextView) linearLayout.findViewById(R.id.txt_info_four);
        TextView txt_five = (TextView) linearLayout.findViewById(R.id.txt_info_five);

        if (mGender == 1) {
            txt_one.setText(data.getFrom_name());
            txt_two.setText(R.string.lmarquee_just_now_man);
            txt_three.setText(data.getTo_name());
            txt_four.setText(R.string.lmarquee_send_man);
            txt_five.setText(data.getNum() + mContext.getString(R.string.num) + data.getGname());
        } else if (mGender == 2) {
            txt_one.setText(data.getTo_name());
            txt_two.setText(R.string.lmarquee_just_now_woman);
            txt_three.setText(data.getFrom_name());
            txt_four.setText(R.string.lmarquee_send_woman);
            txt_five.setText(data.getNum() + mContext.getString(R.string.num) + data.getGname());
        }
        return linearLayout;
    }
}