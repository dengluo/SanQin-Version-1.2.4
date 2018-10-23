package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.UserInfo;

/**
 * Created by pbids903 on 2018/3/21.
 */

public interface MeInformationUpdateView extends BaseView{
    void getUserInfo(UserInfo userInfo);
}
