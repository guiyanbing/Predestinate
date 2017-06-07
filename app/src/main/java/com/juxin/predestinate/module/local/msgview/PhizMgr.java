package com.juxin.predestinate.module.local.msgview;

import com.juxin.library.log.PLogger;
import com.juxin.library.observe.ModuleBase;
import com.juxin.predestinate.module.local.msgview.smile.SmileItem;
import com.juxin.predestinate.module.local.msgview.smile.SmilePackage;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

/**
 * 表情
 * Created by Kind on 2017/4/17.
 */

public class PhizMgr implements ModuleBase {

    private List<SmilePackage> packages = new ArrayList<>();

    @Override
    public void init() {
        reqCustomFace();
    }

    @Override
    public void release() {

    }

    public List<SmilePackage> getPackages() {
        return packages;
    }

    /**
     * 自定义表情
     */
    private void reqCustomFace() {
        SmilePackage smilePackage = new SmilePackage();
        smilePackage.setName("默认");
        smilePackage.setType("smallface");
        packages.add(smilePackage);

        smilePackage = new SmilePackage();
        smilePackage.setName("自定义");
        smilePackage.setType("customface");

        final SmilePackage finalSmilePackage = smilePackage;
        ModuleMgr.getCommonMgr().reqCustomFace(new RequestComplete() {
            @Override
            public void onRequestComplete(HttpResponse response) {
                if (response.isOk()) {
                    try {
                        finalSmilePackage.parseJsonSmileItem(response.getResponseString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                PLogger.printObject(response.getResponseJson());
            }
        });
        packages.add(smilePackage);
    }

    /**
     * 获取指定名字表情包中的表情信息。
     *
     * @param name
     * @return
     */
    public SmileItem getItem(String name, int index) {
        try {
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