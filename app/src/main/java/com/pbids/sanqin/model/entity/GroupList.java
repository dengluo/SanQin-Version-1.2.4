package com.pbids.sanqin.model.entity;

import java.util.List;

/**
 * Created by pbids920 on 2018/7/27.
 */

public class GroupList {

    private String arid;

    private String area;

    private List<TeamGroupInfo> groupList;

    public String getArid() {
        return arid;
    }

    public void setArid(String arid) {
        this.arid = arid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<TeamGroupInfo> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<TeamGroupInfo> groupList) {
        this.groupList = groupList;
    }
}
