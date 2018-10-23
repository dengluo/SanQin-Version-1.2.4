package com.pbids.sanqin.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.model.entity.GroupList;
import com.pbids.sanqin.model.entity.TeamGroupInfo;
import com.pbids.sanqin.ui.activity.zongquan.AdvancedTeamInfoView;
import com.pbids.sanqin.ui.activity.zongquan.ZhiZongCasesCrowdView;
import com.pbids.sanqin.utils.AddrConst;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;

/**
 *@author : 上官名鹏
 *Description :
 *Date :Create in 2018/7/18 11:24
 *Modified By :
 */
public class ZhiZongCasesCrowdPresenter extends BaasePresenter<ZhiZongCasesCrowdView>{

    private ZhiZongCasesCrowdView casesCrowdView;

    private AdvancedTeamInfoView advancedTeamInfoView;

    private List<RecentContact> recentContacts;

    private List<TeamGroupInfo> teamGroupInfos;

    private List<Team> teams;

    private List<GroupList> groupLists;

    private MsgService msgService = NIMClient.getService(MsgService.class);

    private TeamService teamService = NIMClient.getService(TeamService.class);

    public static final int REQUEST_CODE_TEAM_INFO = 3657;
    public static final int REQUEST_CODE_VAL_FAMILY_MAIN = 3658;
    public static final int REQUEST_CODE_UPDATE_GROUP = 3659;

    private String url;

    public ZhiZongCasesCrowdPresenter(ZhiZongCasesCrowdView casesCrowdView) {
        this.casesCrowdView = casesCrowdView;
        //查询最近会话列表，从会话列表中获取群组未读消息
        recentContacts = msgService.queryRecentContactsBlock();
        //teams为加入的所有群组
        teams = NIMClient.getService(TeamService.class).queryTeamListBlock();
    }

    public ZhiZongCasesCrowdPresenter(AdvancedTeamInfoView advancedTeamInfoView) {
        this.advancedTeamInfoView = advancedTeamInfoView;
    }

    /**
     * 加载群信息
     */
    public DisposableObserver loadTeamInfo(){
        url = AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_LOAD_TEAM_MAIN_INFO;
        DisposableObserver<Response<String>> observer = requestHttpPost(url, null, REQUEST_CODE_TEAM_INFO);
        return observer;
    }

    /**
     * 验证是否有权限
     */
    public DisposableObserver valFamilyMain(){
        url = AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_VAL_FAMILY_MAIN;
        DisposableObserver<Response<String>> observer = requestHttpPost(url, null ,REQUEST_CODE_VAL_FAMILY_MAIN);
        return observer;
    }

    /**
     * 更新群名称
     */
    public DisposableObserver updataUserGroup(String groupId, String groupName, String area){
        HttpParams httpParams = new HttpParams();
        httpParams.put("groupId", groupId);
        httpParams.put("groupName", groupName);
        httpParams.put("area", area);
        url = AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_VAL_UPDATE_GROUP;
        DisposableObserver<Response<String>> observer = requestHttpPost(url, httpParams ,REQUEST_CODE_UPDATE_GROUP);
        return observer;
    }

    /**
     * 查询我加入的群
     */
    public void queryMyTeam(){
        teamService.queryTeamList().setCallback(new RequestCallback<List<Team>>() {
            @Override
            public void onSuccess(List<Team> teams) {
                // 获取成功，teams为加入的所有群组
                teamGroupInfos = new ArrayList<>();
                for (Team team : teams){
                    final TeamGroupInfo teamGroupInfo = new TeamGroupInfo();
                    teamGroupInfo.setGroupId(team.getId());
                    teamGroupInfo.setGroupName(team.getName());
                    teamGroupInfo.setIcon(team.getIcon());
                    teamGroupInfo.setSize(String.valueOf(team.getMemberCount()));
                    teamGroupInfos.add(teamGroupInfo);
                }
                queryMemberHeadNotRead(teamGroupInfos);
                casesCrowdView.successQueryMyTeam(teamGroupInfos);
            }

            @Override
            public void onFailed(int i) {
                // 获取失败，具体错误码见i参数
            }

            @Override
            public void onException(Throwable throwable) {
                // 获取异常
            }
        });
    }

