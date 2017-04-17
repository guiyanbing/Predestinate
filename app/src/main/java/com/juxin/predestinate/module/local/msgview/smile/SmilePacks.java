package com.juxin.predestinate.module.local.msgview.smile;

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


    /**
     *
     */
    public SmilePacks() {
    }

    /**
     * @param allSmilePacks
     * @param sex
     */
    public SmilePacks(AllSmilePacks allSmilePacks, boolean sex) {
        if (allSmilePacks == null) {
            return;
        }

        ver = allSmilePacks.getVer();

        if (sex) {
            deafultImg = allSmilePacks.getMendefault();
            fastInput = allSmilePacks.getMeninput();
        } else {
            deafultImg = allSmilePacks.getWomendefault();
            fastInput = allSmilePacks.getWomeninput();
        }

        packages = allSmilePacks.getPackages(sex);
        allSmiles = allSmilePacks.getAllSmiles();
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
