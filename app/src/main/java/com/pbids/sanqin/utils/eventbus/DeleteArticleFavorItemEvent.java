package com.pbids.sanqin.utils.eventbus;

import com.pbids.sanqin.model.entity.NewsArticle;

/**
 * Created by caiguoliang .
 * 删除收藏的文章
 */

public class DeleteArticleFavorItemEvent {
    //文章
    private NewsArticle article;

    //收藏状态
    private int status;

    public NewsArticle getArticle() {
        return article;
    }

    public void setArticle(NewsArticle article) {
        this.article = article;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
