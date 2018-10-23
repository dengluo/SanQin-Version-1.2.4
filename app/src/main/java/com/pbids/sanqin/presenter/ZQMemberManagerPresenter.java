package com.pbids.sanqin.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.netease.nim.uikit.business.contact.core.item.ItemTypes;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.db.FriendGroupManager;
import com.pbids.sanqin.model.db.FriendGroupMemberManager;
import com.pbids.sanqin.model.entity.FriendGroupDb;
import com.pbids.sanqin.model.entity.FriendGroupMemberDb;
import com.pbids.sanqin.ui.activity.zongquan.ContactListFragment;
import com.pbids.sanqin.ui.activity.zongquan.ZQMemberManagerView;
import com.pbids.sanqin.utils.RandomUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:12
 * @desscribe 类描述:成员管理界面 -- view
 * @remark 备注:
 * @see
 */

public class ZQMemberManagerPresenter extends BaasePresenter<ZQMemberManagerView> {


    FriendGroupManager groupManager = new FriendGroupManager();
    FriendGroupMemberManager groupMemberManager = new FriendGroupMemberManager();

    //组信息
    public FriendGroupDb friendGroupDb ;
    public List<FriendGroupDb> subGroups ;

    @Override
    public void onCreate(ZQMemberManagerView v, Context context) {
        super.onCreate(v, context);
    }

    //分组时默认的分组，如果没有就创建
    public void checkDefaultBlock(List<String> defaults) {
        List<FriendGroupDb> defgroups = groupManager.queryListByPId(friendGroupDb.getGroupId());
        for (String one : defaults) {
            boolean has = false;
            for (FriendGroupDb fgd : defgroups) {
                if (fgd.getGroupName().equals(one)) {
                    has = true;
                    break;
                }
            }
            if (has == false) {
                //不存在就创建
                FriendGroupDb fd = saveGroupToDb(one, ItemTypes.FRIEND_GROUP_CUSTOM, friendGroupDb.getGroupId());
                subGroups.add(fd);
            }
        }
    }

    //创建组时使用
    public void initData() {
        friendGroupDb = new FriendGroupDb();
        subGroups = new ArrayList<>();
    }

    //加载
    public void loadData(){
        //加载组信息
        friendGroupDb = groupManager.queryByGroupId(mView.getGroupId());
        friendGroupDb.loadMemberDb();
        //二级分组
        subGroups = groupManager.queryListByPId(mView.getGroupId());
        for(FriendGroupDb item:subGroups){
            item.loadMemberDb();
        }
    }

    //在列表里添加
    private boolean delMemberinList(FriendGroupDb fgroup, String account){
        List<FriendGroupMemberDb> members = fgroup.getMembers();
        for (FriendGroupMemberDb m :members){
            if(m.getAccount().equals(account)){
                members.remove(m);
                return true;
            }
        }
        return false;
    }

    //从数据库删除用户
    public void delMemberInDb(String groupId, String account, int groupPosition){
        //数据从列表中删除
        if(subGroups!=null && subGroups.size()>0){
            FriendGroupDb fgroup = subGroups.get(groupPosition);
            delMemberinList(fgroup,account);
        }
        if(friendGroupDb!=null&& friendGroupDb.getMembers().size()>0){
            delMemberinList(friendGroupDb,account);
        }
        //db
        groupMemberManager.delMember(groupId,account);
        friendGroupDb.changed = true ;
    }

    //添加用户到数据库
    public void addMemberToDb(String groupId,String account){
        FriendGroupMemberDb one =groupMemberManager.addMember(groupId,account,"");
        friendGroupDb.changed = true ;
    }
    public void addMemberToDb(String groupId,List<String> accounts){
         for (String item:accounts){
             addMemberToDb(groupId,item);
         }
    }
    //删除组 在数据库中
    public void delGroup(){
        groupManager.del(friendGroupDb);
        groupMemberManager.delGroupMember(friendGroupDb.getGroupId());
    }

