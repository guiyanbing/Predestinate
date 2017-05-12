package com.juxin.predestinate.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.SDCardUtil;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.module.util.UIShow;


/**
 * 头像上传页面
 *
 * @author:zhang
 * @Date:2014-12-8
 */
public class UserRegHeadUploadAct extends BaseActivity implements ImgSelectUtil.OnChooseCompleteListener, OnClickListener {
    private Button btn_pick_photo;
    private Button btn_take_photo;
    private ImageButton imgBtn_upload_head;
    private TextView btn_top_right;
    private ImageView img_upload_logo;


    private int intentType = 0; // 0表示跳转到MainAct,1表示跳转到每日推荐页面,否则直接关闭当前页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_user_reg_head_upload);
//        setResult(UIHelper.FROM_HEADUPLOAD);
        initTitle();
        initData();
        initView();
        initEvent();
    }


    private void initTitle() {
        setTitle(getString(R.string.upload_avatar_title));
        setTitleRight(getString(R.string.re_upload_avatar_right_title), new OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToOtherAct();
            }
        });

    }

    private void initView() {
        this.btn_pick_photo = (Button) findViewById(R.id.btn_pick_photo);
        this.btn_take_photo = (Button) findViewById(R.id.btn_take_photo);
        this.imgBtn_upload_head = (ImageButton) findViewById(R.id.imgBtn_upload_head);
        this.img_upload_logo = (ImageView) findViewById(R.id.img_upload_logo);
        String tip_txt = getIntent().getStringExtra("tipText");
        if (tip_txt == null) {
            UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
            if (userDetail.getGender() == 1) {
                img_upload_logo.setImageResource(R.drawable.ic_upload_head_logo_female);
            } else {
                img_upload_logo.setImageResource(R.drawable.ic_upload_head_logo_male);
            }
        } else {
            img_upload_logo.setVisibility(View.GONE);
            TextView tv_tip_txt = (TextView) findViewById(R.id.tv_tip_txt);
            tv_tip_txt.setVisibility(View.VISIBLE);
            tv_tip_txt.setText(tip_txt);
        }
    }

    private void initData() {
        PSP.getInstance().put("recommendDate", TimeUtil.getData());
        intentType = getIntent().getIntExtra("type", 0);
    }


    private void initEvent() {
        this.btn_take_photo.setOnClickListener(this);
        this.btn_pick_photo.setOnClickListener(this);
        this.imgBtn_upload_head.setOnClickListener(this);
        this.btn_top_right.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo:
                ImgSelectUtil.getInstance().pickPhoto(this, this);
                break;
            case R.id.btn_pick_photo:
                ImgSelectUtil.getInstance().pickPhoto(this, this);
                break;
            case R.id.imgBtn_upload_head:
                ImgSelectUtil.getInstance().pickPhoto(this, this);
                break;
        }
    }

    private void redirectToOtherAct() {
        if (intentType == 0) {
            UIShow.showMainClearTask(this);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        redirectToOtherAct();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void clearUserAvatarFile(String headPicPath) {
        SDCardUtil.delFile(headPicPath);
    }


    @Override
    public void onComplete(final String... path) {
        if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
            PToast.showShort(getString(R.string.re_upload_avatar_selet_fail));
            return;
        }
        PLogger.d("path=== " + path[0]);
        LoadingDialog.show(this, "正在上传头像");
        ModuleMgr.getCenterMgr().uploadAvatar(path[0], new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    LoadingDialog.closeLoadingDialog();
                    MsgMgr.getInstance().sendMsg(MsgType.MT_Update_MyInfo, null);
                    clearUserAvatarFile(path[0]);
                    redirectToOtherAct();
                } else {
                    PToast.showShort(getString(R.string.re_upload_avatar_fail));
                }
            }
        });
    }
}
