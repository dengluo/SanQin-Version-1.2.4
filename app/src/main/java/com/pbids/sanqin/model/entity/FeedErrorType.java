package com.pbids.sanqin.model.entity;

/**
 * Created by pbids903 on 2018/3/15.
 */

public class FeedErrorType {
    private long id;
    private String typeName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "FeedErrorType{" +
                "id=" + id +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
