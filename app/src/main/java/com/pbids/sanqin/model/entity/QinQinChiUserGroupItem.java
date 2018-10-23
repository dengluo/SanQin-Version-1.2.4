package com.pbids.sanqin.model.entity;

/**
 * 模块 亲亲池 -> 用户模型
 * Created by liang on 2018/3/6.
 */

public class QinQinChiUserGroupItem {

    private long id = -1;                    //group id
    private String name = "";                //姓名

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
