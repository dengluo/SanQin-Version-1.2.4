package com.pbids.sanqin.ui.activity;

import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.model.entity.FamilyRank;

import java.util.ArrayList;

/**
 * 姓氏认证界面
 */

public interface AuthenticationView extends BaseView {
    public void getFamilyRanks(ArrayList<FamilyRank> familyRanks);
}
