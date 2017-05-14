package com.juxin.predestinate.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgMgr;
import com.juxin.library.observe.MsgType;
import com.juxin.library.view.CircleImageView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.SDCardUtil;


/**
 * 无头像
 * <p>
 * by zhang
 */
public class UserNoHeadUploadAct extends BaseActivity implements ImgSelectUtil.OnChooseCompleteListener, OnClickListener {
    private Button btn_pick_photo;
    private Button btn_take_photo;
    private CircleImageView imgBtn_upload_head;
    private TextView txt_usernohead_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_user_head_no_upload);
        initTitle();
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        this.btn_pick_photo = (Button) findViewById(R.id.btn_pick_photo_nohead);
        this.btn_take_photo = (Button) findViewById(R.id.btn_take_photo_nohead);
        this.imgBtn_upload_head = (CircleImageView) findViewById(R.id.imgBtn_noheard_head);
        this.txt_usernohead_info = (TextView) findViewById(R.id.txt_usernohead_info);
    }

    private void initData() {

        String avatar = ModuleMgr.getCenterMgr().getMyInfo().getAvatar();
        if (!TextUtils.isEmpty(avatar)) {
            ImageLoader.loadCenterCrop(this, avatar, imgBtn_upload_head);
        }
        txt_usernohead_info.setText(TextUtils.isEmpty(ModuleMgr.getCenterMgr().getMyInfo().getReasons()) ? getString(R.string.re_upload_avatar_def) :
                ModuleMgr.getCenterMgr().getMyInfo().getReasons());
    }


    private void initTitle() {
        setTitle(getString(R.string.re_upload_avatar_title));
        setTitleRight("", new OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToOtherAct();
            }
        });

    }

    private void initEvent() {
        this.btn_take_photo.setOnClickListener(this);
        this.btn_pick_photo.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo_nohead:
                ImgSelectUtil.getInstance().pickPhoto(this, this);
                break;
            case R.id.btn_pick_photo_nohead:
                ImgSelectUtil.getInstance().pickPhoto(this, this);
                break;
        }
    }

    private void redirectToOtherAct() {
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

    private void clearUserAvatarFile(String headPicPath) {
        SDCardUtil.delFile(headPicPath);
    }
}
