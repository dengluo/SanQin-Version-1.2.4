package com.pbids.sanqin.common;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bumptech.glide.request.target.ViewTarget;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.https.HttpsUtils;
import com.mob.MobSDK;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.UIKitOptions;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nim.uikit.business.eventbus.RefreshUnreadCountEvent;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.ClientType;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.auth.OnlineClient;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.constant.SystemMessageType;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.util.NIMUtil;
import com.pbids.sanqin.DemoCache;
import com.pbids.sanqin.DemoPushContentProvider;
import com.pbids.sanqin.NIMInitManager;
import com.pbids.sanqin.NimSDKOptionConfig;
import com.pbids.sanqin.NotificationAdmain;
import com.pbids.sanqin.R;
import com.pbids.sanqin.chatroom.ChatRoomSessionHelper;
import com.pbids.sanqin.config.preference.Preferences;
import com.pbids.sanqin.config.preference.UserPreferences;
import com.pbids.sanqin.contact.ContactHelper;
import com.pbids.sanqin.event.DemoOnlineStateContentProvider;
import com.pbids.sanqin.helper.LogoutHelper;
import com.pbids.sanqin.model.db.FriendGroupManager;
import com.pbids.sanqin.model.db.FriendGroupMemberManager;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.SystemMessage;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.server.SanQinService;
import com.pbids.sanqin.session.NimDemoLocationProvider;
import com.pbids.sanqin.session.SessionHelper;
import com.pbids.sanqin.ui.activity.BootPageActivity;
import com.pbids.sanqin.ui.activity.HomePageActivity;
import com.pbids.sanqin.ui.activity.LoginPageActivity;
import com.pbids.sanqin.ui.activity.news.NewsSystemMassageFragment;
import com.pbids.sanqin.ui.activity.news.NewsTopicListFragment;
import com.pbids.sanqin.utils.AppUtils;
import com.pbids.sanqin.utils.eventbus.LocationEvent;
import com.pbids.sanqin.utils.eventbus.NimSystemMessageEvent;
import com.pbids.sanqin.utils.eventbus.SystemMessageHandleEvent;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

//import com.netease.nim.avchatkit.AVChatKit;
//import com.netease.nim.avchatkit.config.AVChatOptions;
//import com.netease.nim.avchatkit.model.ITeamDataProvider;
//import com.netease.nim.avchatkit.model.IUserInfoProvider;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:36
 * @desscribe 类描述:全局的application
 * @remark 备注:
 * @see
 */

public class MyApplication extends Application  {

    //网易云
	public static final String UIKIT_APPKEY = "07410145bd738e90ecffa84f6c3ad117";
	public static final String UIKIT_APPSECRET="7b2e0dfc8616";

    public static final int OK = 1;
    public static final int ERROR = 0;
    private static final String TAG = "wzh";
    private static MyApplication myApplication = null;
    public static UserInfo userInfo;

    public static String deviceId=""; //设备id

    public static UserInfo getUserInfo() {
        if(userInfo==null){
            userInfo = UserInfoManager.queryUserInfo(myApplication);
        }
        return userInfo;
    }