    //创建一个分组
    public FriendGroupDb saveGroupToDb(String groupName,int groupType,String parent){
        //如果为空就创建一个
        FriendGroupDb fgd   = new FriendGroupDb();
        fgd.setGroupName(groupName);
        fgd.setType(groupType); //ItemTypes.FRIEND_GROUP_CUSTOM
        fgd.setArea("");
        fgd.setRemarks("");
        fgd.setPid(parent);
        fgd.setGroupId(MyApplication.getUserInfo().getUserId() + "_" + RandomUtil.randomString(8));
        groupManager.insert(fgd);
        return fgd;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
/*
    @Override
    public void onHttpSuccess(int resultCode, int requestCode,  HttpJsonResponse rescb) {
        //super.onHttpSuccess(resultCode, requestCode, body);
    }

    @Override
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage) {
        //super.onHttpError(resultCode, requestCode, errorCode, errorMessage);
    }*/

    //取组信息
/*
    public FriendGroupDb loadGroupData( ) {
        if ( friendGroupDb == null) {
            friendGroupDb = FriendGroupManager.queryByName(mContext, mView.getGroupId() );
            if (friendGroupDb == null) {
                //如果为空就创建一个
                friendGroupDb = new FriendGroupDb();
            }
        }
        return  friendGroupDb;
    }
*/

    /**
     * 添加成员
     */
/*    public void addMembersToGroup(final List<String> selected,int groupPosition ) {
        Log.v("select_friend",selected.toString());
        List<ContactGroupMemberModel> groupMembers =  loadGroupData().getMemberModels();
        ContactGroupMemberModel item = groupMembers.get(groupPosition);
        for(String accid:selected){
            if(!item.getAccids().contains(accid)){
                item.getAccids().add(accid);
            }
        }
        loadGroupData().setGroupAccids(groupMembers);
        FriendGroupManager.update(mContext,friendGroupDb);
        mView.updateAllUser();
    }*/

    //创建组并检查名称是否重复
/*    public boolean createNewGroup(String groupName,int groupType,String accids){
        if(accids==null || accids.isEmpty()){
            mView.showToast("请选择组成员");
            return false;
        }
        if (ContactListFragment.isCommonGroupName(groupName)) {
            mView.showToast("组名已被使用，请更换");
            return false;
        } else if (FriendGroupManager.queryByName(mContext, groupName) != null) {
            mView.showToast("组名已被使用，请更换");
            return false;
        }
        FriendGroupDb friendGroupDb =   loadGroupData( );
        friendGroupDb.setType(groupType);
        friendGroupDb.setAccids(accids);
        friendGroupDb.setName(groupName);
        friendGroupDb.setCeatetime(new Date());
        FriendGroupManager.insert(mContext, friendGroupDb);
        return true;
    }*/

  /*  //创建空成员的组
    public void createNewGroup(String groupName,int groupType ){
        FriendGroupDb friendGroupDb =  loadGroupData( );
        friendGroupDb.setType(groupType);
        friendGroupDb.setAccids("");
        FriendGroupManager.insert(mContext, friendGroupDb);
    }

    public void delGroup(String groupName){
        FriendGroupManager.del(mContext,loadGroupData( ));
    }

    //更新组
    public void updateGroupMember(String groupName){
        FriendGroupDb friendGroupDb = loadGroupData( );//sava db
        friendGroupDb.setAccids(getMembers());
        FriendGroupManager.update( mContext,friendGroupDb);
    }*/



/*    //取成员
    public String getMembers(){
        List<String> accids = new ArrayList<>();
        List<NimUserInfo> allMember = mView.getContactAdapter().getFriendList();
        for(int i=0; i<allMember.size(); i++){
            NimUserInfo item = allMember.get(i);
            accids.add(item.getAccount());
        }
        String res = StringUtil.convertToDatabaseValue(accids,",");
        return res;
    }*/

    //取成员
/*    public String getMembers(List<String> accids){
        List<NimUserInfo> allMember = mView.getContactAdapter().getFriendList();
        for(int i=0; i<allMember.size(); i++){
            NimUserInfo item = allMember.get(i);
            accids.add(item.getAccount());
        }
        String res = StringUtil.convertToDatabaseValue(accids,",");
        return res;
    }*/

}
