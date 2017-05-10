package com.juxin.predestinate.ui.user.check.self.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.GridView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;

/**
 * 我的相册
 * Created by Su on 2017/5/10.
 */

public class UserPhotoAct extends BaseActivity {

    private GridView grid_photo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f1_user_info_photo_act);
        setBackView(getString(R.string.user_info_photo));
        initView();
    }

    private void initView() {
        grid_photo = (GridView) findViewById(R.id.grid_photo);

        // 计算GridView的横向间隔的宽度
        int horizontalSpacing = ModuleMgr.getAppMgr().getScreenWidth() / 50;
        grid_photo.setHorizontalSpacing(horizontalSpacing);
        // 计算GridView的每一列的宽度
        int columnWidth = (ModuleMgr.getAppMgr().getScreenWidth() - 5 * horizontalSpacing) / 4;
        grid_photo.setColumnWidth(columnWidth);
    }
}
