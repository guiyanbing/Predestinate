package com.juxin.predestinate.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.db.OldDBModule;
import com.juxin.predestinate.module.local.location.LocationMgr;
import com.juxin.predestinate.module.logic.application.App;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.start.NavUserAct;
import com.juxin.predestinate.ui.user.util.CenterConstant;

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
        initData();
        jumpAnimation();
    }

    private void initData() {
        LocationMgr.getInstance().start();//启动定位
        ModuleMgr.getCommonMgr().updateUsers();//软件升级U-P读取
        ModuleMgr.getCommonMgr().requestStaticConfig();//请求一些在线配置信息
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
    }

    /**
     * Intent跳转
     */
    private void skipLogic() {
        if (ModuleMgr.getLoginMgr().checkAuthIsExist()) {
            if (ModuleMgr.getCommonMgr().checkDateAndSave(getUploadHeadKey())&&!checkUserIsUploadAvatar()) {
                int avatar_status = ModuleMgr.getCenterMgr().getMyInfo().getAvatar_status();
                if (avatar_status == CenterConstant.USER_AVATAR_NO_PASS)
                    UIShow.showNoHeadUploadActToMain(SplashActivity.this);//更新
                if (avatar_status == CenterConstant.USER_AVATAR_NO_UPLOAD)
                    UIShow.showRegHeadUploadActToMain(SplashActivity.this);
                finish();
            } else {
                UIShow.showMainClearTask(SplashActivity.this);
            }
        } else {
            Intent intent = new Intent(SplashActivity.this, NavUserAct.class);
            startActivity(intent);
            finish();
        }

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
        int avatar_status = ModuleMgr.getCenterMgr().getMyInfo().getAvatar_status();
        // 判断用户头像是否正常(未上传／未通过时跳转上传头像)
        return !(avatar_status == CenterConstant.USER_AVATAR_NO_PASS || avatar_status == CenterConstant.USER_AVATAR_NO_UPLOAD);
    }

    @Override
    public void onBackPressed() {
        // 空实现，不响应返回键点击
    }
}
