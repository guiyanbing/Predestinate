package com.juxin.predestinate.ui.user.my.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.bean.my.AttentionUserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.config.AreaConfig;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;

/**
 * 关注我的适配器
 * Created by zm on 2017/4/13.
 */
public class AttentionMeAdapter extends BaseRecyclerViewAdapter<AttentionUserDetail> implements RequestComplete, BaseRecyclerViewHolder.OnItemClickListener {

    private Context mContext;
    private int postion;
    private MyViewHolder vh;

    public AttentionMeAdapter(Context mContext) {
        this.mContext = mContext;
        this.setOnItemClickListener(this);
    }

    @Override
    public int[] getItemLayouts() {
        return new int[]{R.layout.f1_attention_panel_list_item};
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindRecycleViewHolder(BaseRecyclerViewHolder viewHolder, final int position) {

        final MyViewHolder mHolder = new MyViewHolder(viewHolder);//初始化MyViewHolder
        final AttentionUserDetail info = getItem(position);
        if (info != null && info.getNickname() != null && info.getAge() > 0 && info.getAvatar() != null && info.getGender() > 0) {
            mHolder.imgHead.setImageResource(R.drawable.f1_userheadpic_weishangchuan);//头像
            checkAndShowAvatarStatus(info.getAvatar_status(), mHolder.imgHead, info.getAvatar());
            mHolder.tvNickname.setText(info.getNickname() != null ? info.getNickname() : mContext.getString(R.string.no_nickname));//昵称
            checkAndShowVipStatus(info.is_vip(), mHolder.imVipState, mHolder.tvNickname);
            mHolder.tvAge.setText(info.getAge() + mContext.getString(R.string.age));//年龄
            checkAndShowCityValue(AreaConfig.getInstance().getCityNameByID(Integer.valueOf(info.getCity())), mHolder.tvDiqu);//地区
            mHolder.tvpiccount.setText(info.getPhotoNum() + mContext.getString(R.string.check_info_album));
        } else {
            mHolder.tvNickname.setText(info.getUid() + "");
            mHolder.tvAge.setText(R.string.loading);
            mHolder.imgHead.setImageResource(R.drawable.f1_userheadpic_weishangchuan);
        }
        if (info.getType() == 0){
            mHolder.tvconcern.setText(R.string.attention_ta);
        }else {
            mHolder.tvconcern.setText(R.string.cancel_the_attention);
        }
        mHolder.tvconcern.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
                AttentionMeAdapter.this.postion = position;
                AttentionMeAdapter.this.vh = mHolder;
                if (userDetail != null) {
                    if (userDetail.getGender() != 2) {//不是女生需要开通vip
                        if (!userDetail.isVip()) {
                            createOpenVipDialog(mContext.getString(R.string.open_the_vip_can_be_operation));//提示开通vip
                            return;
                        }
                    }
                }
                if (!NetworkUtils.isConnected(mContext)) {//未联网返回
                    PToast.showShort(mContext.getString(R.string.net_error_check_your_net));
                    return;
                }
                // 执行关注Class
                LoadingDialog.show((FragmentActivity) mContext);
                if (info.getType() == 0) { //未关注他时
                    ModuleMgr.getCommonMgr().follow(info.getUid(), AttentionMeAdapter.this);//关注他
                    return;
                }
                ModuleMgr.getCommonMgr().unfollow(info.getUid(), AttentionMeAdapter.this);//已关注时取消关注
                //                    MyConcernAct_ItemViewObject obj = new MyConcernAct_ItemViewObject((TextView) view, iPos);
                //                    FollowUser followUser = new FollowUser(mContext, otherId, FollowUser.FOLLOWUSER_ADD, obj);
                //                    followUser.setCallBack(MyConcern_BeAct_Adapter.this);
                //                    followUser.onStart();
            }
        });
    }

    private void createOpenVipDialog(String str) {//vip弹框
        final Dialog dialog = new Dialog(mContext, R.style.dialog);
        View view = LayoutInflater.from(mContext).inflate(R.layout.f1_app_tips_dialog, null);
        TextView txt_update_info = (TextView) view.findViewById(R.id.txt_update_info);
        txt_update_info.setText(str);
        TextView txt_update_submit = (TextView) view.findViewById(R.id.txt_update_submit);
        TextView txt_update_cancel = (TextView) view.findViewById(R.id.txt_update_cancel);
        txt_update_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_update_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //跳转到我的y币界面
                UIShow.showBuyCoinActivity(mContext);
            }
        });
        dialog.setContentView(view);
        dialog.show();
    }

    private void checkAndShowAvatarStatus(int status, ImageView img, String avatar) {
        switch (status) {
            case 0:// 正在审核
                img.setImageResource(R.drawable.f1_otheruserheadpic_shenhezhong);
                break;
            case 1:// 审核通过
                ImageLoader.loadAvatar(mContext, avatar, img);
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

    private void checkAndShowCityValue(String city, TextView tvCity) {
        if (city == null) {
            tvCity.setText("");
        } else {
            tvCity.setText(city);
        }
    }

    @Override
    public int getRecycleViewItemType(int position) {
        return 0;
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        LoadingDialog.closeLoadingDialog();
        try {
            if (response.isOk()) {//返回成功
                if (getList().get(postion).getType() == 0) {
                    vh.tvconcern.setText(mContext.getString(R.string.privatechat_title_unsubscribe));
                    getList().get(postion).setType(1);
                    if (postion != 0) {
                        notifyItemChanged(postion);
                        return;
                    }
                    notifyDataSetChanged();
                    return;
                }
                vh.tvconcern.setText(mContext.getString(R.string.attention_ta));
                getList().get(postion).setType(0);
                if (postion != 0) {
                    notifyItemChanged(postion);
                    return;
                }
                notifyDataSetChanged();
            } else {
                PToast.showShort(mContext.getString(R.string.toast_commit_suggest_error));
            }
        } catch (Exception e) {
            PLogger.e("AttentionMeAdapter_________"+e.toString());
        }
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