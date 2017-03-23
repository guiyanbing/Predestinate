package com.juxin.predestinate.module.album.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.album.help.AlbumHelper;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.PhotoUtils;
import com.juxin.predestinate.module.util.UIUtil;

import java.util.ArrayList;

/**
 * 确认完成界面
 * <p>
 * Created by Su on 2016/7/24.
 */
public class ResetOrFinishAct extends BaseActivity implements View.OnClickListener {
    private ImageView photoImg;
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setCanNotify(false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_album_confirm_act);

        initView();
        setImg();
    }

    private void initView() {
        imgPath = getIntent().getStringExtra("path");
        photoImg = (ImageView) findViewById(R.id.confirm_img);
        TextView reset = (TextView) findViewById(R.id.reset);
        TextView finish = (TextView) findViewById(R.id.finish);

        reset.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    private void setImg() {
        if (TextUtils.isEmpty(imgPath)) {
            return;
        }
        Bitmap bitmap = PhotoUtils.decodeSampledBitmap(imgPath, 720, 1280);
        if (bitmap == null) {
            return;
        }
        UIUtil.setImageBitmap(photoImg, bitmap);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reset:
                finish();
                break;

            case R.id.finish:
                if (AlbumHelper.getInstance().isCrop()) {
                    AlbumHelper.getInstance().skipToCrop(this, imgPath);
                } else {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(imgPath);
                    AlbumHelper.getInstance().showTransferAct(this, list);
                }
                break;
        }
    }
}
