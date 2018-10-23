package com.pbids.sanqin.presenter;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.model.HttpParams;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.BaaseView;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.model.entity.GroupList;
import com.pbids.sanqin.model.entity.TeamGroupInfo;
import com.pbids.sanqin.ui.activity.zongquan.ZhiZongCasesCrowdSearchView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;
/**
 * @author : 上官名鹏
 *         Description :
 *         Date :Create in 2018/7/19 11:10
 *         Modified By :
 */
public class ZhiZongCasesCrowdSearchPresenter extends BaasePresenter<BaaseView> {

    private ZhiZongCasesCrowdSearchView searchView;

    private String url;

    private static final int REQUEST_CODE_SEARCH_TEAM = 3879;

    private static final int REQUEST_VAL_JOIN_TEAM = 3880;

    private boolean isFamilyMain;

    //加入群信息
    private List<Team> teams;

    private TeamService teamService = NIMClient.getService(TeamService.class);

    //从服务器上拿到的群信息，可能服务器上存的群id，在云信查询不到，需要排除
    private List<TeamGroupInfo> teamGroupInfos;

    //要返回的List，根据地区返回
    private List<GroupList> dataGroupList;

    public ZhiZongCasesCrowdSearchPresenter(ZhiZongCasesCrowdSearchView searchView) {
        this.searchView = searchView;
    }

    //异步操作，需要保证此次请求必须完成
    public synchronized void queryTeamById(final List<String> strings) {
        teams = new ArrayList<>();
        for (final String tid : strings){
            teamService.searchTeam(tid).setCallback(new RequestCallback<Team>() {
                @Override
                public void onSuccess(Team team) {
                    teams.add(team);
                    queryFinish(tid,strings);
                }

                @Override
                public void onFailed(int i) {
                    queryFinish(tid,strings);
                }

                @Override
                public void onException(Throwable throwable) {
                    queryFinish(tid,strings);
                }
            });
        }
    }

    private void queryFinish(String tid, List<String> strings){
        //删除一个表示当前群信息加入完成。
        strings.remove(tid);
        //加入群size等于零时，表示加入信息加载完毕
        if(strings.size() == 0){
            requestTeamInfoFinish();
        }
    }

    //此方法表示群信息从云信上查询完成，此方法可以获取群人数和图片信息
    private synchronized void requestTeamInfoFinish() {
        //表示某个地区下的群信息加载完成
        if (isFamilyMain) {
            for (GroupList groupList : dataGroupList) {
                for (TeamGroupInfo teamGroupInfo : groupList.getGroupList()) {
                    for (Team team : teams) {
                        if(teamGroupInfo.getGroupId().equals(team.getId())) {
                            teamGroupInfo.setIcon(team.getIcon());
                            teamGroupInfo.setSize(String.valueOf(team.getMemberCount()));
                            teamGroupInfo.setMyTeam(team.isMyTeam());
                            //找到相应的数据之后，直接跳出本次for循环
                            break;
                        }
                    }
                }
            }
            //群分组信息查询完毕可以，可以显示
            removeList(dataGroupList);
            if (searchView != null) searchView.success(dataGroupList);
        } else {
            for (Team team : teams) {
                for (TeamGroupInfo teamGroupInfo : teamGroupInfos) {
                    if(teamGroupInfo.getGroupId().equals(team.getId())){
                        teamGroupInfo.setIcon(team.getIcon());
                        teamGroupInfo.setSize(String.valueOf(team.getMemberCount()));
                        teamGroupInfo.setMyTeam(team.isMyTeam());
                    }
                }
            }
            //加入群信息查询完毕可以，可以显示
            if (searchView != null) searchView.successTeamInfo(teamGroupInfos);
        }
    }

    /**
     * 搜索群名
     *
     * @param name
     */
    public void searchTeam(String name, boolean isFamilyMain) {
        this.isFamilyMain = isFamilyMain;
        HttpParams httpParams = new HttpParams();
        boolean isTeamId = ValidatorUtil.isInteger(name);
        if (isTeamId) {
            httpParams.put("groupId", name);
        } else {
            httpParams.put("groupName", name);
        }
        url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_LOAD_TEAM_MAIN_INFO_SERRCH;
        requestHttpPost(url, httpParams, REQUEST_CODE_SEARCH_TEAM);
    }

