package com.yisingle.app.data;

import android.support.annotation.DrawableRes;

/**
 * Created by jikun on 17/8/1.
 */

public class SideData {

    private String text;
    private
    @DrawableRes
    int drawableId;

    public SideData(String text, @DrawableRes int drawableId) {
        this.text = text;
        this.drawableId = drawableId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public
    @DrawableRes
    int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(@DrawableRes int drawableId) {
        this.drawableId = drawableId;
    }
}
