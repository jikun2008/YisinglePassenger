package com.yisingle.app.data;

/**
 * Created by jikun on 17/5/14.
 */

@SuppressWarnings("unused")
public class MainTabData {
    private String title;
    private boolean isChoose;

    public MainTabData(String title) {
        this.title = title;
        this.isChoose = false;
    }

    public MainTabData(String title,boolean isChoose) {
        this.title = title;
        this.isChoose = isChoose;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
