package com.juxin.predestinate.ui.discover.cardimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.util.UIShow;

/**
 * Created by zm on 2017/6/2
 */
public class CardViewInfo extends CardAssist implements View.OnClickListener{

    private View cardImageView;
    private TextView phoneNum;
    private ImageView imgUserOne;
    private ImageView imgUserTwo;
    private ImageView imgUserThree;
    private ImageView imgUserFour;

    private View cardInforView;
    private TextView txvName,txvAge,txvOnline;
    private ImageView imgPhoto,imgPhone,imgVideo;
    private LinearLayout llVideo,llVoice,llSend,llGift;

    private Object userInfo;
    private Context mContext;

    @Override
    View cardImageView(Context context, int width) {
        cardImageView = LayoutInflater.from(context).inflate(R.layout.f1_userimage_item,null);
        phoneNum = (TextView) cardImageView.findViewById(R.id.userimage_item_txt_phonenum);
        imgUserOne = (ImageView) cardImageView.findViewById(R.id.userimage_item_img_one);
        imgUserTwo = (ImageView) cardImageView.findViewById(R.id.userimage_item_img_two);
        imgUserThree = (ImageView) cardImageView.findViewById(R.id.userimage_item_img_three);
        imgUserFour = (ImageView) cardImageView.findViewById(R.id.userimage_item_img_four);
        this.mContext = context;
        return cardImageView;
    }

    @Override
    View cardInforView(Context context, int width) {
        cardInforView = LayoutInflater.from(context).inflate(R.layout.f1_userinfor_item,null);
        txvName = (TextView) cardInforView.findViewById(R.id.userinfo_item_txt_name);
        txvAge = (TextView) cardInforView.findViewById(R.id.userinfo_item_txt_age);
        txvOnline = (TextView) cardInforView.findViewById(R.id.userinfo_item_txt_online);
        imgPhoto = (ImageView) cardInforView.findViewById(R.id.userinfo_item_img_auth_phone);
        imgPhone = (ImageView) cardInforView.findViewById(R.id.userinfo_item_img_auth_phone);
        imgVideo = (ImageView) cardInforView.findViewById(R.id.userinfo_item_img_auth_video);
        llVideo = (LinearLayout) cardInforView.findViewById(R.id.userinfo_item_ll_video);
        llVoice = (LinearLayout) cardInforView.findViewById(R.id.userinfo_item_ll_voice);
        llSend = (LinearLayout) cardInforView.findViewById(R.id.userinfo_item_ll_send);
        llGift = (LinearLayout) cardInforView.findViewById(R.id.userinfo_item_ll_gift);
        llVideo.setOnClickListener(this);
        llVoice.setOnClickListener(this);
        llSend.setOnClickListener(this);
        llGift.setOnClickListener(this);
        return cardInforView;
    }

    @Override
    public void setData(Object data){
        this.userInfo = data;
        if (cardImageView != null){

        }
        if (cardInforView != null){

        }
    }

    @Override
    public void onImgClick(Object data) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.userinfo_item_ll_video://视频
//                if (config == null || !config.isVideoChat()) {
//                    PToast.showShort(getContext().getString(R.string.user_other_not_video_chat));
//                    return;
//                }
//                VideoAudioChatHelper.getInstance().inviteVAChat((Activity) getContext(), chatAdapter.getLWhisperId(), VideoAudioChatHelper.TYPE_VIDEO_CHAT);
                break;
            case R.id.userinfo_item_ll_voice://语音
//                if (config == null || !config.isVoiceChat()) {
//                    PToast.showShort(getContext().getString(R.string.user_other_not_voice_chat));
//                    return;
//                }
//                VideoAudioChatHelper.getInstance().inviteVAChat((Activity) getContext(), chatAdapter.getLWhisperId(), VideoAudioChatHelper.TYPE_AUDIO_CHAT);
                break;
            case R.id.userinfo_item_ll_send://发私信
//                UIShow.showPrivateChatAct();
                break;
            case R.id.userinfo_item_ll_gift://送礼物
                UIShow.showBottomGiftDlg(mContext,123);
                break;
        }
    }
}