    @Override
    public void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb) {
        switch (requestCode) {
            case REQUEST_CODE_SEARCH_TEAM:
                if(rescb.getData()!=null){
                    if (isFamilyMain) {
                        dataGroupList = rescb.getDataList(GroupList.class);
                        if(dataGroupList.size()>0){
                            queryHeadIconMemeberGroup(dataGroupList);
                        }else{
                            if (searchView != null) searchView.success(new ArrayList<GroupList>());
                        }
                    } else {
//                        HashMap map = (HashMap) rescb.getData();
//                        JSONArray jsonArray = (JSONArray) map.get("groupList");
//                        JSON.parseArray(jsonArray.toJSONString(),TeamGroupInfo.class);
                        if(rescb.getData()!=null){
                            teamGroupInfos = rescb.getDataList(TeamGroupInfo.class);
                            //排除云信里没有的群，并添加到新的集合
                            if (teamGroupInfos.size() != 0) {
                                queryHeadIconMemeber(teamGroupInfos);
                            }else{
                                if (searchView != null) searchView.successTeamInfo(new ArrayList<TeamGroupInfo>());
                            }
                        }
                    }
                }else{
                    if (searchView != null) searchView.success(new ArrayList<GroupList>());
                }
                break;
            case REQUEST_VAL_JOIN_TEAM:
                if(rescb.getStatus()== Const.OK){
                    final JSONObject jsonData = rescb.getJsonData();
                    if(jsonData!=null){
                        int applyGroupNumber = (int) jsonData.get("applyGroupNumber");
                        searchView.joinTeamSuccess(applyGroupNumber);
                    }else {
                        searchView.joinTeamFailed();
                    }
                }else{
                    searchView.joinTeamFailed();
                }
                break;
        }

    }

    /**
     * 删除没有群的地区
     * @param groupList
     */
    private void removeList(List<GroupList> groupList){
        for (GroupList groupList1: groupList) {
            if(groupList1.getGroupList().size()<=0){
                groupList.remove(groupList1);
                removeList(groupList);
                return;
            }
        }
    }

    /**
     * 查询群人数和群头像
     *
     * @param teamGroupInfos
     */
    private void queryHeadIconMemeber(List<TeamGroupInfo> teamGroupInfos) {
        if (teamGroupInfos.size() != 0) {
            List<String> list = new ArrayList<>();
            for (TeamGroupInfo teamGroupInfo : teamGroupInfos) {
                String groupId = teamGroupInfo.getGroupId();
                if(groupId!=null&&!groupId.equals("")){
                    list.add(groupId);
                }
            }
            if(list.size()>0){
                queryTeamById(list);
            }
        }
    }

    /**
     * 查询群人数和群头像
     */
    private void queryHeadIconMemeberGroup(List<GroupList> dataList) {
        List<String> list = new ArrayList<>();
        for (GroupList groupList:dataList) {
            for (TeamGroupInfo teamGroupInfo:groupList.getGroupList()){
                String groupId = teamGroupInfo.getGroupId();
                if(groupId!=null&&!groupId.equals("")){
                    list.add(groupId);
                }
            }
        }
        if(list.size()>0){
            queryTeamById(list);
        }
    }

    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        super.onHttpError(resultCode, requestCode, errorCode, errorMessage);
    }

    /**
     * 查询我加入的群数量
     * @return
     */
    public int queryMyTeamSize(){
        List<Team> teams = NIMClient.getService(TeamService.class).queryTeamListBlock();
        return teams.size();
    }

    /**
     * 验证加入群超过限制
     */
    public void valJoinTeamNumber(){
        HttpParams httpParams = new HttpParams();
        url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_VAL_JOIN_TEAM;
        requestHttpGet(url,httpParams,REQUEST_VAL_JOIN_TEAM);
    }
}
