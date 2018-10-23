/*
 * View3.java
 * 
 * Version info
 * 
 * Create time
 * 
 * Last modify time
 * 
 * Copyright (c) 2010 FOXCONN Technology Group All rights reserved
 */
package com.pbids.sanqin;

import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;
import android.graphics.PorterDuff.Mode;

import com.wwengine.hw.Brush;
import com.wwengine.hw.WWHandWrite;


/**
 * View3.java
 * <p>
 */
/**
 * description
 * 
 * @author cairuizhi
 */
public class SelfAbsoluteLayout extends AbsoluteLayout 
{
    private static final String TAG = "SelfAbsoluteLayout";

    private float mx;
    private float my;
    private Path mPath;
    private Paint mPaint;
    private Paint mPaintText;
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private static char[]  mResult1;
    private static char[]  mResult2;
    private static short[] mTracks;
    private static int		mCount;   
    private Bitmap mScreenBitmap;
    private static boolean mBrushShow = true;
    private static Context mContext;
    private static int mFontSize;
    private static Brush mBrush;
    long endTime=0;
    
    /**
     * @param context
     */
    public SelfAbsoluteLayout(Context context) {
        super(context);
        init();
    }

    /**
     * @param context
     * @param attrs
     */
    public SelfAbsoluteLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        mFontSize = context.getResources().getDimensionPixelSize(R.dimen.dp_18);

        init();
        hw_init();
        
