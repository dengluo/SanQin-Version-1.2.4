package com.pbids.sanqin.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseActivity;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.utils.eventbus.ActivityFinishEvent;
import com.pbids.sanqin.utils.eventbus.ToWindowFocusChanged;

import org.greenrobot.eventbus.Subscribe;

import me.yokeyword.eventbusactivityscope.EventBusActivityScope;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:14
 * @desscribe 类描述:登陆的Activity
 * @remark 备注:
 * @see
 */
public class LoginPageActivity extends BaseActivity{

    private static final String TAG = LoginPageActivity.class.getSimpleName();
    private static final String KICK_OUT = "KICK_OUT";
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;

    ToWindowFocusChanged toWindowFocusChanged;


    public static void start(Context context) {
        start(context, false);
    }
    public static void start(Context context, boolean kickOut) {
        Intent intent = new Intent(context, LoginPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KICK_OUT, kickOut);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        if (findFragment(MainFragment.class) == null) {
            loadRootFragment(R.id.fl_container, LoginPageFragment.newInstance());
        }
        toWindowFocusChanged = new ToWindowFocusChanged();
    }

    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        //super.onBackPressedSupport();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        EventBusActivityScope.getDefault(this).post(toWindowFocusChanged);
//        EventBusActivityScope.getDefault(this).register(this);
    }

    @Subscribe
    public void onActivityFinshEvent(ActivityFinishEvent activityFinshEvent){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

}
