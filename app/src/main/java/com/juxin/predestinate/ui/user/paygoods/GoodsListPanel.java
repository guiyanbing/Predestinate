package com.juxin.predestinate.ui.user.paygoods;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.baseui.BaseViewPanel;
import com.juxin.predestinate.module.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品列表通用panel
 * Created by Su on 2017/3/31.
 */

public class GoodsListPanel extends BaseViewPanel {
    private float toDpMutliple = 1; //根据屏幕密度获取屏幕转换倍数

    private RecyclerView recyclerView;
    private GoodsListAdapter adapter;

    public GoodsListPanel(Context context) {
        super(context);
        setContentView(R.layout.p1_goods_list_panel);

        initView();
    }

    private void initView() {
        toDpMutliple = UIUtil.toDpMultiple((Activity) getContext());
        recyclerView = (RecyclerView) findViewById(R.id.goods_list);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new ItemSpaces((int) (10 * toDpMutliple)));

        adapter = new GoodsListAdapter();
        recyclerView.setAdapter(adapter);

        List<String> data = new ArrayList<>();
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        data.add("8");
        adapter.setList(data);
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
