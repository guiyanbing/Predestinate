package com.juxin.predestinate.ui.mail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.juxin.mumu.bean.message.Msg;
import com.juxin.mumu.bean.message.MsgMgr;
import com.juxin.mumu.bean.message.MsgType;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.chat.msgtype.BaseMessage;
import com.juxin.predestinate.module.logic.baseui.BaseFragment;
import com.juxin.predestinate.module.logic.swipemenu.SwipeListView;
import com.juxin.predestinate.module.logic.swipemenu.SwipeMenu;
import com.juxin.predestinate.module.logic.swipemenu.SwipeMenuCreator;
import com.juxin.predestinate.module.util.UIShow;
import com.juxin.predestinate.ui.mail.item.MailMsgID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 消息
 * Created by Kind on 2017/3/20.
 */

public class MailFragment extends BaseFragment implements MsgMgr.IObserver,
        AdapterView.OnItemClickListener, SwipeListView.OnSwipeItemClickedListener {

    private MailFragmentAdapter mailFragmentAdapter;
    private SwipeListView listMail;
    //  private CustomFrameLayout viewGroup;
    private View mViewTop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setContentView(R.layout.mail_fragment);
        setTitle(getResources().getString(R.string.main_btn_mail));
        setTitleRight("忽略未读", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //忽略所有未读消息
//                PickerDialogUtil.showSimpleAlertDialog(getActivity(), new SimpleTipDialog.ConfirmListener() {
//                    @Override
//                    public void onCancel() {}
//
//                    @Override
//                    public void onSubmit() {
//                        ModuleMgr.getChatListMgr().markWhisperAsRead();
//                        MMToast.showShort("忽略成功!");
//                    }
//                }, "忽略未读消息,但消息不会删除.", "忽略消息");
            }
        });
        initView();

        // addMessageListener(MsgType.User_List_Msg_Change, this);
        //    addMessageListener(MsgType.MT_APP_Suspension_Notice, this);
        return getContentView();
    }

    private void initView() {
        listMail = (SwipeListView) findViewById(R.id.mail_list);

        //      viewGroup = (CustomFrameLayout) LayoutInflater.from(getContext()).inflate(R.layout.y2_tips_view_group, null);
        mViewTop = LayoutInflater.from(getContext()).inflate(R.layout.layout_margintop, null);
        //      viewGroup.addView(mViewTop);

        // ModuleMgr.getTipsBarMgr().attach(TipsBarMsg.Mail_Page, viewGroup, null);
        //    listMail.addHeaderView(viewGroup);
        View listview_footer = LayoutInflater.from(getActivity()).inflate(R.layout.common_footer_distance, null);
        listMail.addFooterView(listview_footer);
        listview_footer.setOnClickListener(null);
        mailFragmentAdapter = new MailFragmentAdapter(getContext(), null);
        listMail.setAdapter(mailFragmentAdapter);
        mailFragmentAdapter.updateAllData();

        listMail.setPullLoadEnable(false);
        listMail.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                if (menu.getViewType() == 1) {
                    menu.setTitle("忽略");
                } else {
                    menu.setTitle("删除");
                }
                menu.setTitleSize(18);
                menu.setTitleColor(Color.WHITE);
            }
        });

        listMail.setPullLoadEnable(false);
        listMail.setPullRefreshEnable(false);
        listMail.setOnItemClickListener(this);
        listMail.setSwipeItemClickedListener(this);
    }

    @Override
    public void onSwipeChooseOpened() {
    }

    @Override
    public void onSwipeChooseClosed() {
    }

    @Override
    public void onSwipeChooseChecked(int position, boolean isChecked) {
    }

    @Override
    public void onSwipeMenuClick(int position, SwipeMenu swipeMenu, View contentView) {
        List<BaseMessage> baseMessageList = mailFragmentAdapter.getList();
        final BaseMessage item = baseMessageList.get(position);
        if (item != null) {
            MailMsgID mailMsgID = MailMsgID.getMailMsgID(item.getLWhisperID());
            if (mailMsgID != null) {
                switch (mailMsgID) {
                    case WhoAttentionMe_Msg:

                        break;
                    case MyFriend_Msg:

                        break;
                }
            } else {
                ArrayList<String> strings = new ArrayList<String>();
                strings.add(item.getWhisperID());
//                ModuleMgr.getChatListMgr().deleteWhisperChatUser(strings, new ChatMsgInterface.DelChatUserComplete() {
//                    @Override
//                    public void onReqChatUser(boolean ret) {
//                    }
//                });
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BaseMessage message = (BaseMessage) parent.getAdapter().getItem(position);
        if (message != null) {
            MailMsgID mailMsgID = MailMsgID.getMailMsgID(message.getLWhisperID());
            if (mailMsgID != null) {
                switch (mailMsgID) {
                    case WhoAttentionMe_Msg://谁关注我
                        UIShow.showMyAttentionAct(getContext());
                        break;
                    case MyFriend_Msg://我的好友
                        UIShow.showMyFriendsAct(getActivity());
                        break;
                }
            } else {
                UIShow.showPrivateChatAct(getActivity(), message.getLWhisperID(), message.getName(), message.getKfID());
            }
        }
    }

    @Override
    public void onMessage(MsgType msgType, Msg msg) {
        if (mailFragmentAdapter == null) return;
        switch (msgType) {
            case User_List_Msg_Change:
                mailFragmentAdapter.updateAllData();
                break;
            case MT_APP_Suspension_Notice:
                Map<String, Object> parms = (Map<String, Object>) msg.getData();
//                if (parms.containsKey(TipsBarMgr.TipsMgrIsShow)) {
//                    String isShow = (String) parms.get(TipsBarMgr.TipsMgrIsShow);
//                    Log.d("_test", "MailFragment onMessage isshow = " + isShow);
//                    if (isShow.equals(TipsBarMgr.TipsMgrIsShow_none)) {
//                        viewGroup.removeAllViews();
//                        viewGroup.addView(mViewTop);
//                    }
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        resetTipsState(hidden);
    }

    public void resetTipsState(boolean isHidden) {
        //   if (viewGroup != null)
        //  if (isHidden) {
        //        ModuleMgr.getTipsBarMgr().detach();
        //     } else {
        //          ModuleMgr.getTipsBarMgr().attach(TipsBarMsg.Mail_Page, viewGroup, null);
        //      }
    }
}