package com.juxin.predestinate.ui.discover.Cards;

import android.content.Context;
import android.view.View;

import com.juxin.predestinate.R;

import java.util.List;




/**
 * Created by zm .
 */
public class CardsAdapter extends BaseCardAdapter {
    private List<ContentBean> datas;
    private Context context;

    public CardsAdapter(List<ContentBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public void setData(List<ContentBean> datas) {
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.f1_id_card_auth_act;
    }

    @Override
    public void onBindData(int position, View cardview) {
        if (datas == null || datas.size() == 0) {
            return;
        }
    }

    /**
     * 如果可见的卡片数是3，则可以不用实现这个方法
     *
     * @return
     */
    @Override
    public int getVisibleCardCount() {
        return 4;
    }
}