    /**
     * 查询群人数和头像
     * @param teamGroupInfos
     */
    private void queryMemberHeadNotRead(final List<TeamGroupInfo> teamGroupInfos) {
        for (final TeamGroupInfo teamGroupInfo : teamGroupInfos) {
            //查询群人数和头像
            for (Team team : teams) {
                if (team.getId().equals(teamGroupInfo.getGroupId())) {
                    teamGroupInfo.setSize(String.valueOf(team.getMemberCount()));
                    teamGroupInfo.setIcon(team.getIcon());
                }
            }
            //查询消息未读数
            for(RecentContact recentContact :recentContacts){
                if(teamGroupInfo.getGroupId().equals(recentContact.getContactId())){
                    teamGroupInfo.setMessageCount(recentContact.getUnreadCount());
                }
            }
        }
    }

    @Override
    public void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb) {
        if(resultCode == Const.OK){
            switch (requestCode){
                case REQUEST_CODE_TEAM_INFO:
                    Log.i("TeamInfo:", rescb.toString());
                    if(rescb!=null){
                        if(rescb.getData()!=null&&!rescb.getData().equals("")){
                            groupLists = rescb.getDataList(GroupList.class);
                            if(groupLists.isEmpty()&&groupLists.size()<0){
                                if(casesCrowdView!=null) casesCrowdView.failed("没有创建的群");
                            }else{
                                for (GroupList groupList:groupLists) {
                                    queryMemberHeadNotRead(groupList.getGroupList());
                                }
                                if(casesCrowdView!=null) casesCrowdView.successQueryTeam(groupLists);
                            }
                        }else{
                            if(casesCrowdView!=null) casesCrowdView.failed("没有创建的群");
                        }
                    }else{
                        if(casesCrowdView!=null) casesCrowdView.failed("没有创建的群");
                    }
                    break;
                case REQUEST_CODE_VAL_FAMILY_MAIN:
                    JSONObject jsonData = rescb.getJsonData();
                    String familyMain = jsonData.getString("state");
                    if(familyMain.equals("1")){
                        if(casesCrowdView != null) casesCrowdView.valFamilyMainSuccess(true);
                    }else if(familyMain.isEmpty()&&familyMain.equals("")){
                        if(casesCrowdView != null) casesCrowdView.valFamilyMainFailed("查询权限失败");
                    }
                    else{
                        if(casesCrowdView != null) casesCrowdView.valFamilyMainSuccess(false);
                    }
                    break;
                case REQUEST_CODE_UPDATE_GROUP:
                    Log.i(TAG, "onHttpSuccess: "+rescb);
                    if(resultCode==Const.OK){
                        if(casesCrowdView != null) casesCrowdView.updateSuccess();
                        if(advancedTeamInfoView != null) advancedTeamInfoView.updateSuccess();
                    }else {
                        if(casesCrowdView != null) casesCrowdView.updateFail();
                        if(advancedTeamInfoView != null) advancedTeamInfoView.updateFailed("群名上传服务器失败，请重新更新");
                    }
                    break;
            }
        }
    }


//    for (RecentContact recentContact : recentContacts) {
//        if (recentContact.getSessionType() == SessionTypeEnum.ChatRoom.Team) {
//            if (teamGroupInfo.getGroupId().equals(recentContact.getContactId())) {
//                // 锚点
//                List<String> uuid = new ArrayList<>(1);
//                uuid.add(recentContact.getRecentMessageId());
//
//                List<IMMessage> messages = NIMClient.getService(MsgService.class).queryMessageListByUuidBlock(uuid);
//
//                if (messages == null || messages.size() < 1) {
//                    return;
//                }
//                final IMMessage anchor = messages.get(0);
//
//                // 查未读消息
//                NIMClient.getService(MsgService.class).queryMessageListEx(anchor, QueryDirectionEnum.QUERY_OLD, recentContact.getUnreadCount(), false).setCallback(new RequestCallbackWrapper<List<IMMessage>>() {
//
//                    @Override
//                    public void onResult(int code, List<IMMessage> result, Throwable exception) {
//
//                        if (code == ResponseCode.RES_SUCCESS && result != null) {
////                                result.add(0, anchor);
//                            int size = result.size();
//                            //同步获取最近会话列表完成，异步获取未读数成功后和会话列表的群Id比对，然后进行赋值
//                            for (TeamGroupInfo teamGroupInfo : dataList) {
//                                for (IMMessage imMessage : result) {
//                                    if (teamGroupInfo.getGroupId().equals(imMessage.getSessionId())) {
//                                        teamGroupInfo.setMessageCount(size);
//                                    }
//                                }
//                            }
//                        }
//                        // 查询最近联系人列表数据
//                        loadItemList(teamGroupInfos, recyclerView);
//
//                    }
//                });
//            } else {
//                teamGroupInfo.setMessageCount(0);
//            }
//        }
//    }

    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        casesCrowdView.failed(errorMessage);
    }
}
