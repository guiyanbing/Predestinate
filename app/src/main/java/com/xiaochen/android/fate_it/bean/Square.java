package com.xiaochen.android.fate_it.bean;

import android.support.annotation.IntegerRes;

/**
 * 普通跳转条目
 * Created by ZRP on 2016/12/27.
 */
public class Square {

    @IntegerRes
    private int icon;
    private String name;
    private String des;
    private Class clz;

    public Square(int icon, String name, String des, Class clz) {
        this.icon = icon;
        this.name = name;
        this.des = des;
        this.clz = clz;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public Class getClz() {
        return clz;
    }

    public void setClz(Class clz) {
        this.clz = clz;
    }
}
