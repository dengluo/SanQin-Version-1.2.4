package com.pbids.sanqin.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:44
 * @desscribe 类描述:首页list的HomeNewsInformation的具体childer对象
 * @remark 备注:
 * @see
 */
public class NewsInformation {
    private String morelink;
    private int ismore;
    private List<NewsArticle> list;

    public String getMorelink() {
        return morelink;
    }

    public void setMorelink(String morelink) {
        this.morelink = morelink;
    }

    public int getIsmore() {
        return ismore;
    }

    public void setIsmore(int ismore) {
        this.ismore = ismore;
    }

    public List<NewsArticle> getList() {
        return list;
    }

    public void setList(List<NewsArticle> list) {
        this.list = list;
    }
}
