package com.juxin.predestinate.module.local.msgview;

import com.juxin.library.log.PLogger;
import com.juxin.library.observe.ModuleBase;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.local.msgview.smile.SmilePackage;
import com.juxin.predestinate.module.local.msgview.smile.SmilePacks;
import java.util.List;

/**
 * 表情
 * Created by Kind on 2017/4/17.
 */

public class PhizMgr implements ModuleBase {

    private SmilePacks smilePacks = new SmilePacks();

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
            PLogger.printThrowable(e);
        }

        return null;
    }
}