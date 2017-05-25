package com.juxin.predestinate.module.local.msgview.chatview.input;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.juxin.library.log.PToast;
import com.juxin.library.observe.MsgType;
import com.juxin.library.observe.PObserver;
import com.juxin.predestinate.R;
import com.juxin.predestinate.bean.file.UpLoadResult;
import com.juxin.predestinate.module.local.album.ImgSelectUtil;
import com.juxin.predestinate.module.local.msgview.ChatAdapter;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.baseui.custom.ViewPagerAdapter;
import com.juxin.predestinate.module.logic.config.Constant;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.module.util.UIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义
 * Created by Kind on 2017/3/30.
 */
public class ChatCustomSmilePanel extends ChatBaseSmilePanel implements AdapterView.OnItemClickListener, ChatCustomSmileAdapter.DelCEmojiCallBack, PObserver {

    //保存表情资源的列表
    private static int pageResNum = 8;
    private boolean mOutDelClick = false;
    private List<SmileItem> items = null;

    private TextView mOutDelTv;

    public ChatCustomSmilePanel(Context context, List<SmileItem> items, ChatAdapter.ChatInstance chatInstance, TextView outDelTv) {
        super(context, chatInstance);
        if (items == null) {
            items = new ArrayList<>();
        }
        if (items.size() == 0 || (items.size() > 0 && !"custom".equals(items.get(0).getPic()))) {
            items.add(0, new SmileItem("custom"));
        }
        this.items = items;
        this.mOutDelTv = outDelTv;
        setContentView(R.layout.p1_chat_default_smile);
        initView();
    }

    public void setDeleteClick(boolean del) {
        mOutDelClick = del;
        initView();
    }

    public void initView() {
        ViewPager vp = (ViewPager) findViewById(R.id.chat_panel_viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getAllViews());
        vp.setAdapter(viewPagerAdapter);
        initPointsView(vp, viewPagerAdapter.getCount());
    }

    private List<View> getAllViews() {
        List<View> views = new ArrayList<>();
        View view;
        int index = 0;

        while ((view = getChildView(index++)) != null) {
            views.add(view);
        }
        return views;
    }

    private View getChildView(int index) {
        List<SmileItem> listTemp = getPageRes(index);
        if (listTemp == null) {
            return null;
        }

        View view = View.inflate(getContext(), R.layout.p1_chat_smile_grid, null);
        GridView gv = (GridView) view.findViewById(R.id.chat_panel_gridview);
        gv.setNumColumns(pageResNum / 2);
        gv.setGravity(Gravity.CENTER);
        gv.setVerticalSpacing(UIUtil.dp2px(5));

        ChatCustomSmileAdapter customSmileAdapter = new ChatCustomSmileAdapter(getContext(), listTemp, index, mOutDelClick, this);
        gv.setAdapter(customSmileAdapter);
        gv.setOnItemClickListener(this);

        return view;
    }

    /**
     * 根据每页需要显示的表情数，截取对应的文件名List。
     *
     * @param index 对应页。
     * @return 指定页的资源信息。
     */
    public List<SmileItem> getPageRes(int index) {
        if (items == null) {
            return null;
        }

        List<SmileItem> listTemp = items;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (id == -1) return;
        SmileItem item = (SmileItem) parent.getAdapter().getItem(position);
        if ("custom".equals(item.getPic())) {
            if (mOutDelClick) {
                mOutDelTv.setText("删除");
                setDeleteClick(false);
            }
            ImgSelectUtil.getInstance().pickPhotoGallery(context, new ImgSelectUtil.OnChooseCompleteListener() {
                @Override
                public void onComplete(String... path) {
                    if (path == null || path.length == 0 || TextUtils.isEmpty(path[0])) {
                        return;
                    }

                    ModuleMgr.getMediaMgr().sendHttpFile(Constant.UPLOAD_TYPE_FACE, path[0], new RequestComplete() {
                        @Override
                        public void onRequestComplete(HttpResponse response) {
                            if (response.isOk()) {
                                UpLoadResult upLoadResult = (UpLoadResult) response.getBaseData();
                                uploadCFace(upLoadResult.getFile_http_path());
                            } else {
                                PToast.showShort("表情添加失败!");
                            }
                        }
                    });
                }
            });
        } else {
            ModuleMgr.getChatMgr().sendImgMsg(null, getChatInstance().chatAdapter.getWhisperId(), item.getPic());
        }
    }

    private void uploadCFace(final String url) {
        ModuleMgr.getCommonMgr().addCustomFace(url, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (!response.isOk()) {
                    PToast.showShort("表情添加失败");
                    return;
                }
                items.add(new SmileItem(url));
                initView();
            }
        });
    }

    @Override
    public void delCEmoji(final String url, final int curPage, final int positon) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ModuleMgr.getCommonMgr().delCustomFace(url, new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (!response.isOk()) {
                    PToast.showShort("表情删除失败");
                    return;
                }
                if (null != items) {
                    PToast.showShort("表情删除成功");
                    items.remove(curPage * pageResNum + positon);
                }
                initView();
            }
        });
    }

    @Override
    public void onMessage(String key, Object value) {
        if (null == value) {
            return;
        }
        switch (key) {
            case MsgType.MT_ADD_CUSTOM_SMILE:
                uploadCFace((String) value);
                break;

            default:
                break;
        }
    }
}
