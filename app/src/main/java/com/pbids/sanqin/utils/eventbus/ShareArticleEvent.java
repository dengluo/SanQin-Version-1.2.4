package com.pbids.sanqin.utils.eventbus;

import com.pbids.sanqin.model.entity.NewsArticle;

//分享事件处理
public class ShareArticleEvent {

    private NewsArticle article;

    private long aid;

    public NewsArticle getArticle() {
        return article;
    }

    public void setArticle(NewsArticle article) {
        this.article = article;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }
}
