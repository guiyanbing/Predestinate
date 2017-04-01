package com.juxin.predestinate.bean.center.update;

import com.juxin.predestinate.bean.net.BaseData;

import org.json.JSONObject;

/**
 * 软件更新接口返回数据结构体
 */
public class AppUpdate extends BaseData {

    private int ver;            //软件版本号
    private String title;       //标题，显示软件的版本
    private int force;          //是否为强制升级
    private String summary;     //升级描述信息
    private String url;         //升级包下载地址

    @Override
    public void parseJson(String s) {
        JSONObject jsonObject = getJsonObject(s);
        this.setVer(jsonObject.optInt("ver"));
        this.setTitle(jsonObject.optString("title"));
        this.setForce(jsonObject.optInt("force"));
        this.setSummary(jsonObject.optString("summary"));
        this.setUrl(jsonObject.optString("url"));
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "AppUpdate{" +
                "ver=" + ver +
                ", title='" + title + '\'' +
                ", force=" + force +
                ", summary='" + summary + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
