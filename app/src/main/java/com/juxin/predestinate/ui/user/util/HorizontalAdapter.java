package com.juxin.predestinate.ui.user.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserPhoto;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;

import java.util.List;

/**
 * HorizontalListView适配器
 * Created by Su on 2017/5/12.
 */
public class HorizontalAdapter extends ExBaseAdapter<UserPhoto> {
    private int showType = AlbumHorizontalPanel.EX_HORIZONTAL_ALBUM;
    private int params;

    // data
    private String url;

    public HorizontalAdapter(Context context, int showType, int params, List<UserPhoto> datas) {
        super(context, datas);
        this.showType = showType;
        this.params = params;
    }

    public void setParams(int params) {
        this.params = params;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = inflate(R.layout.p1_user_checkinfo_secret_adapter);
            mHolder.img_media = (ImageView) convertView.findViewById(R.id.img_media);
            mHolder.img_shade = (ImageView) convertView.findViewById(R.id.img_shade);
            mHolder.tv_num = (TextView) convertView.findViewById(R.id.tv_num);
            mHolder.img_media.setLayoutParams(new RelativeLayout.LayoutParams(params, params));
            mHolder.img_shade.setLayoutParams(new RelativeLayout.LayoutParams(params, params));
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }

        // 展示相册
        if (showType == AlbumHorizontalPanel.EX_HORIZONTAL_ALBUM) {
            UserPhoto userPhoto = getItem(position);
            url = userPhoto.getThumb();
        }

        ImageLoader.loadCenterCrop(App.context, url, mHolder.img_media);
        if (showType == AlbumHorizontalPanel.EX_HORIZONTAL_VIDEO)
            mHolder.img_shade.setImageResource(R.drawable.p1_user_check_video_lock);
        return convertView;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private class ViewHolder {
        ImageView img_media, img_shade;
        TextView tv_num;
    }
}