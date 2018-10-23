package com.pbids.sanqin.utils;

public class AddrConst {

//    //--------------生产服务器
    public static final String SERVER_ADDRESS_USER = "http://app.huaqinchi.com:8081"; //user
    public static final String SERVER_ADDRESS_PAYMENT = "http://app.huaqinchi.com:8082";//payment
    public static final String SERVER_ADDRESS_NEWS = "http://app.huaqinchi.com:8083";//news

    //--------------预生产服务器
//    public static final String SERVER_ADDRESS_USER = "http://dapp.huaqinchi.com:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://dapp.huaqinchi.com:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "http://dapp.huaqinchi.com:8083";//news

//    public static final String SERVER_ADDRESS_USER = "http://39.108.222.58:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://39.108.222.58:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "http://39.108.222.58:8083";//news

    //测试服务器
//    public static final String SERVER_ADDRESS_USER = " "; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://120.77.247.117:9812";//payment
//    public static final String SERVER_ADDRESS_NEWS = "http://120.77.247.117:9515";//news

//    public static final String SERVER_ADDRESS_USER = "http://192.168.5.8:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://192.168.5.8:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "http://192.168.5.8:8083";//news

    //me
//    public static final String SERVER_ADDRESS_USER = "http://192.168.5.162:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://192.168.5.8:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "http://192.168.5.8:8083";//news

    //me
//    public static final String SERVER_ADDRESS_USER = "http://192.168.5.8:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://192.168.5.8:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "http://192.168.5.162:8083";//news

    //蛇皮怪
//    public static final String SERVER_ADDRESS_USER = "   http://192.168.5.91:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://192.168.5.8:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "   http://192.168.5.8:8083";//news

    //张键宇
//    public static final String SERVER_ADDRESS_USER = "   http://192.168.5.221:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://192.168.5.221:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "   http://192.168.5.221:8083";//news

    //张键宇
//    public static final String SERVER_ADDRESS_USER = "   http://192.168.42.1:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://192.168.42.1:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "   http://192.168.42.1:8083";//news

    //张键宇
//    public static final String SERVER_ADDRESS_USER = "   http://192.168.42.128:8081"; //user
//    public static final String SERVER_ADDRESS_PAYMENT = "http://192.168.42.128:8082";//payment
//    public static final String SERVER_ADDRESS_NEWS = "   http://192.168.42.128:8083";//news


//    public static final String SERVER_ADDRESS_NEWS = "http://192.168.5.32:9092";//news
//    public static final String SERVER_ADDRESS_IM = "http://192.168.5.32:9092";//IM test


