package com.juxin.library.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.juxin.library.R;
import com.juxin.library.image.transform.BlurImage;
import com.juxin.library.image.transform.RoundedCorners;

/**
 * 基于Glide图片请求，处理类
 */
public class ImageLoader {

    /**
     * 加载头像
     */
    public static void loadAvatar(Context context, String url, ImageView view) {
        loadCenterCrop(context, url, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    /**
     * CenterCrop加载图片
     */
    public static void loadCenterCrop(Context context, String url, ImageView view) {
        loadCenterCrop(context, url, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static void loadCenterCrop(Context context, String url, ImageView view, int defResImg, int errResImg) {
        CenterCrop bitmapCenterCrop = new CenterCrop(context);
        loadPic(context, url, view, defResImg, errResImg, bitmapCenterCrop);
    }

    /**
     * FitCenter加载图片
     */
    public static void loadFitCenter(Context context, String url, ImageView view) {
        loadFitCenter(context, url, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int resImg) {
        FitCenter bitmapFitCenter = new FitCenter(context);
        loadPic(context, url, view, resImg, resImg, bitmapFitCenter);
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int defResImg, int errResImg) {
        FitCenter bitmapFitCenter = new FitCenter(context);
        loadPic(context, url, view, defResImg, errResImg, bitmapFitCenter);
    }

    /**
     * 图片圆角处理: 默认全角处理，其他需求自行重载方法
     *
     * @param roundPx 圆角弧度
     */
    public static void loadRoundCorners(Context context, String url, int roundPx, ImageView view) {
        CenterCrop centerCrop = new CenterCrop(context);
        RoundedCorners roundedCorners = new RoundedCorners(context, roundPx, 0, RoundedCorners.CornerType.ALL);
        loadPic(context, url, view, R.drawable.default_pic, R.drawable.default_pic, centerCrop, roundedCorners);
    }

    /**
     * 网络图片高斯模糊处理
     *
     * @param level 模糊等级
     */
    public static void loadBlurImg(Context context, String url, int level, ImageView view) {
        CenterCrop centerCrop = new CenterCrop(context);
        BlurImage blurImage = new BlurImage(context, level);
        loadPic(context, url, view, R.drawable.default_pic, R.drawable.default_pic, centerCrop, blurImage);
    }

    /**
     * 本地图片高斯模糊处理
     */
    public static void localBlurImg(Context context, int localResImg, int level, ImageView view) {
        CenterCrop centerCrop = new CenterCrop(context);
        BlurImage blurImage = new BlurImage(context, level);
        localPic(context, localResImg, view, R.drawable.default_pic, R.drawable.default_pic, centerCrop, blurImage);
    }


    // ==================================== 内部私有调用 =============================================

    /**
     * 网络图片处理
     */
    private static void loadPic(Context context, String url, ImageView view, int defResImg, int errResImg, Transformation<Bitmap>... transformation) {
        if (TextUtils.isEmpty(url)) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context instanceof Activity && ((Activity) context).isDestroyed()) return;

        GlideUrl glideUrl = new GlideUrl(url);
        Glide.with(context)
                .load(glideUrl)
                .dontAnimate()
                .placeholder(defResImg)
                .error(errResImg)
                .bitmapTransform(transformation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    /**
     * 本地图片处理
     */
    private static void localPic(Context context, int localResImg, ImageView view, int defResImg, int errResImg, Transformation<Bitmap>... transformation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context instanceof Activity && ((Activity) context).isDestroyed()) return;

        Glide.with(context)
                .load(localResImg)
                .dontAnimate()
                .placeholder(defResImg)
                .error(errResImg)
                .bitmapTransform(transformation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }
}
