package com.pbids.sanqin.ui.view;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.Rotate3dAnimation;

/**
 * Created by pbids903 on 2017/11/28.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:13
 * @desscribe 类描述:应用底部导航栏自定义view
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.MainFragment
 */
public class BottomBarView extends FrameLayout implements View.OnClickListener{

    public static final int GONE_VIREPAGE_NAME = 0;
    public static final int VISIBLE_VIREPAGE_NAME = 1;
    public static final int START_LAYOUT_ANIMIATION = 2;
    public static final int END_LAYOUT_ANIMATION = 3;
    public static final int HIDDEN_LAYOUT_ANIMATION = 4;
    public static final int SHOW_LAYOUT_ANIMATION = 5;
    public static final int BOTTOM_ANIMATION_TIME = 3000;
    public static final int BOTTOM_ANIMATION_WAVE_TIME = 500;

    private  float hpVpOffsetY = 0 ;

    ImageView hpVpRgZhizong;
    ImageView hpDoorCircle;
    RelativeLayout hpVpRgR1;
    ImageView hpVpRgZongquan;
    RelativeLayout hpVpRgR2;
    ImageView hpVpRgNews;
    RelativeLayout hpVpRgR3;
    ImageView hpVpRgMe;
    RelativeLayout hpVpRgR4;
    RelativeLayout hpDoor;
    WaveView hpWave;                  //主页水波效果实现
//    View hpVpNameTopLayout;

    FrameLayout mDoorAnimationLayout;//主页点击门扣姓氏布局（有上下滑动的动画效果）

    OnBottomBarViewClickLisener mOnBottomBarViewClickLisener;
    OnBottomBarViewAnimLisenear onBottomBarViewAnimLisenear;

    private int currentPage =0;
    private boolean isShowing = false;
    float deviationY;

    private Vibrator mVibrator;  //声明一个振动器对象
    Context mContext;
    SoundPool soundPool;
    private boolean isRedyPlay = false;

    int sceneWidth = 0;
    int sceneHeight = 0;


    public BottomBarView(Context context) {
        super(context);
        init(context);
    }

    public BottomBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomBarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public void init(Context context){
        mContext = context;
        //hpVpOffsetY = getResources().getDimension(R.dimen.dp_30);//姓氏偏移距离
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        //float density = dm.density;
        sceneWidth =  dm.widthPixels;
        sceneHeight =  dm.heightPixels;

        View view = LayoutInflater.from(context).inflate(R.layout.home_page_bottom_bar,null);
        hpVpRgR1 = (RelativeLayout) view.findViewById(R.id.hp_vp_rg_r1);
        hpVpRgR2 = (RelativeLayout) view.findViewById(R.id.hp_vp_rg_r2);
        hpVpRgR3 = (RelativeLayout) view.findViewById(R.id.hp_vp_rg_r3);
        hpVpRgR4 = (RelativeLayout) view.findViewById(R.id.hp_vp_rg_r4);
        hpDoor = (RelativeLayout) view.findViewById(R.id.hp_door);
        hpDoorCircle = (ImageView) view.findViewById(R.id.hp_door_circle);
        hpWave = (WaveView) view.findViewById(R.id.hp_wave);

        hpVpRgZhizong = (ImageView) view.findViewById(R.id.hp_vp_rg_zhizong);
        hpVpRgZongquan = (ImageView) view.findViewById(R.id.hp_vp_rg_zongquan);
        hpVpRgNews = (ImageView) view.findViewById(R.id.hp_vp_rg_news);
        hpVpRgMe = (ImageView) view.findViewById(R.id.hp_vp_rg_me);

        hpVpRgR1.setOnClickListener(this);
        hpVpRgR2.setOnClickListener(this);
        hpVpRgR3.setOnClickListener(this);
        hpVpRgR4.setOnClickListener(this);
        hpDoor.setOnClickListener(this);

        hpVpRgZhizong.setSelected(true);

        mVibrator = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
        deviationY = context.getResources().getDimension(R.dimen.dp_3);
        playSound(R.raw.kou);
        addView(view);
    }

    public void setOnBottomBarViewClickLisener(OnBottomBarViewClickLisener onBottomBarViewClickLisener){
        this.mOnBottomBarViewClickLisener = onBottomBarViewClickLisener;
    }

    public void setDoorAnimationLayout(FrameLayout doorAnimationLayout){
        this.mDoorAnimationLayout = doorAnimationLayout;
    }

