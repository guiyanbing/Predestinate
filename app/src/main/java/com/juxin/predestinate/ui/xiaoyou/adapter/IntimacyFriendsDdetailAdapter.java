package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.LabelsList;

import java.util.List;


/**
 * Created by zhang on 2017/3/21.
 */
public class IntimacyFriendsDdetailAdapter extends ExBaseAdapter<LabelsList.LabelInfo> {
    public IntimacyFriendsDdetailAdapter(Context context, List<LabelsList.LabelInfo> datas) {
        super(context, datas);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.p1_xiaoyou_label_detial_item);
            vh = new ViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final LabelsList.LabelInfo info = getItem(position);
        //设置具体label的亲密度数据
        //        vh.imgHead
        vh.txvName.setText(info.getLabelName());
        vh.txvNum.setText(info.getNum() + "");
        return convertView;
    }

    class ViewHolder {

        ImageView imgHead;
        TextView txvName, txvIntimate, txvNum;


        public ViewHolder(View convertView) {
            initView(convertView);
        }

        private void initView(View convertView) {
            imgHead = (ImageView) convertView.findViewById(R.id.friend_Label_detial_img_head);
            txvName = (TextView) convertView.findViewById(R.id.friend_Label_detial_txv_name);
            //            txvIntimate = (TextView) convertView.findViewById(R.id.friend_Label_detial_txv_intimate);
            txvNum = (TextView) convertView.findViewById(R.id.friend_Label_detial_txv_num);
        }
    }

}
