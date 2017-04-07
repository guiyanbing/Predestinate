package com.juxin.predestinate.ui.recommend;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.controls.xRecyclerView.XRecyclerView;
import com.juxin.library.log.PToast;
import com.juxin.library.view.CircularCoverView;
import com.juxin.mumu.bean.utils.MMToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.recommend.TagInfo;
import com.juxin.predestinate.module.util.TimeUtil;

import java.util.List;

/**
 * 推荐的人筛选标签适配器
 * Created YAO on 2017/3/30.
 */

public class RecommendFilterAdapter extends XRecyclerView.Adapter<RecommendFilterAdapter.MyViewHolder> {
    private Context context;
    private List<TagInfo> list;
    public static final int FILTER_TAG_CHOSEN = 0;
    public static final int FILTER_TAG = 1;
    private TagItemClickListener mListener;

    public RecommendFilterAdapter(Context context, List<TagInfo> list, int tag, TagItemClickListener mListener) {
        this.context = context;
        this.list = list;
        this.mListener = mListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.p1_recommend_filter_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_tagName.setText(list.get(position).getTagName());
        holder.tv_tagName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClick(position, holder.tv_tagName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tagName;

        public MyViewHolder(View view) {
            super(view);
            tv_tagName = (TextView) view.findViewById(R.id.tv_tagName);
        }
    }

    public interface TagItemClickListener {
        void itemClick(int position, View itemView);
    }
}
