package com.pbids.sanqin.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.widget.Toast;

/*import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.avchatkit.constant.AVChatExtras;*/
import com.netease.nimlib.sdk.NIMSDK;
import com.netease.nimlib.sdk.NimIntent;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.pbids.sanqin.DemoCache;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseActivity;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.db.FriendGroupManager;
import com.pbids.sanqin.model.entity.FriendGroupDb;
import com.pbids.sanqin.utils.CommonUtil;
import com.pbids.sanqin.utils.eventbus.ActivityFinishEvent;
import com.pbids.sanqin.utils.eventbus.OnClickNotificationEvent;
import com.pbids.sanqin.utils.eventbus.NimSystemMessageEvent;
import com.pbids.sanqin.utils.eventbus.ShowSystemMessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:13
 * @desscribe 类描述:广告页Activity
 * @remark 备注:
 * @see
 */
public class BootPageActivity extends BaseActivity {

    private static final String TAG = "BootPageActivity";

    private static boolean firstEnter = true; // 是否首次进入

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        DemoCache.setMainTaskLaunching(true);
        // APP进程重新起来
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, BootPageFragment.newInstance());
        }
        if (!firstEnter) {
            onIntent(); // APP进程还在，Activity被重新调度起来
        } else {

        }
        //-------------------处理不删除数据问题----------------
        //处理内容问题
        String groupname = "宗亲会";
        FriendGroupManager fm = new FriendGroupManager();
        FriendGroupDb groupDb = fm.queryByGroupName(groupname);
        if (groupDb != null) {
            fm.updateGroupName(groupDb.getGroupId(),"宗人堂");
        }
        //---------------------end-------------------------------
    }

    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Subscribe
    public void onActivityFinshEvent(ActivityFinishEvent activityFinshEvent){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firstEnter) {
            firstEnter = false;
        }
    }


    @Override
    public BasePresenter initPresenter() {
        return null;
    }

/*    // 首次进入，打开欢迎界面
    private void showSplashView() {
//        getWindow().setBackgroundDrawableResource(R.drawable.splash_bg);
//        customSplash = true;
    }*/



    // 处理收到的Intent
    private void onIntent() {
        if (TextUtils.isEmpty(DemoCache.getAccount())) {
            // 判断当前app是否正在运行
        /*  if (!SysInfoUtil.stackResumed(this)) {
                LoginActivity.start(this);
            }
            finish();*/
        } else {
            // 已经登录过了，处理过来的请求
            Intent intent = getIntent();
            if (intent != null) {
                if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
                    //点击消息栏进入此处理
                    parseNotifyIntent(intent);
                    return;
                }
                if (intent.hasExtra(MyApplication.NOTIFICATION_SYSTEM)) {
                    //系统消息
					if(CommonUtil.isForeground( this, HomePageActivity.class.getSimpleName() ) ){
						//被回收
                        // showMainActivity(new Intent().putExtra(MyApplication.NOTIFICATION_SYSTEM, MyApplication.NOTIFICATION_SYSTEM));
						showMainActivity(intent);
					}else {
						//event bus 发送事件
						ShowSystemMessageEvent evt = new ShowSystemMessageEvent();
						evt.setType(intent.getIntExtra("type",0) );
						EventBus.getDefault().post(evt);
						finish();
					}
                } else if (intent.hasExtra(MyApplication.NOTIFICATION_NIM_SYSTEM)) {
                    //系统消息
                    if(CommonUtil.isForeground( this, HomePageActivity.class.getSimpleName() ) ){
                        //被回收
                        showMainActivity(intent);
                    }else {
                        //event bus 发送事件
                        NimSystemMessageEvent evt = new NimSystemMessageEvent();
                        evt.setType(intent.getIntExtra("type",0) );
                        EventBus.getDefault().post(evt);
                        finish();
                    }
                }
                else if (NIMSDK.getMixPushService().isFCMIntent(intent)) {
                    //parseFCMNotifyIntent(NIMSDK.getMixPushService().parseFCMPayload(intent));
                }
            }

            if (!firstEnter && intent == null) {
                finish();
            } else {
                showMainActivity();
            }
        }
    }
    //点击消息栏进入此处理
    private void parseNotifyIntent(Intent intent) {
        ArrayList<IMMessage> messages = (ArrayList<IMMessage>) intent.getSerializableExtra(NimIntent.EXTRA_NOTIFY_CONTENT);
        if (messages == null || messages.size() > 1) {
            showMainActivity(null);

        } else {
        	//如果是活动的就发送事件
			if(CommonUtil.isForeground( this, HomePageActivity.class.getSimpleName() ) ){
				//event bus 发送事件
				OnClickNotificationEvent notificationEvent = new OnClickNotificationEvent();
				notificationEvent.setMessage( messages.get(0));
				notificationEvent.setNotifyType(NimIntent.EXTRA_NOTIFY_CONTENT);
				EventBus.getDefault().post(notificationEvent);
			}else{
				//被回收
				showMainActivity(new Intent().putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, messages.get(0)));
			}
			finish();
        }
    }

    //启动主界面
    private void showMainActivity() {
        showMainActivity(null);
    }

    private void showMainActivity(Intent intent) {
        HomePageActivity.start(this,intent);
//        intent = new Intent(this, HomePageActivity.class);
//        startActivity(intent);
        finish();
    }

}
