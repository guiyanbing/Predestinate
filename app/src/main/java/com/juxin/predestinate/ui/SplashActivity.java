package com.juxin.predestinate.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.base.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.main.MainActivity;

/**
 * 闪屏页面
 * Created by ZRP on 2016/12/27.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        jumpAnimation();
    }

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
                UIShow.show(SplashActivity.this, MainActivity.class);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animator.start();
    }
}
