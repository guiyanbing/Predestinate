package com.juxin.predestinate.ui.recommend;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PToast;
import com.juxin.library.view.CircleImageView;
import com.juxin.library.view.CircularCoverView;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.recommend.TagInfo;
import com.juxin.predestinate.module.util.TimeUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

import java.util.List;

/**
 * 推荐的人筛选标签适配器
 * Created YAO on 2017/3/30.
 */

public class RecommendFilterAdapter extends BaseRecyclerViewAdapter<TagInfo> {
    public static final int FILTER_TAG_CHOSEN = 0;
    public static final int FILTER_TAG = 1;
    private int tag;
    private Context context;


    public void setTagType(Context context, int tag) {
        this.tag = tag;
        this.context = context;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.p1_recommend_filter_item};
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {
        TagInfo tagInfo = getItem(position);
        final TextView tv_tagName = viewHolder.findViewById(R.id.tv_tagName);
        ImageView iv_mark = viewHolder.findViewById(R.id.iv_mark);
        RelativeLayout rl_item = viewHolder.findViewById(R.id.rl_item);

        tv_tagName.setText(tagInfo.getTagName());
        if (tag == FILTER_TAG_CHOSEN) {
            iv_mark.setVisibility(View.INVISIBLE);
            tv_tagName.setTextColor(context.getResources().getColor(R.color.btn_getcode_stroke));
            rl_item.setBackgroundResource(R.drawable.p1_tag_chosen_bg);
        } else if (tag == FILTER_TAG) {
            iv_mark.setVisibility(position > 1 ? View.INVISIBLE : View.VISIBLE);
            tv_tagName.setTextColor(context.getResources().getColor(R.color.text_zhuyao_black));
            rl_item.setBackgroundResource(R.drawable.p1_tag_bg);
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

}
