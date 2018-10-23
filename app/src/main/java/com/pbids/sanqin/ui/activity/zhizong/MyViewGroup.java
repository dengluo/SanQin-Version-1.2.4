package com.pbids.sanqin.ui.activity.zhizong;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by pbids903 on 2018/3/26.
 */

public class MyViewGroup extends ViewGroup{
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
//        super.onLayout(b,i,i1,i2,i3);
    }
}
