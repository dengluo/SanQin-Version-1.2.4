package com.pbids.sanqin.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.netease.nim.avchatkit.AVChatProfile;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.avchatkit.constant.AVChatExtras;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.support.permission.MPermission;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.msg.SystemMessageObserver;
import com.netease.nimlib.sdk.msg.SystemMessageService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.SystemMessage;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseActivity;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonFinalVariable;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.config.preference.Preferences;
import com.pbids.sanqin.helper.LogoutHelper;
import com.pbids.sanqin.helper.SystemMessageUnreadManager;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.presenter.HomePagePresenter;
import com.pbids.sanqin.reminder.ReminderManager;
import com.pbids.sanqin.session.SessionHelper;
import com.pbids.sanqin.team.TeamCreateHelper;
import com.pbids.sanqin.ui.activity.news.NewsSystemMassageFragment;
import com.pbids.sanqin.ui.activity.news.NewsTopicListFragment;
import com.pbids.sanqin.ui.activity.zongquan.ContactListFragment;
import com.pbids.sanqin.utils.eventbus.CameraEvent;
import com.pbids.sanqin.utils.eventbus.LocationEvent;
import com.pbids.sanqin.utils.eventbus.OnClickNotificationEvent;
import com.pbids.sanqin.utils.eventbus.PermissionEvent;
import com.pbids.sanqin.utils.eventbus.ShowSystemMessageEvent;
import com.pbids.sanqin.utils.eventbus.ToWindowFocusChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:14
 * @desscribe 类描述:应用主的Activity
 * @remark 备注:
 * @see
 */
public class HomePageActivity extends BaseActivity implements HomePageView{

    private static final String EXTRA_APP_QUIT = "APP_QUIT";
    private static final int REQUEST_CODE_NORMAL = 1;
    private static final int REQUEST_CODE_ADVANCED = 2;
    private static final String TAG = HomePageActivity.class.getSimpleName();
    private static final int BASIC_PERMISSION_REQUEST_CODE = 100;


    HomePagePresenter homePagePresenter;
    CameraEvent onCameraEvent;
    PermissionEvent permissionEvent;
    LocationEvent onLocationEvent;
    ToWindowFocusChanged toWindowFocusChanged;

    private boolean customSplash = false;

    public static void start(Context context, Intent extras) {
        Intent intent = new Intent();
        intent.setClass(context, HomePageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//显示状态栏
//        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        super.onCreate(savedInstanceState);

        if(NIMClient.getStatus()!= StatusCode.LOGINED){
            Toast.makeText(this,"登陆数据已失败，请重新登陆",Toast.LENGTH_LONG).show();
            MyApplication.logoutApp(this);
            this.finish();
            return;
        }

        setContentView(R.layout.activity_home_page);
        //请求用到的权限
        requestBasicPermission();
        //如果有请求信息 如点击通知 跳转到相应的界面
        //onParseIntent();

		// eventbux
		if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }

/* im 后面调整		// 等待同步数据完成
		boolean syncCompleted = LoginSyncDataStatusObserver.getInstance().observeSyncDataCompletedEvent(new Observer<Void>() {
			@Override
			public void onEvent(Void v) {

				syncPushNoDisturb(UserPreferences.getStatusConfig());

				DialogMaker.dismissProgressDialog();
			}
		});

		LogUtil.i(TAG, "sync completed = " + syncCompleted);
		if (!syncCompleted) {
			DialogMaker.showProgressDialog(MainActivity.this, getString(R.string.prepare_data)).setCanceledOnTouchOutside(false);
		} else {
			syncPushNoDisturb(UserPreferences.getStatusConfig());
		}

		onInit();*/
        //处理浏览器唤醒app的请求
        Intent intent = getIntent();
        Uri data = intent.getData();
        String type = "";
        if(data!=null){
            type = data.getQueryParameter("type");
        }
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, MainFragment.newInstance(type));
        }
        //

        //
        onCameraEvent = new CameraEvent();
        toWindowFocusChanged = new ToWindowFocusChanged();
        permissionEvent = new PermissionEvent();
        onLocationEvent = new LocationEvent();


        ((MyApplication)getApplication()).setLocationOption(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation != null) {
                    onLocationEvent.setResultCode(aMapLocation.getErrorCode());
                    if (aMapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
//                        aMapLocation.getAddress();
                        onLocationEvent.setProvince(aMapLocation.getProvince());
                        onLocationEvent.setCtiy(aMapLocation.getCity());
//                        aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                        aMapLocation.getLatitude();//获取纬度
//                        aMapLocation.getLongitude();//获取经度
//                        aMapLocation.getAccuracy();//获取精度信息
//                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date date = new Date(aMapLocation.getTime());
//                        df.format(date);//定位时间
                    } else {
                        if(aMapLocation.getErrorCode()==12){
                            Toast.makeText(HomePageActivity.this,"请打开GPS定位!",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(HomePageActivity.this, aMapLocation.getErrorInfo(),Toast.LENGTH_SHORT).show();
                        }
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                    EventBusActivityScope.getDefault(HomePageActivity.this).post(onLocationEvent);
                }else{
                    EventBusActivityScope.getDefault(HomePageActivity.this).post(onLocationEvent);
                }
            }
        });
