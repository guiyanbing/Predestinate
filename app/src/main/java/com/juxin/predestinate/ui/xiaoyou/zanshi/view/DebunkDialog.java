//package com.juxin.predestinate.ui.xiaoyou.zanshi.view;
//
//import android.content.DialogInterface;
//import android.graphics.drawable.AnimationDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.FragmentActivity;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.juxin.mumu.bean.log.MMLog;
//import com.juxin.predestinate.R;
//import com.juxin.predestinate.module.logic.baseui.BaseDialogFragment;
//
//
///**
// * 吐槽动画播放弹框
// * <p>使用：
// * <p>DebunkDialog.getInstance().show(getActivity()); 或 DebunkDialog.getInstance().show(getActivity(), frames, 250, "不要再吐槽啦", 3000);<p>
// * Created by ZRP on 2017/1/3.
// */
//public class DebunkDialog extends BaseDialogFragment {
//
//    private static DebunkDialog debunkDialog;
//
//    private ImageView debunk_frame;
//    private TextView debunk_des;
//
//    private int duration_frame = 100;// 动画帧播放时长
//    private int duration = 4 * 1000;// dialog自动消失时间
//    private String des_string = "";// 展示文字
//    private Drawable[] frame_drawables = null;// 播放的动画资源
//    private AnimationDrawable frameAnimation = null;
//    private Handler handler = new Handler();
//
//    public static DebunkDialog getInstance() {
//        if (debunkDialog == null) debunkDialog = new DebunkDialog();
//        return debunkDialog;
//    }
//
//    public DebunkDialog() {
//        settWindowAnimations(R.style.AnimScaleInScaleOutOverShoot);
//        setGravity(Gravity.CENTER);
//        setDialogSizeRatio(0.8, 0);
//        setDimAmount(0);
//        setCancelable(true);
//        setCanceledOnTouchOutside(false);
//        setOnDismissListener(new DialogInterface.OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialog) {
//                resetRef();
//            }
//        });
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        super.onCreateView(inflater, container, savedInstanceState);
//        setContentView(R.layout.p1_dialog_debunk);
//
//        View contentView = getContentView();
//        initView(contentView);
//        return contentView;
//    }
//
//    private void initView(View view) {
//        debunk_frame = (ImageView) view.findViewById(R.id.debunk_frame);
//        debunk_des = (TextView) view.findViewById(R.id.debunk_des);
//
//        if (frame_drawables == null || frame_drawables.length < 1) {
//            frame_drawables = new Drawable[]{
//                    getDrawable(R.drawable.chaofeng_idle_0), getDrawable(R.drawable.chaofeng_idle_1),
//                    getDrawable(R.drawable.chaofeng_idle_2), getDrawable(R.drawable.chaofeng_idle_3),
//                    getDrawable(R.drawable.chaofeng_idle_4), getDrawable(R.drawable.chaofeng_idle_5),
//                    getDrawable(R.drawable.chaofeng_idle_6), getDrawable(R.drawable.chaofeng_idle_7),
//                    getDrawable(R.drawable.chaofeng_idle_8), getDrawable(R.drawable.chaofeng_idle_9),
//                    getDrawable(R.drawable.chaofeng_idle_10), getDrawable(R.drawable.chaofeng_idle_11),
//                    getDrawable(R.drawable.chaofeng_idle_12), getDrawable(R.drawable.chaofeng_idle_13),
//                    getDrawable(R.drawable.chaofeng_idle_14), getDrawable(R.drawable.chaofeng_idle_15),
//                    getDrawable(R.drawable.chaofeng_idle_16), getDrawable(R.drawable.chaofeng_idle_17),
//                    getDrawable(R.drawable.chaofeng_idle_18), getDrawable(R.drawable.chaofeng_idle_19),
//                    getDrawable(R.drawable.chaofeng_idle_20), getDrawable(R.drawable.chaofeng_idle_21),
//                    getDrawable(R.drawable.chaofeng_idle_22), getDrawable(R.drawable.chaofeng_idle_23),
//                    getDrawable(R.drawable.chaofeng_idle_24), getDrawable(R.drawable.chaofeng_idle_25),
//                    getDrawable(R.drawable.chaofeng_idle_26), getDrawable(R.drawable.chaofeng_idle_27),
//                    getDrawable(R.drawable.chaofeng_idle_28), getDrawable(R.drawable.chaofeng_idle_29),
//                    getDrawable(R.drawable.chaofeng_idle_30), getDrawable(R.drawable.chaofeng_idle_31),
//                    getDrawable(R.drawable.chaofeng_idle_32), getDrawable(R.drawable.chaofeng_idle_33),
//                    getDrawable(R.drawable.chaofeng_idle_34), getDrawable(R.drawable.chaofeng_idle_35),
//                    getDrawable(R.drawable.chaofeng_idle_36), getDrawable(R.drawable.chaofeng_idle_37)
//            };
//        }
//        if (!TextUtils.isEmpty(des_string)) {
//            debunk_des.setVisibility(View.VISIBLE);
//            debunk_des.setText(des_string);
//        } else {
//            debunk_des.setVisibility(View.GONE);
//        }
//
//        setAnimationFrames();
//    }
//
//    /**
//     * 展示frame动画弹框，使用默认的值
//     *
//     * @param activity FragmentActivity
//     */
//    public void show(FragmentActivity activity) {
//        show(activity, null, 0, null, 0);
//    }
//
//    /**
//     * 展示frame动画弹框，使用自定义的值
//     *
//     * @param activity       FragmentActivity
//     * @param frames         动画帧图片
//     * @param duration_frame 每一帧图片展示的时长
//     * @param des            动画文字描述
//     * @param duration       dialog展示时长
//     */
//    public void show(FragmentActivity activity, Drawable[] frames, int duration_frame, String des, int duration) {
//        if (frames != null && frames.length > 1) this.frame_drawables = frames;
//        if (duration_frame > 0) this.duration_frame = duration_frame;
//        if (!TextUtils.isEmpty(des)) this.des_string = des;
//        if (duration > 0) this.duration = duration;
//
//        debunkDialog.showDialog(activity);
//    }
//
//    /**
//     * 设置播放帧
//     */
//    private void setAnimationFrames() {
//        frameAnimation = new AnimationDrawable();
//        for (Drawable drawable : frame_drawables) {
//            frameAnimation.addFrame(drawable, duration_frame);
//        }
//        frameAnimation.setOneShot(false);//设置循环播放
//        debunk_frame.setImageDrawable(frameAnimation);
//        startAnimation();
//
//        handler.removeCallbacks(runnable);
//        handler.postDelayed(runnable, duration);
//    }
//
//    /**
//     * 停止动画且关闭dialog
//     */
//    private Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            stopAnimation();
//            dismissAllowingStateLoss();
//
//            resetRef();
//        }
//    };
//
//    /**
//     * 展示语音播放动画
//     */
//    private void startAnimation() {
//        if (frameAnimation != null) frameAnimation.start();
//    }
//
//    /**
//     * 停止语音播放动画
//     */
//    private void stopAnimation() {
//        if (frameAnimation != null) frameAnimation.stop();
//        debunk_frame.setImageResource(R.drawable.chaofeng_idle_0);
//    }
//
//    @Override
//    public void onCancel(DialogInterface dialog) {
//        super.onCancel(dialog);
//        resetRef();
//    }
//
//    /**
//     * 重置引用
//     */
//    private void resetRef() {
//        frame_drawables = null;
//        des_string = null;
//        debunkDialog = null;
//    }
//
//    /**
//     * 根据资源id获取一个Drawable
//     *
//     * @param id 资源id
//     * @return Drawable实例
//     */
//    private Drawable getDrawable(int id) {
//        try {
//            return getContext().getResources().getDrawable(id);
//        } catch (Exception e) {
//            MMLog.printThrowable(e);
//        }
//        return null;
//    }
//}
