package com.pbids.sanqin.model.entity;

import java.util.List;

/**
 *@author : 上官名鹏
 *Description :
 *Date :Create in 2018/7/18 11:29
 *Modified By :
 */

public class CasesCrowd {

    private String crowdName;

    private int crowdCount;

    private List<CasesCrowd> casesCrowds;

    private String  portraitImg;

    private String teamId;

    private int memberCount;

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public String getCrowdName() {
        return crowdName;
    }

    public void setCrowdName(String crowdName) {
        this.crowdName = crowdName;
    }

    public int getCrowdCount() {
        return crowdCount;
    }

    public void setCrowdCount(int crowdCount) {
        this.crowdCount = crowdCount;
    }

    public List<CasesCrowd> getCasesCrowds() {
        return casesCrowds;
    }

    public void setCasesCrowds(List<CasesCrowd> casesCrowds) {
        this.casesCrowds = casesCrowds;
    }

    public String getPortraitImg() {
        return portraitImg;
    }

    public void setPortraitImg(String portraitImg) {
        this.portraitImg = portraitImg;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }
}
