package com.juxin.predestinate.ui.user.check.adapter;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.others.SecretMedia;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * 私密相册/视频adapter
 * Created by Su on 2017/3/27.
 */

public class SecretMediaAdapter extends BaseRecyclerViewAdapter {
    public static final int SECRET_VIDEO = 2;  // 私密视频

    private int secretType = 1;
    private int params;    // 展示参数

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_user_checkinfo_secret_adapter};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
        SecretMedia data = (SecretMedia) getItem(position);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(params, params);

        ImageView img_media = viewHolder.findViewById(R.id.img_media);
        ImageView img_shade = viewHolder.findViewById(R.id.img_shade);
        TextView tv_num = viewHolder.findViewById(R.id.tv_num);
        img_media.setLayoutParams(lp);
        img_shade.setLayoutParams(lp);

        ImageLoader.loadCenterCrop(App.context, data.getCoverUrl(), img_media);
        if (secretType == SECRET_VIDEO)
            img_shade.setImageResource(R.drawable.p1_user_check_video_lock);
        if (data.getViewTimes() > 0) tv_num.setText("+" + data.getViewTimes());
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setSecretType(int secretType) {
        this.secretType = secretType;
    }

    public void setParams(int params) {
        this.params = params;
    }
}
