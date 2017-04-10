package com.juxin.predestinate.bean.recommend;

import com.juxin.mumu.bean.net.BaseData;

/**
 * 标签信息(用于筛选推荐的人)
 * Created YAO on 2017/4/6.
 */

public class TagInfo {

    private String tagName;
    private int tagMark;//标识
    private int position;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagMark() {
        return tagMark;
    }

    public void setTagMark(int tagMark) {
        this.tagMark = tagMark;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
