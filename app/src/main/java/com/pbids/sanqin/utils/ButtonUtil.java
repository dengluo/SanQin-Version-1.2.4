package com.pbids.sanqin.utils;

import android.widget.Button;

import com.pbids.sanqin.R;

/**
 * Created by pbids903 on 2017/12/28.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:28
 * @desscribe 类描述:按钮button工具
 * @remark 备注:
 * @see
 */
public class ButtonUtil {
    //设置按钮不可点击
    public static void setOnClickFalse(Button button){
        if(button.isClickable()){
            button.setClickable(false);
            button.setBackgroundResource(R.drawable.selector_app_cancel);
        }
    }
    //设置按钮为可以点击
    public static void setOnClickTrue(Button button){
        if(!button.isClickable()){
            button.setClickable(true);
            button.setBackgroundResource(R.drawable.selector_app_comfirm);
        }
    }
}
