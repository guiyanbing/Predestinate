package com.juxin.predestinate.module.util;

import java.io.Serializable;

/**
 * 添加阴影类
 * Created by zm on 2016/8/6.
 */
public class ShadowProperty implements Serializable {
    /**
     * 阴影颜色
     */
    private int shadowColor;
    /**
     * 阴影半径
     */
    private int shadowRadius;
    /**
     * 阴影x偏移
     */
    private int shadowDx;
    /**
     * 阴影y偏移
     */
    private int shadowDy;

    public int getShadowOffset() {
        return getShadowOffsetHalf() * 2;
    }

    public int getShadowOffsetHalf() {
        return 0 >= shadowRadius ? 0 : Math.max(shadowDx, shadowDy) + shadowRadius;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public ShadowProperty setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public int getShadowRadius() {
        return shadowRadius;
    }

    public ShadowProperty setShadowRadius(int shadowRadius) {
        this.shadowRadius = shadowRadius;
        return this;
    }

    public int getShadowDx() {
        return shadowDx;
    }

    public ShadowProperty setShadowDx(int shadowDx) {
        this.shadowDx = shadowDx;
        return this;
    }

    public int getShadowDy() {
        return shadowDy;
    }

    public ShadowProperty setShadowDy(int shadowDy) {
        this.shadowDy = shadowDy;
        return this;
    }
}
