package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.hot.UserInfoHot;
import com.juxin.predestinate.bean.center.user.hot.UserInfoHotList;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.discover.Cards.BaseCardAdapter;
import com.juxin.predestinate.ui.discover.Cards.CardsAdapter;
import com.juxin.predestinate.ui.discover.Cards.CardsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/6/6.
 */

public class HotFragment extends BaseFragment implements RequestComplete, BaseCardAdapter.OnDataNeedReq, CardsView.CardsSlideListener {

    private CardsView cardsView;
    private CardsAdapter adapter;

    private List<UserInfoHot> viewData = new ArrayList<>();

    private int page = 0;

    private boolean isRef = false;

    private int nowPosition = 0;

    private boolean isFirst = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.f1_hot_fragment);
        initView();
        initData();
        return getContentView();

    }

    private void initData() {
        page = 1;
        ModuleMgr.getCommonMgr().reqUserInfoHotList(page, true, this);
    }

    private void loadMoreData() {
        if (isRef) {
            page = 1;
            ModuleMgr.getCommonMgr().reqUserInfoHotList(page, true, this);
        } else {
            page++;
            ModuleMgr.getCommonMgr().reqUserInfoHotList(page, false, this);
        }
    }

    private void initView() {
        cardsView = (CardsView) findViewById(R.id.hot_card_view);
        adapter = new CardsAdapter(viewData, getContext(), this);
        cardsView.setCardsSlideListener(this);
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()) {
            UserInfoHotList list = new UserInfoHotList();
            list.parseJson(response.getResponseString());

            if (list.getHotLists().size() != 0) {
                isRef = list.isRef();
                if (isRef) {
                    if (nowPosition < viewData.size()) {
                        List<UserInfoHot> temp = new ArrayList<>();
                        for (int i = 0; i < viewData.size(); i++) {
                            if (i >= nowPosition) {
                                temp.add(viewData.get(i));
                            }
                        }

                        if (viewData.size() != 0) {
                            viewData.clear();
                        }
                        viewData.addAll(0, temp);
                        nowPosition = 0;
                    } else {
                        if (viewData.size() != 0) {
                            viewData.clear();
                        }
                    }
                }
                viewData.addAll(viewData.size(), list.getHotLists());
                adapter.setData(viewData);
                if (isFirst) {
                    cardsView.setAdapter(adapter);
                    isFirst = false;
                } else {
                    cardsView.notifyDatasetChanged(nowPosition);
                }
            }
        }
    }

    @Override
    public void onNeedReq() {
        loadMoreData();
    }

    @Override
    public void onShow(int index) {
        nowPosition = index;
    }

    @Override
    public void onCardVanish(int index, CardsView.SlideType type) {

    }

    @Override
    public void onItemClick(View cardImageView, int index) {

    }
}
