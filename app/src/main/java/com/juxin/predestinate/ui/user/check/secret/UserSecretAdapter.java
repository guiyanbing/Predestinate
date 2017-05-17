package com.juxin.predestinate.ui.user.check.secret;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * 私密相册/视频页adapter
 * Created by Su on 2017/3/30.
 */

public class UserSecretAdapter extends BaseRecyclerViewAdapter {

    private boolean isDele;

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_user_secret_adapter};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, int position) {
//        SecretMedia data = (SecretMedia) getItem(position);
        final String data = (String) getItem(position);

        ImageView img_media = viewHolder.findViewById(R.id.img_secret);
        TextView tv_publishTime = viewHolder.findViewById(R.id.publish_time);
        ImageView btn_delete = viewHolder.findViewById(R.id.btn_delete);

        btn_delete.setVisibility(isDele ? View.VISIBLE : View.GONE);
//        ImageLoader.loadCenterCrop(App.context, data.getCoverUrl(), img_media);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PToast.showShort(data);
            }
        });
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    public void setDele(boolean isDele) {
        this.isDele = isDele;
    }
}