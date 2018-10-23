package com.pbids.sanqin.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * group
 * 模块： 亲亲池  ->  好友分组
 * Created by caiguoliang 2018-3-2
 */

public class QinQinChiUserGroup {

    private List<QinQinChiUserGroupItem> groups ; //分组列表数据

    public List<QinQinChiUserGroupItem> getGroups() {
        return groups;
    }

    public void setGroups(List<QinQinChiUserGroupItem> groups) {
        this.groups = groups;
    }

    public boolean addGroupItem(QinQinChiUserGroupItem group){
        return groups.add(group);
    }
    public boolean removeGroupItem(QinQinChiUserGroupItem group ){
        return groups.remove(group);
    }
}
