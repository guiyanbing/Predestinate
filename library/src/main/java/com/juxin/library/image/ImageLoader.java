package com.juxin.library.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.juxin.library.R;
import com.juxin.library.image.transform.BlurImage;
import com.juxin.library.image.transform.CircleTransform;
import com.juxin.library.image.transform.RoundedCorners;

/**
 * 基于Glide图片请求，处理类
 */
public class ImageLoader {
    private static CenterCrop bitmapCenterCrop;
    private static FitCenter bitmapFitCenter;
    private static CircleTransform circleTransform;

    public static void init(Context context) {
        Context mContext = context.getApplicationContext();
        //Glide相关全局变量
        bitmapCenterCrop = new CenterCrop(mContext);
        bitmapFitCenter = new FitCenter(mContext);
        circleTransform = new CircleTransform(mContext);
    }

    /**
     * 加载头像
     */
    public static <T> void loadAvatar(Context context, T model, ImageView view) {
        loadPic(context, model, view, R.drawable.default_head, R.drawable.default_head, bitmapCenterCrop);
    }

    public static <T> void loadCircleAvatar(Context context, T model, ImageView view) {
        loadCircle(context, model, view, R.drawable.default_head, 0, 0);
    }
    /**
     * CenterCrop加载图片
     */
    public static <T> void loadCenterCrop(Context context, T model, ImageView view) {
        loadCenterCrop(context, model, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static <T> void loadCenterCrop(Context context, T model, ImageView view, int defResImg, int errResImg) {
        loadPic(context, model, view, defResImg, errResImg, bitmapCenterCrop);
    }

    /**
     * FitCenter加载图片
     */
    public static <T> void loadFitCenter(Context context, T model, ImageView view) {
        loadFitCenter(context, model, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static <T> void loadFitCenter(Context context, T model, ImageView view, int defResImg, int errResImg) {
        loadPic(context, model, view, defResImg, errResImg, bitmapFitCenter);
    }

    /**
     * 图片圆角处理: 默认全角处理，其他需求自行重载方法
     */
    public static <T> void loadRoundCorners(Context context, T model, ImageView view) {
        loadRoundCorners(context, model, 8, view);
    }

    /**
     * @param roundPx 圆角弧度
     */
    public static <T> void loadRoundCorners(Context context, T model, int roundPx, ImageView view) {
        RoundedCorners roundedCorners = new RoundedCorners(context, roundPx, 0, RoundedCorners.CornerType.ALL);
        loadPic(context, model, view, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop, roundedCorners);
    }

    /**
     * 网络图片高斯模糊处理
     *
     * @param level 模糊等级
     */
    public static <T> void loadBlurImg(Context context, T model, int level, ImageView view) {
        BlurImage blurImage = new BlurImage(context, level);
        loadPic(context, model, view, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop, blurImage);
    }

    /**
     * 加载为圆形图像
     */
    public static <T> void loadCircle(final Context context, final T model, final ImageView view, int defImg, int borderWidth, int borderColor) {
        circleTransform.setBorderWidth(borderWidth);
        circleTransform.setBorderColor(borderColor);
        loadPicWithCallback(context, defImg,  new GlideCallback() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                loadPic(context, model, view, resource, resource, bitmapCenterCrop, circleTransform);
            }
        }, bitmapCenterCrop, circleTransform);
    }

    /**
     * 加载图片： 带回调
     */
    public static <T> void loadPicWithCallback(Context context, T model, GlideCallback callback) {
        loadPicWithCallback(context, model, callback, bitmapCenterCrop);
    }

    /**
     * 以静态图片展示Gif
     */
    public static <T> void loadPicAsBmp(Context context, T model, ImageView view) {
        loadPicAsBmp(context, model, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    // ==================================== 内部私有调用 =============================================
    public static <T> void loadPic(Context context, T model, ImageView view, int defResImg, int errResImg, Transformation<Bitmap>...transformation) {
        try {
            getRequestBuilder(context, model, transformation)
                    .placeholder(defResImg)
                    .error(errResImg)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void loadPic(Context context, T model, ImageView view, Drawable defResImg, Drawable errResImg, Transformation<Bitmap>...transformation) {
        try {
            getRequestBuilder(context, model, transformation)
                    .placeholder(defResImg)
                    .error(errResImg)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void loadPicWithCallback(Context context, T model, final GlideCallback callback, BitmapTransformation... transformations) {
        try {
            getRequestBuilder(context, model, transformations)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            callback.onResourceReady(resource, glideAnimation);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void loadPicAsBmp(Context context, T model, ImageView view, int defResImg, int errResImg) {
        getBmpRequestBuilder(context, model, bitmapFitCenter)
                .placeholder(defResImg)
                .error(errResImg)
                .into(view);
    }

    private static <T> DrawableRequestBuilder<T> getRequestBuilder(Context context, T model, Transformation<Bitmap>...transformation) {
        return Glide.with(context)
                .load(model)
                .dontAnimate()
                .bitmapTransform(transformation)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    private static <T> BitmapRequestBuilder<T, Bitmap> getBmpRequestBuilder(Context context, T model, BitmapTransformation... transformations) {
        return Glide.with(context)
                .load(model)
                .asBitmap()
                .dontAnimate()
                .transform(transformations)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    // 请求回调
    public interface GlideCallback {
        void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation);
    }
}
