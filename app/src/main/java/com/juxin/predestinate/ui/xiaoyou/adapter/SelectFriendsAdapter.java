package com.juxin.predestinate.ui.xiaoyou.adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.bean.SimpleFriendsList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * 选择联系人页面适配
 * Created by zm on 2016/9/7.
 */
public class SelectFriendsAdapter extends BaseFriendsAdapter<SimpleFriendsList.SimpleFriendInfo> {

    private OnContactSelect contactSelect;
    private Set<SimpleFriendsList.SimpleFriendInfo> uids = new HashSet<>();

    private void setChecked(int position) {
        if (getItem(position) != null) {
            getItem(position).setIsCheck(!getItem(position).isCheck());
            notifyDataSetChanged();
            if (getItem(position).isCheck()){
                uids.add(getItem(position));
            }else {
                uids.remove(getItem(position));
            }
            if (contactSelect != null) {
                contactSelect.onSelectChange(uids);
            }
        }
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_xiaoyou_select_contact_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {
        MyViewHolder vh = new MyViewHolder(viewHolder);
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
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    class MyViewHolder {
        CheckBox chbSel;
        LinearLayout invite_catalog_layout;
        ImageView imgHead;
        TextView invite_catalog, txvName;
        View invite_msg_divider_line, invite_catalog_divider_1, invite_catalog_divider_2;
        RelativeLayout invite_msg_info_layout;
        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            chbSel = convertView.findViewById(R.id.xiaoyou_select_chb_sel);
            invite_catalog_divider_1 = convertView.findViewById(R.id.invite_catalog_divider_1);
            invite_catalog_layout = convertView.findViewById(R.id.invite_catalog_layout);
            invite_catalog = convertView.findViewById(R.id.invite_catalog);
            invite_catalog_divider_2 = convertView.findViewById(R.id.invite_catalog_divider_2);
            invite_msg_info_layout = convertView.findViewById(R.id.invite_msg_info_layout);
            imgHead = convertView.findViewById(R.id.xiaoyou_select_img_head);
            txvName = convertView.findViewById(R.id.xiaoyou_select_txv_name);
            invite_msg_divider_line = convertView.findViewById(R.id.invite_msg_divider_line);
        }
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

    public Set<SimpleFriendsList.SimpleFriendInfo> getUids(){
        return this.uids;
    }

    public void setOnContactSelectLinear(OnContactSelect mOnContactSelect){
        this.contactSelect = mOnContactSelect;
    }

    public interface OnContactSelect {
        void onSelectChange(Set<SimpleFriendsList.SimpleFriendInfo> list);
    }
}