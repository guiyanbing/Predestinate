package com.juxin.predestinate.module.local.msgview;

import android.text.TextUtils;

import com.juxin.library.observe.ModuleBase;
import com.juxin.mumu.bean.log.MMLog;
import com.juxin.predestinate.module.local.msgview.smile.AllSmilePacks;
import com.juxin.predestinate.module.local.msgview.smile.FastInputItem;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.local.msgview.smile.SmilePackage;
import com.juxin.predestinate.module.local.msgview.smile.SmilePacks;
import com.juxin.predestinate.module.logic.application.ModuleMgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 表情
 * Created by Kind on 2017/4/17.
 */

public class PhizMgr implements ModuleBase {

    private SmilePacks smilePacks = new SmilePacks();

    private Map<Long, Integer> giftLevel = new HashMap<Long, Integer>();

    @Override
    public void init() {

    }

    @Override
    public void release() {

    }

    public SmilePacks getSmilePacks() {
        return smilePacks;
    }

    /**
     * 获取针对用户的礼物等级。
     *
     * @param uid
     * @return
     */
    public int getGiftLevel(Long uid) {
        Integer value = giftLevel.get(uid);

        return value == null ? 0 : value;
    }

    /**
     * 设置针对用户的礼物等级。
     *
     * @param uid
     * @param level
     */
    public void setGiftLevel(Long uid, int level) {
        giftLevel.put(uid, level);
    }


    /**
     * 调用前，key必须用TextUtils.isEmpty(key)进行判断。
     *
     * @param key
     * @return
     */
    public List<SmileItem> matchingSmileItems(CharSequence key) {
        try {
            Map<String, FastInputItem> matchingItems = getSmilePacks().getFastInput();
            FastInputItem fastInputItem = matchingItems.get(key.toString());

            if (fastInputItem == null) {
                return null;
            }

            List<Integer> matchingIds = fastInputItem.getIds();

            SmileItem smileItem = null;
            List<SmileItem> smileItems = new ArrayList<SmileItem>();
            Map<Integer, SmileItem> allSmiles = getSmilePacks().getAllSmiles();

            for (Integer matchingId : matchingIds) {
                smileItem = allSmiles.get(matchingId);

                if (smileItem != null) {
                    smileItems.add(smileItem);
                }

            }

            return smileItems;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取指定名字表情包中的表情信息。
     *
     * @param name
     * @return
     */
    public SmileItem getItem(String name, int index) {
        try {
            SmilePacks smilePacks = getSmilePacks();
            List<SmilePackage> packages = smilePacks.getPackages();

            for (SmilePackage aPackage : packages) {
                if (name.equals(aPackage.getName())) {
                    return aPackage.getItems().get(index);
                }
            }
        } catch (Exception e) {
            MMLog.printThrowable(e);
        }

        return null;
    }
}