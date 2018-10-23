package com.pbids.sanqin.model.entity;

/**
 * Created by pbids920 on 2018/6/27.
 */

public class BindPhoneBack {

    private String message;

    private int toPage;

    public BindPhoneBack(int toPage) {
        this.toPage = toPage;
    }

    public int getToPage() {
        return toPage;
    }

    public void setToPage(int toPage) {
        this.toPage = toPage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
