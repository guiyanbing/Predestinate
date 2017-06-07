package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

import com.juxin.library.log.PSP;
import com.juxin.library.log.PToast;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatViewPanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.ViewPagerAdapter;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.config.UrlParam;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.TimerUtil;
import com.juxin.predestinate.module.util.VideoAudioChatHelper;
import com.juxin.predestinate.ui.user.check.bean.VideoConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2017/3/31.
 */
public class ChatExtendPanel extends ChatViewPanel implements RequestComplete {
    private ChatExtend chatExtend = new ChatExtend();
    private ViewPager vp = null;
    private CommonGridBtnPanel.BtnAdapter chatExtendAdapter;
    private VideoConfig config;

    public ChatExtendPanel(Context context, ChatAdapter.ChatInstance chatInstance) {
        super(context, chatInstance);

        chatInstance.chatExtendPanel = this;

        setContentView(R.layout.p1_chat_extend);
        initView();
    }

    public void initView() {
        vp = (ViewPager) findViewById(R.id.chat_panel_viewpager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getAllViews());
        vp.setAdapter(viewPagerAdapter);

        show(false);
    }

    // 延迟请求音视频开关配置
    private void reqVideoChatConfig() {
        TimerUtil.beginTime(new TimerUtil.CallBack() {
            @Override
            public void call() {
                ModuleMgr.getCenterMgr().reqVideoChatConfig(getChatInstance().chatAdapter.getLWhisperId(), ChatExtendPanel.this);
            }
        }, 200);
    }

    private List<View> getAllViews() {
        List<View> views = new ArrayList<View>();
        View view = null;
        int index = 0;

        while ((view = getChildView(index++)) != null) {
            views.add(view);
        }

        return views;
    }

    private View getChildView(int index) {
        reqVideoChatConfig();
        List<CommonGridBtnPanel.BTN_KEY> listTemp = chatExtend.getPageExtend(index);

        if (listTemp == null || listTemp.isEmpty()) {
            return null;
        }

        View view = View.inflate(getContext(), R.layout.p1_chat_smile_grid, null);
        GridView gv = (GridView) view.findViewById(R.id.chat_panel_gridview);
        gv.setNumColumns(4);

        List<CommonGridBtnPanel.BTN_KEY> list = new ArrayList<>();
        list.addAll(listTemp);

        chatExtendAdapter = new CommonGridBtnPanel.BtnAdapter(getContext(), list);
        gv.setAdapter(chatExtendAdapter);

        chatExtendAdapter.setBtnClickListener(new CommonGridBtnPanel.BtnClickListener() {
            @Override
            public void onClick(CommonGridBtnPanel.BTN_KEY key) {
                if (key == null) return;
                final ChatAdapter chatAdapter = getChatInstance().chatAdapter;
                switch (key) {
                    case IMG:
                        ImgSelectUtil.getInstance().pickPhotoGallery(getContext(), new ImgSelectUtil.OnChooseCompleteListener() {
                            @Override
                            public void onComplete(String... path) {
                                if (path.length > 0) {
                                    //TODO 发送图片
                                    ModuleMgr.getChatMgr().sendImgMsg(chatAdapter.getChannelId(), chatAdapter.getWhisperId(), path[0]);
                                }
                            }
                        });
                        break;
                    case VIDEO://视频聊天
                        if (config == null || !config.isVideoChat()) {
                            PToast.showShort(getContext().getString(R.string.user_other_not_video_chat));
                            return;
                        }
                        PSP.getInstance().put(ModuleMgr.getCommonMgr().getPrivateKey(Constant.APPEAR_TYPE_SURE), true);
                        VideoAudioChatHelper.getInstance().inviteVAChat((Activity) getContext(), chatAdapter.getLWhisperId(), VideoAudioChatHelper.TYPE_VIDEO_CHAT);
                        break;
                    case VOICE://语音
                        if (config == null || !config.isVoiceChat()) {
                            PToast.showShort(getContext().getString(R.string.user_other_not_voice_chat));
                            return;
                        }
                        VideoAudioChatHelper.getInstance().inviteVAChat((Activity) getContext(), chatAdapter.getLWhisperId(), VideoAudioChatHelper.TYPE_AUDIO_CHAT);
                        break;
                }
            }
        });

        return view;
    }

    /**
     * 设置新的扩展信息。
     *
     * @param chatExtendDatas 扩展功能表。
     */
    public void setChatExtendDatas(List<CommonGridBtnPanel.BTN_KEY> chatExtendDatas) {
        if (chatExtendDatas == null) {
            getChatInstance().chatAdapter.setShowExtend(false);
            chatExtend.setChatExtendDatas(new ArrayList<CommonGridBtnPanel.BTN_KEY>());
        } else {
            getChatInstance().chatAdapter.setShowExtend(true);
            chatExtend.setChatExtendDatas(chatExtendDatas);
        }

        if (vp != null) {
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getAllViews());
            vp.setAdapter(viewPagerAdapter);
        }
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.getUrlParam() == UrlParam.reqVideoChatConfig) {
            if (response.isOk()) {
                config = (VideoConfig) response.getBaseData();
                if (config == null) return;

                CommonGridBtnPanel.BTN_KEY.VIDEO.setPrice(config.getVideoPrice());
                CommonGridBtnPanel.BTN_KEY.VIDEO.setEnable(config.isVideoChat());
                CommonGridBtnPanel.BTN_KEY.VOICE.setPrice(config.getAudioPrice());
                CommonGridBtnPanel.BTN_KEY.VOICE.setEnable(config.isVoiceChat());
                chatExtendAdapter.notifyDataSetChanged();
            }
        }
    }
}
