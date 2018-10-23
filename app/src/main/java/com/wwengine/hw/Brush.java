package com.wwengine.hw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
//import android.graphics.Paint.Cap;
//import android.graphics.Paint.Join;
//import android.graphics.Paint.Style;

//import android.util.Log;

public class Brush
{
  public static final int FUDE_MODE_DOWN = 0;
  public static final int FUDE_MODE_MOVE = 2;
  public static final int FUDE_MODE_UP = 1;
  private static final int FUDE_MODE_DOWN2 = 3;	// 只能内部用的 pen 消息

  private float LastStep = 0.0F;
  private int mLastWidth;
  private float mLatestPointX;
  private float mLatestPointY;
  private int mMaxWidth = 20;
  private int mMinWidth = 5;
  private Paint mPaint;
  private int mSpeedStep = 40;
  private float mUpLastX;
  private float mUpLastY;
  private boolean mMoving;
  private int mDotCount = 0;

  private void DrawPenDot(float paramFloat1, float paramFloat2, float paramFloat3, double paramDouble, Bitmap mBitmap, Canvas paramCanvas)
  {
	  Brush mBrush = new Brush();
	  
	  if ((mDotCount % 3) == 0)
	  {
		  mBrush.initReference(mMaxWidth, mMinWidth, mSpeedStep/2, mPaint.getColor());	  
		  mBrush.dispatchDrawEvent(paramFloat1, paramFloat2, FUDE_MODE_DOWN2, mBitmap);
		  mBrush.dispatchDrawEvent(paramFloat1-paramFloat3, paramFloat2-paramFloat3, FUDE_MODE_MOVE, mBitmap);
		  mBrush.dispatchDrawEvent(paramFloat1-paramFloat3, paramFloat2-(paramFloat3/2), FUDE_MODE_UP, mBitmap);
	  }
	  else if ((mDotCount % 2) == 0)
	  {
		  mBrush.initReference(mMaxWidth, mMinWidth, mSpeedStep/4, mPaint.getColor());	  
		  mBrush.dispatchDrawEvent(paramFloat1, paramFloat2, FUDE_MODE_DOWN2, mBitmap);
		  mBrush.dispatchDrawEvent(paramFloat1-paramFloat3, paramFloat2-paramFloat3, FUDE_MODE_MOVE, mBitmap);
		  mBrush.dispatchDrawEvent(paramFloat1-paramFloat3, paramFloat2-(paramFloat3/2), FUDE_MODE_UP, mBitmap);
	  }
	  else
	  {
		  mBrush.initReference(mMaxWidth, mMinWidth, mSpeedStep, mPaint.getColor());	  
		  mBrush.dispatchDrawEvent(paramFloat1, paramFloat2, FUDE_MODE_DOWN2, mBitmap);
		  mBrush.dispatchDrawEvent(paramFloat1-paramFloat3, paramFloat2-paramFloat3, FUDE_MODE_MOVE, mBitmap);
		  mBrush.dispatchDrawEvent(paramFloat1-paramFloat3, paramFloat2, FUDE_MODE_UP, mBitmap);
	  }
	  mDotCount++;
  }

  private void DrawPenTrack(float f, float f1, int i, float f2, float f3, int j, Canvas canvas)
  {
		int k = i;
		int l = j;
		float f4 = f - f2;
		float f5 = f1 - f3;
		int j1 = (int)Math.abs(f4);
		int k1 = (int)Math.abs(f5);
		float f6;
		float f8;
		int l1;
		float f9;
		float f13;
		float f14;
		float f15;
		int i2;
		float f7;
		float f10;
		float f11;
		float f12;
		float f16;
		float f17;
		int j2;
		int k2;
				
	    if (j1 > k1)
		{
			if (f4 > 0.0F)
				f6 = 1.0F;
			else
				f6 = -1.0F;
			f7 = j1;
			f8 = f5 / f7;
			l1 = j1;
		} 
		else
		{
			if (f5 > 0.0F)
				f8 = 1.0F;
			else
				f8 = -1.0F;
			if (f4 == 0.0F)
			{
				f6 = 0.0F;
			} 
			else
			{
				float f18 = k1;
				f6 = f4 / f18;
			}
			l1 = k1;
		}
		if (l1 <= 0)
			return;
		f4 = f2;
		f5 = f3;
		f9 = j;
		f10 = l1;
		f11 = i;
		f12 = j;
		f13 = (f11 - f12) / f10;
		f14 = mLatestPointX;
		f15 = mLatestPointY;

		f16 = f14;
		f17 = f15;
		
		Paint paint;
		paint = mPaint;

		i2 = 0;
		do
		{
			j2 = i2;
			k2 = l1;
			if (j2 >= k2)
			{
				return;
			}
			f4 += f6;
			f5 += f8;
			f9 += f13;
			float f19 = (int)f4;
			float f20 = (int)f5;

			float f21 = f9;
			
	        paint.setStrokeWidth(f21);
			canvas.drawLine(f19, f20, f14, f15, paint);			

			f14 = f19;
			f15 = f20;
			i2++;
		} while (true);
  }


