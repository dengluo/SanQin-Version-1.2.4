package com.pbids.sanqin.common;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:37
 * @desscribe 类描述:3d旋转动画
 * @remark 备注:现在好像没有被使用
 * @see
 */
public class Rotate3dAnimation extends Animation {
  
    //开始角度  
    private float startDegree;  
    //结束角度  
    private float endDegree;  
  
    /** 
     * 这个旋转动画围绕在2D空间的中心点执行.你可以用X轴坐标(叫做centerX)和Y轴(叫做centerY) 
     * 坐标来定义这个中心点 
     */  
    private float centerX;  
    private float centerY;  
    /** 
     * 控制镜头景深，不需要的话给0值即可 
     * mReverse 为true，表示反方向，false 表示正方向 
     */  
    private float deepZ;  
    private boolean mReverse;  
    //用于辅助实现3d效果。  
    private Camera mCamera;
  
    //X轴方向，或Y轴方向  
    enum DIRECTION {  
        X, Y  
    }  
  
    DIRECTION direction = DIRECTION.X;
  
    public Rotate3dAnimation(float fromDegree, float toDegree, float centerX,
                      float centerY, float deepZ, boolean reverse) {  
        this.startDegree = fromDegree;  
        this.endDegree = toDegree;  
        this.centerX = centerX;  
        this.centerY = centerY;  
        this.deepZ = deepZ;  
        this.mReverse = reverse;  
    }  
  
    Rotate3dAnimation(float fromDegree, float toDegree, float centerX,
                      float centerY, float deepZ, boolean reverse, DIRECTION direction) {  
        this.startDegree = fromDegree;  
        this.endDegree = toDegree;  
        this.centerX = centerX;  
        this.centerY = centerY;  
        this.deepZ = deepZ;  
        this.mReverse = reverse;  
        this.direction = direction;  
    }  
  
    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {  
        super.initialize(width, height, parentWidth, parentHeight);  
        mCamera = new Camera();
    }  
  
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);  
        float fromDegree = startDegree;  
        float degree = fromDegree + (endDegree - startDegree) * interpolatedTime;  
  
        final Matrix matrix = t.getMatrix();
  
        mCamera.save();  
        if (mReverse) {  
            mCamera.translate(0, 0, deepZ * interpolatedTime);  
        } else {  
            mCamera.translate(0, 0, deepZ * (1 - interpolatedTime));  
        }  
        if (direction == DIRECTION.Y) {  
            mCamera.rotateY(degree);  
        } else {  
            mCamera.rotateX(degree);  
        }  
        mCamera.getMatrix(matrix);  
        mCamera.restore();  
  
        matrix.preTranslate(-centerX, -centerY);  
        matrix.postTranslate(centerX, centerY);  
    }  
}  