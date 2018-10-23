package com.pbids.sanqin.ui.activity.zongquan;

import com.pbids.sanqin.base.BaaseView;
import com.pbids.sanqin.ui.adapter.SanQinContactDataAdapter;

import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/5 17:05
 * @desscribe 类描述:
 * @remark 备注:
 * @see
 */

public interface ZQMemberManagerView extends BaaseView{

    SanQinContactDataAdapter getContactAdapter();

    String getGroupId();

    int getCtrl();

    //void addAllUser(List<String> addids);
    void updateAllUser();
}
