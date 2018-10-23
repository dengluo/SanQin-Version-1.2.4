package com.pbids.sanqin.ui.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.pbids.sanqin.R;

import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/1/14.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:07
 * @desscribe 类描述:基础Dialog
 * @remark 备注:
 * @see
 */
public abstract class BaseDialog extends Dialog{

    public BaseDialog(@NonNull Context context) {
        super(context, R.style.DialogFullScreen);
        initView();
    }

    abstract void  initView();

    public void setGrayBottom(){
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attr = window.getAttributes();
        if (attr != null) {
            attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            attr.width = ViewGroup.LayoutParams.MATCH_PARENT;
            attr.gravity = Gravity.BOTTOM;//设置dialog 在布局中的位置

            window.setAttributes(attr);
        }
    }

    public void setGrayCenter(){
        Window window = this.getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams attr = window.getAttributes();
        if (attr != null) {
            attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            attr.width = ViewGroup.LayoutParams.MATCH_PARENT;
            attr.gravity = Gravity.CENTER;//设置dialog 在布局中的位置

            window.setAttributes(attr);
        }
    }

    public void setBottomUpAnimation(){
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.DialogBottom);
    }
}
