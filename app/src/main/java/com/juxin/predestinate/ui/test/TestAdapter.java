package com.juxin.predestinate.ui.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.User;

import java.util.List;

/**
 * Created by Steven on 16/12/20.
 */
public class TestAdapter extends BaseAdapter {
    private List<User> mWineList;
    private Context mContext;

    public TestAdapter(Context mContext, List<User> mWineList) {
        this.mWineList = mWineList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mWineList == null ? 0 : mWineList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.test_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_avatar = (TextView) convertView.findViewById(R.id.tv_avatar);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        User wine = mWineList.get(position);
        viewHolder.tv_avatar.setText(wine.getAvatar());
        viewHolder.tv_name.setText(wine.getNickname());

        return convertView;
    }

    class ViewHolder {
        TextView tv_avatar;
        TextView tv_name;
    }
}
