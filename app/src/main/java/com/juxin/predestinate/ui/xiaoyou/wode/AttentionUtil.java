package com.juxin.predestinate.ui.xiaoyou.wode;

import com.google.gson.Gson;
import com.juxin.library.log.PSP;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.AttentionList;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.AttentionUserDetail;
import com.juxin.predestinate.ui.xiaoyou.wode.bean.AttentionUserDetailList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zm on 2017/5/4
 */
public class AttentionUtil {

    private static final String USERSKEY = "USERSKEY";
    private static Map<Long,AttentionUserDetail> userInfos = new HashMap<>();

    //初始化
    public static void initUserDetails(){
        if (userInfos.size()<=0){
            String jsonStr = PSP.getInstance().getString(USERSKEY+ModuleMgr.getCenterMgr().getMyInfo().getUid(),"");
//            PSP.getInstance().remove(USERSKEY+ModuleMgr.getCenterMgr().getMyInfo().getUid());
            AttentionUserDetailList list = new AttentionUserDetailList();
            list.parseJson(jsonStr);
            List<AttentionUserDetail> userDetails = list.getAttentionUserDetailList();
            for (AttentionUserDetail detail:userDetails){
                userInfos.put(detail.getUid(),detail);
            }
        }
    }

    public static void addUser(AttentionUserDetail userDetail){
        userInfos.put(userDetail.getUid(),userDetail);
    }

    public static AttentionUserDetail getUserDetail(long uid){
        return userInfos.get(uid);
    }

    public static void saveUserDetails(){
        Gson gson = new Gson();
        List<AttentionUserDetail> userDetails = new ArrayList<>();
        for (Long uid:userInfos.keySet()){
            userDetails.add(userInfos.get(uid));
        }
        String jsonStr = gson.toJson(userDetails);
        PSP.getInstance().put(USERSKEY+ ModuleMgr.getCenterMgr().getMyInfo().getUid(),jsonStr);
    }

    /**
     * 返回内存中存储的用户信息
     * @return
     */
    public static List<AttentionUserDetail> HandleAttentionList(List<AttentionList.AttentionInfo> infos){
        List<AttentionUserDetail> userDetails = new ArrayList<>();
        for (int i = 0 ;i < infos.size();i++){
            AttentionUserDetail detail = getUserDetail(infos.get(i).getUid());
            if ( detail != null){
                userDetails.add(detail);
                infos.remove(i);
                i--;
                continue;
            }
        }
        return userDetails;
    }
}
