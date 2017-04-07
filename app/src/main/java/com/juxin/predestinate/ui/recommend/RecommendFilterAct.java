package com.juxin.predestinate.ui.recommend;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.area.City;
import com.juxin.predestinate.bean.recommend.TagInfo;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.picker.picker.AddressPicker;
import com.juxin.predestinate.module.logic.baseui.picker.picker.RangePicker;
import com.juxin.predestinate.module.logic.config.InfoConfig;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.third.recyclerholder.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 推荐的人筛选页面
 * Created YAO on 2017/4/6.
 */

public class RecommendFilterAct extends BaseActivity {
    CustomRecyclerView cv_tag, cv_chosen;
    RecyclerView rv_chosen, rv_tag;
    TextView tv_del;
    RecommendFilterAdapter chosenAdapter, tagAdapter;
    private List<TagInfo> listTag, listChosen;
    private final int spanCount = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_recommendfilter_act);
        setTitle(getResources().getString(R.string.title_recommend_filter));
        setTitleRight("提交", R.color.title_right_commit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 筛选请求
                String chosen = "";
                for (int i = 0; i < listChosen.size(); i++) {
                    chosen += listChosen.get(i).getTagName() + "--";
                }
                MMToast.showShort(chosen);
            }
        });
        initView();
    }

    private void getTag() {
        listTag = new ArrayList<>();
        listChosen = new ArrayList<>();
        String tags[] = {"选择地区", "年龄", "开放的都市少女", "爽快", "可爱", "萝莉", "御姐"};
        for (int i = 0; i < tags.length; i++) {
            TagInfo tagInfo = new TagInfo();
            tagInfo.setTagName(tags[i]);
            tagInfo.setPosition(i);
            listTag.add(tagInfo);
        }
    }

    //选择地区
    private void addArea(final int position, final String type) {
        PickerDialogUtil.showAddressPickerDialog2(this, new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(City city) {
                if ("update".equals(type)) {
                    TagInfo tagInfo = listChosen.get(position);
                    tagInfo.setTagMark(city.getCityID());
                    tagInfo.setTagName(city.getProvinceName() + city.getCityName());
                } else if ("add".equals(type)) {
                    TagInfo tagInfo = new TagInfo();
                    tagInfo.setTagMark(city.getCityID());
                    tagInfo.setTagName(city.getProvinceName() + city.getCityName());
                    tagInfo.setPosition(position);
                    listChosen.add(tagInfo);
                }
                chosenAdapter.notifyDataSetChanged();
            }
        }, "", "");
    }

    //选择年龄
    private void addAge(final int position, final String type) {
        PickerDialogUtil.showRangePickerDialog(this, new RangePicker.OnRangePickListener() {
            @Override
            public void onRangePicked(String firstText, String secondText) {
                if ("update".equals(type)) {
                    TagInfo tagInfo = listChosen.get(position);
                    tagInfo.setTagName(firstText + "~" + secondText);
                    tagInfo.setPosition(position);
                } else if ("add".equals(type)) {
                    TagInfo tagInfo = new TagInfo();
                    tagInfo.setTagName(firstText + "~" + secondText);
                    tagInfo.setPosition(position);
                    listChosen.add(tagInfo);
                }
                chosenAdapter.notifyDataSetChanged();
            }
        }, InfoConfig.getInstance().getAgeN().getShow(), "", "", "请选择");
    }

    //检测重复标签
    private boolean checkRepeatAdd(int position) {
        if (listChosen.size() > 0) {
            for (int i = 0; i < listChosen.size(); i++) {
                if (listChosen.get(i).getPosition() == position) {
                    if (position == 0) {
                        addArea(position, "update");
                    } else if (position == 1) {
                        addAge(position, "update");
                    } else {
                        MMToast.showShort(getResources().getString(R.string.toast_chosen_repeat));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void initRecycleView() {
        rv_chosen = cv_chosen.getRecyclerView();
        rv_tag = cv_tag.getRecyclerView();
        tagAdapter = new RecommendFilterAdapter(this, listTag, RecommendFilterAdapter.FILTER_TAG, new RecommendFilterAdapter.TagItemClickListener() {
            @Override
            public void itemClick(int position, View itemView) {
                if (!checkRepeatAdd(position)) {
                    if (listChosen.size() < 3) {
                        if (position == 0) {
                            addArea(position, "add");
                        } else if (position == 1) {
                            addAge(position, "add");
                        } else {
                            listChosen.add(listTag.get(position));
                            chosenAdapter.notifyDataSetChanged();
                        }
                    } else {
                        MMToast.showShort(getResources().getString(R.string.toast_chosen_tagmax));
                    }
                }
            }
        });
        chosenAdapter = new RecommendFilterAdapter(this, listChosen, RecommendFilterAdapter.FILTER_TAG_CHOSEN, new RecommendFilterAdapter.TagItemClickListener() {
            @Override
            public void itemClick(int position, View itemView) {
                MMToast.showShort("点击了chosen");
            }
        });
        rv_tag.setLayoutManager(new GridLayoutManager(this, spanCount));
        rv_chosen.setLayoutManager(new GridLayoutManager(this, spanCount));
        rv_tag.setAdapter(tagAdapter);
        rv_chosen.setAdapter(chosenAdapter);
        rv_tag.setItemAnimator(new DefaultItemAnimator());
        rv_chosen.setItemAnimator(new DefaultItemAnimator());
        rv_tag.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.px20_dp)));
        rv_chosen.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.px20_dp)));
        cv_tag.showRecyclerView();
        cv_chosen.showRecyclerView();
    }


    private void initView() {
        getTag();
        cv_chosen = (CustomRecyclerView) findViewById(R.id.cv_tag_chosen);
        cv_tag = (CustomRecyclerView) findViewById(R.id.cv_tag);
        tv_del = (TextView) findViewById(R.id.tv_del);
        initRecycleView();

        tv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除选择标签
                listChosen.clear();
                chosenAdapter.notifyDataSetChanged();
            }
        });


    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            //不是第一个的格子都设一个左边和底部的间距
            outRect.left = space;
            outRect.bottom = space;
//            //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
            if (parent.getChildLayoutPosition(view) % 3 == 0) {
                outRect.left = 0;
            }
        }

    }
}
