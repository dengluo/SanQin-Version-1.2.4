package com.pbids.sanqin.model.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:45
 * @desscribe 类描述:用户数据实体类
 * @remark 备注:
 * @see
 */
@Entity
public class UserInfo {
    @Id(autoincrement = true)
    private Long _id;
    private long createTime=0;
    private String email="";               //邮箱
    private String faceUrl="";             //用户头像
    private long userId=-1;                      //用户id
    private String loginIP="";             //登陆IP
    private long loginTime=0;                //登陆时间
    private int newGiftCount;               //新礼券数量
    private String password="";            //密码
    private String phone="";               //电话号码
    private String quickResponseCode="";  //二维码
    private int rank=0;
    private String sex="";                 //性别
    private int state;
    private String token="";               //用户登陆登陆成功的token；
    private String name ="";            //用户名
    private String signature="";           //个性签名
    private String location="";            //所在地,用逗号隔开
    private String locationId="";          //所在地Id,用逗号隔开
    private String nativePlace="";         //籍贯,用逗号隔开
    private String nativePlaceId="";       //籍贯Id,用逗号隔开
    private float accountBalance=0;        //账户余额
    private String surname="";              //认证的姓氏名字;
    private String noticeSurnames="";      //关注的姓氏,多个姓氏用逗号隔开
    private int surnameStatus=0;           //姓氏认证的状态；0 为未认证 1 认证成功
    private int surnameId=0;                //姓氏表的id
    private int Level=1;                    //用户等级
    private int empiric=0;                  //当前经验
    private String levelName="";            //等级名称 头衔
//    private String userLevel="1";          //用户等级
//    private String currentEx = "200";      //当前经验
    private int upgradeEx = 0;              // 升级经验
    private int vip = 0;                    //是否为vip 0 普通会员  1 2 3 分别代表会员等级
    private String noticeSurnameIds="";
    private int isBindCard=0;               //是否绑定银行卡（借记卡）1为已绑定 0为未绑定
    private int isSetPayword=0;             //是否设置支付密码 0未设置 1 已设置
    private String idNumber="";             //身份证
    private int isRealName;                 //是否实名认证 1实名认证过 0 未实名认证
    private String cardNumber="";           //绑定的银行卡号
    private String signDays="";             //签到的天数 日期用英文逗号隔开
    private int brickCount;                 //砖的数量
    private int giftBrickCount=0;           //好友赠送的砖块
    private int activityBrickCount=0;       //活动获得的砖块
    private int currentEmpiric=0;           //当前等级升级所需的总经验
    private int clanStatus = 0;    //是否有过商务合作  0为未进行合作 1 为有过商务合作
    //增加
    private String deviceId;
    private int reid;
    private String nickName;

    @Generated(hash = 1761972851)
    public UserInfo(Long _id, long createTime, String email, String faceUrl,
            long userId, String loginIP, long loginTime, int newGiftCount,
            String password, String phone, String quickResponseCode, int rank,
            String sex, int state, String token, String name, String signature,
            String location, String locationId, String nativePlace,
            String nativePlaceId, float accountBalance, String surname,
            String noticeSurnames, int surnameStatus, int surnameId, int Level,
            int empiric, String levelName, int upgradeEx, int vip,
            String noticeSurnameIds, int isBindCard, int isSetPayword,
            String idNumber, int isRealName, String cardNumber, String signDays,
            int brickCount, int giftBrickCount, int activityBrickCount,
            int currentEmpiric, int clanStatus, String deviceId, int reid,
            String nickName) {
        this._id = _id;
        this.createTime = createTime;
        this.email = email;
        this.faceUrl = faceUrl;
        this.userId = userId;
        this.loginIP = loginIP;
        this.loginTime = loginTime;
        this.newGiftCount = newGiftCount;
        this.password = password;
        this.phone = phone;
        this.quickResponseCode = quickResponseCode;
        this.rank = rank;
        this.sex = sex;
        this.state = state;
        this.token = token;
        this.name = name;
        this.signature = signature;
        this.location = location;
        this.locationId = locationId;
        this.nativePlace = nativePlace;
        this.nativePlaceId = nativePlaceId;
        this.accountBalance = accountBalance;
        this.surname = surname;
        this.noticeSurnames = noticeSurnames;
        this.surnameStatus = surnameStatus;
        this.surnameId = surnameId;
        this.Level = Level;
        this.empiric = empiric;
        this.levelName = levelName;
        this.upgradeEx = upgradeEx;
        this.vip = vip;
        this.noticeSurnameIds = noticeSurnameIds;
        this.isBindCard = isBindCard;
        this.isSetPayword = isSetPayword;
        this.idNumber = idNumber;
        this.isRealName = isRealName;
        this.cardNumber = cardNumber;
        this.signDays = signDays;
        this.brickCount = brickCount;
        this.giftBrickCount = giftBrickCount;
        this.activityBrickCount = activityBrickCount;
        this.currentEmpiric = currentEmpiric;
        this.clanStatus = clanStatus;
        this.deviceId = deviceId;
        this.reid = reid;
        this.nickName = nickName;
    }

    @Generated(hash = 1279772520)
    public UserInfo() {
    }

