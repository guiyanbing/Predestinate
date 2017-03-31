package com.juxin.predestinate.ui.user.check.edit;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.util.UIUtil;
import com.juxin.predestinate.ui.user.check.adapter.UserSecretAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 私密相册/视频页
 * Created by Su on 2017/3/29.
 */

public class UserSecretAct extends BaseActivity implements View.OnClickListener {
    private static final int SECRET_SHOW_COLUMN = 3; // 列数
    private float toDpMutliple = 1; //根据屏幕密度获取屏幕转换倍数
    private UserDetail userDetail;

    private RecyclerView recyclerView;
    private UserSecretAdapter secretAdapter;
    private static boolean isFirst = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_user_secret_act);

        initTitle();
        initView();
    }

    private void initTitle() {
        toDpMutliple = UIUtil.toDpMultiple(this);
        userDetail = ModuleMgr.getCenterMgr().getMyInfo();
        setBackView();
        setTitleRight(getString(R.string.user_self_secret_edit), this);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.secret_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, SECRET_SHOW_COLUMN));
        recyclerView.addItemDecoration(new ItemSpaces((int) (10 * toDpMutliple)));

        secretAdapter = new UserSecretAdapter();
        recyclerView.setAdapter(secretAdapter);

        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        data.add("8");
        secretAdapter.setList(data);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.base_title_right_txt:
                if (isFirst) {
                    isFirst = false;
                    setTitleRight(getString(R.string.user_self_secret_cancel), this);
                    secretAdapter.setDele(true);
                }else {
                    isFirst = true;
                    setTitleRight(getString(R.string.user_self_secret_edit), this);
                    secretAdapter.setDele(false);
                }
                secretAdapter.notifyDataSetChanged();
                break;
        }
    }

    /**
     * margin
     */
    private class ItemSpaces extends RecyclerView.ItemDecoration {
        private int space;

        public ItemSpaces(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.right = space;
            outRect.left = space;
            outRect.top = space;
        }
    }
}
