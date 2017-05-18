package com.juxin.predestinate.ui.user.check.secret;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.check.secret.bean.VideoPreviewBean;

/**
 * 私密相册/视频页adapter
 * Created by Su on 2017/3/30.
 */

public class UserSecretAdapter extends BaseRecyclerViewAdapter {
    private int params;   // item宽高

    public UserSecretAdapter() {
        int horizontalSpacing = ModuleMgr.getAppMgr().getScreenWidth() / 50;
        params = (ModuleMgr.getAppMgr().getScreenWidth() - 8 * horizontalSpacing) / 3;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_user_secret_adapter};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        VideoPreviewBean data = (VideoPreviewBean) getItem(position);

        ImageView img_preview = viewHolder.findViewById(R.id.iv_video_preview);
        TextView tv_hot = viewHolder.findViewById(R.id.tv_hot_value);
        TextView tv_time = viewHolder.findViewById(R.id.tv_video_time);
        img_preview.setLayoutParams(new RelativeLayout.LayoutParams(params, params));

//        ImageLoader.loadCenterCrop(App.context, data.getCoverUrl(), img_media);
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }
}
