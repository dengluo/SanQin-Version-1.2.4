package com.pbids.sanqin.model.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:44
 * @desscribe 类描述:svg，中国地图各个省份的线条对象
 * @remark 备注:
 * @see
 */
public class Provice {
    /**
     * 绘制路径
     */
    protected Path path;
    /**
     * 绘制颜色
     */
    private  int drawColor = 0xFFFFE5CC;  //默认黄色

    private static int Mleft=0, Mright=0, Mtop=0, Mbotton=0;

    public Provice(Path path) {
        this.path = path;
    }

    public void draw(Canvas canvas, Paint paint, boolean isSelect) {
        if (isSelect) {
            //选中时，绘制描边效果
            paint.setStrokeWidth(2);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setShadowLayer(8,0,0,0xffffff);
            canvas.drawPath(path,paint);

            //选中时，绘制地图
            paint.clearShadowLayer();
            paint.setColor(drawColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setStrokeWidth(2);
            canvas.drawPath(path, paint);
        }else {
            //非选中时，绘制描边效果
            paint.clearShadowLayer();
            paint.setStrokeWidth(1);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(drawColor);
            canvas.drawPath(path, paint);

            //非选中时，绘制地图
            paint.setStyle(Paint.Style.STROKE);
            int strokeColor = 0xFFFFFFFF;
            paint.setColor(strokeColor);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * 是否被选中
     */
    public boolean isSelect(int x, int y) {
        //构造一个区域对象
        RectF rectF = new RectF();
//        计算控制点的边界
        path.computeBounds(rectF,true);
        Region region = new Region();
        region.setPath(path, new Region((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom));

//        if (rectF.left > Mleft) {
//            Mleft = (int)rectF.left;
//        }
//        if (rectF.top > Mtop) {
//            Mtop = (int)rectF.top;
//        }
//        if (rectF.right > Mright) {
//            Mright = (int)rectF.right;
//        }
//        if (rectF.bottom > Mbotton) {
//            Mbotton = (int)rectF.bottom;
//        }
//        System.out.println(Mleft + " " + Mtop + " " + Mright + " " + Mbotton);
        return region.contains(x,y);
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        this.drawColor = drawColor;
    }
}
