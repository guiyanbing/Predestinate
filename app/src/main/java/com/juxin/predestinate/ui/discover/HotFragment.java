package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.view.CustomFrameLayout;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.hot.UserInfoHot;
import com.juxin.predestinate.bean.center.user.hot.UserInfoHotList;
import com.juxin.predestinate.module.local.statistics.StatisticsDiscovery;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.discover.Cards.CardsAdapter;
import com.juxin.predestinate.ui.discover.Cards.CardsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhang on 2017/6/6.
 */

public class HotFragment extends BaseFragment implements RequestComplete, CardsView.CardsSlideListener {

    private CustomFrameLayout hot_frame;
    private CardsView cardsView;
    private CardsAdapter adapter;

    private List<UserInfoHot> viewData = new ArrayList<>();

    private int cachDataSize = 10;

    private int page = 0;
    //是否需要重新加载数据
    private boolean isRef = false;

    private int nowPosition = 0;

    //是否是首次加载数据
    private boolean isFirst = true;
    //是否可以继续加载数据
    private boolean isNeedReq = false;
    //请求锁，确保不会多次请求
    private boolean isCanReq = false;

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
            isCanReq = false;
            ModuleMgr.getCommonMgr().reqUserInfoHotList(page, false, this);
        }
    }

    private void initView() {
        hot_frame = (CustomFrameLayout) findViewById(R.id.hot_frame);
        hot_frame.setList(new int[]{R.id.common_loading, R.id.hot_card_nodata, R.id.hot_card_layout});
        cardsView = (CardsView) findViewById(R.id.hot_card_view);
        adapter = new CardsAdapter(viewData, getContext());
        cardsView.setCardsSlideListener(this);
        showLoading();
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()) {
            UserInfoHotList list = new UserInfoHotList();
            list.parseJson(response.getResponseString());
            if (list.getHotLists().size() != 0) {
                isRef = list.isRef();
                if (isRef && page == 1 && list.getHotLists().size() < cachDataSize) {
                    isNeedReq = false;
                } else {
                    isNeedReq = true;
                }

                PLogger.d("HotFragment======》  --- isNeedReq = " + isNeedReq + " --- isRef = " + isRef);

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
                PLogger.d("HotFragment======》  --- nowPosition = " + nowPosition + " --- viewData size = " + viewData.size());
                viewData.addAll(viewData.size(), list.getHotLists());
                adapter.setData(viewData);

                PLogger.d("HotFragment======》  --- nowPosition = " + nowPosition + " --- viewData size = " + viewData.size());
                adapter.setNeedrReq(isNeedReq);
                if (isFirst) {
                    cardsView.setAdapter(adapter);
                    isFirst = false;
                } else {
                    cardsView.notifyDatasetChanged(nowPosition);
                }
                showCardView();
            } else {
                if (page == 1) {
                    showNodata();
                }
            }
        } else {
            showNodata();
        }
        isCanReq = true;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (viewData.size() == 0 && page == 1) {
                initData();
            }
        }
    }


    @Override
    public void onShow(int index) {
        if (viewData.size() != 0) {
            int position = index % viewData.size();
            nowPosition = position;
            //判断是否需要请求数据
            if (position + cachDataSize >= viewData.size() && isNeedReq && isCanReq) {
                loadMoreData();
            }
        }
    }

    @Override
    public void onCardVanish(int index, CardsView.SlideType type) {
        if (viewData.size() != 0) {
            //统计
            int position = index % viewData.size();
            StatisticsDiscovery.onHotRemove(viewData.get(position).getUid());
        }

    }

    @Override
    public void onItemClick(View cardImageView, int index) {

    }

    @Override
    public void onLastCardBack() {
        PToast.showShort(getString(R.string.hot_card_last_item_tip));
    }

    private void showNodata() {
        hot_frame.show(R.id.hot_card_nodata);
        if (viewData.size() != 0) {
            viewData.clear();
        }
    }

    private void showCardView() {
        hot_frame.show(R.id.hot_card_layout);
    }

    private void showLoading() {
        hot_frame.showLoading(R.id.common_loading, R.id.loading_gif, R.drawable.p1_loading);
    }

}
