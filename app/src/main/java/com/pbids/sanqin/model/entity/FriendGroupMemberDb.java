package com.pbids.sanqin.model.entity;

import org.greenrobot.greendao.annotation.Entity;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:45
 * @desscribe 类描述:分组成员
 * @remark 备注:
 * @see
 */
@Entity
public class FriendGroupMemberDb {

    @Id(autoincrement = true)
    private Long id;
    private String gid;  //用户分组id,必须唯一
    private String account = ""; //组名
    private String alias = ""; //别名

    @Generated(hash = 1599375297)
    public FriendGroupMemberDb(Long id, String gid, String account, String alias) {
        this.id = id;
        this.gid = gid;
        this.account = account;
        this.alias = alias;
    }

    @Generated(hash = 1707133714)
    public FriendGroupMemberDb() {
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
