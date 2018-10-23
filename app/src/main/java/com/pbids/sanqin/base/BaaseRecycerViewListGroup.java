package com.pbids.sanqin.base;

import java.util.ArrayList;
import java.util.List;

/**
 * group
 *
 * Created by caiguoliang 2018-3-2
 */

public class BaaseRecycerViewListGroup<T> {

    protected String header; //分组头部文字
    protected String Footer ;

    protected List<T> mList = new ArrayList<>(); //分组列表数据
    //是否显示无数据
    private boolean nodata = false;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public List<T> getList() {
        return mList;
    }

    public int getListSize() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public void setLists(List<T> lists) {
        this.mList = lists;
    }

    public void addChild(T one){
        mList.add(one);
    }

    public T getChild(int position){
        return mList.get(position);
    }

    public String getFooter() {
        return Footer;
    }

    public void setFooter(String footer) {
        Footer = footer;
    }
}
