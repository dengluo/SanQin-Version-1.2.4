package com.pbids.sanqin.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.pbids.sanqin.R;

public class BadgeView extends View {

    //定义画笔
    private Paint textPaint;
    private Paint shapePaint;

    private int height;
    private int width;

    public BadgeView(Context context) {
        super(context);
        initView();
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 初始化画笔
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        height = getHeight();
        width = getWidth();

        shapePaint.setColor(getResources().getColor(R.color.authentication_success_text));
        canvas.drawCircle(width / 2, height / 2, Math.min(width, height) / 2, shapePaint);

    }
}
