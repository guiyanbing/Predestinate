package com.juxin.predestinate.ui.user.auth;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.config.VideoVerifyBean;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.local.album.help.AlbumHelper;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.utils.Video;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;

/**
 * 视频认证
 * IQQ
 */

public class MyAuthenticationVideoAct extends BaseActivity implements View.OnClickListener{
    private Context context;
    private static int PhotoUploadResult = 10001, VideoUploadResult = 10002;
    private View authPic, authVideo;
    private ImageView ivAuthPic, ivAuthVideo, ivPic, ivVideo;
    private TextView tvAuthPic, tvAuthVideo, tvMakePic, tvMakeVideo;
    private boolean isMakePhotoOK = false, isMakeVideoOk = false, isMakeing = false;
    private String sPicNoHttp = "";
    private VideoVerifyBean videoVerifyBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_authentication_video_act);
        videoVerifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
        context = this;
        setBackView("视频认证");
        setHideTopRightView(videoVerifyBean.getStatus() != 0);
        initView();
        getStatus();
    }


    private void initView() {
        authPic = findViewById(R.id.ll_auth_status_pic);
        authVideo = findViewById(R.id.ll_auth_status_video);
        ivAuthPic = (ImageView) findViewById(R.id.iv_auth_pic);
        ivAuthVideo = (ImageView) findViewById(R.id.iv_auth_video);
        tvAuthPic = (TextView) findViewById(R.id.tv_auth_pic);
        tvAuthVideo = (TextView) findViewById(R.id.tv_auth_video);
        ivPic = (ImageView) findViewById(R.id.iv_pic);
        ivVideo = (ImageView) findViewById(R.id.iv_video);
        tvMakePic = (TextView) findViewById(R.id.tv_make_pic);
        tvMakeVideo = (TextView) findViewById(R.id.tv_make_video);
        changeAllStatus(videoVerifyBean.getStatus());

        if (!"".equals(videoVerifyBean.getImgurl()) && videoVerifyBean.getStatus() != 0)
            ImageLoader.loadCenterCrop(this, videoVerifyBean.getImgurl(), ivPic);
        if (new File(Video.getPicPath()).exists() && videoVerifyBean.getStatus() != 0) {
            loadLocalVideoImg();
        }
        tvMakePic.setOnClickListener(this);
        tvMakeVideo.setOnClickListener(this);
        ivPic.setOnClickListener(this);
        ivVideo.setOnClickListener(this);

    }


    private void loadLocalVideoImg() {
        try {
            String vp = Video.getPicPath();
            FileInputStream fis = new FileInputStream(vp);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            ivVideo.setImageBitmap(bitmap);
        } catch (Exception e) {

        }

    }

    private void changeAllStatus(int status) {
        tvMakePic.setVisibility(View.GONE);
        tvMakeVideo.setVisibility(View.GONE);
        changePicStatus(status);
        changeVideoStatus(status);
    }

    private void changePicStatus(int status) {
        tvMakePic.setVisibility(View.GONE);
        changeStatus(ivAuthPic, tvAuthPic, status);
    }

    private void changeVideoStatus(int status) {
        tvMakeVideo.setVisibility(View.GONE);
        changeStatus(ivAuthVideo, tvAuthVideo, status);
    }

    private void changeStatus(ImageView iv, TextView tv, int status) {
        iv.setVisibility(View.VISIBLE);
        tv.setVisibility(View.VISIBLE);
        ivAuthVideo.setOnClickListener(null);
        switch (status) {
            case 0:
                iv.setVisibility(View.GONE);
                tv.setVisibility(View.GONE);
                break;
            case 1:
                iv.setBackgroundResource(R.drawable.f1_auth_ing);
                tv.setText("审核中");
                break;
            case 2:
                iv.setBackgroundResource(R.drawable.f1_auth_fail);
                tv.setText("审核未通过");
                tvMakeVideo.setVisibility(View.VISIBLE);
                tvMakePic.setVisibility(View.VISIBLE);
                break;
            case 3:
                iv.setBackgroundResource(R.drawable.f1_auth_ok);
                tv.setText("审核通过");
                break;
        }
    }

    private void changeVideoPlay() {
        ivAuthVideo.setVisibility(View.VISIBLE);
        tvAuthVideo.setVisibility(View.GONE);
        ivAuthVideo.setBackgroundResource(R.drawable.f1_video_play);
        ivAuthVideo.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_make_pic:
                if (isMakeing)
                    return;
                isMakeing = true;
                ImgSelectUtil.getInstance().takePhoto(MyAuthenticationVideoAct.this, true);
                break;
            case R.id.iv_pic:
                if (isMakeing)
                    return;
                isMakeing = true;
                if (videoVerifyBean.getStatus() == 0)
                    ImgSelectUtil.getInstance().takePhoto(MyAuthenticationVideoAct.this,true);
                break;
            case R.id.tv_make_video:
                if (isMakeing)
                    return;
                isMakeing = true;
                UIShow.showRecordVideoAct(this, VideoUploadResult);
                break;
            case R.id.iv_video:
                if (isMakeing)
                    return;
                isMakeing = true;
                if (videoVerifyBean.getStatus() == 0)
                    UIShow.showRecordVideoAct(this, VideoUploadResult);
                break;
            case R.id.iv_auth_video:
                Video.videoPlay(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        isMakeing = false;
        if (resultCode == PhotoUploadResult) {

        } else if (requestCode == VideoUploadResult) {
            if (resultCode == RESULT_OK) {
                String sVideo = data.getStringExtra(RecordVideoAct.EXTRA_RECORD_FILE_PATH);
                Video.getVideoThumb(sVideo);
                loadLocalVideoImg();
                VideoUpload(sVideo);
            }
        } else {
            isMakeing = false;
            String path = AlbumHelper.getInstance().getPhotoUri().getPath();
            if (path == null  || TextUtils.isEmpty(path)) {
                return;
            }
            if (FileUtil.isExist(path)) {
                uploadAuthPic(path);
            }
        }
    }


    private void checkAndShowSubmit() {
        if (isMakePhotoOK && isMakeVideoOk) {
            setHideTopRightView(false);
            videoVerifyBean.setStatus(0);
        }
    }

    private void uploadAuthPic(String sPic) {
        LoadingDialog.show(MyAuthenticationVideoAct.this, "正在上传照片");
        ModuleMgr.getMediaMgr().sendHttpFile(Constant.UPLOAD_TYPE_VIDEO_CHAT, sPic, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                LoadingDialog.closeLoadingDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response.getResponseString());
                    if ("ok".equals(jsonObject.optString("status")) && jsonObject.optJSONObject("res") != null) {
                        String spic = jsonObject.optJSONObject("res").optString("file_http_path");
                        sPicNoHttp = jsonObject.optJSONObject("res").optString("file_s_path");
                        ImageLoader.loadCenterCrop(context, spic, ivPic);
                        videoVerifyBean.setImgurl(spic);
                        tvMakePic.setVisibility(View.VISIBLE);
                        isMakePhotoOK = true;
                        checkAndShowSubmit();
                        changePicStatus(0);
                        tvMakePic.setVisibility(View.VISIBLE);
                    } else {
                        PToast.showShort("照片处理失败请重试");
                    }
                } catch (JSONException e) {
                    PToast.showShort("照片处理失败请重试");
                }
            }
        });
    }


    public void VideoUpload(String sPath) {
        ModuleMgr.getMediaMgr().sendHttpFile(Constant.UPLOAD_TYPE_VIDEO, sPath, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.getResponseString());
                    if ("ok".equals(jsonObject.optString("status")) && jsonObject.optJSONObject("res") != null) {
                        videoVerifyBean.setVideourl(jsonObject.optJSONObject("res").optString("file_http_path"));
                        loadLocalVideoImg();
                        tvMakeVideo.setVisibility(View.VISIBLE);
                        isMakeVideoOk = true;
                        checkAndShowSubmit();
                        changeVideoStatus(0);
                        changeVideoPlay();
                        tvMakeVideo.setVisibility(View.VISIBLE);
                    } else {
                        PToast.showShort("视频处理失败请重试");
                    }
                } catch (JSONException e) {
                    PToast.showShort("视频处理失败请重试");
                }
            }
        });
    }

    private void submitAuth() {
        if ("".equals(sPicNoHttp) ||
                "".equals(videoVerifyBean.getVideourl())) {
            PToast.showShort("必须完成照片和视频的上传");
            return;
        }
        ModuleMgr.getCommonMgr().addVideoVerify(sPicNoHttp, videoVerifyBean.getVideourl(), new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.getResponseString().contains("ok")) {
                    videoVerifyBean.setStatus(1);
                    changeAllStatus(1);
                    setHideTopRightView(true);
                    PToast.showShort("提交成功，请等待审核");
                } else {
                    PToast.showShort("提交失败，请稍后重试");
                }
            }
        });

    }

    // 是否隐藏右上角view
    protected void setHideTopRightView(boolean bool) {
        if (bool) {
            setTitleRight("", null);
        } else {
            setTitleRight("提交", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitAuth();
                }
            });
        }
    }

    private void getStatus() {
        ModuleMgr.getCommonMgr().requestVideochatConfigSendUI(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                ModuleMgr.getCommonMgr().setVideoVerify((VideoVerifyBean) response.getBaseData());
                videoVerifyBean = ModuleMgr.getCommonMgr().getVideoVerify();
                changeAllStatus(videoVerifyBean.getStatus());
            }
        });
    }

}
