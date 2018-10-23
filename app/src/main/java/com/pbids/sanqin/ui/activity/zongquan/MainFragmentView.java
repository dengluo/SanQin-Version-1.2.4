package com.pbids.sanqin.ui.activity.zongquan;

import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.model.entity.VersionInfo;

/**
 *@author : 上官名鹏
 *Description :
 *Date :Create in 2018/6/13 12:04
 *Modified By :
 */

public interface MainFragmentView {

    void showDialog(int number);

    void updateSuccess(String message,int type);

    void updateFailed(String message,int type);

    void joinTeamOk(int number);

    void joinTeamNo(String message);

    void versionInfo(VersionInfo versionInfo);

    void checkError(String message);

    void updateUserInfo(UserInfo userInfo);
}
