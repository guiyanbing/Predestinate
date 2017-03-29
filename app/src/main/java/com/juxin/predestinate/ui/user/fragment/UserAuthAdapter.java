package com.juxin.predestinate.ui.user.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.unread.BadgeView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * 个人中心布局adapter
 * Created by Su on 2017/3/28.
 */

public class UserAuthAdapter extends BaseRecyclerViewAdapter {
    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_user_fragment_list_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        UserAuth userAuth = (UserAuth) getItem(position);

        View item_view = viewHolder.findViewById(R.id.item_view);
        ImageView item_icon = viewHolder.findViewById(R.id.item_icon);
        TextView item_text = viewHolder.findViewById(R.id.item_text);
        TextView item_hint = viewHolder.findViewById(R.id.item_hint);
        BadgeView item_badge = viewHolder.findViewById(R.id.item_badge);
        View divider_line = viewHolder.findViewById(R.id.divider_line);
        View divider_view = viewHolder.findViewById(R.id.divider_view);

        //区块划分
        int select = getListItemSelection(userAuth, position);
        if (select == 1) {
            divider_line.setVisibility(View.GONE);
            divider_view.setVisibility(View.VISIBLE);
        } else if (select == -1) {
            divider_line.setVisibility(View.VISIBLE);
            divider_view.setVisibility(View.GONE);
        } else {
            divider_line.setVisibility(View.GONE);
            divider_view.setVisibility(View.GONE);
        }

        //角标提示
        item_badge.setVisibility(View.GONE);
        item_hint.setVisibility(View.GONE);

        if (!userAuth.isShow()) {
            item_view.setVisibility(View.GONE);
            return;
        }
        item_view.setVisibility(View.VISIBLE);
        item_icon.setBackgroundResource(userAuth.getRes());
        item_text.setText(userAuth.getName());
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    /**
     * 处理当前item的level
     */
    private int getListItemSelection(UserAuth userAuth, int position) {
        if (position + 1 < getItemCount()) {
            if (userAuth.getLevel() != ((UserAuth) getItem(position + 1)).getLevel()) {
                return 1;//不同区块
            }
        } else {
            return 0;//集合末尾
        }
        return -1;//相同区块
    }
}