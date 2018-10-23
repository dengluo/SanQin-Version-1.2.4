package com.pbids.sanqin.ui.activity.zongquan;

import com.netease.nimlib.sdk.team.model.Team;
import com.pbids.sanqin.base.BaaseView;
import com.pbids.sanqin.model.entity.CasesCrowd;
import com.pbids.sanqin.model.entity.GroupList;
import com.pbids.sanqin.model.entity.TeamGroupInfo;

import java.util.List;

/**
 *@author : 上官名鹏
 *Description :
 *Date :Create in 2018/7/18 11:24
 *Modified By :
 */

public interface ZhiZongCasesCrowdView extends BaaseView{

    void successQueryTeam(List<GroupList> groupLists);

    void failed(String message);

    void successQueryMyTeam(List<TeamGroupInfo> teamGroupInfos);

    void valFamilyMainSuccess(boolean familyMain);

    void valFamilyMainFailed(String message);

    void updateSuccess();

    void updateFail();
}
