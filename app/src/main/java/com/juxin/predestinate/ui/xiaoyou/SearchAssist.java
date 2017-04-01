package com.juxin.predestinate.ui.xiaoyou;

import com.juxin.predestinate.module.logic.request.HTCallBack;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import com.juxin.predestinate.ui.xiaoyou.adapter.FriendsAdapter;
import com.juxin.predestinate.ui.xiaoyou.bean.FriendsList;

import java.util.List;

/**
 * Created by zm on 2017/3/29
 */
public class SearchAssist implements RequestComplete{
    private FriendsAdapter mFriendsAdapter;
    private List<FriendsList.FriendInfo> arrSearchList;
    private HTCallBack mHTCallBack;
    public SearchAssist(){
        super();
        mFriendsAdapter = new FriendsAdapter();
    }

    /**
     * 发送查询
     * @param str
     */
    public void reqSearch(String str){
        //网络请求
        if (mHTCallBack != null){
            mHTCallBack.cancel();//取消之前的网络请求
        }
        //建立新的请求
    }

    @Override
    public void onRequestComplete(HttpResponse response) {
        if (response.isOk()){

        }else {

        }
    }
    //页面退出时调用
    public void onDestroy() {
        if (mHTCallBack != null){
            mHTCallBack.cancel();
        }
    }
}
