package com.pbids.sanqin.ui.activity.zongquan;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.selector.activity.ContactSelectActivity;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.UserInfoManager;
import com.pbids.sanqin.model.entity.UserInfo;
import com.pbids.sanqin.presenter.BrickPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.SignInDaysView;
import com.pbids.sanqin.ui.view.dialog.OneTextTwoBtDialog;
import com.pbids.sanqin.ui.view.dialog.RewardDialog;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ErrorCode;
import com.pbids.sanqin.utils.TimeUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;
import static android.text.InputType.TYPE_CLASS_NUMBER;

/**
 * Created by pbids903 on 2018/2/7.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:10
 * @desscribe 类描述:烧砖界面
 * @remark 备注:
 * @see
 */
public class BrickFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, BrickView, SensorEventListener {

    public static final int REQUEST_CODE_BRICK_GIVEFRIEND = 16861;

    AnimationDrawable sparkleAnimationDrawable;
    //    AnimationDrawable fireDoorAnimationDrawable;
    TranslateAnimation brickAnim;
    ObjectAnimator animator;
    long endTime = 0;
    long intervalTime = 0;
    boolean animContinue = true;
    boolean canClick = false;
    boolean hasClickNumber = true;
    int clickNumner = 0;
    private static final int HAND_ANIMATION_TIME = 300;
    Timer timer;

    private int brickGiveCount;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;//手机震动

    @Bind(R.id.campaign_reward_number)
    TextView campaignRewardNumber;
    @Bind(R.id.contribution_hall_bt)
    TextView contributionHallBt;
    @Bind(R.id.give_friend_bt)
    TextView giveFriendBt;
    @Bind(R.id.sparkle)
    ImageView sparkle;
    @Bind(R.id.hand)
    ImageView hand;
    @Bind(R.id.zhuangzhuang)
    ImageView zhuangzhuang;
    @Bind(R.id.fire_door)
    ImageView fireDoor;
    @Bind(R.id.house)
    ImageView house;
    @Bind(R.id.next)
    ImageView next;
    @Bind(R.id.brick)
    ImageView brick;

    BrickPresenter presenter;
    @Bind(R.id.brick_number)
    TextView brickNumber;
    @Bind(R.id.zongquan_signin_progess)
    SignInDaysView zongquanSigninProgess;

