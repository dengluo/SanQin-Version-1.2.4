package com.pbids.sanqin.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lzy.okgo.model.HttpParams;
import com.netease.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.impl.cache.DataCacheManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pbids.sanqin.DemoCache;
import com.pbids.sanqin.base.BaaseFragment;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.config.preference.Preferences;
import com.pbids.sanqin.config.preference.UserPreferences;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.view.BindPhoneFragmentView;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ErrorCode;

/**
 * @author : 上官名鹏
 * Description :
 * Date :Create in 2018/9/12 19:28
 * Modified By :
 */
public class BindPhoneFragmentPresenter extends BaasePresenter<BindPhoneFragmentView>{

    public static final int REQUEST_SENDCODE = 564564;
    public static final int REQUEST_AULOGIN = 56454;

    private BindPhoneFragmentView bindPhoneFragmentView;

    public BindPhoneFragmentPresenter(BindPhoneFragmentView bindPhoneFragmentView) {
        this.bindPhoneFragmentView = bindPhoneFragmentView;
    }

    public void sendCode(String phone){
        HttpParams httpParams = new HttpParams();
        httpParams.put("phone",phone);
        requestHttpPost(AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_LOGINBINDPHONE,httpParams,REQUEST_SENDCODE);
    }

    /**
     * 发送验证码
     * @param resultCode
     * @param requestCode
     * @param rescb
     */
    @Override
    public void onHttpSuccess(int resultCode, int requestCode, HttpJsonResponse rescb) {
        int status = rescb.getStatus();
        String message = rescb.getMessage();
        switch (requestCode){
            case REQUEST_SENDCODE:
                if(status== MyApplication.OK){
                    if(bindPhoneFragmentView!=null) bindPhoneFragmentView.showToast(message);
                }else{
                    if(bindPhoneFragmentView!=null) bindPhoneFragmentView.showToast(message);
                }
                break;
            case REQUEST_AULOGIN:
                if(status== MyApplication.OK){
                    UserInfo userInfo = (UserInfo) rescb.getJavaData(UserInfo.class);
                    pareUserInfo(userInfo,"Phone",message);
//                    if(bindPhoneFragmentView!=null) bindPhoneFragmentView.showToast(message);
                }else{
                    if(bindPhoneFragmentView!=null) {
                        bindPhoneFragmentView.showToast(rescb.getMessage());
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 登录
     * @param telPhone
     * @param smsCode
     */
    public void autoLogin(String telPhone, String smsCode,String info,String type) {
        JSONObject data = JSON.parseObject(info);
        HttpParams params = new HttpParams();
        params.put("SMSCode",smsCode);
        params.put("phone",telPhone);
        params.put("faceUrl",data.getString("icon"));
        if(type.equals("QQ")){
            params.put("identifier",data.getString("userID"));
        }else{
            params.put("openId",data.getString("unionid"));
//            bindPhoneFragmentView.showToast(info);
        }
        params.put("identityType",type);
        params.put("deviceId",MyApplication.deviceId);
        requestHttpPost(AddrConst.SERVER_ADDRESS_USER+AddrConst.ADDRESS_LOGINBINDPHONE,params,REQUEST_AULOGIN);
    }

    public void pareUserInfo(UserInfo userInfo, final String type, String message) {
        MyApplication.setUserInfo(userInfo);
        UserInfoManager.insertUserInfo(((BaaseFragment) bindPhoneFragmentView).getContext(), MyApplication.getUserInfo());
        SharedPreferences sp = ((BaaseFragment) bindPhoneFragmentView).getContext().getSharedPreferences("sanqin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (type.equals("phone")) {
            editor.putString("login_way", "phone");
        } else if (type.equals("QQ")) {
            editor.putString("login_way", "QQ");
        } else if (type.equals("WeChat")) {
            editor.putString("login_way", "WeChat");
        }
        editor.commit();
        //阿里云推送
        String token = MyApplication.getUserInfo().getToken();
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
        cloudPushService.bindAccount(token, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.v("yun_push", "bind account success");
            }

            @Override
            public void onFailed(String s, String s1) {
                Log.v("yun_push", "bind account failed");
            }
        });
        //网易云信同步登陆
        doLogin(new IMLoginCb() {
            @Override
            public void onResultStatus(int sta) {
                switch (sta) {
                    case 0:
                        bindPhoneFragmentView.showToast("用户登录失败，错误代码：" + ErrorCode.ERROR_USER_LOGIN_AUTH);
                        break;
                    case 1:
                        //success
                        //创建默认群组
                        //CommonGroup.checkGroup();  //这个暂时不处理
                        bindPhoneFragmentView.onHttpSuccess();
                        break;
                    case -1:
                        bindPhoneFragmentView.showToast("用户登录失败，错误代码：" + ErrorCode.ERROR_USER_LOGIN_ERROR);
                        break;
                }
            }
        });
    }

    public interface IMLoginCb {
        void onResultStatus(int sta);
    }

    //网易云信同步登陆
    public void doLogin(final IMLoginCb cb) {
        //云信登陆
        LoginInfo info = MyApplication.getLoginInfo();
        if (info != null) {
            DemoCache.setAccount(info.getAccount());
            NIMClient.getService(AuthService.class).login(info).setCallback(new RequestCallback<LoginInfo>() {
                @Override
                public void onSuccess(LoginInfo loginInfo) {
                    DemoCache.setAccount(loginInfo.getAccount());
                    saveLoginInfo(loginInfo.getAccount(), loginInfo.getToken());
                    // 初始化消息提醒配置
                    initNotificationConfig();
                    DataCacheManager.buildDataCacheAsync(); // build data cache on auto login
                    // 等待同步数据完成
                    boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
                        @Override
                        public void onEvent(Void v) {
                            cb.onResultStatus(1);
                        }
                    });
                    if (!syncCompleted) {
                        //显示处理中...
                    } else {
                        cb.onResultStatus(1);
                    }
                }

                @Override
                public void onFailed(int i) {
                    cb.onResultStatus(0);
                }

                @Override
                public void onException(Throwable throwable) {
                    cb.onResultStatus(-1);
                }
            });
        }
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        //statusBarNotificationConfig.
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }



}
