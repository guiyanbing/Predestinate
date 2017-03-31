package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;

import java.util.List;


/**
 * Created by zm on 2016/9/7.
 */
public class AthenticationAdapter extends ExBaseAdapter<String> {
    public AthenticationAdapter(Context context, List<String> datas) {
        super(context, datas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.p1_user_athentication_item);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        String str = getItem(position);
        vh.tevName.setText(str+"");
        //设置认证状态
//        vh.tevState

        return convertView;
    }

    class ViewHolder {
        TextView tevName,tevState;

        public ViewHolder(View convertView) {
            initView(convertView);
        }

        private void initView(View convertView) {
            tevName = (TextView) convertView.findViewById(R.id.user_txv_name);
            tevState = (TextView) convertView.findViewById(R.id.user_tev_state);
        }
    }
}
