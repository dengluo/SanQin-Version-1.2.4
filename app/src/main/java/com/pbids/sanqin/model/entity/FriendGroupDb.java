package com.pbids.sanqin.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.pbids.sanqin.model.db.FriendGroupMemberManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:45
 * @desscribe 类描述:分组
 * @remark 备注:
 * @see
 */
@Entity
public class FriendGroupDb {

    @Id(autoincrement = true)
    private Long id;
    private String groupId;  // 用户分组id,必须唯一
    private String groupName = ""; // 组名
    private String remarks = ""; // 备注
    private String area = "" ;// 地区
    private String pid = "" ; // 父节点
    private int type =0 ; //类型


    @Generated(hash = 1788279165)
    public FriendGroupDb(Long id, String groupId, String groupName, String remarks,
            String area, String pid, int type) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.remarks = remarks;
        this.area = area;
        this.pid = pid;
        this.type = type;
    }
    @Generated(hash = 793423814)
    public FriendGroupDb() {
    }



    // ------------------ 成员管理 ---------------
    @Transient
    private List<FriendGroupMemberDb> members = new ArrayList<>() ;

    @Transient
    public boolean changed = false;

    public void loadMemberDb() {
        if (groupId == null && groupId.isEmpty()) {
            return;
        }
        FriendGroupMemberManager memberManager = new FriendGroupMemberManager();
        members = memberManager.queryByGroupId(groupId);
    }

    //
    public void addMember(FriendGroupMemberDb m){
        members .add(m);
    }
    public List<FriendGroupMemberDb> getMembers(){
        return  members;
    }
    //添加新成员
    public void addAccount(String account,String groupId){
        FriendGroupMemberDb memberDb = new FriendGroupMemberDb();
        memberDb.setAccount(account);
        memberDb.setAlias("");
        memberDb.setGid(groupId);
        members.add(memberDb);
    }
    public List<String> getAccounts(){
        List<String> accounts = new ArrayList<>();
        //用这个方式 顺序不会错乱
        for(int i=0;i<members.size();i++){
            FriendGroupMemberDb item = members.get(i);
            accounts.add(item.getAccount());
        }
        /*for(FriendGroupMemberDb item:members){
            accounts.add(item.getAccount());
        }*/
        return accounts;
    }
    // ------------------ 成员管理 --- end ---------------



    /*
    public List<String> getAccidList() {
        if (accids == null || accids.isEmpty()) {
            return new ArrayList<>();
        }
        return  java.util.Arrays.asList(accids.split(",")) ;
    }*/


   /* //成员组 最少为一个
    public List<ContactGroupMemberModel> getMemberModels(){
        List<ContactGroupMemberModel> groupMembers;
        if(accids.isEmpty()){
            return new ArrayList<>();
        }
        try {
            groupMembers = JSONArray.parseArray (accids,ContactGroupMemberModel.class);
        } catch (Exception e){
            //如何有错误，就清空，解决上个版本保存格式不同
            accids = "";
            return new ArrayList<>();
        }
        return groupMembers;
    }

    //保存 json 数据
    public void setGroupAccids(List<ContactGroupMemberModel> groupMembers ){
        accids = JSONArray.toJSONString ( groupMembers );
    }*/

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
