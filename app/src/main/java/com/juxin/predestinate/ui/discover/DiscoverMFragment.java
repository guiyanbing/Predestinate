package com.juxin.predestinate.ui.discover;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;

/**
 * 创建日期：2017/6/6
 * 描述:
 * 作者:lc
 */
public class DiscoverMFragment extends BaseFragment implements View.OnClickListener {

    private BaseFragment current;
    private RadioButton discover_recommend, discover_hot;
    private DiscoverFragment discoverFragment;
    private HotFragment hotFragment;
    private FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.discover_m_fragment);
        setTopView();
        initView();
        initFragment();
        return getContentView();
    }

    private void setTopView() {
        setTitleRightImg(R.drawable.f1_discover_select_ico, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discoverFragment.showDiscoverSelectDialog();
            }
        });
//        MsgMgr.getInstance().attach(this);
    }

    private void initView() {
        if (ModuleMgr.getCenterMgr().getMyInfo().isMan()) {
            View mDisManTitle = LayoutInflater.from(getContext()).inflate(R.layout.f1_discover_man_title, null);
            setTitleCenterContainer(mDisManTitle);
            discover_recommend = (RadioButton) findViewById(R.id.discover_recommend);
            discover_hot = (RadioButton) findViewById(R.id.discover_hot);
            discover_recommend.setOnClickListener(this);
            discover_hot.setOnClickListener(this);
        } else {
            setTitle(getString(R.string.discover_title));
        }
    }

    private void initFragment() {
        fragmentManager = getChildFragmentManager();
        discoverFragment = new DiscoverFragment();
        if (ModuleMgr.getCenterMgr().getMyInfo().isMan()) {
            hotFragment = new HotFragment();
        }
        switchContent(discoverFragment);
    }

    /**
     * 切换当前显示的fragment
     */
    private void switchContent(BaseFragment fragment) {
        if (current != fragment) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            if (current != null) {
                transaction.hide(current);
            }
            if (!fragment.isAdded()) { //先判断是否被add过
                transaction.add(R.id.discover_container, fragment).commitAllowingStateLoss();
            } else {
                transaction.show(fragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            current = fragment;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.discover_recommend:
                setTitleRightImgVisible();
                discover_recommend.setChecked(true);
                discover_hot.setChecked(false);
                switchContent(discoverFragment);
                //统计
                DisCoverStatistics.onClickRecommend();
                break;
            case R.id.discover_hot:
                setTitleRightImgGone();
                discover_recommend.setChecked(false);
                discover_hot.setChecked(true);
                switchContent(hotFragment);
                //统计
                DisCoverStatistics.onClickHot();
                break;
            default:
                break;
        }
    }
}