        if (mBrushShow)
        {
	        mScreenBitmap = Bitmap.createBitmap(
	        		context.getResources().getDisplayMetrics().widthPixels, 
	        		context.getResources().getDisplayMetrics().heightPixels, 
	        		Bitmap.Config.ARGB_8888);
	
	        mBrush = new Brush();
//	        mBrush.initReference(
//	        		context.getResources().getDimensionPixelSize(R.dimen.dp_20),
//	        		context.getResources().getDimensionPixelSize(R.dimen.dp_6),
//	        		context.getResources().getDimensionPixelSize(R.dimen.dp_40), 0xE9888DEA);
 	        mBrush.initReference(
	        		context.getResources().getDimensionPixelSize(R.dimen.dp_20),
	        		context.getResources().getDimensionPixelSize(R.dimen.dp_6),
	        		context.getResources().getDimensionPixelSize(R.dimen.dp_40), 0xFF000000);
        }
    }

    private void hw_init()
    {
		// ������д����
		byte[] hwData = readData(this.getContext().getAssets(), "hwdata.bin");
		if (hwData == null) {
			return;
		}
		
		// ��Ȩ��飬һ��Ҫ�� init ֮ǰ
		WWHandWrite.apkBinding(getContext());
		
        if (WWHandWrite.hwInit(hwData, 0) != 0)
        {
        	return;
        }

    	mResult1 = new char[256];
    	mResult2 = new char[256];
    	mTracks = new short[1024];
    	mCount = 0;
    }
    
    private void init() 
    {

        // why only set background then invalidate() valid
        this.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        
        if (!mBrushShow) {
	        mPath = new Path();
	        mPaint = new Paint();
	        mPaint.setAntiAlias(true);
	        mPaint.setDither(true);
//	        mPaint.setColor(0xFFFF0000);
	        mPaint.setColor(0xFF000000);
	        mPaint.setStyle(Paint.Style.STROKE);
	        mPaint.setStrokeJoin(Paint.Join.ROUND);
	        mPaint.setStrokeCap(Paint.Cap.ROUND);
	        mPaint.setStrokeWidth(6);
        }
        
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mPaintText.setColor(0xFFFF0000);
        mPaintText.setColor(0xFF000000);
        mPaintText.setTextSize(mFontSize);
        mPaintText.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) 
    {
        super.onDraw(canvas);
        if (mBrushShow){
            canvas.drawBitmap(mScreenBitmap, 0.0F, 0.0F, null);
        } else{
            canvas.drawPath(mPath, mPaint);
        }

        //canvas.drawText(mResult.toString(), 10, 100, mPaintText);
//        canvas.drawText(mResult1, 0, mResult1.length, 5, 20 + mFontSize/2, mPaintText);
//        canvas.drawText(mResult2, 0, mResult2.length, 5, 40 + mFontSize/2, mPaintText);

//        if(System.currentTimeMillis()-endTime>200 || endTime==0){
//            mOnWriteChangeLisener.writeChange(mResult1);
//            endTime = System.currentTimeMillis();
//        }
        mOnWriteChangeLisener.writeChange(mResult1);
    }

    private void touch_start(float x, float y) 
    {
    	if (mBrushShow)
    		mBrush.dispatchDrawEvent(x, y, Brush.FUDE_MODE_DOWN, mScreenBitmap);
    	else
    		mPath.moveTo(x, y);
        mX = x;
        mY = y;
        if((mCount+2)<mTracks.length){
            mTracks[mCount++] = (short)x;
            mTracks[mCount++] = (short)y;
        }
    }

    private void touch_move(float x, float y) 
    {
    	if (mBrushShow)
    	{
    		mBrush.dispatchDrawEvent(x, y, Brush.FUDE_MODE_MOVE, mScreenBitmap);
            mX = x;
            mY = y;
    	}
    	else
    	{
	        float dx = Math.abs(x - mX);
	        float dy = Math.abs(y - mY);
	        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) 
	        {
	        	mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
	            mX = x;
	            mY = y;
	        }
    	}
        if((mCount+2)<mTracks.length){
            mTracks[mCount++] = (short)x;
            mTracks[mCount++] = (short)y;
        }
    }

    private void touch_up() 
    {
    	if (mBrushShow)
    		mBrush.dispatchDrawEvent(mX, mY, Brush.FUDE_MODE_UP, mScreenBitmap);
    	else
    		mPath.lineTo(mX, mY);
        if((mCount+2)<mTracks.length){
            mTracks[mCount++] = -1;
            mTracks[mCount++] = 0;
        }
        recognize();
    }
    
    private void recognize() 
    {
    	short[] mTracksTemp;
    	int countTemp = mCount;

    	mTracksTemp = mTracks.clone(); 
    	mTracksTemp[countTemp++] = -1;
    	mTracksTemp[countTemp++] = -1;
  	
    	WWHandWrite.hwRecognize(mTracksTemp, mResult1, 10, 0x8000);	// ���� + ��д��ĸ
    }
    
    public void reset_recognize()
    {
    	mCount = 0;
    	mResult1 = new char[256];
    	mResult2 = new char[256];
    	if (mBrushShow)
    	{
            Canvas localCanvas = new Canvas(mScreenBitmap);
            localCanvas.drawColor(0, Mode.CLEAR);
    	}
    	else
    	{
    		mPath.reset();
    	}
    	invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) 
    {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) 
        {
        case MotionEvent.ACTION_DOWN:
            touch_start(x, y);
            invalidate();
            break;

        case MotionEvent.ACTION_MOVE:
            touch_move(x, y);
            invalidate();
            break;

        case MotionEvent.ACTION_UP:
            touch_up();
            invalidate();
            break;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    ////////////////////////////////////////////////////////////////
    //  readData
    ////////////////////////////////////////////////////////////////
    private static byte[] readData(AssetManager am, String name) {
		try {

			InputStream is = am.open(name);
			if (is == null) {
				return null;
			}

			int length = is.available();
			if (length <= 0) {
				return null;
			}

			byte[] buf = new byte[length];
			if (buf == null) {
				return null;
			}

			if (is.read(buf, 0, length) == -1) {
				return null;
			}

			is.close();
			
			return buf;

		} catch (Exception ex) {
			return null;
		}
	}

    private OnWriteChangeLisener mOnWriteChangeLisener;

    public interface OnWriteChangeLisener{
        void writeChange(char[]  mResult);
    }

    public void setOnWriteChangeLisener(OnWriteChangeLisener onWriteChangeLisener){
        this.mOnWriteChangeLisener = onWriteChangeLisener;
    }
}
