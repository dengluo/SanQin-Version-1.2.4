package com.pbids.sanqin.ui.activity.zongquan;

import com.pbids.sanqin.model.entity.CasesCrowd;
import com.pbids.sanqin.model.entity.GroupList;
import com.pbids.sanqin.model.entity.TeamGroupInfo;

import java.util.List;

/**
 *@author : 上官名鹏
 *Description :
 *Date :Create in 2018/7/19 11:48
 *Modified By :
 */

public interface ZhiZongCasesCrowdSearchView {

    void success(List<GroupList> groupLists);

    void failed(String message);

    void successTeamInfo(List<TeamGroupInfo> teamGroupInfos);

    void joinTeamSuccess(int number);

    void joinTeamFailed();
}
