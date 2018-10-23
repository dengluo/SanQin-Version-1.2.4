package com.pbids.sanqin.utils;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 巫哲豪
 * @date on 2018/3/7 15:43
 * @desscribe 类描述:
 * @remark 备注:
 * @see
 */

public class MemoryReleaseUtil {
//    public static void releaseRelease(View view){
//        if(view instanceof ImageView){
//            ImageView imageView = (ImageView) view;
//            imageView.setImageResource(0);
//            imageView.setBackgroundResource(0);
//            imageView.setBackground(null);
//            imageView.clearAnimation();
//        }
//
//        view = null;
//    }

    public static void releaseRelease(Object... objects){
        for(int i=0;i<objects.length;i++){
            Object object = objects[i];
            if(object==null){
                continue;
            }
            if(object instanceof ImageView){
                ImageView imageView = (ImageView) object;
                imageView.setImageResource(0);
                imageView.setBackgroundResource(0);
                imageView.setBackground(null);
                imageView.clearAnimation();
            }else if(object instanceof Bitmap){
                Bitmap bitmap = (Bitmap) object;
                bitmap.recycle();
            }else if(object instanceof AnimationDrawable){
                AnimationDrawable animationDrawable = (AnimationDrawable) object;
                animationDrawable.stop();
            }else if(object instanceof TextView){
                TextView textView = (TextView) object;
                textView.setBackgroundResource(0);
                textView.setBackgroundResource(0);
                textView.setBackground(null);
                textView.clearAnimation();
            }else if(object instanceof ObjectAnimator){
                ObjectAnimator objectAnimator = (ObjectAnimator) object;
                objectAnimator.cancel();
            }

            object = null;
        }
    }
}
