package com.juxin.predestinate.ui.user.my.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PToast;
import com.juxin.library.utils.NetworkUtils;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.my.AttentionUserDetail;
import com.juxin.predestinate.module.local.chat.MessageRet;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.ExBaseAdapter;
import com.juxin.predestinate.module.logic.config.AreaConfig;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.UIShow;

import java.util.List;


/**
 * 我的关注适配器
 * Created by zm on 2017/4/13.
 */
public class MyAttentionAdapter extends ExBaseAdapter<AttentionUserDetail> {

    private int followType = 2;   // 关注、取消关注
    private int follow;

    public MyAttentionAdapter(Context context, List<AttentionUserDetail> datas) {
        super(context, datas);
    }

    /**
     * 获取信息的准确位置（加锁）
     *
     * @param info 某一条具体的信息
     * @return 返回 info 在list中的position
     */
    private synchronized int getPosition(AttentionUserDetail info) {
        int size = getList().size();
        for (int i = 0; i < size; i++) {
            if (info.getUid() == getList().get(i).getUid()) {
                return i;
            }
        }
        return -1;
    }

    private String getContent(String nickname) {
        if (!TextUtils.isEmpty(nickname) && !"null".equals(nickname))
            return "[" + nickname + "]" + getContext().getString(R.string.net_error_check_your_net);
        else
            return getContext().getString(R.string.net_error_check_your_net);
    }

    private void checkAndShowAvatarStatus(int status, ImageView img, String avatar) {
        switch (status) {
            case 0:// 正在审核
                img.setImageResource(R.drawable.f1_otheruserheadpic_shenhezhong);
                break;
            case 1:// 审核通过
                //			imageLoader.displayImage(avatar, img, options);
                ImageLoader.loadAvatar(getContext(), avatar, img);
                break;
            case 2:// 未通过
                img.setImageResource(R.drawable.f1_otheruserheadpic_weitongguo);
                break;
            default:
                ImageLoader.loadAvatar(getContext(), avatar, img);
                break;
        }
    }

    private void checkAndShowVipStatus(Boolean isVip, ImageView imgVipStatus, TextView tvNickName) {
        if (isVip) {
            imgVipStatus.setVisibility(View.VISIBLE);
//            tvNickName.setTextColor(getContext().getResources().getColor(R.color.color_F36D8E));
        } else {
            imgVipStatus.setVisibility(View.GONE);
//            tvNickName.setTextColor(getContext().getResources().getColor(R.color.usersnickname));
        }
    }

    public void onItemClick(View convertView, int position) {
        //跳转他人资料页
        UIShow.showCheckOtherInfoAct(getContext(), getItem(position).getUid());
    }

//    private void handleFollowSuccess() {
//        switch (followType) {
//            case 1:
//                follow += 1;
//                PToast.showShort(mContext.getResources().getString(R.string.user_info_follow_suc));
//                iv_follow.setImageResource(R.drawable.f1_followed_star);
//                user_follow.setText(mContext.getString(R.string.user_info_follow_count, follow));
//                if (userProfile != null) {
//                    userProfile.setIsFollowed(1);
//                }
//                break;
//
//            case 2:
//                follow -= 1;
//                PToast.showShort(mContext.getResources().getString(R.string.user_info_unfollow_suc));
//                iv_follow.setImageResource(R.drawable.f1_follow_star);
//                user_follow.setText(mContext.getString(R.string.user_info_follow_count, follow));
//                if (userProfile != null) {
//                    userProfile.setIsFollowed(0);
//                }
//                break;
//        }
//    }

