package com.pbids.sanqin.ui.activity.me;

import com.pbids.sanqin.common.BaseView;

/**
 * Created by pbids903 on 2017/12/28.
 */

public interface MeFamilyManageView extends BaseView{
    void getFamilyManageInformation(int peopleMonthlyGrow,int peopleNum,int wealth,int wealthMonthlyGrow,String surname);
}
