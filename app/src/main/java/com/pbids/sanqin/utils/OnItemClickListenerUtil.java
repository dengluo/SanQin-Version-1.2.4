package com.pbids.sanqin.utils;

import android.view.View;

/**
 * Created by pbids903 on 2017/12/15.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:35
 * @desscribe 类描述:recyclerview的click工具
 * @remark 备注:
 * @see
 */
public class OnItemClickListenerUtil {

    public OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onClick(View v,int position);
        void onLongClick(View v,int position);
     }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this. mOnItemClickListener=onItemClickListener;
     }
}
