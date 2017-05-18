package com.juxin.predestinate.ui.user.my.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;


/**
 * Created by zm on 2016/12/8
 */
public class AddPhotoView extends LinearLayout implements View.OnClickListener, ImgSelectUtil.OnChooseCompleteListener{

    private ImageView imgPhoto,imgUp;
    private TextView tvTip;
    private String strPath;

    public AddPhotoView(Context context) {
        this(context, null);
    }

    public AddPhotoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AddPhotoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //初始化
    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.f1_add_photo_view, this);
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imgPhoto = (ImageView) findViewById(R.id.add_photo_img_photo);
        imgUp = (ImageView) findViewById(R.id.add_photo_img_up);
        tvTip = (TextView) findViewById(R.id.add_photo_tv_tip);
        imgPhoto.setOnClickListener(this);
    }

    /**
     * 获取图片地址
     *
     * @return
     */
    public String getImgStrPath(){
        return strPath;
    }

    /**
     * 设置图片
     */
    public void setImg(String url){
        tvTip.setVisibility(View.INVISIBLE);
        ImageLoader.loadRoundCorners(getContext(), url, 8, imgPhoto);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_photo_img_photo://添加图片
                ImgSelectUtil.getInstance().pickPhotoGallery(getContext(), this);
                break;
        }
    }

    @Override
    public void onComplete(String... path) {
        if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
            return;
        }
        strPath = path[0];
        ImageLoader.loadRoundCorners(getContext(), path[0], 8, imgPhoto);

        //上传图片
//        if (FileUtil.isExist(path[0])) {
//            LoadingDialog.show((FragmentActivity)getContext(), "正在上传照片");
//            ModuleMgr.getMediaMgr().sendHttpFile(Constant.UPLOAD_TYPE_VIDEO_CHAT, sPic, new RequestComplete() {
//                @Override
//                public void onRequestComplete(HttpResponse response) {
//                    LoadingDialog.closeLoadingDialog();
//                    try {
//                        JSONObject jsonObject = new JSONObject(response.getResponseString());
//                        if ("ok".equals(jsonObject.optString("status")) && jsonObject.optJSONObject("res") != null) {
//                            String spic = jsonObject.optJSONObject("res").optString("file_http_path");
//                            sPicNoHttp = jsonObject.optJSONObject("res").optString("file_s_path");
//                            ImageLoader.loadCenterCrop(context, spic, ivPic);
//                            videoVerifyBean.setImgurl(spic);
//                            tvMakePic.setVisibility(View.VISIBLE);
//                            isMakePhotoOK = true;
//                            checkAndShowSubmit();
//                            changePicStatus(0);
//                            tvMakePic.setVisibility(View.VISIBLE);
//                        } else {
//                            PToast.showShort("照片处理失败请重试");
//                        }
//                    } catch (JSONException e) {
//                        PToast.showShort("照片处理失败请重试");
//                    }
//                }
//            });
//        }

        if (!TextUtils.isEmpty(path[0])){
            tvTip.setVisibility(View.INVISIBLE);
            this.setBackgroundResource(R.color.transparent);
        }
    }
}