    public static final String ADDRESS_LOGIN = "/SMSLogin";
    public static final String ADDRESS_ME_UPDATE_USERINFORMATION = "/user/save";
    public static final String ADDRESS_ME_BUSINESS_COOPERATION = "/businessCooperation/save";// 商务合作
    public static final String ADDRESS_ME_FEEDBACK = "/feedback/save";// 用户反馈
    public static final String ADDRESS_ME_BINDING_PGONE = "/user/bindPhone";// 绑定手机号码接口
    public static final String ADDRESS_LINKPAGE = "/linkPage/index";// 引导页
    public static final String ADDRESS_SURNAME_AUTHENTICATION = "/user/approveSurname"; //姓氏认证
    public static final String ADDRESS_AUTHLOGIN = "/authLogin"; //授权登陆
    public static final String ADDRESS_ACTIVITY_LIST = "/activity/list"; //查询我的活动列表
    public static final String ADDRESS_GENEALOGY = "/surname/get"; //族谱姓氏详情
    public static final String ADDRESS_ACCOUNTCASH = "/userCoupon/list"; //我的礼券列表
    public static final String ADDRESS_USECOUPON = "/userCoupon/useCoupon"; //使用礼券
    public static final String ADDRESS_FAVOR_LIST = "/favor/list"; //收藏列表
    public static final String ADDRESS_FAVOR_GET = "/favor/delete"; //批量删除收藏
    public static final String ADDRESS_BINDCARD_SAVEBINDCARD = "/bindCard/saveBindCard"; //实名认证
    public static final String ADDRESS_QUERY_APPVERSION= "/appVersion/queryAppVersion"; //检测版本更新
    public static final String ADDRESS_FAMILY_NEWS= "/surname/news"; //家族资讯
    public static final String ADDRESS_CLAN_INFO= "/clan/queryClanInfo"; //家族资讯
    public static final String ADDRESS_CLAN_Distributed= "/clan/queryClanPeoDistributed"; //家族人员分布
    public static final String ADDRESS_ACCOUNT_QUERY_INFO= "/account/queryAccountInfo"; //查询当前用户账户信息
    public static final String ADDRESS_FAMILY_ACTIVITY= "/surname/activity"; //查询当前用户账户信息
    public static final String ADDRESS_HOME= "/article/home"; //查询当前用户账户信息
    public static final String ADDRESS_BINDCARD_LIST= "/bindCard/list"; //查询绑定银行卡
    public static final String ADDRESS_BINDCARD_DEL= "/bindCard/delBindCard"; //删除绑定银行卡
    public static final String ADDRESS_ACCOUNT_BALANCEPAY= "/account/balancePay"; //余额付款
    public static final String ADDRESS_NEWSMORE = "/article/more"; //查看更多
    //    public static final String ADDRESS_CARDINFO = "/bindCard/queryCardInfo"; //搜索银行卡
    public static final String ADDRESS_CARDINFO = "/bindCard/queryCardBin"; //搜索银行卡
    public static final String ADDRESS_SAVE_BINDCARD = "/bindCard/saveBindCard"; //绑定银行卡银行卡
    public static final String ADDRESS_UPDATE_PAYWORD = "/account/updatePayword"; //修改（设置）支付密码
    public static final String ADDRESS_CHECK_PAYWORD = "/account/checkPayword"; //支付密码校验
    public static final String ADDRESS_NAME_VERIFIED = "/user/realNameVerified"; //实名认证
    public static final String ADDRESS_USER_UPGRADE = "/user/userUpgrade"; //用户升级
    public static final String ADDRESS_ACTIVITY_PAYINFO = "/activity/payinfo"; //取活动支付信息
    public static final String ADDRESS_ACTIVITY_ORDER = "/activity/order"; //查看活动报名信息详情
    public static final String ADDRESS_USER_QUERYUSERINFO = "/user/queryUserInfo"; //查询用户实时信息
    public static final String ADDRESS_USER_USERUPGRADE = "/user/userUpgrade"; //用户升级
    public static final String ADDRESS_SURNAMEINFO_QUERYSURNAMEINFO = "/surnameInfo/querySurnameInfo"; //查询宗祠信息
    public static final String ADDRESS_SURNAMEINFO_USERDONATE = "/surnameInfo/userDonate"; //贡献全部砖块给宗祠
    public static final String ADDRESS_SURNAMEINFO_QUERYDONATERANK = "/surnameInfo/queryDonateRank"; //查询宗祠捐赠排行
    public static final String ADDRESS_USER_CHANGEBINDPHONE = "/user/changeBindPhone"; //查询宗祠捐赠排行
    public static final String ADDRESS_FEEDBACK_QUERYTYPE = "/feedback/queryFeedbackType"; //查询反馈类型
    public static final String ADDRESS_NEWARTICLE_FOUR = "/view/shelves/list.html?"; //首页博古架4个h5界面
    public static final String ADDRESS_NEWARTICLE_ZONGREN = "/surname/detail?"; //首页博古架宗人堂
    public static final String ADDRESS_NEWARTICLE_JIACI = "/view/suron/list.html?"; //首页博古架宗祠
    public static final String ADDRESS_USER_SHAKEFINDUSER = "/user/shakeFindUser"; //摇一摇
    public static final String ADDRESS_USER_GIVEFRIENDS = "/user/giveFriends"; //摇一摇
    public static final String ADDRESS_TAG_FAVOR= "/tag/favor?"; //我关注的标签
    public static final String ADDRESS_TAG_DEL= "/tag/del?"; //删除我关注的标签
    public static final String ADDRESS_DEL_NOTICE_SURNAME= "/noticeSurname/delNoticeSurname?"; //删除我关注的姓氏
    public static final String ADDRESS_ARTICLE_FORWARD= "/article/forward"; //文章转发量
    public static final String ADDRESS_PICK_UP_BRICK_NUMBER = "/rewardbrick/getOpenOne";//登录获取砖块数量
    public static final String ADDRESS_PICK_UP_BRICK = "/user/addBrick?";//领取砖块
    public static final String ADDRESS_INVITE_REGISTER = "/invite/";//邀请注册
    public static final String ADDRESS_INVITE_GETIMAGESURL = "/invite/generating_poster";//获取邀请海报图片地址
    public static final String ADDRESS_LOAD_TEAM_MAIN_INFO_SERRCH = "/userGroup/queryUserGroup";//宗人群群搜索
    public static final String ADDRESS_LOAD_TEAM_MAIN_INFO = "/userGroup/queryAll";//宗人群首页信息
    public static final String ADDRESS_VAL_FAMILY_MAIN = "/userGroup/queryUserIdentity";//是否是家族总管
    public static final String ADDRESS_VAL_UPDATE_GROUP = "/userGroup/updataUserGroup";//更新群名称
    public static final String ADDRESS_TRANSFERGROUPS = "/userGroup/transferGroups";//转让群
    public static final String ADDRESS_VAL_JOIN_TEAM = "/userGroup/queryUserJoinGroup";//验证群
    public static final String ADDRESS_UPDATE_NICK_NAME = "/user/updateNickName";//修改别称
    public static final String ADDRESS_GETWITHDRAWTIME = "/account/getWithdrawTime";//获取提现时间
    public static final String ADDRESS_ISUSEROPENID = "/user/isUserOpenId";//根据第三方登录id查询用户
    public static final String ADDRESS_LOGINBINDPHONE = "/user/loginBindPhone";//第三方登录绑定手机号

}
