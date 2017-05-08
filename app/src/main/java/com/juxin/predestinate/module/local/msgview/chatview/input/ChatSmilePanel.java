package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.R;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.chatview.base.ChatViewPanel;
import com.juxin.predestinate.module.local.msgview.smile.GiftItem;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.local.msgview.smile.SmilePackage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.HorizontalListView;

import java.util.List;

/**
 * Created by Kind on 2017/3/31.
 */

public class ChatSmilePanel extends ChatViewPanel implements AdapterView.OnItemClickListener{

  //  private HorizontalListView smilepackagesView = null;
    private FrameLayout smilePackageLayouts = null;
    private ChatSmileAdapter chatSmileAdapter;

    public ChatSmilePanel(Context context, ChatAdapter.ChatInstance chatInstance) {
        super(context, chatInstance);

        chatInstance.chatSmilePanel = this;

        setContentView(R.layout.p1_chat_smile);
        initView();
    }

    public void initView() {
 //       smilepackagesView = (HorizontalListView) findViewById(R.id.chat_smile_packages);
        smilePackageLayouts = (FrameLayout) findViewById(R.id.chat_smile_package_layouts);

        chatSmileAdapter = new ChatSmileAdapter(getContext(), ModuleMgr.getPhizMgr().getSmilePacks().getPackages());
//        smilepackagesView.setAdapter(chatSmileAdapter);
//        smilepackagesView.setOnItemClickListener(this);
        chatSmileAdapter.setCheckPosition(0);

        addView_Package_Default();

        show(false);
    }

    public void resetSmilePackages() {
        if (chatSmileAdapter == null) {
            return;
        }

       // chatSmileAdapter.setList(ModuleMgr.getPhizMgr().getSmilePacks().getPackages());

//        if (ChatListMgr.Folder.sys_notice == getChatInstance().chatAdapter.getFolder()) {
//             chatSmileAdapter.setList(ModuleMgr.getSmileMgr().getSmilePacks().getPackages(new String[]{"gift"}));
//        } else {
//              chatSmileAdapter.setList(ModuleMgr.getSmileMgr().getSmilePacks().getPackages());
//        }
    }

    private void addView_Package_Default() {
        smilePackageLayouts.removeAllViews();

        ChatDefaultSmilePanel chatSmilePanel = new ChatDefaultSmilePanel(getContext(), getChatInstance());

        smilePackageLayouts.addView(chatSmilePanel.getContentView());
    }

    private void addView_Package_Big(List<SmileItem> items, int type) {
        smilePackageLayouts.removeAllViews();

        ChatBigSmilePanel chatSmilePanel = new ChatBigSmilePanel(getContext(), items, getChatInstance(), type);

        smilePackageLayouts.addView(chatSmilePanel.getContentView());
    }

    private void addView_Package_Gift(List<GiftItem> items) {
        smilePackageLayouts.removeAllViews();

        ChatGiftSmilePanel chatSmilePanel = new ChatGiftSmilePanel(getContext(), items, getChatInstance());

        smilePackageLayouts.addView(chatSmilePanel.getContentView());
    }

    private void addView(SmilePackage smilePackage) {
        if (smilePackage == null) {
            return;
        }

        addView_Package_Default();
//
//        if ("smallface".equals(smilePackage.getType())) {
//            addView_Package_Default();
//        } else if ("bigface".equals(smilePackage.getType())) {
//            addView_Package_Big(smilePackage.getItems(), 0);
//        } else if ("minigame".equals(smilePackage.getType())) {
//            addView_Package_Big(smilePackage.getItems(), 1);
//        } else if ("gift".equals(smilePackage.getType())) {
      //      ModuleMgr.getSmileMgr().reqGiftLevel(getChatInstance().chatAdapter.getLWhisperId(), null);
    //        addView_Package_Gift(ModuleMgr.getSmileMgr().getGiftShop().getItems());
      //  }
    }

    /**
     * 选中指定表情显示。
     *
     * @param name
     */
    public void selectSmile(String name) {
        try {
            int position = 0;
            List<SmilePackage> smilePackages = chatSmileAdapter.getList();

            for (SmilePackage smilePackage : smilePackages) {
                if (name.equals(smilePackage.getName())) {
                    chatSmileAdapter.setCheckPosition(position);
                    addView(smilePackage);
                }

                ++position;
            }
        } catch (Exception e) {
            MMLog.printThrowable(e);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SmilePackage smilePackage = null;

        try {
            smilePackage = (SmilePackage) parent.getAdapter().getItem(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (smilePackage == null) {
            return;
        }

        chatSmileAdapter.setCheckPosition(position);//设置选中效果
        addView(smilePackage);
    }
}
