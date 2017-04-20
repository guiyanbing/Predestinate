package com.juxin.predestinate.ui.pay;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;

/**
 * Created by Kind on 2017/4/19.
 */

public class BasePayPannel extends BaseViewPanel implements View.OnClickListener {

    public FragmentActivity activity;

    public ImageView paylist_item_icon;
    public TextView paylist_item_title, paylist_item_remark;


    public BasePayPannel(FragmentActivity activity) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.y2_paylist_item);
        findViewById(R.id.paylist_item).setOnClickListener(this);
        paylist_item_icon = (ImageView) findViewById(R.id.paylist_item_icon);
        paylist_item_title = (TextView) findViewById(R.id.paylist_item_title);
        paylist_item_remark = (TextView) findViewById(R.id.paylist_item_remark);




    }

    /**
     * activity
     *
     * @return
     */
    public FragmentActivity getActivity() {
        return this.activity;
    }

    @Override
    public void onClick(View v) {}


    public void setTextTitle(String text){
        paylist_item_title.setText(text);
    }

    public void setTextRemark(String text){
        paylist_item_remark.setText(text);
    }

    public void setImageIcon(int resId){
        paylist_item_icon.setImageResource(resId);
    }

}