    public UserInfo updateInfo(UserInfo userInfo){
//        this._id = userInfo.get_id();
        this.createTime = userInfo.getCreateTime();
        this.email = userInfo.getEmail();
        this.faceUrl = userInfo.getFaceUrl();
        this.userId = userInfo.getUserId();
        this.loginIP = userInfo.getLoginIP();
        this.loginTime = userInfo.getLoginTime();
        this.newGiftCount = userInfo.getNewGiftCount();
        this.password = userInfo.getPassword();
        this.phone = userInfo.getPhone();
        this.quickResponseCode = userInfo.getQuickResponseCode();
        this.rank = userInfo.getRank();
        this.sex = userInfo.getSex();
        this.state = userInfo.getState();
        this.token = userInfo.getToken();
        this.name = userInfo.getName();
        this.signature = userInfo.getSignature();
        this.location = userInfo.getLocation();
        this.locationId = userInfo.getLocationId();
        this.nativePlace = userInfo.getNativePlace();
        this.nativePlaceId = userInfo.getNativePlaceId();
        this.accountBalance = userInfo.getAccountBalance();
        this.surname = userInfo.getSurname();
        this.noticeSurnames = userInfo.getNoticeSurnames();
        this.surnameStatus = userInfo.getSurnameStatus();
        this.surnameId = userInfo.getSurnameId();
        this.Level = userInfo.getLevel();
        this.empiric = userInfo.getEmpiric();
        this.levelName = userInfo.getLevelName();
        this.upgradeEx = userInfo.getUpgradeEx();
        this.vip = userInfo.getVip();
        this.noticeSurnameIds = userInfo.getNoticeSurnameIds();
        this.isBindCard = userInfo.getIsBindCard();
        this.isSetPayword = userInfo.getIsSetPayword();
        this.idNumber = userInfo.getIdNumber();
        this.isRealName = userInfo.getIsRealName();
        this.cardNumber = userInfo.getCardNumber();
        this.signDays = userInfo.getSignDays();
        this.brickCount = userInfo.getBrickCount();
        this.giftBrickCount = userInfo.getGiftBrickCount();
        this.activityBrickCount = userInfo.getActivityBrickCount();
        this.clanStatus = userInfo.getClanStatus();
        this.currentEmpiric = userInfo.getCurrentEmpiric();
        return this;
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

    public String getSignDays() {
        return signDays;
    }

    public void setSignDays(String signDays) {
        this.signDays = signDays;
    }

    public int getGiftBrickCount() {
        return giftBrickCount;
    }

    public void setGiftBrickCount(int giftBrickCount) {
        this.giftBrickCount = giftBrickCount;
    }

    public int getActivityBrickCount() {
        return activityBrickCount;
    }

    public void setActivityBrickCount(int activityBrickCount) {
        this.activityBrickCount = activityBrickCount;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public int getBrickCount() {
        return brickCount;
    }

    public void setBrickCount(int brickCount) {
        this.brickCount = brickCount;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public float getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(float accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getNativePlaceId() {
        return nativePlaceId;
    }

    public void setNativePlaceId(String nativePlaceId) {
        this.nativePlaceId = nativePlaceId;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNoticeSurnames() {
        return noticeSurnames;
    }

    public void setNoticeSurnames(String noticeSurnames) {
        this.noticeSurnames = noticeSurnames;
    }

    public int getSurnameStatus() {
        return surnameStatus;
    }

    public void setSurnameStatus(int surnameStatus) {
        this.surnameStatus = surnameStatus;
    }

    public int getSurnameId() {
        return surnameId;
    }

    public void setSurnameId(int surnameId) {
        this.surnameId = surnameId;
    }

    public int getIsBindCard() {
        return isBindCard;
    }

    public void setIsBindCard(int isBindCard) {
        this.isBindCard = isBindCard;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
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

    public int getNewGiftCount() {
        return newGiftCount;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public void setNewGiftCount(int newGiftCount) {
        this.newGiftCount = newGiftCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getNoticeSurnameIds() {
        return noticeSurnameIds;
    }

    public void setNoticeSurnameIds(String noticeSurnameIds) {
        this.noticeSurnameIds = noticeSurnameIds;
    }
    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getEmpiric() {
        return empiric;
    }

    public void setEmpiric(int empiric) {
        this.empiric = empiric;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getUpgradeEx() {
        return upgradeEx;
    }

    public void setUpgradeEx(int upgradeEx) {
        this.upgradeEx = upgradeEx;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "_id=" + _id +
                ", createTime=" + createTime +
                ", email='" + email + '\'' +
                ", faceUrl='" + faceUrl + '\'' +
                ", userId=" + userId +
                ", loginIP='" + loginIP + '\'' +
                ", loginTime=" + loginTime +
                ", newGiftCount=" + newGiftCount +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", quickResponseCode='" + quickResponseCode + '\'' +
                ", rank=" + rank +
                ", sex='" + sex + '\'' +
                ", state=" + state +
                ", token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", signature='" + signature + '\'' +
                ", location='" + location + '\'' +
                ", locationId='" + locationId + '\'' +
                ", nativePlace='" + nativePlace + '\'' +
                ", nativePlaceId='" + nativePlaceId + '\'' +
                ", accountBalance=" + accountBalance +
                ", surname='" + surname + '\'' +
                ", noticeSurnames='" + noticeSurnames + '\'' +
                ", surnameStatus=" + surnameStatus +
                ", surnameId=" + surnameId +
                ", Level=" + Level +
                ", empiric=" + empiric +
                ", levelName='" + levelName + '\'' +
                ", upgradeEx=" + upgradeEx +
                ", vip=" + vip +
                ", noticeSurnameIds='" + noticeSurnameIds + '\'' +
                ", isBindCard=" + isBindCard +
                ", isSetPayword=" + isSetPayword +
                ", idNumber='" + idNumber + '\'' +
                ", isRealName=" + isRealName +
                ", cardNumber='" + cardNumber + '\'' +
                ", signDays='" + signDays + '\'' +
                ", brickCount=" + brickCount +
                ", giftBrickCount=" + giftBrickCount +
                ", activityBrickCount=" + activityBrickCount +
                ", currentEmpiric=" + currentEmpiric +
                ", clanStatus=" + clanStatus +
                '}';
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getReid() {
        return this.reid;
    }

    public void setReid(int reid) {
        this.reid = reid;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
