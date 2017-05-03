package com.juxin.predestinate.ui.user.check.self;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.user.check.bean.UserPersonInfo;

import java.util.List;

/**
 * UserInfoAct
 */
public class UserDetailAdapter extends ExBaseAdapter<UserPersonInfo> {

    public UserDetailAdapter(Context context, List datas) {
        super(context, datas);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflate(R.layout.f1_user_detail_list_item);
            mHolder.layout_detail_list_item_title = (LinearLayout) convertView.findViewById(R.id.layout_detail_list_item_title);
            mHolder.txt_detail_list_item_title = (TextView) convertView.findViewById(R.id.txt_detail_list_item_title);
            mHolder.txt_detail_list_item_name = (TextView) convertView.findViewById(R.id.txt_detail_list_item_name);
            mHolder.txt_detail_list_item_value = (TextView) convertView.findViewById(R.id.txt_detail_list_item_value);
            mHolder.txt_detail_list_item_contact = (TextView) convertView.findViewById(R.id.txt_detail_list_item_contact);
            mHolder.view_detail_middle_line = (LinearLayout) convertView.findViewById(R.id.view_detail_middle_line);
            mHolder.view_detail_bottom_line = convertView.findViewById(R.id.view_detail_bottom_line);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        UserPersonInfo info = getItem(position);
        showOrHideTopTitle(mHolder, info, position);
        showMiddleOrBottomLine(mHolder, info, position);
        showOrHideContactText(mHolder, info);
        mHolder.txt_detail_list_item_name.setText(info.getName());
        mHolder.txt_detail_list_item_value.setText(info.getValue());
        return convertView;
    }

    private class ViewHolder {
        public LinearLayout layout_detail_list_item_title;
        public TextView txt_detail_list_item_title;
        public TextView txt_detail_list_item_name;
        public TextView txt_detail_list_item_value;
        public TextView txt_detail_list_item_contact;
        public LinearLayout view_detail_middle_line;
        public View view_detail_bottom_line;
    }

    private void showOrHideTopTitle(ViewHolder holder, UserPersonInfo info, int position) {
        if (position > 0) {
            if (info.getTitle().equals(getList().get(position - 1).getTitle())) {
                holder.layout_detail_list_item_title.setVisibility(View.GONE);
            } else {
                holder.layout_detail_list_item_title.setVisibility(View.VISIBLE);
                holder.txt_detail_list_item_title.setText(info.getTitle());
            }
        } else {
            holder.layout_detail_list_item_title.setVisibility(View.VISIBLE);
            holder.txt_detail_list_item_title.setText(info.getTitle());
        }
    }

    private void showMiddleOrBottomLine(ViewHolder holder, UserPersonInfo info, int position) {
        if (position + 1 < getList().size()) {
            if (info.getTitle().equals(getList().get(position + 1).getTitle())) {
                holder.view_detail_middle_line.setVisibility(View.VISIBLE);
                holder.view_detail_bottom_line.setVisibility(View.GONE);
            } else {
                holder.view_detail_bottom_line.setVisibility(View.VISIBLE);
                holder.view_detail_middle_line.setVisibility(View.GONE);
            }
        } else {
            holder.view_detail_bottom_line.setVisibility(View.GONE);
            holder.view_detail_middle_line.setVisibility(View.GONE);
        }
    }

    private void showOrHideContactText(ViewHolder holder, UserPersonInfo info) {
        if (info.getContact() != null) {
            holder.txt_detail_list_item_contact.setVisibility(View.VISIBLE);
            holder.txt_detail_list_item_contact.setText(info.getContact());
        } else {
            holder.txt_detail_list_item_contact.setVisibility(View.GONE);
        }
    }
}
