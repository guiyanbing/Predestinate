package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;

import java.util.List;


/**
 * Created by zm on 2016/9/7.
 */
public class CloseFriendsAdapter extends ExBaseAdapter<LabelsList.LabelInfo> {
    public CloseFriendsAdapter(Context context, List<LabelsList.LabelInfo> datas) {
        super(context, datas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.p1_xiaoyou_intimacy_item);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        LabelsList.LabelInfo info = getItem(position);
        vh.tevName.setText(info.getLabelName()+"");
        vh.tevNum.setText(info.getNum()+"人");

        return convertView;
    }

    class ViewHolder {
        TextView tevName,tevNum;

        public ViewHolder(View convertView) {
            initView(convertView);
        }

        private void initView(View convertView) {
            tevName = (TextView) convertView.findViewById(R.id.friend_intimacy_txv_label);
            tevNum = (TextView) convertView.findViewById(R.id.friend_intimacy_txv_num);
        }
    }
}