    public static void setUserInfo(UserInfo userInfo) {
        MyApplication.userInfo = userInfo;
    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;

    LocationEvent locationEvent;

    //系统消息 通知管理
	public static NotificationAdmain newsTopicSubscribeNotificationAdmain ;
	//公众号消息 通知管理
	public static NotificationAdmain systemMessageNotificationAdmain;
	static int NOTIFICATION_ID = 13565400;
	static int NOTIFICATION_NEWSTOPIC_SUBSCRIBE_ID = 17565660;
	public static String NOTIFICATION_SYSTEM = "NOTIFICATION_SYSTEM";
	public static String NOTIFICATION_NIM_SYSTEM = "NOTIFICATION_NIM_SYSTEM";

    //系统消息 添加好友通知管理
    public static NotificationAdmain sysNotificationAdmain ;
    static int NOTIFICATION_NIMSYS_SUBSCRIBE_ID = 17596160;

    public static SystemMessageManager sysMessManager ;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;

        sysMessManager = SystemMessageManager.instance();
        //启动服务
        startService(new Intent(MyApplication.this, SanQinService.class));

        ViewTarget.setTagId(R.id.glide_tag);
        MobSDK.init(this, "22cf103b097e6", "f4a392d64d9c26d771453b0cdee400cc");

        AppUtils.init(this);
        initOkGo();
        initLocation();

        // 云信
        DemoCache.setContext(this);
        LoginInfo loginInfo = getLoginInfo();
        NIMInit(loginInfo, this);

        //初始化阿里云推送
        //模拟器初始化报异常
        try{
            initCloudChannel(this);
        }catch(Exception e){
            e.printStackTrace();
        }

        //----------------------------------Bugly------------
        // 获取当前包名
        String packageName = this.getPackageName();
        // 获取当前进程名
        String processName = this.getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        //注册时申请的APPID
        CrashReport.initCrashReport(getApplicationContext(), "72344a801d", false);
    }
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    public static void NIMInit(LoginInfo loginInfo , final Context context){
        //notification
		systemMessageNotificationAdmain = new NotificationAdmain(context,NOTIFICATION_ID);
		newsTopicSubscribeNotificationAdmain = new NotificationAdmain(context,NOTIFICATION_NEWSTOPIC_SUBSCRIBE_ID);
        sysNotificationAdmain= new NotificationAdmain(context,NOTIFICATION_NIMSYS_SUBSCRIBE_ID);

        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
//		LoginInfo loginInfo = getLoginInfo();
        NIMClient.init(context, loginInfo , NimSDKOptionConfig.getSDKOptions(context));
        if (NIMUtil.isMainProcess(context)) {
			// 在主进程中初始化UI组件，判断所属进程方法请参见demo源码。
			// init pinyin
			PinYin.init(context);
			PinYin.validate();
			// 初始化UIKit模块
			initUiKit(context);
			// 初始化消息提醒
			NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
			// 云信sdk相关业务初始化
			NIMInitManager.getInstance().init(true);

			// 如果有自定义通知是作用于全局的，不依赖某个特定的 Activity，那么这段代码应该在 Application 的 onCreate 中就调用
			NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(new Observer<CustomNotification>() {
				@Override
				public void onEvent(CustomNotification message) {
					// 在这里处理自定义通知。
					String content = message.getContent();
					//些处云信的自定义消息  ，已改为阿里的推送
                    //parseSystemMessage(context,content,message.getTime(),message.getFromAccount(),message.getSessionId());
				}
			}, true);

            NIMClient.getService(AuthServiceObserver.class).observeOtherClients(MyApplication.getApplication().clientsObserver, true);

            //云信系统信息通知
            NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(new Observer<com.netease.nimlib.sdk.msg.model.SystemMessage>() {
                @Override
                public void onEvent(com.netease.nimlib.sdk.msg.model.SystemMessage systemMessage) {
                    //onIncomingMessage(systemMessage);
                    //Log.v("cgl","ddd");
                    if(systemMessage.getType()== SystemMessageType.AddFriend){
                        Intent intent = new Intent(context, BootPageActivity.class);
                        intent.putExtra(NOTIFICATION_NIM_SYSTEM,NOTIFICATION_NIM_SYSTEM);
                        intent.putExtra("type", NimSystemMessageEvent.ADD_FRIEND);
                        int smallIcon = R.drawable.app_logo;
                        String ticker = "系统消息";
                        newsTopicSubscribeNotificationAdmain.normal_notification(intent, smallIcon, ticker, "验证请求", systemMessage.getContent());
                        //这里
                    }
                }
            }, true);
        }
    }


    //多端登录和互踢
    public Observer<List<OnlineClient>> clientsObserver = new Observer<List<OnlineClient>>() {
        @Override
        public void onEvent(List<OnlineClient> onlineClients) {
            if (onlineClients != null && onlineClients.size() > 0) {
                OnlineClient client = onlineClients.get(0);
                switch (client.getClientType()) {
                    case ClientType.Windows:
                        //Log.v("cgl", "window");
                    case ClientType.MAC:
                        //Log.v("cgl", "mac");
                        break;
                    case ClientType.Web:
                        //Log.v("cgl", "web");
                        break;
                    case ClientType.iOS:
                        //Log.v("cgl", "ios");
                        kickOtherClient(client);

                    case ClientType.Android:
                        //Log.v("cgl", "android");
                        kickOtherClient(client);

                        break;
                    default:

                        break;
                }
            }
        }
    };

    private static void kickOtherClient(OnlineClient client){
        NIMClient.getService(AuthService.class).kickOtherClient(client).setCallback(new RequestCallback<Void>() {
            @Override
            public void onSuccess(Void param) {
                // 踢出其他端成功
                Log.v("cgl", "kickOtherClient success");
            }

            @Override
            public void onFailed(int code) {
                // 踢出其他端失败，返回失败code
                logoutApp(MyApplication.getApplication());
            }

            @Override
            public void onException(Throwable exception) {
                // 踢出其他端错误
                logoutApp(MyApplication.getApplication());
            }
        });
    }


