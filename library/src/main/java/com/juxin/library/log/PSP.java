package com.juxin.library.log;

import android.content.Context;

import net.grandcentrix.tray.AppPreferences;
import net.grandcentrix.tray.core.TrayItem;

import java.util.Collection;

/**
 * sharedPreferences管理类，须在application中进行init操作
 * Created by ZRP on 2016/9/8.
 */
public class PSP {

    private volatile static PSP instance = null;

    private AppPreferences sharedPreferences;

    private PSP() {
    }

    public static PSP getInstance() {
        if (instance == null) {
            synchronized (PSP.class) {
                if (instance == null) {
                    instance = new PSP();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        sharedPreferences = new AppPreferences(context);
    }

    // ===================初始化=====================

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public synchronized void put(String key, Object object) {
        if (sharedPreferences == null) {
            PLogger.d("sharedPreferences is null.");
            return;
        }
        if (object instanceof String) {
            sharedPreferences.put(key, (String) object);
        } else if (object instanceof Integer) {
            sharedPreferences.put(key, (Integer) object);
        } else if (object instanceof Boolean) {
            sharedPreferences.put(key, (Boolean) object);
        } else if (object instanceof Float) {
            sharedPreferences.put(key, (Float) object);
        } else if (object instanceof Long) {
            sharedPreferences.put(key, (Long) object);
        } else {
            sharedPreferences.put(key, String.valueOf(object));
        }
    }

    public synchronized String getString(String key, String defaultValue) {
        return sharedPreferences == null ? defaultValue : sharedPreferences.getString(key, defaultValue);
    }

    public synchronized int getInt(String key, int defaultValue) {
        return sharedPreferences == null ? defaultValue : sharedPreferences.getInt(key, defaultValue);
    }

    public synchronized boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences == null ? defaultValue : sharedPreferences.getBoolean(key, defaultValue);
    }

    public synchronized float getFloat(String key, float defaultValue) {
        return sharedPreferences == null ? defaultValue : sharedPreferences.getFloat(key, defaultValue);
    }

    public synchronized long getLong(String key, long defaultValue) {
        return sharedPreferences == null ? defaultValue : sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * 移除某个key值已经对应的值
     */
    public synchronized void remove(String key) {
        if (sharedPreferences != null && sharedPreferences.contains(key))
            sharedPreferences.remove(key);
    }

    /**
     * 清除所有数据
     */
    public synchronized void clear() {
        if (sharedPreferences != null) sharedPreferences.clear();
    }

    /**
     * 查询某个key是否已经存在
     */
    public synchronized boolean contains(String key) {
        return sharedPreferences != null && sharedPreferences.contains(key);
    }

    /**
     * 返回所有的键值对
     */
    public synchronized Collection<TrayItem> getAll() {
        return sharedPreferences == null ? null : sharedPreferences.getAll();
    }
}