    @Override
    public void onClick(View v) {
        if(null == mOnBottomBarViewClickLisener){
            return;
        }
        switch (v.getId()) {
            case R.id.hp_vp_rg_r1://知宗
                onclickHome();
                break;
            case R.id.hp_vp_rg_r2://宗圈
                onclickContact();
                break;
            case R.id.hp_vp_rg_r3://消息
                onclickNews();
                break;
            case R.id.hp_vp_rg_r4://我的
                onclickMy();
                break;
            case R.id.hp_door://门扣按钮
                hpDoor.setClickable(false);
                hpWave.setDuration(BOTTOM_ANIMATION_WAVE_TIME);
                hpWave.setStyle(Paint.Style.FILL);
                hpWave.setColor(Color.RED);
                hpWave.setInitialRadius(60);
                hpWave.setSpeed(BOTTOM_ANIMATION_WAVE_TIME+100);
                hpWave.setInterpolator(new LinearOutSlowInInterpolator());
                hpWave.start();

                new DoorStart().run();
                mVibrator.vibrate(300);
                if(isRedyPlay){
                    soundPool.play(1, 1, 1, 0, 0, 1);
                }

                hpWave.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hpWave.stop();
                    }
                }, BOTTOM_ANIMATION_WAVE_TIME);
                startNameViewPagerAnimationNow();
                break;
            default:
                break;
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public interface OnBottomBarViewClickLisener {
        void OnTabClick(int hiddePosition,int showPosition);
        FrameLayout returnHpVpNameLayout();
//        View returnTopView();
    }

    public interface OnBottomBarViewAnimLisenear{
        void openAnimStart();
        void openAnimEnd();
        void closeAnimStart();
        void closeAnimEnd();
    }

    public void setOnBottomBarViewAnimLisenear(OnBottomBarViewAnimLisenear lisenear){
        onBottomBarViewAnimLisenear = lisenear;
    }

    //显示姓氏
    public void startNameViewPagerAnimationNow(){
        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    public void stopNameViewPagerAnimation(){
        mHandler.removeCallbacks(mRunnable);
    }

    //
    public void startNameViewPagerAnimation() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, BOTTOM_ANIMATION_TIME);
    }

