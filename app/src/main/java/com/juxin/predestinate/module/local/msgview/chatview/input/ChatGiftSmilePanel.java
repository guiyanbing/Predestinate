package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.smile.GiftItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kind on 2017/3/31.
 */

public class ChatGiftSmilePanel extends ChatBaseSmilePanel implements AdapterView.OnItemClickListener{
    private ViewPager vp = null;
    private List<GiftItem> items = null;
    private List<BaseAdapter> adapters = new ArrayList<BaseAdapter>();


    public ChatGiftSmilePanel(Context context, List<GiftItem> items, ChatAdapter.ChatInstance chatInstance) {
        super(context, chatInstance);

        this.items = items;

        setContentView(R.layout.p1_chat_default_smile);
        initView();
    }

    public void initView() {
        vp = (ViewPager) findViewById(R.id.chat_panel_viewpager);

//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getAllViews());
//        vp.setAdapter(viewPagerAdapter);
//
//        initPointsView(vp, viewPagerAdapter.getCount());
    }

    private List<View> getAllViews() {
        List<View> views = new ArrayList<View>();
        View view = null;
        int index = 0;

        adapters.clear();

        while ((view = getChildView(index++)) != null) {
            views.add(view);
        }

        return views;
    }

    private View getChildView(int index) {
        List<GiftItem> listTemp = getPageRes(index);

        if (listTemp == null) {
            return null;
        }

        View view = View.inflate(getContext(), R.layout.p1_chat_smile_grid, null);
        GridView gv = (GridView) view.findViewById(R.id.chat_panel_gridview);
        gv.setNumColumns(pageResNum);

        final ChatGiftSmileAdapter chatSmileAdapter = new ChatGiftSmileAdapter(getContext(), listTemp, getChatInstance().chatAdapter);
        gv.setAdapter(chatSmileAdapter);
        gv.setOnItemClickListener(this);

        adapters.add(chatSmileAdapter);
        return view;
    }

    /**
     * 保存表情资源的列表。
     */
    private static int pageResNum = 4;

    /**
     * 根据每页需要显示的表情数，截取对应的文件名List。
     *
     * @param index 对应页。
     * @return 指定页的资源信息。
     */
    public List<GiftItem> getPageRes(int index) {
        if (items == null) {
            return null;
        }

        List<GiftItem> listTemp = items;
        int start = index * pageResNum;
        int offset = listTemp.size() - start;

        if (offset <= 0) {
            return null;
        }

        if (offset > pageResNum) {
            offset = pageResNum;
        }

        return listTemp.subList(start, start + offset);
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
//        if (id == -1) {
//            return;
//        }
//
//        final ChatAdapter chatAdapter = getChatInstance().chatAdapter;
//        String f_name = "";
//        String t_name = "";
//        long f_uid = 0;
//        long t_uid = 0;
//
//        try {
//            f_uid = ModuleMgr.getCenterMgr().getMyInfo().getUser().getUid();
//            f_name = ModuleMgr.getCenterMgr().getMyInfo().getUser().getNickname();
//            t_uid = chatAdapter.getLWhisperId();
//            t_name = chatAdapter.getUserInfo(t_uid).getName();
//        } catch (Exception e) {
//
//        }
//
//        LoadingDialog.show(getContext(), "正在送礼物");
//
//        GiftItem item = (GiftItem) parent.getAdapter().getItem(position);
//
//        final long finalT_uid = t_uid;
//        ModuleMgr.getChatMgr().sendGiftMsg(
//                chatAdapter.getFolder(), chatAdapter.getChannelId(), chatAdapter.getWhisperId(),
//                f_uid, t_uid, f_name, t_name,
//                item.getGid(), item.getName(), item.getInfo(), item.getImg(), item.getRes(),
//                new ChatMgr.GiftMsgComplete() {
//                    @Override
//                    public void onGiftComplete(boolean ret, BaseMessage messages) {
//                        LoadingDialog.closeLoadingDialog();
//
//                        if (ret) {
//                            if (messages instanceof GiftExMessage) {
//                                int level = ((GiftExMessage) messages).getLevel();
//                                int oldLevel = ModuleMgr.getSmileMgr().getGiftLevel(finalT_uid);
//
//                                if (level > oldLevel) {
//                                    ModuleMgr.getSmileMgr().setGiftLevel(finalT_uid, level);
//
//                                    MsgMgr.getInstance().sendMsgToUI(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//                                                BaseAdapter baseAdapter = (BaseAdapter) parent.getAdapter();
//
//                                                if (position == pageResNum - 1) {
//                                                    boolean fresh = false;
//
//                                                    for (BaseAdapter adapter : adapters) {
//                                                        if (adapter == baseAdapter) {
//                                                            fresh = true;
//                                                            continue;
//                                                        }
//
//                                                        if (fresh) {
//                                                            adapter.notifyDataSetChanged();
//                                                            break;
//                                                        }
//                                                    }
//                                                } else {
//                                                    baseAdapter.notifyDataSetChanged();
//                                                }
//                                            } catch (Exception e) {
//                                                MMLog.printThrowable(e);
//                                            }
//                                        }
//                                    });
//
//                                    try {
//                                        List<GiftItem> giftItems = items;
//                                        boolean jumpOut = false;
//
//                                        for (GiftItem giftItem : giftItems) {
//                                            if (oldLevel < giftItem.getLevel() && giftItem.getLevel() <= level) {
//                                                jumpOut = true;
//                                                final String tipContent = "已解锁礼物 [" + giftItem.getName() + "]";
////                                                MMToast.showShort(tipContent);
//
//                                                MsgMgr.getInstance().sendMsg(new Runnable() {
//                                                    @Override
//                                                    public void run() {
//                                                        try {
//                                                            chatAdapter.addClientCustomTip(tipContent);
//                                                        } catch (Exception e) {
//                                                            MMLog.printThrowable(e);
//                                                        }
//                                                    }
//                                                }, 500);
//                                            } else {
//                                                if (jumpOut) {
//                                                    break;
//                                                }
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        MMLog.printThrowable(e);
//                                    }
//                                }
//                            }
//                        } else {
//                            MMToast.showShort("网络连接失败");
//                        }
//                    }
//                }
//        );
    }
}
