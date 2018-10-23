package com.pbids.sanqin.presenter;

import java.io.File;

/**
 *@author : 上官名鹏
 *Description :
 *Date :Create in 2018/6/29 20:58
 *Modified By :
 */

public interface MeInviteView {

    void generatingPoster(File file);

    void getUrlSuccess(String url);

    void error();

    void dismissDown();

    void downSchedule(int schedule);
}