/*    public void startNameViewPagerAnimationForClear() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, BOTTOM_ANIMATION_TIME);
    }*/

    final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(null == mOnBottomBarViewClickLisener){
                return false;
            }
            int what = msg.what;
            TranslateAnimation mHiddenAction = null;
            if (what == GONE_VIREPAGE_NAME) {
                //
                /*mHiddenAction = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        1.0f);*/
                Float ty = getResources().getDimension(R.dimen.dp_300) - hpVpOffsetY;
                ty = sceneHeight - ty;
                mHiddenAction = new TranslateAnimation(0, 0, 0, ty);
                //动画持续时间
                mHiddenAction.setDuration(BOTTOM_ANIMATION_WAVE_TIME);
                //动画 侦听
                mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // anim start
                        if(onBottomBarViewAnimLisenear!=null){
                            onBottomBarViewAnimLisenear.closeAnimStart();
                        }
                        isShowing = true;
                        hpDoor.setClickable(false);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        // anim end
                        if(onBottomBarViewAnimLisenear!=null){
                            onBottomBarViewAnimLisenear.closeAnimEnd();
                        }
                        mDoorAnimationLayout.setVisibility(View.GONE);
                        hpDoor.setClickable(true);
                        mHandler.removeCallbacks(mRunnable);
                        isShowing = false;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            } else if (what == VISIBLE_VIREPAGE_NAME) {
               /* mHiddenAction = new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        0.0f,
                        Animation.RELATIVE_TO_SELF,
                        1.0f,
                         Animation.RELATIVE_TO_SELF,
                         0.0f);*/
                Float ty = getResources().getDimension(R.dimen.dp_300) - hpVpOffsetY;
                ty = sceneHeight - ty;
                mHiddenAction = new TranslateAnimation(0, 0, ty, 0);

                mHiddenAction.setDuration(BOTTOM_ANIMATION_WAVE_TIME);
                mHiddenAction.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        if(onBottomBarViewAnimLisenear!=null){
                            onBottomBarViewAnimLisenear.openAnimStart();
                        }
                        isShowing = true;
                        hpDoor.setClickable(false);
                        mDoorAnimationLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if(onBottomBarViewAnimLisenear!=null){
                            onBottomBarViewAnimLisenear.openAnimEnd();
                        }
                        //mHandler.postDelayed(mRunnable, BOTTOM_ANIMATION_TIME);
                        hpDoor.setClickable(true);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            if (null != mHiddenAction) {
                mDoorAnimationLayout.startAnimation(mHiddenAction);
            }
            return true;
        }
    });

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(null == mDoorAnimationLayout){
                mDoorAnimationLayout = mOnBottomBarViewClickLisener.returnHpVpNameLayout();
            }
            if(mDoorAnimationLayout!=null){
                if (mDoorAnimationLayout.getVisibility() == View.GONE) {
                    mHandler.sendEmptyMessage(1);
                } else if (mDoorAnimationLayout.getVisibility() == View.VISIBLE) {
                    mHandler.sendEmptyMessage(0);
                }
            }
        }
    };


    //门扣动画 start
    private final class DoorStart implements Runnable {

        public void run() {
            final float centerX = hpDoorCircle.getWidth() / 2.0f;
            final float centerY = hpDoorCircle.getHeight() / 2.0f;
            Rotate3dAnimation rotation = null;
            hpDoorCircle.requestFocus();
            rotation = new Rotate3dAnimation(0, 15, centerX, centerY-deviationY, 0f, false);
            rotation.setDuration(180);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            rotation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    new DoorEnd().run();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            // 开始动画
            hpDoorCircle.startAnimation(rotation);
        }
    }

    //门扣动画 end
    private final class DoorEnd implements Runnable {

        public void run() {
            final float centerX = hpDoorCircle.getWidth() / 2.0f;
            final float centerY = hpDoorCircle.getHeight() / 2.0f;
            Rotate3dAnimation rotation = null;
            hpDoorCircle.requestFocus();
            rotation = new Rotate3dAnimation(15, 0, centerX, centerY-deviationY, 0f,false);
            rotation.setDuration(180);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());
            // 开始动画
            hpDoorCircle.startAnimation(rotation);
        }
    }

    //震动milliseconds毫秒
    public void vibrate(final Activity activity, long milliseconds) {
        if(mVibrator==null){
            mVibrator = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        }
        mVibrator.vibrate(milliseconds);
    }

    //取消震动
    public void virateCancle(final Activity activity){
        if(mVibrator==null){
            mVibrator = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        }
        mVibrator.cancel();
    }

    /**
     * 适合播放声音短，文件小
     * 可以同时播放多种音频
     * 消耗资源较小
     */
    public void playSound(int rawId) {
        /*if (Build.VERSION.SDK_INT >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            //传入音频的数量
            builder.setMaxStreams(1);
            //AudioAttributes是一个封装音频各种属性的类
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            //设置音频流的合适属性
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        } else {
            //第一个参数是可以支持的声音数量，第二个是声音类型，第三个是声音品质
            soundPool = new SoundPool(1, AudioManager.STREAM_SYSTEM, 5);
        }
        //第一个参数Context,第二个参数资源Id，第三个参数优先级
        soundPool.load(mContext.getApplicationContext(), rawId, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
//                soundPool.play(1, 1, 1, 0, 0, 1);
                isRedyPlay = true;
            }
        });
        soundPool.autoPause();
        soundPool.autoResume();
        //第一个参数id，即传入池中的顺序，第二个和第三个参数为左右声道，第四个参数为优先级，第五个是否循环播放，0不循环，-1循环
        //最后一个参数播放比率，范围0.5到2，通常为1表示正常播放
//        soundPool.play(1, 1, 1, 0, 0, 1);
        //回收Pool中的资源
//        soundPool.release();
*/
    }

    public void onclickHome(){
        if(currentPage==0){
            return;
        }
        mOnBottomBarViewClickLisener.OnTabClick(currentPage,0);
        hpVpRgZhizong.setSelected(true);
        hpVpRgZongquan.setSelected(false);
        hpVpRgNews.setSelected(false);
        hpVpRgMe.setSelected(false);
        currentPage = 0;
    }

    public void onclickContact(){
        if(currentPage==1){
            return;
        }
        mOnBottomBarViewClickLisener.OnTabClick(currentPage,1);
        hpVpRgZhizong.setSelected(false);
        hpVpRgZongquan.setSelected(true);
        hpVpRgNews.setSelected(false);
        hpVpRgMe.setSelected(false);
        currentPage = 1;
    }

    public void onclickNews(){
        if(currentPage==2){
            return;
        }
        mOnBottomBarViewClickLisener.OnTabClick(currentPage,2);
        hpVpRgZhizong.setSelected(false);
        hpVpRgZongquan.setSelected(false);
        hpVpRgNews.setSelected(true);
        hpVpRgMe.setSelected(false);
        currentPage =2;
    }

    public void onclickMy(){
        if(currentPage==3){
            return;
        }
        mOnBottomBarViewClickLisener.OnTabClick(currentPage,3);
        hpVpRgZhizong.setSelected(false);
        hpVpRgZongquan.setSelected(false);
        hpVpRgNews.setSelected(false);
        hpVpRgMe.setSelected(true);
        currentPage = 3;
    }

}
