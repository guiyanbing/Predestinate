package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatViewPanel;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.ViewPagerAdapter;
import com.juxin.predestinate.ui.user.complete.CommonGridBtnPanel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2017/3/31.
 */
public class ChatExtendPanel extends ChatViewPanel {
    private ChatExtend chatExtend = new ChatExtend();
    private ViewPager vp = null;

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
        List<CommonGridBtnPanel.BTN_KEY> listTemp = chatExtend.getPageExtend(index);

        if (listTemp == null || listTemp.isEmpty()) {
            return null;
        }

        View view = View.inflate(getContext(), R.layout.p1_chat_smile_grid, null);
        GridView gv = (GridView) view.findViewById(R.id.chat_panel_gridview);
        gv.setNumColumns(4);

        List<CommonGridBtnPanel.BTN_KEY> list = new ArrayList<CommonGridBtnPanel.BTN_KEY>();
        list.addAll(listTemp);

        CommonGridBtnPanel.BtnAdapter chatExtendAdapter = new CommonGridBtnPanel.BtnAdapter(getContext(), list);
        gv.setAdapter(chatExtendAdapter);

        chatExtendAdapter.setBtnClickListener(new CommonGridBtnPanel.BtnClickListener() {
            @Override
            public void onClick(CommonGridBtnPanel.BTN_KEY key) {
                if (key != null) {
                    switch (key) {
                        case IMG:
                            ImgSelectUtil.getInstance().pickPhotoGallery(getContext(), new ImgSelectUtil.OnChooseCompleteListener() {
                                @Override
                                public void onComplete(String... path) {
                                    if (path.length > 0) {
                                        ChatAdapter chatAdapter = getChatInstance().chatAdapter;
//                                        if (!ModuleMgr.getCommonMgr().headRemindOnChat()) {
//                                            return;
//                                        }
                                        //TODO 发送图片
                                        ModuleMgr.getChatMgr().sendImgMsg(chatAdapter.getChannelId(), chatAdapter.getWhisperId(), path[0]);
                                    }
                                }
                            });
                            break;
                        case VIDEO:
//                            VideoRecordDialog recordDialog = new VideoRecordDialog();
//                            recordDialog.setData(getChatInstance());
//                            recordDialog.showDialog((FragmentActivity) App.activity);
                            break;
                     //   case TREASURE:
//                            List<Long> longList = new ArrayList<Long>();
//                            longList.add(getChatInstance().chatAdapter.getLWhisperId());
//                            ModuleMgr.getMsgCommonMgr().treasureSendInvitation(longList, new HttpMgr.IReqComplete() {
//                                @Override
//                                public void onReqComplete(HttpResult result) {
//                                    if (result.isOk()) {//解析发送邀请信息状态
//                                        PToast.showShort("邀请成功!");
//                                        //打开游戏方法
//                                        UIShow.showMyGame(App.getActivity(), Constant.GAME_COWRY, "");
//                                    } else {
//                                        PToast.showShort("邀请失败，请重试!");
//                                    }
//                                }
//                            });
                       //     break;
                    }
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
}
