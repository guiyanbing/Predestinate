package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;

import java.util.List;


/**
 * 选择联系人页面适配
 * Created by zm on 2016/9/7.
 */
public class SelectFriendsAdapter extends ExBaseAdapter<SimpleFriendsList.SimpleFriendInfo> {

    private OnContactSelect contactSelect;
    public SelectFriendsAdapter(Context context, List<SimpleFriendsList.SimpleFriendInfo> datas,OnContactSelect contactSelect) {
        super(context, datas);
        this.contactSelect = contactSelect;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder vh;
        if (convertView == null) {
            convertView = inflate(R.layout.p1_xiaoyou_select_contact_item);
            vh = new ViewHolder();

            vh.chbSel = (CheckBox) convertView.findViewById(R.id.xiaoyou_select_chb_sel);
            vh.invite_catalog_divider_1 = convertView.findViewById(R.id.invite_catalog_divider_1);
            vh.invite_catalog_layout = (LinearLayout) convertView.findViewById(R.id.invite_catalog_layout);
            vh.invite_catalog = (TextView) convertView.findViewById(R.id.invite_catalog);
            vh.invite_catalog_divider_2 = convertView.findViewById(R.id.invite_catalog_divider_2);
            vh.invite_msg_info_layout = (RelativeLayout) convertView.findViewById(R.id.invite_msg_info_layout);
            vh.imgHead = (ImageView) convertView.findViewById(R.id.xiaoyou_select_img_head);
            vh.txvName = (TextView) convertView.findViewById(R.id.xiaoyou_select_txv_name);
            vh.invite_msg_divider_line = convertView.findViewById(R.id.invite_msg_divider_line);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        SimpleFriendsList.SimpleFriendInfo info = getItem(position);
        if (info.isCheck()){
            vh.chbSel.setChecked(true);
        }else {
            vh.chbSel.setChecked(false);
        }
        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            vh.invite_catalog_layout.setVisibility(View.VISIBLE);
            vh.invite_catalog.setText(info.getSortKey());
            vh.invite_catalog_divider_1.setVisibility(View.VISIBLE);
            vh.invite_catalog_divider_2.setVisibility(View.VISIBLE);
        } else {
            vh.invite_catalog_layout.setVisibility(View.GONE);
            vh.invite_catalog_divider_1.setVisibility(View.GONE);
            vh.invite_catalog_divider_2.setVisibility(View.GONE);
        }

        if (position + 1 < getList().size()) {
            int temp = getSectionForPosition(position + 1);
            if (position + 1 == getPositionForSection(temp)) {
                vh.invite_msg_divider_line.setVisibility(View.GONE);
            } else {
                vh.invite_msg_divider_line.setVisibility(View.VISIBLE);
            }
        } else {
            vh.invite_msg_divider_line.setVisibility(View.GONE);
        }

        vh.txvName.setText(info.getNickname()+"");
        //设置头像
//        vh.imgHead

        vh.invite_msg_info_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(position);
            }
        });

        vh.chbSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(position);
            }
        });
        return convertView;
    }


    private void setChecked(int position) {
        if (getItem(position) != null) {
            getItem(position).setIsCheck(!getItem(position).isCheck());
            notifyDataSetChanged();
            if (contactSelect != null) {
                contactSelect.onSelectChange(getList());
            }
        }
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getItem(position).getSortKey().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getList().get(i).getSortKey();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    class ViewHolder {
        CheckBox chbSel;
        LinearLayout invite_catalog_layout;
        ImageView imgHead;
        TextView invite_catalog, txvName;
        View invite_msg_divider_line, invite_catalog_divider_1, invite_catalog_divider_2;
        RelativeLayout invite_msg_info_layout;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<SimpleFriendsList.SimpleFriendInfo> list) {
        setList(list);
        notifyDataSetChanged();
    }

    public interface OnContactSelect {
        void onSelectChange(List<SimpleFriendsList.SimpleFriendInfo> list);
    }
}
