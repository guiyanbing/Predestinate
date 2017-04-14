package com.juxin.predestinate.ui.recommend;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.area.City;
import com.juxin.predestinate.bean.recommend.TagInfo;
import com.juxin.predestinate.bean.recommend.TagInfoList;
import com.juxin.predestinate.module.logic.baseui.BaseActivity;
import com.juxin.predestinate.module.logic.baseui.picker.picker.AddressPicker;
import com.juxin.predestinate.module.logic.baseui.picker.picker.RangePicker;
import com.juxin.predestinate.module.logic.config.InfoConfig;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
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

    private int age_min, age_max, provinceID, cityID;

    private List<Integer> tagsList = new ArrayList<>();
    private final int areaTagId = -11;
    private final int ageTagId = -12;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p1_recommendfilter_act);
        setBackView(getResources().getString(R.string.title_recommend_filter));
        setTitleRight("提交", R.color.title_right_commit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsList.clear();
                Intent data = new Intent();
                for (int i = 0; i < listChosen.size(); i++) {
                    TagInfo tagInfo = listChosen.get(i);
                    switch (tagInfo.getTagType()) {
                        case 0://印象tag
                            tagsList.add(listChosen.get(i).getTagID());
                            break;
                        case 1://地区
                            data.putExtra("province", provinceID);
                            data.putExtra("city", cityID);
                            break;
                        case 2://年龄
                            data.putExtra("age_min", age_min);
                            data.putExtra("age_max", age_max);
                            break;
                    }
                }
                if (tagsList.size() > 0) {
                    int tags[] = new int[tagsList.size()];
                    for (int i = 0; i < tagsList.size(); i++) {
                        tags[i] = tagsList.get(i);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putIntArray("tags", tags);
                    data.putExtras(bundle);
                }
                setResult(200, data);
                back();
            }
        });
        initView();
    }

    private void initTag() {
        listChosen = new ArrayList<>();
        listTag = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            TagInfo tagInfo = new TagInfo();
            if (i == 0) {
                tagInfo.setTagName("选择地区");
                tagInfo.setTagType(1);
                tagInfo.setTagID(areaTagId);
            } else if (i == 1) {
                tagInfo.setTagName("年龄");
                tagInfo.setTagType(2);
                tagInfo.setTagID(ageTagId);
            }
            listTag.add(tagInfo);
        }
        TagInfoList tags = getIntent().getParcelableExtra("tags");
        listTag.addAll(tags.getTagInfos());
    }

    //选择地区
    private void addArea(final String type) {
        PickerDialogUtil.showAddressPickerDialog2(this, new AddressPicker.OnAddressPickListener() {
            @Override
            public void onAddressPicked(City city) {
                provinceID = city.getProvinceID();
                cityID = city.getCityID();
                if ("update".equals(type)) {
                    for (int i = 0; i < listChosen.size(); i++) {
                        if (listChosen.get(i).getTagID() == areaTagId) {
                            TagInfo tagInfo = listChosen.get(i);
                            tagInfo.setTagName(city.getProvinceName() + city.getCityName());
                        }
                    }
                } else if ("add".equals(type)) {
                    TagInfo tagInfo = new TagInfo();
                    tagInfo.setTagType(1);
                    tagInfo.setTagName(city.getProvinceName() + city.getCityName());
                    tagInfo.setTagID(areaTagId);
                    listChosen.add(tagInfo);
                }
                chosenAdapter.notifyDataSetChanged();
            }
        }, "", "");
    }

    //选择年龄
    private void addAge(final String type) {
        PickerDialogUtil.showRangePickerDialog(this, new RangePicker.OnRangePickListener() {
            @Override
            public void onRangePicked(String firstText, String secondText) {
                age_min = InfoConfig.getInstance().getAgeN().getSubmitWithShow(firstText);
                age_max = InfoConfig.getInstance().getAgeN().getSubmitWithShow(secondText);
                if ("update".equals(type)) {
                    for (int i = 0; i < listChosen.size(); i++) {
                        if (listChosen.get(i).getTagID() == ageTagId) {
                            TagInfo tagInfo = listChosen.get(i);
                            tagInfo.setTagName(firstText + ("不限".equals(secondText) ? "以上" : "~" + secondText));
                        }
                    }
                } else if ("add".equals(type)) {
                    TagInfo tagInfo = new TagInfo();
                    tagInfo.setTagName(firstText + ("不限".equals(secondText) ? "以上" : "~" + secondText));
                    tagInfo.setTagType(2);
                    tagInfo.setTagID(ageTagId);
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
                if (listChosen.get(i).getTagID() == listTag.get(position).getTagID()) {
                    if (listChosen.get(i).getTagID() == areaTagId) {
                        addArea("update");
                    } else if (listChosen.get(i).getTagID() == ageTagId) {
                        addAge("update");
                    } else {
                        MMToast.showShort(getResources().getString(R.string.toast_chosen_repeat));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void initAdapter(RecyclerView recycleView, RecommendFilterAdapter adapter, List<TagInfo> list) {
        adapter.setList(list);
        int spanCount = 3;
        recycleView.setLayoutManager(new GridLayoutManager(this, spanCount));
        recycleView.setAdapter(adapter);
        recycleView.addItemDecoration(new SpaceItemDecoration((int) getResources().getDimension(R.dimen.px20_dp)));
    }

    private void initRecycleView() {
        rv_chosen = cv_chosen.getRecyclerView();
        rv_tag = cv_tag.getRecyclerView();
        tagAdapter = new RecommendFilterAdapter(this,RecommendFilterAdapter.FILTER_TAG);
        chosenAdapter = new RecommendFilterAdapter(this,RecommendFilterAdapter.FILTER_TAG_CHOSEN);
        initAdapter(rv_tag, tagAdapter, listTag);
        initAdapter(rv_chosen, chosenAdapter, listChosen);
        cv_tag.showRecyclerView();
        cv_chosen.showRecyclerView();
        tagAdapter.setOnItemClickListener(new BaseRecyclerViewHolder.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                if (!checkRepeatAdd(position)) {
                    if (listChosen.size() < 3) {
                        if (position == 0) {
                            addArea("add");
                        } else if (position == 1) {
                            addAge("add");
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
    }


    private void initView() {
        initTag();
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

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        SpaceItemDecoration(int space) {
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