    private void handleFollowFail() {
        String msg = "";
        switch (followType) {
            case 1:
                msg = getContext().getResources().getString(R.string.user_info_follow_fail);
                break;

            case 2:
                msg = getContext().getResources().getString(R.string.user_info_unfollow_fail);
                break;
        }
        PToast.showShort(msg);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        MyViewHolder mHolder;
        if (convertView == null) {
            convertView = inflate(R.layout.f1_attention_panel_list_item);
            mHolder = new MyViewHolder(convertView);
            convertView.setTag(mHolder);
        } else {
            mHolder = (MyViewHolder) convertView.getTag();
        }

        final AttentionUserDetail info = getItem(position);
        mHolder.imgHead.setImageResource(R.drawable.f1_userheadpic_weishangchuan);
        checkAndShowAvatarStatus(info.getAvatar_status(), mHolder.imgHead, info.getAvatar());
        mHolder.tvNickname.setText(info.getShowName());
        checkAndShowVipStatus(info.is_vip(), mHolder.imVipState, mHolder.tvNickname);
        if (info.getAge() <= 0)
            mHolder.tvAge.setText(getContext().getString(R.string.loading));
        else
            mHolder.tvAge.setText(info.getAge() + getContext().getString(R.string.age));
        mHolder.tvDiqu.setText(AreaConfig.getInstance().getCityNameByID(Integer.valueOf(info.getCity())));
        mHolder.tvpiccount.setText(info.getPhotoNum() + getContext().getString(R.string.check_info_album));
        mHolder.tvconcern.setText(getContext().getString(R.string.cancel_the_attention));
        mHolder.tvconcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!NetworkUtils.isConnected(getContext())) {
                    PToast.showShort(getContext().getString(R.string.tip_net_error));
                    return;
                }
                TextView txtAttention = (TextView) view;
                // 取消关注
                txtAttention.setText(R.string.canceling);
                String content;
                if (!TextUtils.isEmpty(info.getNickname()) && !"null".equals(info.getNickname()))
                    content = "[" + info.getNickname() + "]" + getContext().getString(R.string.just_looking_for_you);
                else
                    content = getContext().getString(R.string.just_looking_for_you);

                ModuleMgr.getChatMgr().sendAttentionMsg(info.getUid(), content, info.getKf_id(), 2, new IMProxy.SendCallBack() {
                    @Override
                    public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                        MessageRet messageRet = new MessageRet();
                        messageRet.parseJson(contents);
//                        Log.e("TTTTTTTTTTTTT11111", contents + "|||");

                        if (messageRet.getS() == 0) {
                            int mPosition = getPosition(info);
                            getList().get(mPosition).setType(0);
                            getList().remove(mPosition);
                            notifyDataSetChanged();
                        } else {
                            handleFollowFail();
                        }

                    }

                    @Override
                    public void onSendFailed(NetData data) {
                        handleFollowFail();
                    }
                });

//                ModuleMgr.getCommonMgr().unfollow(info.getUid(), new RequestComplete() {
//                    @Override
//                    public void onRequestComplete(HttpResponse response) {
//                        //        Log.e("TTTTTTTTTMM",response.getResponseString()+"||"+ postion);
//                        if (response.isOk()) {
//                            int mPosition = getPosition(info);
//                            getList().get(mPosition).setType(0);
//                            getList().remove(mPosition);
//                            MyAttentionAdapter.this.notifyItemRemoved(mPosition+1);
//                        }
//                    }
//                });
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(view, position);
            }
        });
        return convertView;
    }

//    @Override
//    public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
//        MessageRet messageRet = new MessageRet();
//        messageRet.parseJson(contents);
//
//        if (messageRet.getS() == 0) {
//            handleFollowSuccess();
//        } else {
//            handleFollowFail();
//        }
//    }
//
//    @Override
//    public void onSendFailed(NetData data) {
//        handleFollowFail();
//    }

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

        public MyViewHolder(View convertView) {
            initView(convertView);
        }

        private void initView(View convertView) {
            imgHead = (ImageView) convertView.findViewById(R.id.concern_list_item_head);
            tvAge = (TextView) convertView.findViewById(R.id.concern_list_item_age);
            tvDiqu = (TextView) convertView.findViewById(R.id.concern_list_item_diqu);
            tvNickname = (TextView) convertView.findViewById(R.id.concern_list_item_nickname);
            tvpiccount = (TextView) convertView.findViewById(R.id.concern_list_item_piccount);
            tvconcern = (TextView) convertView.findViewById(R.id.concern_list_item_btnconcern);
            imVipState = (ImageView) convertView.findViewById(R.id.concern_list_item_img_vipState);
        }
    }
}