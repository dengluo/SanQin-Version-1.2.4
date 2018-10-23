package com.pbids.sanqin.model.entity;

import java.util.List;

/**
 * 好友分组
 */

public class QinQinChiFriendGroup {
    
    private String sortLetters;
    private List<QinQinChiFriend> sortUserModels;

    private int userCount = 0 ;


    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public List<QinQinChiFriend> getSortUserModels() {
        return sortUserModels;
    }

    public void setSortUserModels(List<QinQinChiFriend> sortUserModels) {
        this.sortUserModels = sortUserModels;
    }

    @Override
    public String toString() {
        return "SortUserGroupModel{" +
                "sortLetters='" + sortLetters + '\'' +
                ", sortUserModels=" + sortUserModels +
                '}';
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
