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
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.user.paygoods.bean.PayGood;

import java.util.ArrayList;

/**
 * 商品列表通用panel
 * Created by Su on 2017/3/31.
 */

public class GoodsListPanel extends BaseViewPanel implements BaseRecyclerViewHolder.OnItemClickListener {
    private float toDpMutliple = 1; //根据屏幕密度获取屏幕转换倍数

    private RecyclerView recyclerView;
    private GoodsListAdapter adapter;

    private int position = 0;

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
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void refresh(ArrayList<PayGood> payGoodList) {
        adapter.setList(payGoodList);
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void onItemClick(View convertView, int position) {
        this.position = position;
        adapter.updateData(position);
    }

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
