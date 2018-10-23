package com.pbids.sanqin.model.entity;

/**
 * Created by pbids920 on 2018/6/22.
 */

public class UserInfoEntity {


    /**
     * code : 0
     * data : {"accountBalance":0,"activityBrickCount":0,"brickCount":0,"clanStatus":0,"currentEmpiric":0,"deviceId":"0b0645fd1f0b46d4875ef57165fd6d55","email":"","empiric":0,"faceUrl":"http://sanqin-upload.oss-cn-shenzhen.aliyuncs.com/2018/2/9/faceImg9081028568205557289.png","giftBrickCount":0,"isBindCard":0,"isRealName":0,"isSetPayword":0,"level":11,"levelName":"草民","location":"","loginIP":"","loginTime":1529593415000,"name":"15969696969","nativePlace":"北京市,市辖区,东城区","newGiftCount":0,"noticeSurnameIds":"","noticeSurnames":"","phone":"15969696969","quickResponseCode":"","reId":322,"sex":"保密","signDays":"2018-06-21,0,0","signature":"","surname":"上官","surnameId":549,"surnameStatus":1,"token":"91E258BC42C673547CDB54EFE0387944","upgradeEx":99,"userId":468,"vip":0}
     * message : 登陆成功
     * status : 1
     */

    private int code;
    private DataBean data;
    private String message;
    private int status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * accountBalance : 0
         * activityBrickCount : 0
         * brickCount : 0
         * clanStatus : 0
         * currentEmpiric : 0
         * deviceId : 0b0645fd1f0b46d4875ef57165fd6d55
         * email :
         * empiric : 0
         * faceUrl : http://sanqin-upload.oss-cn-shenzhen.aliyuncs.com/2018/2/9/faceImg9081028568205557289.png
         * giftBrickCount : 0
         * isBindCard : 0
         * isRealName : 0
         * isSetPayword : 0
         * level : 11
         * levelName : 草民
         * location :
         * loginIP :
         * loginTime : 1529593415000
         * name : 15969696969
         * nativePlace : 北京市,市辖区,东城区
         * newGiftCount : 0
         * noticeSurnameIds :
         * noticeSurnames :
         * phone : 15969696969
         * quickResponseCode :
         * reId : 322
         * sex : 保密
         * signDays : 2018-06-21,0,0
         * signature :
         * surname : 上官
         * surnameId : 549
         * surnameStatus : 1
         * token : 91E258BC42C673547CDB54EFE0387944
         * upgradeEx : 99
         * userId : 468
         * vip : 0
         */

        private int accountBalance;
        private int activityBrickCount;
        private int brickCount;
        private int clanStatus;
        private int currentEmpiric;
        private String deviceId;
        private String email;
        private int empiric;
        private String faceUrl;
        private int giftBrickCount;
        private int isBindCard;
        private int isRealName;
        private int isSetPayword;
        private int level;
        private String levelName;
        private String location;
        private String loginIP;
        private long loginTime;
        private String name;
        private String nativePlace;
        private int newGiftCount;
        private String noticeSurnameIds;
        private String noticeSurnames;
        private String phone;
        private String quickResponseCode;
        private int reId;
        private String sex;
        private String signDays;
        private String signature;
        private String surname;
        private int surnameId;
        private int surnameStatus;
        private String token;
        private int upgradeEx;
        private int userId;
        private int vip;
        private String nickName;

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public int getAccountBalance() {
            return accountBalance;
        }

        public void setAccountBalance(int accountBalance) {
            this.accountBalance = accountBalance;
        }

        public int getActivityBrickCount() {
            return activityBrickCount;
        }

        public void setActivityBrickCount(int activityBrickCount) {
            this.activityBrickCount = activityBrickCount;
        }

        public int getBrickCount() {
            return brickCount;
        }

        public void setBrickCount(int brickCount) {
            this.brickCount = brickCount;
        }

        public int getClanStatus() {
            return clanStatus;
        }

        public void setClanStatus(int clanStatus) {
            this.clanStatus = clanStatus;
        }

        public int getCurrentEmpiric() {
            return currentEmpiric;
        }

        public void setCurrentEmpiric(int currentEmpiric) {
            this.currentEmpiric = currentEmpiric;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getEmpiric() {
            return empiric;
        }

        public void setEmpiric(int empiric) {
            this.empiric = empiric;
        }

        public String getFaceUrl() {
            return faceUrl;
        }

        public void setFaceUrl(String faceUrl) {
            this.faceUrl = faceUrl;
        }

        public int getGiftBrickCount() {
            return giftBrickCount;
        }

        public void setGiftBrickCount(int giftBrickCount) {
            this.giftBrickCount = giftBrickCount;
        }

        public int getIsBindCard() {
            return isBindCard;
        }

        public void setIsBindCard(int isBindCard) {
            this.isBindCard = isBindCard;
        }

        public int getIsRealName() {
            return isRealName;
        }

        public void setIsRealName(int isRealName) {
            this.isRealName = isRealName;
        }

        public int getIsSetPayword() {
            return isSetPayword;
        }

        public void setIsSetPayword(int isSetPayword) {
            this.isSetPayword = isSetPayword;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getLevelName() {
            return levelName;
        }

        public void setLevelName(String levelName) {
            this.levelName = levelName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLoginIP() {
            return loginIP;
        }

        public void setLoginIP(String loginIP) {
            this.loginIP = loginIP;
        }

        public long getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(long loginTime) {
            this.loginTime = loginTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNativePlace() {
            return nativePlace;
        }

        public void setNativePlace(String nativePlace) {
            this.nativePlace = nativePlace;
        }

        public int getNewGiftCount() {
            return newGiftCount;
        }

        public void setNewGiftCount(int newGiftCount) {
            this.newGiftCount = newGiftCount;
        }

        public String getNoticeSurnameIds() {
            return noticeSurnameIds;
        }

        public void setNoticeSurnameIds(String noticeSurnameIds) {
            this.noticeSurnameIds = noticeSurnameIds;
        }

        public String getNoticeSurnames() {
            return noticeSurnames;
        }

        public void setNoticeSurnames(String noticeSurnames) {
            this.noticeSurnames = noticeSurnames;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getQuickResponseCode() {
            return quickResponseCode;
        }

        public void setQuickResponseCode(String quickResponseCode) {
            this.quickResponseCode = quickResponseCode;
        }

        public int getReId() {
            return reId;
        }

        public void setReId(int reId) {
            this.reId = reId;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getSignDays() {
            return signDays;
        }

        public void setSignDays(String signDays) {
            this.signDays = signDays;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }

        public int getSurnameId() {
            return surnameId;
        }

        public void setSurnameId(int surnameId) {
            this.surnameId = surnameId;
        }

        public int getSurnameStatus() {
            return surnameStatus;
        }

        public void setSurnameStatus(int surnameStatus) {
            this.surnameStatus = surnameStatus;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getUpgradeEx() {
            return upgradeEx;
        }

        public void setUpgradeEx(int upgradeEx) {
            this.upgradeEx = upgradeEx;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getVip() {
            return vip;
        }

        public void setVip(int vip) {
            this.vip = vip;
        }
    }
}
