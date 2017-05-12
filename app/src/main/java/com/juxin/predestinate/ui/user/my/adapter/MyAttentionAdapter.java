package com.juxin.predestinate.ui.user.my.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.AttentionUserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.config.AreaConfig;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;


/**
 * 我的关注适配器
 * Created by zm on 2017/4/13.
 */
public class MyAttentionAdapter extends BaseRecyclerViewAdapter<AttentionUserDetail> implements BaseRecyclerViewHolder.OnItemClickListener {

    private Context mContext;

    public MyAttentionAdapter(Context mContext){
        this.mContext = mContext;
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.f1_attention_panel_list_item};
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder= super.onCreateViewHolder(parent, viewType);
        return viewHolder;
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {

        MyViewHolder mHolder = new MyViewHolder(viewHolder);
        final AttentionUserDetail info = getItem(position);
        if (info != null && info.getNickname() != null && info.getAge() > 0 && info.getAvatar() != null && info.getGender() > 0) {
            mHolder.imgHead.setImageResource(R.drawable.f1_userheadpic_weishangchuan);
            checkAndShowAvatarStatus(info.getAvatar_status(), mHolder.imgHead, info.getAvatar());
            mHolder.tvNickname.setText(info.getNickname() != null ? info.getNickname() : "无昵称");
            checkAndShowVipStatus(info.is_vip(), mHolder.imVipState, mHolder.tvNickname);
            mHolder.tvAge.setText(info.getAge() + "岁");
            mHolder.tvDiqu.setText(AreaConfig.getInstance().getCityNameByID(Integer.valueOf(info.getCity())));
            mHolder.tvpiccount.setText(info.getPhotoNum() + "照片");
        } else {
            mHolder.tvNickname.setText(info.getUid()+"");
            mHolder.tvAge.setText("加载中...");
            mHolder.imgHead.setImageResource(R.drawable.f1_userheadpic_weishangchuan);
        }
        mHolder.tvconcern.setText("取消关注");
//        mHolder.tvconcern.setTag(R.id.tag_first, position);
//        mHolder.tvconcern.setTag(R.id.tag_second, info.getOther_id());
        mHolder.tvconcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtils.isConnected(mContext)) {
                    PToast.showShort(mContext.getString(R.string.tip_net_error));
                    return;
                }
                TextView txtAttention = (TextView) view;
                // 取消关注
                txtAttention.setText("取消中...");
                ModuleMgr.getCommonMgr().unfollow(info.getUid(), new RequestComplete() {
                    @Override
                    public void onRequestComplete(HttpResponse response) {
                        //        Log.e("TTTTTTTTTMM",response.getResponseString()+"||"+ postion);
                        if (response.isOk()) {
                            int mPosition = getPosition(info);
                            getList().get(mPosition).setType(0);
                            getList().remove(mPosition);
                            MyAttentionAdapter.this.notifyItemRemoved(mPosition+1);
                        }
                    }
                });
            }
        });
    }

    private synchronized int getPosition(AttentionUserDetail info){
        int size = getListSize();
        for (int i = 0 ;i < size;i++){
            if (info.getUid() == getList().get(i).getUid()){
                return i;
            }
        }
        return -1;
    }

    private void checkAndShowAvatarStatus(int status, ImageView img, String avatar) {
        switch (status) {
            case 0:// 正在审核
                img.setImageResource(R.drawable.f1_otheruserheadpic_shenhezhong);
                break;
            case 1:// 审核通过
                //			imageLoader.displayImage(avatar, img, options);
                ImageLoader.loadAvatar(mContext,avatar,img);
                break;
            case 2:// 未通过
                img.setImageResource(R.drawable.f1_otheruserheadpic_weitongguo);
                break;
            default:
                ImageLoader.loadAvatar(mContext, avatar, img);
                break;
        }
    }

    private void checkAndShowVipStatus(Boolean isVip, ImageView imgVipStatus, TextView tvNickName) {
        if (isVip) {
            imgVipStatus.setVisibility(View.VISIBLE);
            tvNickName.setTextColor(mContext.getResources().getColor(R.color.color_F36D8E));
        } else {
            imgVipStatus.setVisibility(View.GONE);
            tvNickName.setTextColor(mContext.getResources().getColor(R.color.usersnickname));
        }
    }
    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    @Override
    public void onItemClick(View convertView, int position) {
        //跳转他人资料页
        UIShow.showCheckOtherInfoAct(mContext, getItem(position).getUid());
    }

    class MyViewHolder {

        // 头像
        public ImageView imgHead;
        // 年龄
        public TextView tvAge;
        // 身高
        public TextView tvDiqu;
        // 昵称
        public TextView tvNickname;
        // 照片数量
        public TextView tvpiccount;
        // 关注按钮
        public TextView tvconcern;
        // VIP角标
        public ImageView imVipState;

        public MyViewHolder(BaseRecyclerViewHolder convertView) {
            initView(convertView);
        }

        private void initView(BaseRecyclerViewHolder convertView) {
            imgHead = convertView.findViewById(R.id.concern_list_item_head);
            tvAge = convertView.findViewById(R.id.concern_list_item_age);
            tvDiqu = convertView.findViewById(R.id.concern_list_item_diqu);
            tvNickname = convertView.findViewById(R.id.concern_list_item_nickname);
            tvpiccount = convertView.findViewById(R.id.concern_list_item_piccount);
            tvconcern = convertView.findViewById(R.id.concern_list_item_btnconcern);
            imVipState = convertView.findViewById(R.id.concern_list_item_img_vipState);
        }
    }
}