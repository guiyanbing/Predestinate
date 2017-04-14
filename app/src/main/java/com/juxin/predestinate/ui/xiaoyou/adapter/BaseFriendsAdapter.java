package com.juxin.predestinate.ui.xiaoyou.adapter;

import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.BaseFriendInfo;


/**
 * xiaoyouFragment的适配器
 * Created by zm on 2017/3/25.
 */
public abstract class BaseFriendsAdapter<T> extends BaseRecyclerViewAdapter<T> {

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return ((BaseFriendInfo)getItem(position)).getSortKey().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = ((BaseFriendInfo)getList().get(i)).getSortKey();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }
}