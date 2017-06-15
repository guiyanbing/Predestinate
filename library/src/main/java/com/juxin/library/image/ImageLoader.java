package com.juxin.library.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
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
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
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
    private static RoundedCorners roundedCorners;
    private static RoundedCorners roundedCornerTop;
    private static BlurImage blurImage;
    private static SparseArray<Drawable> cache = new SparseArray<>();//替换图缓存

    public static void init(Context context) {
        Context mContext = context.getApplicationContext();
        //Glide相关全局变量
        bitmapCenterCrop = new CenterCrop(mContext);
        bitmapFitCenter = new FitCenter(mContext);
        circleTransform = new CircleTransform(mContext);
        roundedCorners = new RoundedCorners(mContext, 8, 0, RoundedCorners.CornerType.ALL);
        roundedCornerTop = new RoundedCorners(mContext, 15, 0, RoundedCorners.CornerType.TOP);
        blurImage = new BlurImage(context);
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
        loadRoundAvatar(context, model, view, 8);
    }

    public static <T> void loadRoundAvatar(Context context, T model, ImageView view, int roundPx) {
        loadRound(context, model, view, roundPx, R.drawable.default_head, R.drawable.default_head);
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
    public static <T> void loadRound(Context context, T model, ImageView view) {
        loadRound(context, model, view, 8, R.drawable.default_pic, R.drawable.default_pic);
    }

    /**
     * 图片圆角处理: 上面两个角
     */
    public static <T> void loadRoundTop(Context context, T model, ImageView view) {
        loadRoundTop(context, model, view, 15, R.drawable.default_pic, R.drawable.default_pic);
    }


    /**
     * @param roundPx 圆角弧度
     */
    public static <T> void loadRoundTop(Context context, T model, ImageView view,
                                        int roundPx, int defResImg, int errResImg) {
        roundedCornerTop.setRadius(roundPx);
        loadStylePic(context, model, view, defResImg, errResImg, bitmapCenterCrop, roundedCornerTop);
    }

    /**
     * @param roundPx 圆角弧度
     */
    public static <T> void loadRound(Context context, T model, ImageView view,
                                     int roundPx, int defResImg, int errResImg) {
        roundedCorners.setRadius(roundPx);
        loadStylePic(context, model, view, defResImg, errResImg, bitmapCenterCrop, roundedCorners);
    }

    /**
     * 加载为圆形图像
     */
    public static <T> void loadCircle(Context context, T model, ImageView view,
                                      int defResImg, int errResImg, int borderWidth, int borderColor) {
        circleTransform.setBorderWidth(borderWidth);
        circleTransform.setBorderColor(borderColor);
        loadStylePic(context, model, view, defResImg, errResImg, bitmapCenterCrop, circleTransform);
    }

    /**
     * 网络图片高斯模糊处理
     */
    public static <T> void loadBlur(Context context, T model, ImageView view) {
        loadBlur(context, model, view, 8);
    }

    public static <T> void loadBlur(Context context, T model, ImageView view, int level) {
        blurImage.setRadius(level);
        loadPic(context, model, view, R.drawable.default_pic, R.drawable.default_pic, bitmapCenterCrop, blurImage);
    }

    /**
     * 以静态图片展示Gif
     */
    public static <T> void loadGifAsBmp(Context context, T model, ImageView view) {
        loadGifAsBmp(context, model, view, R.drawable.default_pic, R.drawable.default_pic);
    }

    /**
     * 带回调的加载
     */
    public static <T> void loadPicWithCallback(Context context, T model, GlideCallback callback) {
        loadPicWithCallback(context, model, callback, (Transformation<Bitmap>[]) null);
    }

    // ==================================== 内部私有调用 =============================================

    /**
     * 带占位图预处理样式的加载函数
     *
     * @param context
     * @param model
     * @param view
     * @param defResImg
     * @param errResImg
     * @param transformation
     * @param <T>
     */
    private static <T> void loadStylePic(final Context context, final T model, final ImageView view,
                                         int defResImg, final int errResImg,
                                         final Transformation<Bitmap>... transformation) {
        if (!isInvalidTag(view, model, transformation))
            return;
        setImgTag(view, model, transformation);

        loadPicWithCallback(context, defResImg, new GlideCallback() {
                    @Override
                    public void onResourceReady(final GlideDrawable defRes) {
                        loadPicWithCallback(context, errResImg, new GlideCallback() {
                                    @Override
                                    public void onResourceReady(GlideDrawable errRes) {
                                        loadPic(context, model, view, defRes, errRes, transformation);
                                    }
                                },
                                transformation);
                    }
                },
                transformation);
    }

    private static <T> void loadPic(Context context, T model, ImageView view,
                                    int defResImg, int errResImg,
                                    Transformation<Bitmap>... transformation) {
        try {
            if (!isInvalidTag(view, model, transformation))
                return;
            setImgTag(view, model, transformation);
            loadPic(context, model, view, defResImg > 0 ? ContextCompat.getDrawable(context, defResImg) : null,
                    errResImg > 0 ? ContextCompat.getDrawable(context, errResImg) : null, transformation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void loadPic(final Context context, final T model, final ImageView view,
                                    final Drawable defResImg, final Drawable errResImg,
                                    final Transformation<Bitmap>... transformation) {
        try {
            //先加载默认图
            if (isInvalidTag(view, model, transformation))
                return;
            view.setImageDrawable(defResImg);

            if (model == null)
                return;

            //再去网络请求
            loadPicWithCallback(context, model, new GlideCallback() {
                @Override
                public void onResourceReady(GlideDrawable resource) {
                    if (isInvalidTag(view, model, transformation))
                        return;
                    getDrawableBuilder(context, model)
                            .bitmapTransform(transformation)
//                            .placeholder(defResImg)
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
     * 带回调的加载
     */
    private static <T> void loadPicWithCallback(final Context context, final T model, final GlideCallback callback, final Transformation<Bitmap>... transformation) {
        try {
            if (isActDestroyed(context))
                return;

            if (model instanceof Integer && (Integer) model > 0) {
                int key = getCacheKey((Integer) model, transformation);
                Drawable drawable = cache.get(key);
                if (drawable != null && callback != null) {
                    callback.onResourceReady((GlideDrawable) drawable);
                    return;
                }
            }

            DrawableRequestBuilder<T> builder = getDrawableBuilder(context, model);

            if (transformation != null && transformation.length > 0)
                builder.bitmapTransform(transformation);

            builder.into(new SimpleTarget<GlideDrawable>() {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    if (model instanceof Integer && (Integer) model > 0) {
                        int key = getCacheKey((Integer) model, transformation);
                        cache.put(key, resource);
                    }

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
        return getRequest(context, model)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE);
    }

    private static <T> BitmapRequestBuilder<T, Bitmap> getBitmapBuilder(Context context, T model) {
        return getRequest(context, model)
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
    }

    private static <T> DrawableTypeRequest<T> getRequest(Context context, T model) {
        return Glide.with(context).load(model);
    }

    private static boolean isActDestroyed(Context context) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                context instanceof Activity && ((Activity) context).isDestroyed();
    }

    /**
     * 是否无效标记
     *
     * @param view
     * @param model
     * @param <T>
     * @return
     */
    private static <T> boolean isInvalidTag(ImageView view, T model, Object[] trans) {
        Object url_obj = view.getTag(R.string.view_url_tag_id);
        int url_tag = url_obj == null ? 0 : (int) url_obj;
        if (model == null ? 0 != url_tag : model.hashCode() != url_tag)
            return true;

        Object trans_obj = view.getTag(R.string.view_trans_tag_id);
        int trans_tag = trans_obj == null ? 0 : (int) trans_obj;
        if (trans == null ? 0 != trans_tag : getArrayHash(trans) != trans_tag)
            return true;

        return false;
    }

    /**
     * 设置标记
     *
     * @param view
     * @param model
     * @param trans
     */
    private static <T> void setImgTag(ImageView view, T model, Object[] trans) {
        view.setTag(R.string.view_url_tag_id, model == null ? 0 : model.hashCode());
        view.setTag(R.string.view_trans_tag_id, trans == null ? 0 : getArrayHash(trans));
    }

    /**
     * 按存储对象的Hash值计算Array的Hash值
     *
     * @param trans
     * @return
     */
    private static int getArrayHash(Object[] trans) {
        int objHash = 0;
        if (trans != null) {
            for (Object tran : trans)
                objHash = objHash ^ tran.hashCode();
        }
        return objHash;
    }

    private static int getCacheKey(int resId, Object[] trans) {
        return getArrayHash(trans) ^ resId;
    }

    /**
     * 请求回调
     */
    public interface GlideCallback {
        void onResourceReady(GlideDrawable resource);
    }
}
