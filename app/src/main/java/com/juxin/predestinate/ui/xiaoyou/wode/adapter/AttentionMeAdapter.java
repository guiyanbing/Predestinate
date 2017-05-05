package com.juxin.predestinate.ui.xiaoyou.wode.adapter;

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
import com.juxin.library.log.PToast;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.detail.UserDetail;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.LoadingDialog;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.WebUtil;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewAdapter;
import com.juxin.predestinate.third.recyclerholder.BaseRecyclerViewHolder;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.AttentionUserDetail;

import java.util.HashMap;


/**
 * 我的关注适配器
 * Created by zm on 2017/4/13.
 */
public class AttentionMeAdapter extends BaseRecyclerViewAdapter<AttentionUserDetail> implements RequestComplete {

    private Context mContext;
    private int postion;
    private MyViewHolder vh;

    public AttentionMeAdapter(Context mContext){
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

        final MyViewHolder mHolder = new MyViewHolder(viewHolder);
        final AttentionUserDetail info = getItem(position);
        if (info != null && info.getNickname() != null && info.getAge() > 0 && info.getAvatar() != null && info.getGender() > 0) {
            mHolder.imgHead.setImageResource(R.drawable.f1_userheadpic_weishangchuan);
            checkAndShowAvatarStatus(info.getAvatar_status(), mHolder.imgHead, info.getAvatar());
            mHolder.tvNickname.setText(info.getNickname() != null ? info.getNickname() : "无昵称");
            checkAndShowVipStatus(info.is_vip(), mHolder.imVipState, mHolder.tvNickname);
            mHolder.tvAge.setText(info.getAge() + "岁");
            checkAndShowCityValue(info.getCity(), mHolder.tvDiqu);
            mHolder.tvpiccount.setText(info.getPhotoNum() + "照片");
        } else {
            mHolder.tvNickname.setText(info.getUid()+"");
            mHolder.tvAge.setText("加载中...");
            mHolder.imgHead.setImageResource(R.drawable.f1_userheadpic_weishangchuan);
        }
        if (info.getType() == 0) {
            mHolder.tvconcern.setText("关注TA");
            mHolder.tvconcern.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    UserDetail userDetail = ModuleMgr.getCenterMgr().getMyInfo();
                    AttentionMeAdapter.this.postion = position;
                    AttentionMeAdapter.this.vh = mHolder;
                    if (userDetail != null) {
                        if (userDetail.getGender() != 2) {
                            if (!userDetail.isVip()) {
                                createOpenVipDialog("开通VIP才可以操作!");
                                return;
                            }
                        }
                    }
                    if (!NetworkUtils.isConnected(mContext)) {
                        PToast.showShort(mContext.getString(R.string.net_error_check_your_net));
                        return;
                    }
                    // 执行关注Class
//                    ((TextView) view).setText("取消关注TA");
                    String otherId = info.getUid()+"";
                    if (otherId == null) {
                        return;
                    }
                    LoadingDialog.show((FragmentActivity)mContext);
                    ModuleMgr.getCommonMgr().follow(info.getUid(), AttentionMeAdapter.this);
//                    MyConcernAct_ItemViewObject obj = new MyConcernAct_ItemViewObject((TextView) view, iPos);
//                    FollowUser followUser = new FollowUser(mContext, otherId, FollowUser.FOLLOWUSER_ADD, obj);
//                    followUser.setCallBack(MyConcern_BeAct_Adapter.this);
//                    followUser.onStart();
                }
            });
        } else {
            mHolder.tvconcern.setText("取消关注");
            mHolder.tvconcern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!NetworkUtils.isConnected(mContext)) {
                        PToast.showShort(mContext.getString(R.string.net_error_check_your_net));
                        return;
                    }
                    // 取消关注
//                    ((TextView) view).setText("关注TA");
                    String otherId = info.getUid()+"";
                    if (otherId == null) {
                        return;
                    }
                    LoadingDialog.show((FragmentActivity)mContext);
                    ModuleMgr.getCommonMgr().unfollow(info.getUid(), AttentionMeAdapter.this);
//                    MyConcernAct_ItemViewObject obj = new MyConcernAct_ItemViewObject((TextView) view, iPos);
//                    FollowUser followUser = new FollowUser(mContext, otherId, FollowUser.FOLLOWUSER_DEL, obj);
//                    followUser.setCallBack(MyConcern_BeAct_Adapter.this);
//                    followUser.onStart();
                }
            });
        }
    }
    private void createOpenVipDialog(String str) {
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
                UIShow.showWebActivity(mContext, WebUtil.jointUrl("http://test.game.xiaoyaoai.cn:30081/static/YfbWebApp/pages/prepaid/prepaid.html", new HashMap<String, Object>() {
                    {
                        put("type", 1);
                    }
                }));
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
        if (response.isOk()){
            if (getList().get(postion).getType() == 0){
                vh.tvconcern.setText("取消关注");
                return;
            }
            vh.tvconcern.setText("关注TA");
        }else {
            PToast.showShort("操作失败，请重试！");
        }
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