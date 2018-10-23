package com.pbids.sanqin.model.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:42
 * @desscribe 类描述:首页list包含所有list的对象
 * @remark 备注:其页面为list的group
 * @see com.pbids.sanqin.ui.activity.zhizong.ZhiZongFragment
 */
public class HomeNewsInformation {
    private List<NewsArticle> banner;
    private NewsInformation list;//百家姓文章
    private NewsInformation zhichong_list;//知宗文章
    private NewsInformation surname_list;//我的相关文章

    public List<NewsArticle> getBanner() {
        return banner;
    }

    public void setBanner(List<NewsArticle> banner) {
        this.banner = banner;
    }

    public NewsInformation getList() {
        return list;
    }

    public void setList(NewsInformation list) {
        this.list = list;
    }

    public NewsInformation getZhichong_list() {
        return zhichong_list;
    }

    public void setZhichong_list(NewsInformation zhichong_list) {
        this.zhichong_list = zhichong_list;
    }

    public NewsInformation getSurname_list() {
        return surname_list;
    }

    public void setSurname_list(NewsInformation surname_list) {
        this.surname_list = surname_list;
    }
}
