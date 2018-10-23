package com.pbids.sanqin.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.convert.StringConvert;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.netease.nim.uikit.api.model.main.LoginSyncDataStatusObserver;
import com.netease.nim.uikit.impl.cache.DataCacheManager;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.pbids.sanqin.DemoCache;
import com.pbids.sanqin.base.HttpJsonResponse;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.config.preference.Preferences;
import com.pbids.sanqin.config.preference.UserPreferences;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.ui.activity.LoginPageView;
import com.pbids.sanqin.utils.ErrorCode;
import com.pbids.sanqin.utils.OkGoUtil;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static com.pbids.sanqin.utils.AddrConst.ADDRESS_ISUSEROPENID;
import static com.pbids.sanqin.utils.AddrConst.ADDRESS_LOGIN;
import static com.pbids.sanqin.utils.AddrConst.SERVER_ADDRESS_USER;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:49
 * @desscribe 类描述:登陆页面
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.LoginPageFragment
 */
public class LoginPagePresenter extends BasePresenter {
    private static final String Tag ="LOGIN_PAGE_PRESENTER" ;
    String type;

    LoginPageView mLoginPageView;
    public LoginPagePresenter(LoginPageView loginPageView){
        this.mLoginPageView = loginPageView;
    }

    public DisposableObserver localLogin(String phoneNumber,String testCode,String realTestCode,String pictureCode,String realPictureCode){

        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","login_stringResponse: "+stringResponse.body());
                pareUserInfo(stringResponse.body(),"phone");
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //Log.i("wzh","onError: "+e.getMessage());
                mLoginPageView.onHttpError(LoginPageView.LONGIN_ERROR_HTTP_CODE);

            }

