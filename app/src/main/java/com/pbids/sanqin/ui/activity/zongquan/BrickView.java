package com.pbids.sanqin.ui.activity.zongquan;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.UserInfo;

/**
 * Created by pbids903 on 2018/2/7.
 */

public interface BrickView extends BaseView {

    void getUserInfo(UserInfo userInfo);

    void getUserInfoForBrick(UserInfo userInfo, int laveBurningBrickCount);

}
