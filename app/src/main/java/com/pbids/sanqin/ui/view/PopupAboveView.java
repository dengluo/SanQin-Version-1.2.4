package com.pbids.sanqin.ui.view;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.pbids.sanqin.R;

/**
 * Created by pbids903 on 2017/11/17.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:21
 * @desscribe 类描述:登陆界面的更多弹窗
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.LoginPageFragment
 */
public class PopupAboveView extends PopupWindow {

    private int popupWidth;
    private int popupHeight;

    public PopupAboveView(Activity context, int size, boolean isInsurance) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_login_more, null);
        //获取自身的长宽高
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        ImageView imageView = (ImageView) view.findViewById(R.id.login_pop_bg);
        popupHeight = view.getMeasuredHeight();
        popupWidth = view.getMeasuredWidth();
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);
//        setWidth(popupWidth);
//        setHeight(popupHeight);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

//        setWidth((int)mContext.getResources().getDimension(R.dimen.dp_60));
//        setHeight((int)mContext.getResources().getDimension(R.dimen.dp_106));
        LinearLayout.LayoutParams layoutParams  = new LinearLayout.LayoutParams(
                (int)context.getResources().getDimension(R.dimen.dp_50)
                ,(int)context.getResources().getDimension(R.dimen.dp_106));
        imageView.setLayoutParams(layoutParams);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.nothink));
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.LoginPopupAnimation);
        setContentView(view);
    }
    public void showUp(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }
}
