package com.netease.nim.uikit.custom;

/**
 * Created by pbids920 on 2018/8/2.
 */

public class TransFerTeam {

    private String account;

    private String teamId;

    public TransFerTeam(String account, String teamId) {
        this.account = account;
        this.teamId = teamId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
