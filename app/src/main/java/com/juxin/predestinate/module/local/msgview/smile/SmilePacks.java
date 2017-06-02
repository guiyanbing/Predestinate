package com.juxin.predestinate.module.local.msgview.smile;

import com.juxin.library.log.PLogger;
import com.juxin.predestinate.module.logic.application.ModuleMgr;
import com.juxin.predestinate.module.logic.request.HttpResponse;
import com.juxin.predestinate.module.logic.request.RequestComplete;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kind on 2017/4/17.
 */
public class SmilePacks {

    private int ver = 0;
    private String deafultImg = null;
    private Map<String, FastInputItem> fastInput = null;
    private List<SmilePackage> packages = null;
    private Map<Integer, SmileItem> allSmiles = null;

    public SmilePacks() {
        reqCustomFace();
    }

    /**
     *  自定义表情
     */
    private void reqCustomFace(){
        if(packages == null){
            packages = new ArrayList<>();
        }

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
                if(response.isOk()){
                    try {
                        finalSmilePackage.parseJsonSmileItem(response.getResponseString());
                        PLogger.d("xxxxxxxxxx---------");
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                PLogger.printObject(response.getResponseJson());
             //   {"res":{"list":null},"status":"ok","tm":1494927495}
            }
        });
        packages.add(smilePackage);
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public String getDeafultImg() {
        return deafultImg;
    }

    public void setDeafultImg(String deafultImg) {
        this.deafultImg = deafultImg;
    }

    public Map<String, FastInputItem> getFastInput() {
        return fastInput;
    }

    public void setFastInput(Map<String, FastInputItem> fastInput) {
        this.fastInput = fastInput;
    }

    public List<SmilePackage> getPackages() {
        return packages;
    }

    public List<SmilePackage> getPackages(String[] filters) {
        if (filters == null || filters.length == 0) {
            return getPackages();
        }

        if (packages == null) {
            return null;
        }

        List<SmilePackage> temp = new ArrayList<SmilePackage>();
        boolean find = false;

        for (SmilePackage aPackage : packages) {
            find = false;

            for (String filter : filters) {
                if (filter.equals(aPackage.getType())) {
                    find = true;
                    break;
                }
            }

            if (find) {
                continue;
            }

            temp.add(aPackage);
        }

        return temp;
    }

    public void setPackages(List<SmilePackage> packages) {
        this.packages = packages;
    }

    public Map<Integer, SmileItem> getAllSmiles() {
        return allSmiles;
    }

    public void setAllSmiles(Map<Integer, SmileItem> allSmiles) {
        this.allSmiles = allSmiles;
    }
}
