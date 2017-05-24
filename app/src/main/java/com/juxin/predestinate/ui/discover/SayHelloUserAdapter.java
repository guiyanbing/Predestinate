package com.juxin.predestinate.ui.discover;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;

import java.util.List;

/**
 * Created by zhang on 2017/5/22.
 */

public class SayHelloUserAdapter extends ExBaseAdapter<String> {


    public SayHelloUserAdapter(Context context, List<String> datas) {
        super(context, datas);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.f1_say_hello_user_item);
            vh = new MyViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (MyViewHolder) convertView.getTag();
        }

        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        vh.test_tv.measure(width, height);
        setItemHeight(vh.test_tv.getMeasuredHeight() + 1);

        vh.test_tv.setText(getItem(position).toString());
        if (position == 3) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vh.test_tv.getLayoutParams();
            layoutParams.setMargins(0, 35, 0, 0);
            vh.test_tv.setLayoutParams(layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) vh.test_tv.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, 0);
            vh.test_tv.setLayoutParams(layoutParams);
        }
        return convertView;
    }

    private class MyViewHolder {
        TextView test_tv;

        public MyViewHolder(View convertView) {
            test_tv = (TextView) convertView.findViewById(R.id.test_tv_view);
        }
    }

    private int itemHeight;

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getItemHeight() {
        return itemHeight;
    }
}
