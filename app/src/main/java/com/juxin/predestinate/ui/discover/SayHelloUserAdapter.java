package com.juxin.predestinate.ui.discover;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.local.chat.utils.SortList;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.mail.base.CustomLetterMailItem;

import java.util.List;

/**
 * Created by zhang on 2017/5/22.
 */

public class SayHelloUserAdapter extends ExBaseAdapter<BaseMessage> {

    public SayHelloUserAdapter(Context context, List<BaseMessage> datas) {
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

        vh.letterMailItem.showData(getItem(position));

        if (getItemHeight() == 0) {
            int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            vh.letterMailItem.measure(width, height);
            setItemHeight(vh.letterMailItem.getMeasuredHeight());
        }

        return convertView;
    }

    class MyViewHolder {

        CustomLetterMailItem letterMailItem;

        public MyViewHolder(View convertView) {
            initView(convertView);
        }

        private void initView(View convertView) {
            letterMailItem = (CustomLetterMailItem) convertView.findViewById(R.id.say_hello_users_item);
            letterMailItem.init();
        }
    }

    private int itemHeight;

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    @Override
    public void notifyDataSetChanged() {
        if (getList() != null && getList().size() != 0) {
            SortList.sortWeightTimeListView(getList());
        }
        super.notifyDataSetChanged();
    }
}
