package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.pbids.sanqin.ui.adapter.BaseViewPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:13
 * @desscribe 类描述:自动轮播的viewpager
 * @remark 备注:
 * @see
 */
public class AutoViewPager extends ViewPager {

    private static final String TAG = "AutoViewPager";
    private static final int TIME = 3000;

    private Timer mTimer;
    private AutoTask mTask;
    private boolean isStart;
    private int currentItem;

    public boolean isStart() {
        return isStart;
    }

    public AutoViewPager(Context context) {
        this(context,null);
    }

    public AutoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(AutoViewPager viewPager,BaseViewPagerAdapter adapter){
        adapter.init(viewPager,adapter);
    }

    public void start(){

        if (((BaseViewPagerAdapter)getAdapter()).getRealCount() == 0) {
            return;
        }
        //先停止
        onStop();
        doStart();

    }

    private void doStart() {
        isStart = true;
        if (mTimer == null) {
            mTimer = new Timer();
        }
        mTimer.schedule(new AutoTask(),TIME,TIME);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            changePageView();
        }
    };

    private void changePageView() {
        currentItem = getCurrentItem();
        if(getAdapter()==null){
            return;
        }
        if(currentItem == getAdapter().getCount() - 1){
            currentItem = 0 ;
        }else {
            currentItem++ ;
        }
        setCurrentItem(currentItem);
        //更新小圆点状态
        int nextItem = (currentItem % ((BaseViewPagerAdapter)getAdapter()).getRealCount());
        onPageSelected(nextItem);
    }

    private AutoHandler mHandler = new AutoHandler();

    public void updatePointView(int size) {
        updatePointView(size,0);
    }

    public void updatePointView(int size,int currentItem) {
        if (getParent() instanceof AutoScrollViewPager){
            AutoScrollViewPager pager = (AutoScrollViewPager) getParent();
            pager.initPointView(size,currentItem);
        }else {
            Log.e(TAG,"parent view_donate_records not be AutoScrollViewPager");
        }
    }

    public void onPageSelected(int position) {
        if (getParent() == null) {
            return;
        }

        if (getParent() instanceof AutoScrollViewPager) {
            AutoScrollViewPager pager = (AutoScrollViewPager) getParent();
            pager.updatePointView(position);
        }
    }

    public void onStop(){
        isStart = false;
        //先取消定时器
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void onDestroy(){
        onStop();
    }

    public void onResume(){
        start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                onResume();
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //onTouchEvent中无法拦截到ACTION_DOWN的动作
//                onStop();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public TextView getSubTitle() {
        if (getParent() instanceof AutoScrollViewPager){
            AutoScrollViewPager pager = (AutoScrollViewPager) getParent();
            return pager.getSubTitle();
        }

        return null;
    }

    private class AutoTask extends TimerTask{
        @Override
        public void run() {
            mHandler.post(runnable);
        }
    }

    private final static class AutoHandler extends android.os.Handler{}

}
