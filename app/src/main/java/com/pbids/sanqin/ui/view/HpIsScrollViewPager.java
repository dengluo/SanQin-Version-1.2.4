package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:17
 * @desscribe 类描述:主页可设置是否滑动的ViewPager类
 * @remark 备注:
 * @see
 */
public class HpIsScrollViewPager extends ViewPager {

    private boolean isScrollable=true;

    public void setScrollable(boolean isScrollable){
        this.isScrollable = isScrollable;
    }

    public HpIsScrollViewPager(Context context){
        super(context);
    }

    public HpIsScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }

}
