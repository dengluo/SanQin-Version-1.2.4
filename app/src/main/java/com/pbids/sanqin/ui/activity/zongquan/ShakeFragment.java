package com.pbids.sanqin.ui.activity.zongquan;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.friend.FriendService;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.Province;
import com.pbids.sanqin.presenter.ShakePresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.CityUtil;
import com.pbids.sanqin.utils.StringUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/3/22.
 */

public class ShakeFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, ShakeView, SensorEventListener,CityUtil.OnCityUtilLisenear{


    private ShakeHandler mHandler;
    private int mWeiChatAudio;
    private static final int START_SHAKE = 0x1;
    private static final int AGAIN_SHAKE = 0x2;
    private static final int END_SHAKE = 0x3;

    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Vibrator mVibrator;//手机震动
    private SoundPool mSoundPool;//摇一摇音效

    //记录摇动状态
    private boolean isShake = false;

    ShakePresenter shakePresenter;
    @Bind(R.id.main_shake_top_line)
    ImageView mTopLine;
    @Bind(R.id.main_linear_top)
    LinearLayout mTopLayout;
    @Bind(R.id.main_shake_bottom_line)
    ImageView mBottomLine;
    @Bind(R.id.main_linear_bottom)
    LinearLayout mBottomLayout;
    @Bind(R.id.shake_progressbar_layout)
    LinearLayout shakeProgressbarLayout;
    @Bind(R.id.img_shake_bg)
    ImageView imgShakeBg;
    @Bind(R.id.shake_location_tv)
    TextView shakeLocationTv;
    @Bind(R.id.shake_location_ll)
    LinearLayout shakeLocationLl;

    OptionsPickerView mPickerView;


    public static ShakeFragment newInstance() {
        ShakeFragment fragment = new ShakeFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {

        return shakePresenter = new ShakePresenter(this);
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shake, container, false);
//        userInformationPresenter = new UserInformationPresenter();
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mHandler = new ShakeHandler(this);

        //初始化SoundPool
        mSoundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        mWeiChatAudio = mSoundPool.load(_mActivity, R.raw.weichat_audio, 1);

        //获取Vibrator震动服务
        mVibrator = (Vibrator) _mActivity.getSystemService(_mActivity.VIBRATOR_SERVICE);

        String[] place = MyApplication.getUserInfo().getNativePlace().split("[,]");
        if(place.length>1){
            shakeLocationTv.setText(place[0]);
        }else{
            shakeLocationTv.setText("广东省");
        }


        //默认
        mTopLine.setVisibility(View.GONE);
        mBottomLine.setVisibility(View.GONE);

        CityUtil cityUtil = new CityUtil(_mActivity,1);
        cityUtil.setOnCityUtilLisenear(this);
        cityUtil.doCityUtilAsyncTask();


    }

