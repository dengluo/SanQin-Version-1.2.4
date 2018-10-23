package com.netease.nim.uikit.custom;

/**
 * Created by pbids920 on 2018/8/1.
 */

public class TeamInfo {

    private String id;

    private String name;

    public TeamInfo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
