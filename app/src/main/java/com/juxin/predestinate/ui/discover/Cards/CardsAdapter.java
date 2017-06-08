package com.juxin.predestinate.ui.discover.Cards;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.library.image.ImageLoader;
import com.juxin.library.log.PLogger;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.center.user.hot.UserInfoHot;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.SimpleTipDialog;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.socket.IMProxy;
import com.juxin.predestinate.module.logic.socket.NetData;
import com.juxin.predestinate.module.util.PickerDialogUtil;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;

import java.util.List;


/**
 * Created by zm .
 */
public class CardsAdapter extends BaseCardAdapter<UserInfoHot> {
    private List<UserInfoHot> datas;
    private Context context;
    private ViewHoder vh;
    private OnDataNeedReq onDataNeedReq;

    public CardsAdapter(List<UserInfoHot> datas, Context context, OnDataNeedReq onDataNeedReq) {
        this.datas = datas;
        this.context = context;
        this.onDataNeedReq = onDataNeedReq;
    }

    public void setData(List<UserInfoHot> datas) {
        this.datas = datas;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getCardLayoutId() {
        return R.layout.f1_hot_card_panel;
    }

    @Override
    public void onBindData(final int position, View cardview) {
        if (datas == null || datas.size() == 0) {
            return;
        }

        //判断是否需要请求数据
        if (position + 4 >= datas.size()) {
            if (onDataNeedReq != null) {
                onDataNeedReq.onNeedReq();
            }
        }

        final UserInfoHot infoHot = datas.get(position);
        PLogger.d("onBindData=====> infoHot == " + infoHot.toString());
        if (cardview != null) {
            vh = new ViewHoder(cardview);
            cardview.setTag(vh);
        } else {
            vh = (ViewHoder) cardview.getTag();
        }

        vh.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIShow.showCheckOtherInfoAct(context, infoHot.getUid());
            }
        });

        vh.tv_img_num.setText(infoHot.getPhotoNum() + "");
        ImageLoader.loadRoundTop(context, infoHot.getAvatar(), vh.iv_avatar);
        vh.tv_name.setText(infoHot.getNickname());
        vh.tv_age.setText(context.getString(R.string.user_info_age, infoHot.getAge()));
        vh.tv_online.setText(infoHot.getOnlineState() ? context.getString(R.string.user_online) : infoHot.getLastOnLineTime());
        vh.iv_auth_user.setVisibility(infoHot.isIdcardValidation() ? View.VISIBLE : View.GONE);
        vh.iv_auth_phone.setVisibility(infoHot.isMobileValidation() ? View.VISIBLE : View.GONE);
        vh.iv_auth_video.setVisibility(infoHot.isVideoValidation() ? View.VISIBLE : View.GONE);
        //发视频
        vh.lin_to_video.setBackgroundResource(!infoHot.isVideo_available() ?
                R.drawable.f1_card_infor_item_bg : R.drawable.f1_card_infor_item_unbg);
        vh.tv_video_price.setText(infoHot.getVideoPrice() + "钻石/分");
        vh.lin_to_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoHot.isVideo_available()) {
                    VideoAudioChatHelper.getInstance().inviteVAChat((Activity) context, infoHot.getUid(), VideoAudioChatHelper.TYPE_VIDEO_CHAT, true, Constant.APPEAR_TYPE_NO);
                } else {
                    PToast.showShort(context.getString(R.string.hot_card_video_tips));
                }
            }
        });
        //发语音
        vh.lin_to_voice.setBackgroundResource(!infoHot.isAudio_available() ?
                R.drawable.f1_card_infor_item_bg : R.drawable.f1_card_infor_item_unbg);
        vh.tv_voice_price.setText(infoHot.getAudioPrice() + "钻石/分");
        vh.lin_to_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (infoHot.isAudio_available()) {
                    VideoAudioChatHelper.getInstance().inviteVAChat((Activity) context, infoHot.getUid(), VideoAudioChatHelper.TYPE_AUDIO_CHAT);
                } else {
                    PToast.showShort(context.getString(R.string.hot_card_audio_tips));
                }
            }
        });
        //发私聊
        vh.lin_to_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ModuleMgr.getCenterMgr().getMyInfo().isVip()) {
                    UIShow.showPrivateChatAct(context, infoHot.getUid(), null);
                } else {
                    PickerDialogUtil.showSimpleTipDialogExt((FragmentActivity) context, new SimpleTipDialog.ConfirmListener() {
                        @Override
                        public void onCancel() {
                            doSayhi(infoHot, position);
                        }

                        @Override
                        public void onSubmit() {
                            UIShow.showOpenVipActivity(context);
                        }
                    }, context.getString(R.string.hot_card_price_vip), "", context.getString(R.string.hot_card_price_cancle), context.getString(R.string.hot_card_price_sure), true, R.color.text_zhuyao_black);
                }
            }
        });
        //送礼物
        vh.lin_to_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UIShow.showBottomGiftDlg(context, infoHot.getUid());
            }
        });

    }

    /**
     * 如果可见的卡片数是3，则可以不用实现这个方法
     *
     * @return
     */
    @Override
    public int getVisibleCardCount() {
        return 4;
    }


    class ViewHoder {
        private LinearLayout lin_imgs_view;
        private TextView tv_img_num;
        private ImageView iv_avatar;
        private TextView tv_name, tv_age, tv_online, tv_video_price, tv_voice_price;

        private ImageView iv_auth_user, iv_auth_phone, iv_auth_video;

        private LinearLayout lin_to_video, lin_to_voice, lin_to_message, lin_to_gift;

        public ViewHoder(View cardView) {
            initView(cardView);
        }

        private void initView(View view) {
            lin_imgs_view = (LinearLayout) view.findViewById(R.id.hot_card_imgs_view);
            tv_img_num = (TextView) view.findViewById(R.id.hot_card_img_num);
            iv_avatar = (ImageView) view.findViewById(R.id.hot_card_avatar);

            tv_name = (TextView) view.findViewById(R.id.hot_card_name);
            tv_age = (TextView) view.findViewById(R.id.hot_card_age);
            tv_online = (TextView) view.findViewById(R.id.hot_card_online_state);
            tv_video_price = (TextView) view.findViewById(R.id.hot_card_to_video_price);
            tv_voice_price = (TextView) view.findViewById(R.id.hot_card_to_voice_price);
            iv_auth_user = (ImageView) view.findViewById(R.id.hot_card_auth_user);
            iv_auth_phone = (ImageView) view.findViewById(R.id.hot_card_auth_phone);
            iv_auth_video = (ImageView) view.findViewById(R.id.hot_card_auth_video);

            lin_to_video = (LinearLayout) view.findViewById(R.id.hot_card_to_video);

            lin_to_voice = (LinearLayout) view.findViewById(R.id.hot_card_to_voice);
            lin_to_message = (LinearLayout) view.findViewById(R.id.hot_card_to_message);
            lin_to_gift = (LinearLayout) view.findViewById(R.id.hot_card_to_gift);
        }
    }

    private void doSayhi(UserInfoHot infoHot, final int position) {
        if (ModuleMgr.getCenterMgr().isCanSayHi(context) && !infoHot.is_sayHello()) {
            ModuleMgr.getChatMgr().sendSayHelloMsg(String.valueOf(infoHot.getUid()), context.getString(R.string.say_hello_txt),
                    infoHot.getKf_id(),
                    ModuleMgr.getCenterMgr().isRobot(infoHot.getKf_id()) ?
                            Constant.SAY_HELLO_TYPE_ONLY : Constant.SAY_HELLO_TYPE_SIMPLE, new IMProxy.SendCallBack() {
                        @Override
                        public void onResult(long msgId, boolean group, String groupId, long sender, String contents) {
                            PToast.showShort(context.getString(R.string.user_info_hi_suc));
                            datas.get(position).setIs_sayHello(true);
                        }

                        @Override
                        public void onSendFailed(NetData data) {
                            PToast.showShort(context.getString(R.string.user_info_hi_fail));
                        }
                    });
        } else if (ModuleMgr.getCenterMgr().isCanSayHi(context) && infoHot.is_sayHello()) {
            PToast.showShort(context.getString(R.string.user_info_hi_repeat));
        }
    }

}
