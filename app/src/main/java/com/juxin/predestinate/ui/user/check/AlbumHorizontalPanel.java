package com.juxin.predestinate.ui.user.check;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIUtil;

/**
 * 水平查看相册/视频panel
 * Created by Su on 2017/3/23.
 */

public class AlbumHorizontalPanel extends BaseViewPanel implements AdapterView.OnItemClickListener {
    private float toDpMutliple = 1; //根据屏幕密度获取屏幕转换倍数
    private UserDetail userDetail;

    private RecyclerView recyclerView;

    public AlbumHorizontalPanel(Context context, UserDetail userDetail) {
        super(context);
        setContentView(R.layout.p1_album_horizontal_panel);
        this.userDetail = userDetail;

        initview();
    }

    private void initview() {
        toDpMutliple = UIUtil.toDpMultiple((Activity) getContext());
        recyclerView = (RecyclerView) findViewById(R.id.rv_album_horizontal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 设置RecyclerView横向滚动
        recyclerView.setLayoutManager(linearLayoutManager);

        setParameter();
        refresh();
    }

    /**
     * 设置照片显示参数
     */
    private void setParameter() {
    }

    /**
     * 刷新页面
     */
    public void refresh() {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
