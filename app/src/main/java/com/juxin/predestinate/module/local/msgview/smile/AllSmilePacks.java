package com.juxin.predestinate.module.local.msgview.smile;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Kind on 2017/4/17.
 */

public class AllSmilePacks extends BaseData {

    private int ver = 0;
    private String mendefault = null;//男性默认表情图片 如为空字符串 表示没有默认表情图片
    private Map<String, FastInputItem> meninput = null;
    private String womendefault = null;//女性默认表情图片 如为空字符串 表示没有默认表情图片
    private Map<String, FastInputItem> womeninput = null;
    private List<SmilePackage> packages = null;
    private Map<Integer, SmileItem> allSmiles = null;


    @Override
    public void parseJson(String s) {
        JSONObject json = getJsonObject(s);

//        ver = json.optInt("newver");
//        packages = (List<SmilePackage>) getBaseDataList(json.optJSONArray("facelist"), SmilePackage.class);
//
//        mendefault = json.optString("mendefault");
//        meninput = parseToMap(json.optJSONArray("meninput"));
//        womendefault = json.optString("womendefault");
//        womeninput = parseToMap(json.optJSONArray("womeninput"));

        createAllSmiles();
    }

    private Map<String, FastInputItem> parseToMap(JSONArray jsonArray) {
        Map<String, FastInputItem> map = new HashMap<String, FastInputItem>();

        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); ++i) {
                try {
                    FastInputItem item = (FastInputItem) FastInputItem.class.newInstance();
                    item.parseJson(jsonArray.getString(i));
                    map.put(item.getKey(), item);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return map;
        } else {
            return map;
        }
    }

    private void createAllSmiles() {
        allSmiles = new HashMap<Integer, SmileItem>();

        if (packages == null) {
            return;
        }

        SmileItem temp;
        for (SmilePackage aPackage : packages) {
            Iterator<SmileItem> smileItemIterator = aPackage.getItems().iterator();

            while (smileItemIterator.hasNext()) {
                temp = smileItemIterator.next();
                allSmiles.put(temp.getId(), temp);
            }
        }
    }


    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public String getMendefault() {
        return mendefault;
    }

    public void setMendefault(String mendefault) {
        this.mendefault = mendefault;
    }

    public Map<String, FastInputItem> getMeninput() {
        return meninput;
    }

    public void setMeninput(Map<String, FastInputItem> meninput) {
        this.meninput = meninput;
    }

    public String getWomendefault() {
        return womendefault;
    }

    public void setWomendefault(String womendefault) {
        this.womendefault = womendefault;
    }

    public Map<String, FastInputItem> getWomeninput() {
        return womeninput;
    }

    public void setWomeninput(Map<String, FastInputItem> womeninput) {
        this.womeninput = womeninput;
    }

    public List<SmilePackage> getPackages() {
        return packages;
    }

    public List<SmilePackage> getPackages(boolean sex) {
        List<SmilePackage> temp = new ArrayList<SmilePackage>();
        SmilePackage tempPackage = null;

        for (SmilePackage aPackage : packages) {
            tempPackage = aPackage.clone(sex);

            if (tempPackage == null) {
                continue;
            }

            temp.add(tempPackage);
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

    /**
     * 获取所有icon列表。
     *
     * @return
     */
    public List<String> getAllDownloadImg() {
        List<String> temp = new ArrayList<String>();
        List<String> temps = new ArrayList<String>();

        temp.add(womendefault);
        temp.add(mendefault);

        if (packages != null) {
            for (SmilePackage aPackage : packages) {
                temp.add(aPackage.getIcon());

                temps.addAll(aPackage.getAllDownloadImg());
            }
        }

        temp.addAll(temps);
        return temp;
    }
}
