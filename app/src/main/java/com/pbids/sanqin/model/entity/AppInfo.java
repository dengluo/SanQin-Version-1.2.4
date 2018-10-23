package com.pbids.sanqin.model.entity;

import android.graphics.drawable.Drawable;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:39
 * @desscribe 类描述:app的数据实体类
 * @remark 备注:
 * @see
 */

public class AppInfo {
    String appName;
    Drawable appIcon;
    String packageName;
    int versionCode;
    String versionName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
