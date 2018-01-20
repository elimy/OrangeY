package com.andy.orange.bean;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MusicMeItem {

    private String title;
    private Drawable imageRes;
    private int count;

    public Drawable getImageRes() {
        return imageRes;
    }

    public void setImageRes(Drawable imageRes) {
        this.imageRes = imageRes;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
