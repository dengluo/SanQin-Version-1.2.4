package com.pbids.sanqin.model.entity;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:40
 * @desscribe 类描述:我的活动list实体类
 * @remark 备注:
 * @see
 */
public class CampaignInfo {
    String activityLink;
    int aid;
    long overTime;
    int reviewed;
    long startTime;
    String title;

    public String getActivityLink() {
        return activityLink;
    }

    public void setActivityLink(String activityLink) {
        this.activityLink = activityLink;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public long getOverTime() {
        return overTime;
    }

    public void setOverTime(long overTime) {
        this.overTime = overTime;
    }

    public int getReviewed() {
        return reviewed;
    }

    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "CampaignInfo{" +
                "activityLink='" + activityLink + '\'' +
                ", aid=" + aid +
                ", overTime=" + overTime +
                ", reviewed=" + reviewed +
                ", startTime=" + startTime +
                ", title='" + title + '\'' +
                '}';
    }
}
