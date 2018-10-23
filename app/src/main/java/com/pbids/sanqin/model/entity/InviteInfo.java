package com.pbids.sanqin.model.entity;

import android.graphics.Bitmap;

/**
 *@author : 上官名鹏
 *Description : 注册分享内容实体
 *Date :Create in 2018/6/16 17:03
 *Modified By :
 */

public class InviteInfo {

    private Bitmap img;

    private String imgUrl;

    private String content;

    private String link;

    private String title;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
