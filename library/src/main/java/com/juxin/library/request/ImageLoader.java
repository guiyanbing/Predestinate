package com.juxin.library.request;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperTransformation;
import com.juxin.library.R;

/**
 * 全局图片请求类：Glide
 * Created by ZRP on 2016/9/8.
 */
public class ImageLoader {

    public static void loadPic(Context context, String url, ImageView view, int defResImg, int errResImg, GifBitmapWrapperTransformation transformation) {
        if (TextUtils.isEmpty(url)) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context instanceof Activity && ((Activity) context).isDestroyed()) return;

        GlideUrl glideUrl = new GlideUrl(url);
        Glide.with(context)
                .load(glideUrl)
                .dontAnimate()
                .placeholder(defResImg)
                .error(errResImg)
                .transform(transformation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, int defResImg, int errResImg) {
        CenterCrop bitmapCenterCrop = new CenterCrop(context);
        GifBitmapWrapperTransformation drawableTransformation = new GifBitmapWrapperTransformation(Glide.get(context).getBitmapPool(), bitmapCenterCrop);

        loadPic(context, url, view, defResImg, errResImg, drawableTransformation);
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int defResImg, int errResImg) {
        FitCenter bitmapFitCenter = new FitCenter(context);
        GifBitmapWrapperTransformation drawableTransformation = new GifBitmapWrapperTransformation(Glide.get(context).getBitmapPool(), bitmapFitCenter);

        loadPic(context, url, view, defResImg, errResImg, drawableTransformation);
    }

    public static void loadCenterCrop(Context context, String url, ImageView view) {
        loadCenterCrop(context, url, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static void loadFitCenter(Context context, String url, ImageView view) {
        loadFitCenter(context, url, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static void loadAvatar(Context context, String url, ImageView view) {
        loadCenterCrop(context, url, view, R.drawable.default_pic, R.drawable.default_pic);
    }
}