    //退出
    public static void logoutApp(Context context){
        //
        //UserInfoManager.deleteUserInfo(context, MyApplication.getUserInfo());
//        String token = getUserInfo().getToken();
        CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
        cloudPushService.unbindAccount(new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.v("yun_push","unbind account success");
            }

            @Override
            public void onFailed(String s, String s1) {
                Log.v("yun_push","bind unaccount failed");
            }
        });
        //退出帐户
        UserInfoManager.deleteUserInfo(MyApplication.getApplication(), MyApplication.getUserInfo());
        //IM logout
//        onLogout();
        MyApplication.deleteUserData(context);
        Intent intent = new Intent(MyApplication.getApplication(), LoginPageActivity.class);
        //它可以关掉所要到的界面中间的activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getApplication().startActivity(intent);

    }

    //退出时删除用户数据
    public static void deleteUserData(Context context){
        Preferences.saveUserToken("");
        // 清理缓存&注销监听
        LogoutHelper.logout();
        NIMClient.getService(AuthService.class).logout();
        //清除系统消息
        //SystemMessageManager.clear(context);
        //clear group
        FriendGroupManager.newInstance().clear();
        FriendGroupMemberManager.newInstance().clear();
    }



    //处理消息
    public static void parseSystemMessage(Context context,SystemMessage systemMessage ){
        if(getUserInfo()==null){
            return;
        }
        if ( null != systemMessage ) {
           /* GsonBuilder gsonBuilder = new GsonBuilder();
            //转为mess对象
            SystemMessage systemMessage = null ;
            try {
                //转为mess对象
                systemMessage = gsonBuilder.create().fromJson(messbody, SystemMessage.class);
            }catch (Exception e){
                return;
            }
            if (systemMessage == null || systemMessage.getType() < 1) {
                return;
            }

            systemMessage.setTime(mestime);
            systemMessage.setFromAccount(fromId);
            systemMessage.setSessionId(assid);*/
            systemMessage.setUid(getUserInfo().getUserId());
            int mtype = systemMessage.getType();
            if(mtype>0){
                //保存到数据库
                //SystemMessageManager.add(context,systemMessage);
                sysMessManager.insert(systemMessage);
                //发出事件
                SystemMessageHandleEvent evt= new SystemMessageHandleEvent();
                evt.setMsgTpme(systemMessage.getMsgtype());
                evt.setType(mtype);
                EventBus.getDefault().post(evt);

                //发出通知
                // 设置点击后启动的activity
                Intent intent = new Intent(context, BootPageActivity.class);
                intent.putExtra(NOTIFICATION_SYSTEM,NOTIFICATION_SYSTEM);
                intent.putExtra("type", mtype);
                int smallIcon = R.drawable.app_logo;
                String ticker = "系统消息";

                switch (mtype) {
                    case SystemMessageManager.TYPE_SYSTEM:
                        ticker = "系统消息" ;
                        if (NewsSystemMassageFragment.isBackground) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                systemMessageNotificationAdmain.normal_notification(intent, smallIcon, ticker, "系统消息", systemMessage.getTitle());
                            }
                        }else {
                            HomePageActivity.start(context, intent);
                        }
                        //更新角标
                        EventBus.getDefault().post(new RefreshUnreadCountEvent());
                        break;
                    case SystemMessageManager.TYPE_TOPIC:
                        ticker = "公众号订阅消息";
                        if (NewsTopicListFragment.isBackground) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                newsTopicSubscribeNotificationAdmain.normal_notification(intent, smallIcon, ticker, systemMessage.getTitle(), systemMessage.getContent());
                            }
                        }else {
                            HomePageActivity.start(context, intent);
                        }
                        break;
                }
            }
        }
    }


