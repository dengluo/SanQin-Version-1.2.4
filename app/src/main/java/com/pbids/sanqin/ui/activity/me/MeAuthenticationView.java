package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.common.BaseView;

/**
 * Created by pbids903 on 2018/1/29.
 */

public interface MeAuthenticationView extends BaseView {
    void reviceUserNmae(String name, String surname, int surnameId, int isRealName, String idNumber);
}
