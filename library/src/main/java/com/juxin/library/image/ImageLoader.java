package com.juxin.library.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
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
        loadCircleAvatar(context, model, view, 0);
    }

    public static <T> void loadCircleAvatar(Context context, T model, ImageView view, int borderWidth) {
        loadCircle(context, model, view, R.drawable.default_head, R.drawable.default_head, borderWidth, Color.WHITE);
    }

    public static <T> void loadRoundAvatar(Context context, T model, ImageView view) {
        loadRound(context, model, 8, view, R.drawable.default_head);
    }

    public static <T> void loadRoundAvatar(Context context, T model, ImageView view, int roundPx) {
        loadRound(context, model, roundPx, view, R.drawable.default_head);
    }
    /**
     * CenterCrop加载图片
     */
    public static <T> void loadCenterCrop(Context context, T model, ImageView view) {
        loadCenterCrop(context, model, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static <T> void loadCenterCrop(Context context, T model, ImageView view, int defResImg) {
        loadPic(context, model, view, defResImg, defResImg, bitmapCenterCrop);
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

    public static <T> void loadFitCenter(Context context, T model, ImageView view, int defResImg) {
        loadPic(context, model, view, defResImg, defResImg, bitmapFitCenter);
    }

    public static <T> void loadFitCenter(Context context, T model, ImageView view, int defResImg, int errResImg) {
        loadPic(context, model, view, defResImg, errResImg, bitmapFitCenter);
    }

    /**
     * 图片圆角处理: 默认全角处理，其他需求自行重载方法
     */
    public static <T> void loadRound(Context context, T model, ImageView view) {
        loadRound(context, model, 8, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    public static <T> void loadRound(Context context, T model, int roundPx, ImageView view, int defResImg) {
        loadRound(context, model, roundPx, view, defResImg, defResImg);
    }
    /**
     * @param roundPx 圆角弧度
     */
    public static <T> void loadRound(Context context, T model, int roundPx, ImageView view, int defResImg, int errResImg) {
        RoundedCorners roundedCorners = new RoundedCorners(context, roundPx, 0, RoundedCorners.CornerType.ALL);
        loadPic(context, model, view, defResImg, errResImg, bitmapCenterCrop, roundedCorners);
    }

    /**
     * 网络图片高斯模糊处理
     *
     * @param level 模糊等级
     */
    public static <T> void loadBlur(Context context, T model, int level, ImageView view) {
        BlurImage blurImage = new BlurImage(context, level);
        loadPic(context, model, view, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop, blurImage);
    }

    /**
     * 加载为圆形图像
     */
    public static <T> void loadCircle(final Context context, final T model, final ImageView view,
                                      final int defResImg, final int errResImg, int borderWidth, int borderColor) {
        circleTransform.setBorderWidth(borderWidth);
        circleTransform.setBorderColor(borderColor);
        loadPicWithCallback(context, defResImg, new GlideCallback() {
            @Override
            public void onResourceReady(GlideDrawable resource) {
                loadPic(context, model, view, defResImg, errResImg, bitmapCenterCrop, circleTransform);
            }
        });
    }

    /**
     * 以静态图片展示Gif
     */
    public static <T> void loadGifAsBmp(Context context, T model, ImageView view) {
        loadGifAsBmp(context, model, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    // ==================================== 内部私有调用 =============================================

    private static <T> void loadPic(Context context, T model, ImageView view,
                                    int defResImg, int errResImg,
                                    Transformation<Bitmap>... transformation) {
        try {
            loadPic(context, model, view, context.getResources().getDrawable(defResImg),
                    context.getResources().getDrawable(errResImg), transformation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void loadPic(final Context context, final T model, final ImageView view,
                                    final Drawable defResImg, final Drawable errResImg,
                                    final Transformation<Bitmap>... transformation) {
        try {
            loadPicWithCallback(context, model, new GlideCallback() {
                @Override
                public void onResourceReady(GlideDrawable resource) {
                    getDrawableBuilder(context, model)
                            .bitmapTransform(transformation)
                            .placeholder(defResImg)
                            .error(errResImg)
                            .diskCacheStrategy(resource.isAnimated() ? DiskCacheStrategy.SOURCE : DiskCacheStrategy.ALL)
                            .into(view);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载图片： 带回调
     */
    public static <T> void loadPicWithCallback(final Context context, T model, final GlideCallback callback) {
        try {
            if (isActDestroyed(context))
                return;

            getDrawableBuilder(context, model)
                    .into(new SimpleTarget<GlideDrawable>() {
                        @Override
                        public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                            if (isActDestroyed(context))
                                return;

                            if (callback != null)
                                callback.onResourceReady(resource);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void loadGifAsBmp(Context context, T model, ImageView view, int defResImg, int errResImg) {
        try {
            if (isActDestroyed(context))
                return;

            getBitmapBuilder(context, model)
                    .transform(bitmapFitCenter)
                    .placeholder(defResImg)
                    .error(errResImg)
                    .into(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> DrawableRequestBuilder<T> getDrawableBuilder(Context context, T model) {
        return getDrawableRequest(context, model)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);
    }

    private static <T> BitmapRequestBuilder<T, Bitmap> getBitmapBuilder(Context context, T model) {
        return getDrawableRequest(context, model)
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    private static <T> DrawableTypeRequest<T> getDrawableRequest(Context context, T model) {
        return Glide.with(context).load(model);
    }


    private static boolean isActDestroyed(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context instanceof Activity && ((Activity) context).isDestroyed()) return true;
        return false;
    }

    // 请求回调
    public interface GlideCallback {
        void onResourceReady(GlideDrawable resource);
    }
}
