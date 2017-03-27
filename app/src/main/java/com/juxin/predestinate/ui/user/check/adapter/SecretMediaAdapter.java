package com.juxin.predestinate.ui.user.check.adapter;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.others.SecretMedia;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * 私密相册/视频adapter
 * Created by Su on 2017/3/27.
 */

public class SecretMediaAdapter extends BaseRecyclerViewAdapter {
    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_user_checkinfo_secret_adapter};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        SecretMedia data = (SecretMedia) getItem(position);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }
}
