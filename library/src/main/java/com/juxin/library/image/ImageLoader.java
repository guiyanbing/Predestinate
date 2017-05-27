package com.juxin.library.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.juxin.library.R;
import com.juxin.library.image.transform.BlurImage;
import com.juxin.library.image.transform.RoundedCorners;

/**
 * 基于Glide图片请求，处理类
 */
public class ImageLoader {
    private static CenterCrop bitmapCenterCrop;
    private static FitCenter bitmapFitCenter;

    public static void init(Context context){
        Context mContext = context.getApplicationContext();
        //Glide相关全局变量
        bitmapCenterCrop = new CenterCrop(mContext);
        bitmapFitCenter = new FitCenter(mContext);
    }

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
        loadPic(context, url, view, defResImg, errResImg, bitmapCenterCrop);
    }

    /**
     * FitCenter加载图片
     */
    public static void loadFitCenter(Context context, String url, ImageView view) {
        loadFitCenter(context, url, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int resImg) {
        loadPic(context, url, view, resImg, resImg, bitmapFitCenter);
    }

    public static void loadFitCenter(Context context, String url, ImageView view, int defResImg, int errResImg) {
        loadPic(context, url, view, defResImg, errResImg, bitmapFitCenter);
    }

    public static void loadRoundCorners(Context context, String url, ImageView view) {
        loadRoundCorners(context, url, 8, view);
    }
    /**
     * 图片圆角处理: 默认全角处理，其他需求自行重载方法
     *
     * @param roundPx 圆角弧度
     */
    public static void loadRoundCorners(Context context, String url, int roundPx, ImageView view) {
        RoundedCorners roundedCorners = new RoundedCorners(context, roundPx, 0, RoundedCorners.CornerType.ALL);
        loadPic(context, url, view, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop, roundedCorners);
    }

    /**
     * 网络图片高斯模糊处理
     *
     * @param level 模糊等级
     */
    public static void loadBlurImg(Context context, String url, int level, ImageView view) {
        BlurImage blurImage = new BlurImage(context, level);
        loadPic(context, url, view, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop, blurImage);
    }

    /**
     * 本地图片高斯模糊处理
     */
    public static void localBlurImg(Context context, int localResImg, int level, ImageView view) {
        BlurImage blurImage = new BlurImage(context, level);
        loadPic(context, localResImg, view, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop, blurImage);
    }
    /**
     * 图片圆角处理: 默认全角处理，其他需求自行重载方法(本地图片圆角处理)
     *
     * @param roundPx 圆角弧度
     */
    public static void localRoundCorners (Context context, int localResImg, int roundPx, ImageView view) {
        RoundedCorners roundedCorners = new RoundedCorners(context, roundPx, 0, RoundedCorners.CornerType.ALL);
        loadPic(context, localResImg, view, R.drawable.default_pic, R.drawable.default_pic, bitmapFitCenter, roundedCorners);
    }
    /**
     * 本地图片
     */
    public static void localLocalImg(Context context, int localResImg, ImageView view) {
        loadPic(context, localResImg, view, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop);
    }

    /**
     * 加载gif图片
     */
    public static void loadGif(Context context, int localResImg, ImageView view) {
        Glide.with(context).load(localResImg).asGif().into(view);
    }

    /**
     * 加载图片： 回调
     */
    public static void localImgWithCallback(Context context, String url, GlideCallback callback) {
        loadPicWithCallback(context, url, callback, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop);
    }

    // ==================================== 内部私有调用 =============================================

    private static void loadPic(Context context, Object model, ImageView view, int defResImg, int errResImg, Transformation<Bitmap>... transformation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context instanceof Activity && ((Activity) context).isDestroyed()) return;

        Glide.with(context)
                .load(model)
                .dontAnimate()
                .placeholder(defResImg)
                .error(errResImg)
                .bitmapTransform(transformation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    /**
     * 网络图片处理: 回调
     */
    private static void loadPicWithCallback(Context context, String url, final GlideCallback callback, int defResImg, int errResImg, Transformation<Bitmap>... transformation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context instanceof Activity && ((Activity) context).isDestroyed()) return;

        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(defResImg)
                .error(errResImg)
                .bitmapTransform(transformation)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        callback.onResourceReady(resource, glideAnimation);
                    }
                });
    }
    
    // 请求回调
    public interface GlideCallback {
        void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation);
    }
}
