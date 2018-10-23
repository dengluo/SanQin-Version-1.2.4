package com.pbids.sanqin.model.entity;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:45
 * @desscribe 类描述:app更新的实体类
 * @remark 备注:
 * @see
 */
public class VersionInfo {
    private String appName;
    private String appType;
    private String downLoadPath;
    private long id;
    private String publishStatus;
    private String uploadBy;
    private long uploadTime;
    private String versionDesc;
    private String versionName;
    private String versionNo;
    private int versionCode;
    private int forceUpdate;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getDownLoadPath() {
        return downLoadPath;
    }

    public void setDownLoadPath(String downLoadPath) {
        this.downLoadPath = downLoadPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPublishStatus() {
        return publishStatus;
    }

    public void setPublishStatus(String publishStatus) {
        this.publishStatus = publishStatus;
    }

    public String getUploadBy() {
        return uploadBy;
    }

    public void setUploadBy(String uploadBy) {
        this.uploadBy = uploadBy;
    }

    public long getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(long uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getForceupdate() {
        return forceUpdate;
    }

    public void setForceupdate(int forceupdate) {
        this.forceUpdate = forceupdate;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "appName='" + appName + '\'' +
                ", appType='" + appType + '\'' +
                ", downLoadPath='" + downLoadPath + '\'' +
                ", id=" + id +
                ", publishStatus='" + publishStatus + '\'' +
                ", uploadBy='" + uploadBy + '\'' +
                ", uploadTime=" + uploadTime +
                ", versionDesc='" + versionDesc + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionNo='" + versionNo + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