    public void setShakeBg(int pic) {
        if (imgShakeBg != null) {
            // 设置后面的图片
            Glide.with(_mActivity).load(R.drawable.loading_gif).into(imgShakeBg);
        }
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setLeftArrowCenterTextTitleRightText("摇一摇", "跟我打招呼的人", _mActivity);
        //toolBar.setLeftArrowCenterTextTitle("摇一摇", _mActivity);
        toolBar.setOnToolBarClickLisenear(this);
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        isShake = false;
//        //获取 SensorManager 负责管理传感器
//        mSensorManager = ((SensorManager) _mActivity.getSystemService(_mActivity.SENSOR_SERVICE));
//        if (mSensorManager != null) {
//            //获取加速度传感器
//            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//            if (mAccelerometerSensor != null) {
//                mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
//            }
//        }
//    }

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
        //  Log.i("wzh","onSupportVisible");
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

//    @Override
//    public void onPause() {
//        // 务必要在pause中注销 mSensorManager
//        // 否则会造成界面退出后摇一摇依旧生效的bug
//        if (mSensorManager != null) {
//            mSensorManager.unregisterListener(this);
//        }
//        super.onPause();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                //返回
                pop();
                break;
            case R.id.main_right_layout:
                //跟我打招呼的人
                SayHelloFragment fragment = SayHelloFragment.instance();
                start(fragment);
                break;
        }
    }

    @Override
    public void onHttpSuccess(String type) {
        shakeProgressbarLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onHttpError(String type) {
        shakeProgressbarLayout.setVisibility(View.INVISIBLE);
        Toast.makeText(_mActivity, type, Toast.LENGTH_SHORT).show();
        isShake = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
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
                            //Log.d("wzh", "onSensorChanged: 摇动");

                            //开始震动 发出提示音 展示动画效果
                            mHandler.obtainMessage(START_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            //再来一次震动提示
                            mHandler.obtainMessage(AGAIN_SHAKE).sendToTarget();
                            Thread.sleep(500);
                            mHandler.obtainMessage(END_SHAKE).sendToTarget();


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

    @Override
    public void getFindUserJson(String userJson) {
        ShakePeopleFragment fragment = ShakePeopleFragment.newInstance();
        fragment.getArguments().putString("user_json", userJson);
        start(fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.shake_location_ll)
    public void onViewClicked() {
        mPickerView.show(true);
    }


    @Override
    public void cityCall(final ArrayList<String> provinceNameList, final ArrayList<ArrayList<String>> districtList, final ArrayList<ArrayList<ArrayList<String>>> countyListList, ArrayList<String> provinceIdList, ArrayList<ArrayList<String>> districtIdList, ArrayList<ArrayList<ArrayList<String>>> countyListIdList) {
        if (null == mPickerView) {
            OptionsPickerView.Builder builder = new OptionsPickerView.Builder(_mActivity, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    shakeLocationTv.setText(provinceNameList.get(options1));
//                            + "-" + districtList.get(options1).get(options2)
//                            + "-" + countyListList.get(options1).get(options2).get(options3));
                }
            });
//            builder.isDialog(true);
            mPickerView = new OptionsPickerView(builder);
            mPickerView.setPicker(provinceNameList, districtList, countyListList);
        }
    }

    @Override
    public void cityCall(ArrayList<Province> provinces) {

    }

    private static class ShakeHandler extends Handler {
        private WeakReference<ShakeFragment> mReference;
        private ShakeFragment mShakeFragment;

        public ShakeHandler(ShakeFragment fragment) {
            mReference = new WeakReference<ShakeFragment>(fragment);
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
                    //发出提示音
                    mShakeFragment.mSoundPool.play(mShakeFragment.mWeiChatAudio, 1, 1, 0, 0, 1);
                    mShakeFragment.mTopLine.setVisibility(View.VISIBLE);
                    mShakeFragment.mBottomLine.setVisibility(View.VISIBLE);
                    mShakeFragment.startAnimation(false);//参数含义: (不是回来) 也就是说两张图片分散开的动画

                    // 设置后面的图片
                    mShakeFragment.setShakeBg(R.drawable.loading_gif);

                    break;
                case AGAIN_SHAKE:
                    mShakeFragment.mVibrator.vibrate(300);
                    break;
                case END_SHAKE:
                    //整体效果结束, 将震动设置为false
//                    mShakeFragment.isShake = false;
                    // 展示上下两种图片回来的效果
                    mShakeFragment.startAnimation(true);
                    break;
            }
        }
    }

    /**
     * 开启 摇一摇动画
     *
     * @param isBack 是否是返回初识状态
     */
    private void startAnimation(boolean isBack) {
        //动画坐标移动的位置的类型是相对自己的
        int type = Animation.RELATIVE_TO_SELF;

        float topFromY;
        float topToY;
        float bottomFromY;
        float bottomToY;
        if (isBack) {
            topFromY = -0.5f;
            topToY = 0;
            bottomFromY = 0.5f;
            bottomToY = 0;
        } else {
            topFromY = 0;
            topToY = -0.5f;
            bottomFromY = 0;
            bottomToY = 0.5f;
        }

        //上面图片的动画效果
        TranslateAnimation topAnim = new TranslateAnimation(
                type, 0, type, 0, type, topFromY, type, topToY
        );
        topAnim.setDuration(200);
        //动画终止时停留在最后一帧~不然会回到没有执行之前的状态
        topAnim.setFillAfter(true);

        //底部的动画效果
        TranslateAnimation bottomAnim = new TranslateAnimation(
                type, 0, type, 0, type, bottomFromY, type, bottomToY
        );
        bottomAnim.setDuration(200);
        bottomAnim.setFillAfter(true);

        //大家一定不要忘记, 当要回来时, 我们中间的两根线需要GONE掉
        if (isBack) {
            bottomAnim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //当动画结束后 , 将中间两条线GONE掉, 不让其占位
                    mTopLine.setVisibility(View.GONE);
                    mBottomLine.setVisibility(View.GONE);

                    //开始网络请求前先显示搜索布局
                    shakeProgressbarLayout.setVisibility(View.VISIBLE);
                    //开始网络请求
                    //String[] place = MyApplication.getUserInfo().getNativePlace().split("[,]");
                    String place = shakeLocationTv.getText().toString();
                    if ("".equals(place)) {
                        place = "广东省";
                    }
                    HttpParams httpParams = new HttpParams();
                    httpParams.put("nativePlaceProvince", place);
                    List<String> friends = NIMClient.getService(FriendService.class).getFriendAccounts();
                    String friendsIds = StringUtil.convertToDatabaseValue(friends, ",");
                    httpParams.put("friendsIds", friendsIds);
                    //Log.i("wzh", "friendsIds: " + friendsIds);

                    String url = AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_USER_SHAKEFINDUSER;
                    addDisposable(shakePresenter.submitInformation(url, httpParams, "1"));
                }
            });
        }
        //设置动画
        mTopLayout.startAnimation(topAnim);
        mBottomLayout.startAnimation(bottomAnim);

    }


}
