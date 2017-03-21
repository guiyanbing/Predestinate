package com.juxin.library.controls.smarttablayout;

import android.view.View;

/**
 * Created by Kind on 16/10/14.
 */

public class PagerItem {

    private String title;
    private View view;

    public PagerItem() {
        super();
    }

    public PagerItem(String title, View view) {
        this.title = title;
        this.view = view;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
