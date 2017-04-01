package com.juxin.predestinate.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.update.AppUpdate;
import com.juxin.predestinate.module.local.location.LocationMgr;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.main.MainActivity;
import com.juxin.predestinate.ui.start.NavUserAct;

/**
 * 闪屏页面
 * Created by ZRP on 2016/12/27.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        isCanBack(false);
        setCanNotify(false);//设置该页面不弹出悬浮窗消息通知
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        LocationMgr.getInstance().start();//启动定位
        jumpAnimation();
    }

    private boolean isAnimationEnd = false, isRequestResponse = false;

    private void jumpAnimation() {
        ImageView iv_splash = (ImageView) findViewById(R.id.iv_splash);

        ObjectAnimator animator = ObjectAnimator.ofFloat(iv_splash, "alpha", 0.5f, 1.0f);
        animator.setDuration(3000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                isAnimationEnd = true;
                skipLogic();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
        isRequestResponse = true;
        //TODO 发起软件升级请求
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isRequestResponse = true;
//                AppUpdate appUpdate = new AppUpdate();
//                appUpdate.parseJson("{\n" +
//                        "  \"status\": \"ok\",\n" +
//                        "  \"ver\": \"10100090\",\n" +
//                        "  \"title\": \"v1.4.040\",\n" +
//                        "  \"force\": \"0\",\n" +
//                        "  \"summary\": \"版本：2.4.148\\n1、心动\\n2、抢话费\\n3、即时通讯\\n4、IOS\",\n" +
//                        "  \"url\": \"http://down.yuanfenba.net/yuanfen/new/Fate_It_2_888_10100090_2.4.148.apk\",\n" +
//                        "  \"msg\": \"有新版本了!\",\n" +
//                        "  \"newpackname\":\"\"\n" +
//                        "}");
//                UIShow.showUpdateDialog(SplashActivity.this, appUpdate, runnable);
//            }
//        }, 5000);
    }

    /**
     * 非强制升级点击取消之后继续执行跳转操作
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            skipLogic();
        }
    };

    /**
     * Intent跳转
     */
    private void skipLogic() {
        if (!isAnimationEnd || !isRequestResponse) return;

        Intent intent = null;
        if (ModuleMgr.getLoginMgr().checkAuthIsExist()) {
            ModuleMgr.getCenterMgr().reqMyInfo();
            if (ModuleMgr.getCommonMgr().checkDateAndSave(getUploadHeadKey()) && !checkUserIsUploadAvatar()) {
//TODO                UIShow.showNoHeadActToMain(this);
//                finish();
            } else {
                intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        } else {
            intent = new Intent(SplashActivity.this, NavUserAct.class);
        }
        startActivity(intent);
        finish();
    }

    /**
     * @return 是否到达第二天key
     */
    private String getUploadHeadKey() {
        return "judgeUploadHead" + App.uid;
    }

    /**
     * 判断了是否上传了用户头像
     */
    private boolean checkUserIsUploadAvatar() {
        //TODO 待替换
//        UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
//        String avatar = userDetail.getAvatar();
//        // 判断用户头像是否正常
//        return !(TextUtils.isEmpty(avatar) || userDetail.getAvatar_status() == 2);
        return true;
    }

    @Override
    public void onBackPressed() {
        // 空实现，不响应返回键点击
    }
}