    //记录摇动状态
    private boolean isShake = false;
    private ShakeHandler mHandler;

    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    private static class ShakeHandler extends Handler {
        private WeakReference<BrickFragment> mReference;
        private BrickFragment mShakeFragment;

        public ShakeHandler(BrickFragment fragment) {
            mReference = new WeakReference<BrickFragment>(fragment);
            if (mReference != null) {
                mShakeFragment = mReference.get();
            }
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_SHAKE:
                    //This method requires the caller to hold the permission VIBRATE.
                    mShakeFragment.mVibrator.vibrate(300);
                    mShakeFragment.onBrickClick();
                    //发出提示音
                    //mShakeFragment.mSoundPool.play(mShakeFragment.mWeiChatAudio, 1, 1, 0, 0, 1);
                    //mShakeFragment.mTopLine.setVisibility(View.VISIBLE);
                    //mShakeFragment.mBottomLine.setVisibility(View.VISIBLE);
                    //mShakeFragment.startAnimation(false);//参数含义: (不是回来) 也就是说两张图片分散开的动画

                    break;
                case AGAIN_SHAKE:
                    mShakeFragment.mVibrator.vibrate(300);
                    break;
                case END_SHAKE:
                    // 整体效果结束, 将震动设置为false
                    // mShakeFragment.isShake = false;
                    // 展示上下两种图片回来的效果
                    //mShakeFragment.startAnimation(true);
                    break;
            }
        }
    }

    public static BrickFragment newInstance() {
        BrickFragment fragment = new BrickFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return presenter = new BrickPresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brick, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onSupportVisible() {
        isShake = false;
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) _mActivity.getSystemService(_mActivity.SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (mAccelerometerSensor != null) {
                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
        //Log.i("wzh","onSupportVisible");
        super.onSupportVisible();
    }

    @Override
    public void onSupportInvisible() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
        //Log.i("wzh","onSupportInvisible");
        super.onSupportInvisible();
    }

    private void initView() {
        try{
            //ToDo 发生内存溢出异常 231
            mHandler = new ShakeHandler(this);

            //获取Vibrator震动服务
            mVibrator = (Vibrator) _mActivity.getSystemService(_mActivity.VIBRATOR_SERVICE);

            HttpParams params = new HttpParams();
            //参数省份
            String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USER_QUERYUSERINFO;
            addDisposable(presenter.submitInformation(url, params, "1"));
            setContentLayoutGone();

            Glide.with(_mActivity).load(R.drawable.zhuang_hand).into(hand);
            Glide.with(_mActivity).load(R.drawable.zhuangzhuang).into(zhuangzhuang);
            Glide.with(_mActivity).load(R.drawable.firewood).into(house);

            sparkle.setBackgroundResource(R.drawable.frame_anim_sparkle);
            sparkleAnimationDrawable = (AnimationDrawable) sparkle.getBackground();
            SharedPreferences sharedPreferences = _mActivity.getSharedPreferences("brick_text", MODE_PRIVATE);

            hasClickNumber = sharedPreferences.getBoolean("hasClickNumber", true);
            long time = sharedPreferences.getLong("time", 0);
            boolean isToday = true;
            //Log.i("wzh", "time: " + time);
            if (time != 0) {
//                isToday = TimeUtil.isSameDay(new Date(),new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(""+time));
//                isToday = TimeUtil.isToday(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time)));
                isToday = TimeUtil.isSameDay(new Date(), new Date(time));
                //Log.i("wzh", "isToday: " + isToday);
            }
            if (!isToday) {
                hasClickNumber = true;
                canClick = false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitle("烧砖", _mActivity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Glide.get(_mActivity).clearMemory();
        clearAnim();
        ButterKnife.unbind(this);
    }

    RewardDialog rewardDialog;

    private void onBrickClick() {
        intervalTime = System.currentTimeMillis() - endTime;
        endTime = System.currentTimeMillis();
        handler.removeCallbacks(runnable);//一旦点了人物就移除handler事件（每隔1秒降1个等级）
        if (animator == null) {
//                    animator = ObjectAnimator.ofFloat(hand, "rotation", 0f, -30f, 0f, -30f, 0f, -30f, 0f);
            animator = ObjectAnimator.ofFloat(hand, "rotation", 0f, -30f, 0f);
            hand.setPivotX(hand.getWidth());
            hand.setPivotY(hand.getHeight() / 2);
            animator.setDuration(HAND_ANIMATION_TIME);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    zhuangzhuang.setClickable(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    zhuangzhuang.setClickable(true);
                    if (!hasClickNumber) {
                        Toast.makeText(_mActivity, "今天烧砖的次数已经用完!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    clickNumner++;

                    updateFireLevel();
                    Log.i("wzh", "onAnimationEnd_clickNumner: " + clickNumner);
                    switch (clickNumner % 10) {
                        case 1:
                            break;
                        case 3:
                            if (clickNumner > 10) {
                                break;
                            }
                            if (fireDoor.getVisibility() == View.INVISIBLE) {
                                fireDoor.setVisibility(View.VISIBLE);
                                timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        Message message = new Message();
                                        message.what = -1;
                                        handler.sendMessage(message);
                                    }
                                }, 0, 200);
                            }
                            Glide.with(_mActivity).load(R.drawable.fire_door_small).into(fireDoor);
                            break;
                        case 6:
                            if (clickNumner > 10) {
                                break;
                            }
                            Glide.with(_mActivity).load(R.drawable.fire_door_middle).into(fireDoor);
                            break;
                        case 9:
                            if (clickNumner > 10) {
                                break;
                            }
                            if (sparkle.getVisibility() == View.INVISIBLE) {
                                sparkle.setVisibility(View.VISIBLE);
                            }
                            Glide.with(_mActivity).load(R.drawable.fire_door_strong).into(fireDoor);
                            sparkleStart();
                            break;
                        case 0:
                            if (clickNumner != 0) {
                                handler.removeCallbacks(runnable);
                                getLoadingPop("正在烧制砖块").show();
                                HttpParams params = new HttpParams();
                                params.put("operateCode", 1);
                                addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_USER
                                        + AddrConst.ADDRESS_USER_USERUPGRADE, params, "2"));
                            }
                            break;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        if (brickAnim == null) {
            brickAnim = new TranslateAnimation(0f, 0f, 0f, campaignRewardNumber.getY() - brick.getY() + campaignRewardNumber.getHeight());
            brickAnim.setDuration(1000);
            brickAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if(brick!=null){
                        brick.setVisibility(View.INVISIBLE);
                        brick.clearAnimation();
                    }

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        animator.start();
    }

    @OnClick({R.id.contribution_hall_bt, R.id.give_friend_bt, R.id.zhuangzhuang, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contribution_hall_bt:
                getLoadingPop("正在提交").show();
                HttpParams params = new HttpParams();
                addDisposable(presenter.submitInformation(AddrConst.SERVER_ADDRESS_USER
                        + AddrConst.ADDRESS_SURNAMEINFO_USERDONATE, params, "3"));

                break;
            case R.id.give_friend_bt:
                if (rewardDialog == null) {
                    rewardDialog = new RewardDialog(_mActivity);
                    rewardDialog.toastMsg = "请输入数量";
                    rewardDialog.setInputType(TYPE_CLASS_NUMBER);
                    rewardDialog.setOutFromat("%.0f");
                }
                rewardDialog.setType("friend");
                int brickNumber = MyApplication.getUserInfo().getBrickCount() + MyApplication.getUserInfo().getGiftBrickCount() + MyApplication.getUserInfo().getActivityBrickCount();
                rewardDialog.setMaxNumber((float) brickNumber);
                rewardDialog.setHitAndButtonText("请输入赠送的砖块数目", "赠送给好友");
                rewardDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        _mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideSoftInput();
                            }
                        });
                    }
                });
                rewardDialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                    @Override
                    public void confirm(View view) {
                        //rewardDialog.dismiss();
//                        BrickGiveFriendFragment fragment = BrickGiveFriendFragment.newInstance();
//                        fragment.getArguments().putString("number",(String)view.getTag());
//                        start(fragment);
                        final String numstr = (String) view.getTag();
                        brickGiveCount = Integer.parseInt(numstr);
                        if (brickGiveCount < 1) {
                            Toast.makeText(_mActivity, "请输入数量", Toast.LENGTH_LONG).show();
                        } else {
                            toUserSelected();
                        }
                    }

                    @Override
                    public void cancel(View view) {

                    }
                });
                rewardDialog.show();
                break;
            case R.id.next:
                start(ZCAncestralHallFragment.newInstance());
                break;
            case R.id.zhuangzhuang:
                onBrickClick();
                break;
        }
    }

    //到用户选择面板
    private void toUserSelected() {

        //showToast("添加分组成员");
        ContactSelectActivity.Option option = new ContactSelectActivity.Option();
        option.title = "选择要赠送的好友";
        //这里加入不可选的好友
        //List<String> disableAccounts = new ArrayList<>();
        //option.itemDisableFilter = new ContactIdFilter(disableAccounts);
        option.maxSelectNum = 1;
        option.multi = false;

        option.minSelectedTip = "请选择好友";
        NimUIKit.startContactSelector(_mActivity, option, REQUEST_CODE_BRICK_GIVEFRIEND);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BRICK_GIVEFRIEND && resultCode == Activity.RESULT_OK) {
            final ArrayList<String> selected = data.getStringArrayListExtra(ContactSelectActivity.RESULT_DATA);
            if (selected == null && selected.isEmpty()) {
                Toast.makeText(_mActivity, "请选择要赠送的好友", Toast.LENGTH_LONG).show();
            } else {
                final String toAccount = selected.get(0);
                //Toast.makeText(_mActivity,toAccount,Toast.LENGTH_LONG).show();

                rewardDialog.dismiss();

                final OneTextTwoBtDialog dialog = new OneTextTwoBtDialog(_mActivity);
                dialog.setContentText("是否赠送给该好友" + brickGiveCount + "块砖");
                dialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                    @Override
                    public void confirm(View view) {
                        //确认赠送
                        dialog.dismiss();
                        getLoadingPop("正在提交").show();
                        HttpParams params = new HttpParams();
                        params.put("totalBrickCount", brickGiveCount);
                        params.put("friendsId", toAccount);
                        String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USER_GIVEFRIENDS;
                        addDisposable(presenter.submitInformation(url, params, "togive"));
                    }

                    @Override
                    public void cancel(View view) {
                        //取消赠送
                        dialog.dismiss();
                        toast("已取消赠送");
                    }
                });
                dialog.show();
            }
        }
    }

    /*
     final OneTextTwoBtDialog dialog = new OneTextTwoBtDialog(_mActivity);
                dialog.setContentText("是否赠送给该好友"+brickCount+"块砖");
                dialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
                    @Override
                    public void confirm(View view) {
                        //确认赠送
                        dialog.dismiss();
                        getLoadingPop("正在提交").show();
                        HttpParams params = new HttpParams();
                        params.put("totalBrickCount",brickCount);
                        params.put("friendsId",(((ContactItem) item).getContact()).getContactId());
                        addDisposable(presenter.submitInformation(
                                MyApplication.SERVER_ADDRESS_USER+MyApplication.ADDRESS_USER_GIVEFRIENDS,params,"1"));
                    }

                    @Override
                    public void cancel(View view) {
                        //取消赠送
                        dialog.dismiss();
                    }
                });
                dialog.show();
     */

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            message.obj = clickNumner;
            handler.sendMessage(message);
        }
    };

    private void updateFireLevel() {
        handler.postDelayed(runnable, 1500);
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            //ToDo 发生空指针异常 551
            try {
                int what = msg.what;
                if (what == -1) {
                    if (fireDoor.getAlpha() == 0.8F) {
                        fireDoor.setAlpha(1.0F);
                    } else if (fireDoor.getAlpha() == 1.0F) {
                        fireDoor.setAlpha(0.8F);
                    }
                } else if (what == 1) {
                    int number = (int) msg.obj;
                    if (number < 10) {
                        if (number < 3) {
                            clickNumner = 0;
                        } else if (number >= 3 && number < 6) {
                            timer.cancel();
                            fireDoor.setVisibility(View.INVISIBLE);
                            clickNumner = 0;
                        } else if (number >= 6 && number < 9) {
                            Glide.with(_mActivity).load(R.drawable.fire_door_small).into(fireDoor);
                            clickNumner = 3;
                            updateFireLevel();
                        } else if (number == 9) {
                            sparkleStop();
                            sparkle.setVisibility(View.INVISIBLE);
                            Glide.with(_mActivity).load(R.drawable.fire_door_middle).into(fireDoor);
                            clickNumner = 6;
                            updateFireLevel();
                        }
                    } else if (number >= 10) {
                        sparkleStop();
                        sparkle.setVisibility(View.INVISIBLE);
                        Glide.with(_mActivity).load(R.drawable.fire_door_middle).into(fireDoor);
                        clickNumner = 6;
                        updateFireLevel();
                    }
                    //Log.i("wzh","handler_clickNumner: "+clickNumner);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
    });

    private void sparkleStart() {
        sparkleAnimationDrawable.start();
    }

    private void sparkleStop() {
        if (sparkleAnimationDrawable != null) {
            sparkleAnimationDrawable.stop();
        }
    }

    public void clearAnim() {
        handler.removeCallbacks(runnable);
        if (animator != null) {
            animator.cancel();
        }
        if (brickAnim != null) {
            brickAnim.cancel();
        }

        brickAnim = null;
        animator = null;

        hand.clearAnimation();
        brick.clearAnimation();
        sparkle.clearAnimation();
        fireDoor.clearAnimation();
        house.clearAnimation();

        if (sparkleAnimationDrawable != null) {
            sparkleAnimationDrawable.stop();
        }
        sparkleAnimationDrawable = null;

        sparkle.setBackground(null);
        fireDoor.setBackground(null);
        house.setBackground(null);
        house.setImageResource(0);
        house.setImageResource(0);
        house.setBackgroundResource(0);
        sparkle.setBackgroundResource(0);
        sparkle.setImageResource(0);
        fireDoor.setBackgroundResource(0);
        fireDoor.setImageResource(0);
        brick.setBackgroundResource(0);
        brick.setImageResource(0);
        hand.setBackgroundResource(0);
        hand.setImageResource(0);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        sparkle = null;
        fireDoor = null;
        sparkle = null;
        brick = null;
        hand = null;
        house = null;
    }

    @Override
    public void onHttpSuccess(String type) {
        dismiss();
        if ("3".equals(type)) {
            brickNumber.setText("0块");
            campaignRewardNumber.setText("0块");
            start(ZCAncestralHallFragment.newInstance());
        } else if ("togive".equals(type)) {
            Toast.makeText(_mActivity, "已成功赠送给好友", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        Toast.makeText(_mActivity, type, Toast.LENGTH_SHORT).show();
    }

    //第一次请求信息
    @Override
    public void getUserInfo(UserInfo userInfo) {
        try {
            setContentLayoutVisible();
            brickNumber.setText(userInfo.getBrickCount() + "块");
            //campaignRewardNumber.setText(userInfo.getGiftBrickCount() + "块");
            campaignRewardNumber.setText(userInfo.getActivityBrickCount() + "块");
            String signDays = userInfo.getSignDays();
//        signDays = "2018-03-30,3,0";
//        signDays = "2018-03-31,2,0";
//        signDays = "2018-03-31,3,1";
//        signDays = "2018-04-01,1,0";
//        signDays = "2018-04-02,2,1";
//        signDays = "2018-04-02,1,1";
            // Log.v("signDays","signDays: "+signDays);
            //签到显示处理
            String[] signInfo = signDays.split("[,]");
            //ToDo setDays方法集合发生数组越界问题
            if (signInfo.length == 3) {
                zongquanSigninProgess.setDays(signInfo[0], Integer.valueOf(signInfo[1]), Integer.valueOf(signInfo[2]));
            } else {
                toast("签到数据错误 错误代码:" + ErrorCode.ERROR_USER_SIGNDAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserInfoForBrick(UserInfo userInfo, int laveBurningBrickCount) {
        brick.setVisibility(View.VISIBLE);
        brick.startAnimation(brickAnim);
        MyApplication.getUserInfo().updateInfo(userInfo);
        UserInfoManager.updateUserInfo(_mActivity, MyApplication.getUserInfo());
        // Log.i("wzh", "intervalTime: " + intervalTime);
        canClick = false;
        if (laveBurningBrickCount == 0) {
            timer.cancel();
            fireDoor.setVisibility(View.INVISIBLE);
            sparkleStop();
            sparkle.setVisibility(View.INVISIBLE);
            clickNumner = 0;
            hasClickNumber = false;
            //得到SharedPreferences.Editor对象，并保存数据到该对象中
            SharedPreferences sharedPreferences = _mActivity.getSharedPreferences("brick_text", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasClickNumber", false);
            editor.putLong("time", new Date().getTime());
            editor.commit();
        } else {
            updateFireLevel();
        }
        getUserInfo(userInfo);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            if ((Math.abs(x) > 17 || Math.abs(y) > 17 || Math
                    .abs(z) > 17) && !isShake) {
                isShake = true;
                // TODO: 2016/10/19 实现摇动逻辑, 摇动后进行震动
                Thread thread = new Thread() {
                    @Override
                    public void run() {


                        super.run();
                        try {
                            Log.d("wzh", "onSensorChanged: 摇动");
                            //开始震动 发出提示音 展示动画效果
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            //再来一次震动提示
//                            mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
//                            Thread.sleep(500);
//                            mHandler.obtainMessage(END_SHAKE).sendToTarget();
                            isShake = false;

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