/*

	private void initAVChatKit() {
		AVChatOptions avChatOptions = new AVChatOptions(){
			@Override
			public void logout(Context context) {
				//HomePageActivity.logout(context, true);
			}
		};
		avChatOptions.entranceActivity = BootPageActivity.class;
		avChatOptions.notificationIconRes = R.drawable.app_logo;
		AVChatKit.init(avChatOptions);

		// 初始化日志系统
		//LogHelper.init();
		// 设置用户相关资料提供者
		AVChatKit.setUserInfoProvider(new IUserInfoProvider() {
			@Override
			public com.netease.nimlib.sdk.uinfo.model.UserInfo getUserInfo(String account) {
				return NimUIKit.getUserInfoProvider().getUserInfo(account);
			}

			@Override
			public String getUserDisplayName(String account) {
				return UserInfoHelper.getUserDisplayName(account);
			}
		});
		// 设置群组数据提供者
		AVChatKit.setTeamDataProvider(new ITeamDataProvider() {
			@Override
			public String getDisplayNameWithoutMe(String teamId, String account) {
				return TeamHelper.getDisplayNameWithoutMe(teamId, account);
			}

			@Override
			public String getTeamMemberDisplayName(String teamId, String account) {
				return TeamHelper.getTeamMemberDisplayName(teamId, account);
			}
		});
	}*/
	private static UIKitOptions buildUIKitOptions(Context context) {
		UIKitOptions options = new UIKitOptions();
		// 设置app图片/音频/日志等缓存目录
		options.appCacheDir = NimSDKOptionConfig.getAppCacheDir(context) + "/app";
		return options;
	}
	//----------------------------云信UI组件-----------------------------------
	private static void initUiKit( Context context) {
		// 初始化
		NimUIKit.init(context, buildUIKitOptions(context));
		// 设置地理位置提供者。如果需要发送地理位置消息，该参数必须提供。如果不需要，可以忽略。
		NimUIKit.setLocationProvider(new NimDemoLocationProvider());

		// IM 会话窗口的定制初始化。
		// 注册定位信息提供者类（可选）,如果需要发送地理位置消息，必须提供。
		// demo中使用高德地图实现了该提供者，开发者可以根据自身需求，选用高德，百度，google等任意第三方地图和定位SDK。
		SessionHelper.init();

		// 聊天室聊天窗口的定制初始化。
		ChatRoomSessionHelper.init();

		// 通讯录列表定制：示例代码可详见demo源码中的ContactHelper类。
		// 1.定制通讯录列表中点击事响应处理（一般需要，UIKit 提供默认实现为点击进入聊天界面)
		ContactHelper.init();

		// 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
		NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

		// 在线状态定制初始化。
		NimUIKit.setOnlineStateContentProvider(new DemoOnlineStateContentProvider());
	}


    // 云信
    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
	public static LoginInfo getLoginInfo() {
        //取用户信息
		if(getUserInfo()==null){
			return null;
		}
		String account = getUserInfo().getUserId()+"";
		String token = getUserInfo().getToken();
		DemoCache.setAccount(account.toLowerCase());
		return new LoginInfo(account, token,UIKIT_APPKEY);
	}

    //-----------------------------------end------------------------------------------


    private void initWechat() {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        msgApi.registerApp("wxd61c8cd581e28ad7");
    }

    public static MyApplication getApplication() {
        return myApplication;
    }

    public void initLocation(){
        locationEvent = new LocationEvent();
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //        initCloudChannel(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
    }

    public void setLocationOption(AMapLocationListener  locationListener){
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式,高精度Hight_Accuracy
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationListener(locationListener);
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //开启缓存机制
        mLocationOption.setLocationCacheEnable(true);
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
    }

    public void startLocation(){
        //Log.i("wzw","startLocation");
        //启动定位
        mLocationClient.startLocation();
    }

    private void initOkGo(){
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        /* 打印 okgo 加载数据
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        */

        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        /*
        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        //使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        //使用内存保持cookie，app退出后，cookie消失
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore())); */

        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        /*
        //方法二：自定义信任规则，校验服务端证书
        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        builder.hostnameVerifier(new SafeHostnameVerifier()); */

        /*
        HttpHeaders headers = new HttpHeaders();
        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        params.put("commonParamsKey2", "这里支持中文参数"); */

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
//                .setCacheMode(CacheMode.)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数
    }

    /**
     * 初始化阿里云推送通道
     * @param applicationContext
     */
    private void initCloudChannel(final Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        final CloudPushService cloudPushService = PushServiceFactory.getCloudPushService();
        cloudPushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                //Log.i("aili_push", "init cloudchannel success");
                deviceId = cloudPushService.getDeviceId();

                //Log.i("aili_push", "deviceId:"+deviceId);
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                //Log.e("aili_push", "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

//        MiPushRegister.register(applicationContext, "XIAOMI_ID", "XIAOMI_KEY"); // 初始化小米辅助推送
//        HuaWeiRegister.register(applicationContext); // 接入华为辅助推送
//        GcmRegister.register(applicationContext, "send_id", "application_id"); // 接入FCM/GCM初始化推送
    }

}