            @Override
            public void onComplete() {

            }
        };
        HttpParams params = new HttpParams();
        params.put("phone",phoneNumber);
        params.put("SMSCode",testCode);
        params.put("deviceId",MyApplication.deviceId);
        OkGoUtil.getStringObservableForPost(SERVER_ADDRESS_USER +ADDRESS_LOGIN,params,observer);
        return observer;
    }

    public DisposableObserver loginSendTestCode(String phoneNumber){
        final String[] testCode = new String[1];
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {

            }

            @Override
            public void onError(@NonNull Throwable e) {
                mLoginPageView.onHttpError(LoginPageView.LONGIN_ERROR_HTTP_CODE);
            }

            @Override
            public void onComplete() {
                if(!testCode.equals("")){
                    mLoginPageView.returnVerifyCode(testCode[0]);
                }
            }
        };

        OkGo.<String>post(SERVER_ADDRESS_USER + ADDRESS_LOGIN ).retryCount(0)
                .params("phone",phoneNumber)
                .converter(new StringConvert())
                .adapt(new ObservableResponse<String>())
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(new Consumer<Response<String>>() {
                    @Override
                    public void accept(@NonNull Response<String> stringResponse) throws Exception {
                        //Log.i("wzh","login_stringResponse: "+stringResponse.body());
                        JSONObject jsonObject = new JSONObject(stringResponse.body());
                        int status = jsonObject.getInt("status");
                        if(status==MyApplication.OK){
                            JSONObject data = jsonObject.getJSONObject("data");
                            Log.d("哈哈哈", data.toString());
                            testCode[0] = data.getString("SMSCode");
                        }else{
                            mLoginPageView.onHttpError(jsonObject.getString("message"));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        return observer;
    }

    public DisposableObserver<Response<String>> loginPlatform(String url, HttpParams params, final String type){
        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                pareUserInfo(stringResponse.body(),type);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mLoginPageView.onHttpError(LoginPageView.LONGIN_ERROR_HTTP_CODE);
                mLoginPageView.onHttpError(LoginPageView.LONGIN_ERROR_HTTP_CODE);
            }

            @Override
            public void onComplete() {

            }
        };
        OkGoUtil.getStringObservableForPost(url,params,observer);
        return observer;
    }

    public void pareUserInfo(String stringResponse, final String type){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(stringResponse);
            int status = jsonObject.getInt("status");
            if(status==MyApplication.OK){
                JSONObject data = jsonObject.getJSONObject("data");
                UserInfo userInfo = JSON.parseObject(data.toString(), new TypeReference<UserInfo>() {});
                MyApplication.setUserInfo(userInfo);
                UserInfoManager.insertUserInfo(((BaseFragment)mLoginPageView).getContext(),MyApplication.getUserInfo());
                SharedPreferences sp = ((BaseFragment)mLoginPageView).getContext().getSharedPreferences("sanqin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if(type.equals("phone")){
                    editor.putString("login_way", "phone");
                }else if(type.equals("QQ")){
                    editor.putString("login_way", "QQ");
                }else if(type.equals("WeChat")){
                    editor.putString("login_way", "WeChat");
                }
                editor.commit();
                //阿里云推送
                String token = MyApplication.getUserInfo().getToken();
                CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
                cloudPushService.bindAccount(token,new CommonCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.v("yun_push","bind account success");
                    }

                    @Override
                    public void onFailed(String s, String s1) {
                        Log.v("yun_push","bind account failed");
                    }
                });
                //网易云信同步登陆
                doLogin(new IMLoginCb(){
                    @Override
                    public void onResultStatus(int sta) {
                        switch (sta){
                            case 0:
                                mLoginPageView.showToastInfo("用户登录失败，错误代码：" + ErrorCode.ERROR_USER_LOGIN_AUTH);
                                break;
                            case 1:
                                //success
                                //创建默认群组
                                //CommonGroup.checkGroup();  //这个暂时不处理
                                mLoginPageView.onHttpSuccess(type);
                                break;
                            case -1:
                                mLoginPageView.showToastInfo("用户登录失败，错误代码：" + ErrorCode.ERROR_USER_LOGIN_ERROR);
                                break;
                        }
                    }
                });
				//success
//                mLoginPageView.onHttpSuccess(type);
            }else {
                String message = jsonObject.getString("message");
                if(message.equals("您的账号异常,请发邮件至kf@huaqinchi.com联系处理！")){
                    mLoginPageView.toastStauts("您的账号异常,请发邮件至\nkf@huaqinchi.com联系处理！");
                }else{
                    mLoginPageView.onHttpError(jsonObject.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            mLoginPageView.onHttpError(e.getMessage());
        }
    }

    public interface IMLoginCb {
        void onResultStatus(int sta);
    }
    //fave login info
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

    private void onLoginDone() {
//        loginRequest = null;
//        DialogMaker.dismissProgressDialog();
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
                    onLoginDone();
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
                    Log.v(Tag, "im onFailed:");
                    cb.onResultStatus(0);
                }

                @Override
                public void onException(Throwable throwable) {
                    Log.v(Tag, "im onException:");
                    cb.onResultStatus(-1);
                }
            });
        }
    }

    public DisposableObserver isExistUser(String identifier, String openId){

        DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","login_stringResponse: "+stringResponse.body());
                HttpJsonResponse rescb =  JSON.parseObject(stringResponse.body(),HttpJsonResponse.class);
                if(rescb.getStatus()== Const.OK){
                    //说明有用户
                    if(rescb.getCode()==Const.OK){
                        Object data = rescb.getData();
                        if(data!=null){
                            pareUserInfo(stringResponse.body(),type);
                        }else{
                            mLoginPageView.bindPhone();
                        }
                    }else{
                        String message = rescb.getMessage();
                        if(message.equals("您的账号异常,请发邮件至kf@huaqinchi.com联系处理！")) {
                            mLoginPageView.toastStauts("您的账号异常,请发邮件至\nkf@huaqinchi.com联系处理！");
                        }else{
                            mLoginPageView.bindPhone();
                        }
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //Log.i("wzh","onError: "+e.getMessage());
                mLoginPageView.onHttpError(LoginPageView.LONGIN_ERROR_HTTP_CODE);

            }

            @Override
            public void onComplete() {

            }
        };
        HttpParams params = new HttpParams();
        params.put("deviceId",MyApplication.deviceId);
        if(!identifier.equals("")){
            type="QQ";
            params.put("identifier",identifier);
        }else{
            type="微信";
            params.put("openId", openId);
        }
        OkGoUtil.getStringObservableForGet(SERVER_ADDRESS_USER +ADDRESS_ISUSEROPENID,params,observer);
        return observer;
    }
}