  public void dispatchDrawEvent(float X, float Y, int Action, Bitmap mBitmap)
  {
	  	Canvas canvas = new Canvas(mBitmap);
	  
		switch (Action)
		{
		    case FUDE_MODE_DOWN: // '\0'     //  pen down    FUDE_MODE_DOWN = 0;
	        {
			    float tWidth = mMinWidth;//mMaxWidth + (mMaxWidth/2-1);
			    DrawPenDot(X, Y, tWidth, 159.0f, mBitmap, canvas);
			    mLatestPointX = X;
			    mLatestPointY = Y;
			    mLastWidth = mMaxWidth;
			    return;
	        }
	        
		    case FUDE_MODE_DOWN2:
		    {
			    mLatestPointX = X;
			    mLatestPointY = Y;
			    mLastWidth = mMaxWidth;
		    	return;
		    }

		    case FUDE_MODE_MOVE: // '\002'   // pen move     FUDE_MODE_MOVE = 2;
	        {
			    float f8 = X - mLatestPointX;		
			    float f11 = f8 * f8;		    
			    float f13 = Y - mLatestPointY;
			    float f15 = Y - mLatestPointY;
			    float f16 = f13 * f15;
			    float f17 = f11 + f16;
			    float f19 = f17 / mSpeedStep;
			    float f20 = (float)mMaxWidth - f19;
			    float f21 = mMaxWidth;
			    float f29;
			    int k;
			    float f30;
			    float f31;
			    int l;

			    //Canvas canvas2;
			    if (f20 > f21)
			    {
				    f20 = mMaxWidth;
			    } else
			    {
				    float f34 = mMinWidth;
				    if (f20 < f34)
				    {
					    f20 = mMinWidth;
					    float f35 = mMinWidth;
					    if (f20 < f35)
						    f20 = mMinWidth;
				    }
			    }
			    if (LastStep + f17 > 0.0F)
			    {
				    float f22 = f20 * f17;
				    float f23 = mLastWidth * 3;
				    float f24 = LastStep;
				    float f25 = f23 * f24;
				    float f26 = f22 + f25;
				    float f27 = LastStep;
				    float f28 = 3.0f * f27 + f17;
				    f29 = (int)(f26 / f28);
			    }
				else
			    {
				    f29 = ((float)(mLastWidth * 3) + f20) / 4.0f;
			    }
			    k = (int)f29;
			    f30 = mLatestPointX;
			    f31 = mLatestPointY;
			    l = mLastWidth;

			    DrawPenTrack(X, Y, k, f30, f31, l, canvas);
			    mLastWidth = (int)f29;
			    
	            LastStep = f17;
			    
	            mUpLastX = mLatestPointX;
			    mUpLastY = mLatestPointY;

			    mLatestPointX = X;
			    mLatestPointY = Y;
	            mMoving = true;
			    return;
	        }

		    case FUDE_MODE_UP: // '\001'   // pen up       FUDE_MODE_UP = 1;
	        {
	            if (mMoving)
	            {
			        float tX = X + (2.0F * (X - mUpLastX));
			        float tY = Y + (2.0F * (Y - mUpLastY));
			        DrawPenTrack(tX, tY, 1, X, Y, mLastWidth, canvas);
	            }
	            mMoving = false;
			    return;
	        }

		    default:
		    {
			    return;
		    }
		}
  }

  public  void initReference(int maxWidth, int minWidth, int speed,int Color)
  {
    mMaxWidth = maxWidth;
    mMinWidth = minWidth;
    mSpeedStep = speed;
    mMoving = false;

    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setDither(true);
    mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    mPaint.setStrokeJoin(Paint.Join.ROUND);
    mPaint.setStrokeCap(Paint.Cap.ROUND);
    mPaint.setColor(Color);
  }

}
