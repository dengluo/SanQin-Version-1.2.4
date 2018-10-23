package com.pbids.sanqin.ui.activity;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.VersionInfo;

/**
 * view -- 启动页
 */

public interface BootPageView extends BaseView {
    void versionInfo(VersionInfo versionInfo);
    void checkedUpdate(String type);
    void checkError(String type);

}
