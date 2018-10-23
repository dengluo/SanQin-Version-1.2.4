package com.pbids.sanqin.model.entity;

/**
 * Created by pbids920 on 2018/7/26.
 */

public class TeamGroupInfo {


    /**
     * area :
     * ceateTime : 1532607701000
     * groupId : 619311359
     * groupName : 111
     * groupType : 0
     * remarks :
     * userId : 566
     */

    private String area;
    private long ceateTime;
    private String groupId;
    private String groupName;
    private int groupType;
    private String remarks;
    private int userId;
    private int messageCount;
    private String icon;
    private String size;
    private boolean isMyTeam;

    public boolean isMyTeam() {
        return isMyTeam;
    }

    public void setMyTeam(boolean myTeam) {
        isMyTeam = myTeam;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public long getCeateTime() {
        return ceateTime;
    }

    public void setCeateTime(long ceateTime) {
        this.ceateTime = ceateTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
