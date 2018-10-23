package com.netease.nim.uikit.common;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.List;

public class CommonGroup {

    public static final String GROUP_SANQIN = "三亲";
    public static final String GROUP_JIAZU = "家族";
    public static final String GROUP_ZONGQIN = "宗人堂";


    //创建 群组
    public static Boolean testGroupName(String gname){
        if (gname.equals(GROUP_SANQIN) || gname.equals(GROUP_JIAZU) || gname.equals(GROUP_ZONGQIN)) {
            return true;
        }
        return false;
    }

    public static List<Team> getNormalGroups(){
        List<Team> teams = NIMClient.getService(TeamService.class).queryTeamListByTypeBlock(TeamTypeEnum.Normal);
        return teams;
    }

    //检查名称 不能是公共的
    public static Boolean isCommonGroupName(String gname) {
        if (GROUP_SANQIN.equals(gname) || GROUP_JIAZU.equals(gname) || GROUP_ZONGQIN.equals(gname)) {
            return true;
        }
        return false;
    }

    //是否已经有群组
    public static Boolean hasGroupName(String gname ,List<Team> groups){
        for (Team one : groups) {
            if (one.getName().equals(gname) ) {
                return true;
            }
        }
        return false;
    }
}
