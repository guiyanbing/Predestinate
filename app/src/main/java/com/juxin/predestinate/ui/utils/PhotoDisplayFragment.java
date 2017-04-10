package com.juxin.predestinate.ui.utils;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.juxin.mumu.bean.utils.FileUtil;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.third.TouchImageView;

/**
 * PhotoDisplayAct：查看大图Fragment页面
 */
public class PhotoDisplayFragment extends BaseFragment implements RequestComplete {

    private ProgressBar progress;
    private TouchImageView image;

    private String pic;

    private TouchImageView.OnClickEvent onClickEvent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p1_photo_display_item, container, false);
        pic = getArguments().getString("pic");
        initView(view);
        return view;
    }

    private void initView(View v) {
        progress = (ProgressBar) v.findViewById(R.id.progress);
        image = (TouchImageView) v.findViewById(R.id.image_view);
        if (onClickEvent != null) {
            image.setOnClickEvent(onClickEvent);
        }
        loadPic(pic);
    }

    public void setOnClickEvent(TouchImageView.OnClickEvent onClickEvent) {
        this.onClickEvent = onClickEvent;
    }

    /**
     * 加载图片
     */
    public void loadPic(String pic) {
        if (!TextUtils.isEmpty(pic)) {
            if (FileUtil.isURL(pic)) {
//                ModuleMgr.getHttpMgr().reqBigImage(pic, /*new HttpMgr.IReqComplete() {
//                    @Override
//                    public void onReqComplete(HttpResult result) {
//                        if (result.isOk()) {
//                            progress.setVisibility(View.GONE);
//                            image.setImageBitmap((Bitmap) result.getData());
//                        }
//                    }
//                }*/this);
            } else {
//                ModuleMgr.httpMgr.reqNativeImg(image, pic);
                progress.setVisibility(View.GONE);
            }
        }
    }


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    progress.setVisibility(View.GONE);
                    image.setImageBitmap((Bitmap) msg.obj);
                    break;
                case 11:
                    loadPic(pic);
                    break;
            }
        }
    };

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()) {
            Message message = new Message();
            message.what = 10;
            message.obj = response.getBaseData();
            handler.sendMessage(message);
        } else {
//            if (response.getCode() != null && result.getCode().equals("onPrepareLoad"))
//                handler.sendEmptyMessage(11);
        }
    }
}