//        StatusBarUtils.with(this)
//                .setColor(getResources().getColor(R.color.main_status_color))
////                .setDrawable(getResources().getDrawable(R.drawable.shape))
//                .init();


        registerSystemObserver(true);
        //注册/注销系统消息未读数变化
        registerSystemMessageObservers(true);
        requestSystemMessageUnreadCount();
        //initUnreadCover();

    }

    private void enableMsgNotification(boolean enable) {
      /*  boolean msg = (pager.getCurrentItem() != MainTab.RECENT_CONTACTS.tabIndex);
        if (enable | msg) {
            *//**
             * 设置最近联系人的消息为已读
             *
             * @param account,    聊天对象帐号，或者以下两个值：
             *                    {@link #MSG_CHATTING_ACCOUNT_ALL} 目前没有与任何人对话，但能看到消息提醒（比如在消息列表界面），不需要在状态栏做消息通知
             *                    {@link #MSG_CHATTING_ACCOUNT_NONE} 目前没有与任何人对话，需要状态栏消息通知
             *//*
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
        } else {
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None);
        }*/
    }

    private void registerSystemObserver(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeReceiveSystemMsg(systemMessageObserver, register);
    }

    Observer<SystemMessage> systemMessageObserver = new Observer<SystemMessage>() {
        @Override
        public void onEvent(SystemMessage systemMessage) {
            //onIncomingMessage(systemMessage);
           /* Log.v("cgl","ddd");
            if(systemMessage.getType()== SystemMessageType.AddFriend){

            }*/
        }
    };

    /**
     * 注册/注销系统消息未读数变化
     *
     * @param register
     */
    private void registerSystemMessageObservers(boolean register) {
        NIMClient.getService(SystemMessageObserver.class).observeUnreadCountChange(sysMsgUnreadCountChangedObserver,
                register);
    }

    private Observer<Integer> sysMsgUnreadCountChangedObserver = new Observer<Integer>() {
        @Override
        public void onEvent(Integer unreadCount) {
            SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unreadCount);
            ReminderManager.getInstance().updateContactUnreadNum(unreadCount);
        }
    };
    /**
     * 查询系统消息未读数
     */
    private void requestSystemMessageUnreadCount() {
        int unread = NIMClient.getService(SystemMessageService.class).querySystemMessageUnreadCountBlock();
        SystemMessageUnreadManager.getInstance().setSysMsgUnreadCount(unread);
        ReminderManager.getInstance().updateContactUnreadNum(unread);
    }

    @Override
    protected void onDestroy() {
        //ToDo 部分机型发生崩溃
        try{
            super.onDestroy();
            registerSystemObserver(false);
            registerSystemMessageObservers(false);
            EventBus.getDefault().unregister(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //通知栏点击显示消息窗口
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onClickNotificationEvent(OnClickNotificationEvent notificationEvent){
//    	String m = notificationEvent.getNotifyType();
    	if(notificationEvent.getNotifyType().equals(NimIntent.EXTRA_NOTIFY_CONTENT)){
			IMMessage message = notificationEvent.getMessage();
			parseNotificationEvent(message);
		}
	}

    //显示系统消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowSystemMessageEvent(ShowSystemMessageEvent evt){
		switch (evt.getType()){
			case SystemMessageManager.TYPE_SYSTEM:
				//显示消息面板
				start(NewsSystemMassageFragment.newInstance());
                break;

			case SystemMessageManager.TYPE_TOPIC:
				//显示公众号订阅面板
				start(NewsTopicListFragment.newInstance());
				break;
		}

    }


	@Override
	protected void onStart() {
		super.onStart();
		//如果有请求信息 如点击通知 跳转到相应的界面
        //Todo 部分机型发生崩溃
		try {
            onParseIntent();
        }catch (Exception e){
		    e.printStackTrace();
        }
	}

	//后面完善
	private void onInit() {
		// 加载主页面  # liang    数据同步后执行
//		showMainFragment();

		LogUtil.ui("NIM SDK cache path=" + NIMClient.getSdkStorageDirPath());
	}


//    public static String sHA1(Context context) {
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(
//                    context.getPackageName(), PackageManager.GET_SIGNATURES);
//            byte[] cert = info.signatures[0].toByteArray();
//            MessageDigest md = MessageDigest.getInstance("SHA1");
//            byte[] publicKey = md.digest(cert);
//            StringBuffer hexString = new StringBuffer();
//            for (int i = 0; i < publicKey.length; i++) {
//                String appendString = Integer.toHexString(0xFF & publicKey[i])
//                        .toUpperCase(Locale.US);
//                if (appendString.length() == 1)
//                    hexString.append("0");
//                hexString.append(appendString);
//                hexString.append(":");
//            }
//            String result = hexString.toString();
//            return result.substring(0, result.length()-1);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

	private void parseNotificationEvent( IMMessage message){
		switch (message.getSessionType()) {
			case P2P:
				SessionHelper.startP2PSession(this, message.getSessionId());
				break;
			case Team:
				SessionHelper.startTeamSession(this, message.getSessionId());
				break;
			default:
				break;
		}
	}

    /**
     * 初始化未读红点动画
     */
/*    private void initUnreadCover() {
        DropManager.getInstance().init(getContext(), (DropCover) findView(R.id.unread_cover),
                new DropCover.IDropCompletedListener() {
                    @Override
                    public void onCompleted(Object id, boolean explosive) {
                        if (id == null || !explosive) {
                            return;
                        }

                        if (id instanceof RecentContact) {
                            RecentContact r = (RecentContact) id;
                            NIMClient.getService(MsgService.class).clearUnreadCount(r.getContactId(), r.getSessionType());
                            LogUtil.i("HomeFragment", "clearUnreadCount, sessionId=" + r.getContactId());
                        } else if (id instanceof String) {
                            if (((String) id).contentEquals("0")) {
                                NIMClient.getService(MsgService.class).clearAllUnreadCount();
                                LogUtil.i("HomeFragment", "clearAllUnreadCount");
                            } else if (((String) id).contentEquals("1")) {
                                NIMClient.getService(SystemMessageService.class).resetSystemMessageUnreadCount();
                                LogUtil.i("HomeFragment", "clearAllSystemUnreadCount");
                            }
                        }
                    }
                });
    }*/

	//这里处理跳转
    private void onParseIntent() {
        Intent intent = getIntent();
        if(intent==null){
            return;
        }
        Bundle bundle =  intent.getExtras();
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            IMMessage message = (IMMessage) getIntent().getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
			parseNotificationEvent(message);
         /*   switch (message.getSessionType()) {
                case P2P:
                    SessionHelper.startP2PSession(this, message.getSessionId());
                    break;
                case Team:
                    SessionHelper.startTeamSession(this, message.getSessionId());
                    break;
                default:
                    break;
            }*/
        }else if (intent.hasExtra(MyApplication.NOTIFICATION_SYSTEM)) {
			//显示系统消息面板
			start(NewsSystemMassageFragment.newInstance());
			return;
		} else if(intent.hasExtra(MyApplication.NOTIFICATION_NIM_SYSTEM)){
            //云信系统消息
            //start(ContactListFragment.newInstance());
            MainFragment mainFragment = findFragment(MainFragment.class);
            if(mainFragment!=null){
                mainFragment.OnTabClick(mainFragment.currentPosition,1);
            }
            return;
        } else if (intent.hasExtra(EXTRA_APP_QUIT)) {
            onLogout();
            return;
        } else if (intent.hasExtra(AVChatActivity.INTENT_ACTION_AVCHAT)) {
            if (AVChatProfile.getInstance().isAVChatting()) {
                Intent localIntent = new Intent();
                localIntent.setClass(this, AVChatActivity.class);
                startActivity(localIntent);
            }
        } else if (intent.hasExtra(AVChatExtras.EXTRA_FROM_NOTIFICATION)) {
            String account = intent.getStringExtra(AVChatExtras.EXTRA_ACCOUNT);
            if (!TextUtils.isEmpty(account)) {
                SessionHelper.startP2PSession(this, account);
            }
        }
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        //super.onBackPressedSupport();

        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                //finish();
                moveTaskToBack(false);
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        EventBusActivityScope.getDefault(this).post(toWindowFocusChanged);
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onPause() {
        super.onPause();
        enableMsgNotification(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        enableMsgNotification(false);
  /*      Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (!NimUIKit.isInitComplete()) {
                    LogUtil.i(TAG, "wait for uikit cache!");
                    new Handler().postDelayed(this, 100);
                    return;
                }

                customSplash = false;
                if (canAutoLogin()) {
                    //onIntent();
                } else {
                    //LoginActivity.start(WelcomeActivity.this);
                    //finish();
                }
            }
        };
        if (customSplash) {
            new Handler().postDelayed(runnable, 1000);
        } else {
            runnable.run();
        }*/
    }
    /**
     * 已经登陆过，自动登陆
     */
    private boolean canAutoLogin() {
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();
        return !TextUtils.isEmpty(account) && !TextUtils.isEmpty(token);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode){
                case CommonFinalVariable.IMAGE_REQUEST_CODE:
                    //用于拍照和选择图片的通知（此处有关7.0的权限请求问题）
                    //Log.i("wzh","data.getData(): "+data.getData());
                    Uri uri = data.getData();
                    onCameraEvent.setUri(uri);
                    //Log.i("wzh","onActivityResult: "+resultCode);
                    onCameraEvent.setResultCode(resultCode);
                    onCameraEvent.setRequestCode(requestCode);
                    EventBusActivityScope.getDefault(this).post(onCameraEvent);
                    break;
                case CommonFinalVariable.REQUEST_CODE_TAKE_PICTURE:
                    //Log.i("wzh","onActivityResult: "+resultCode);
                    onCameraEvent.setResultCode(resultCode);
                    onCameraEvent.setRequestCode(requestCode);
                    EventBusActivityScope.getDefault(this).post(onCameraEvent);
                    break;
                case  ContactListFragment.REQUEST_CODE_NORMAL:
                    //选择成员和返回到组聊天
                    //讨论组
                    final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                    if (selected != null && !selected.isEmpty()) {
                        TeamCreateHelper.createNormalTeam( this, null, selected, false, null);
                    } else {
                        Toast.makeText( this, "请选择至少一个联系人！", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case ContactListFragment.REQUEST_CODE_ADVANCED:
                    //高级组
                    final ArrayList<String> selectedAdv = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
                    TeamCreateHelper.createAdvancedTeam( this, selectedAdv);
                    break;
            }
        }

    }



    //权限请求
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       // Log.i("wzh","onRequestPermissionsResult: "+requestCode);
//        for(int i=0;i<grantResults.length;){
//            Log.i("wzh","grantResults("+i+"): "+grantResults[i]);
//        }
//        if(){
//
//        }
        onCameraEvent.setRequestCode(requestCode);
        permissionEvent.setPessionRequestCode(requestCode);
        if (grantResults != null && grantResults.length > 0) {
            permissionEvent.setPessionResultCode(grantResults[0]);
        }
        EventBusActivityScope.getDefault(this).post(onCameraEvent);
        EventBusActivityScope.getDefault(this).post(permissionEvent);
    }

    /**
     * 基本权限管理
     */
    private final String[] BASIC_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    //请求权限
    private void requestBasicPermission() {
        //ToDo 部分机型发生崩溃
        try{
            MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
            MPermission.with(HomePageActivity.this)
                    .setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                    .permissions(BASIC_PERMISSIONS)
                    .request();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public BasePresenter initPresenter() {
        return homePagePresenter = new HomePagePresenter(this);
    }


    // 注销
    private void onLogout() {
        Preferences.saveUserToken("");
        // 清理缓存&注销监听
        LogoutHelper.logout();

        // 启动登录
        LoginPageActivity.start(this);
        finish();
    }

}