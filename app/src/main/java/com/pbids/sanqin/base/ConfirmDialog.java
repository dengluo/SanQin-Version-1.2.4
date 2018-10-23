package com.pbids.sanqin.base;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.ui.view.dialog.BaseDialog;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:09
 * @desscribe 类描述:一个textview两个按钮dialog
 * @remark 备注:
 * @see
 */
public class ConfirmDialog extends Dialog implements ConfirmDialogView {

    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.two_button_one)
    Button twoButtonOne;
    @Bind(R.id.two_button_two)
    Button twoButtonTwo;

    public ConfirmDialog(@NonNull Context context) {
        super(context, R.style.DialogFullScreen);
        initView();
    }

    void initView() {
        setContentView(R.layout.pop_one_text_two_button);
        ButterKnife.bind(this);
        setGrayCenter();
        setCanceledOnTouchOutside(false);
    }

    @OnClick({R.id.two_button_one, R.id.two_button_two})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.two_button_one:
                if(confirmListener!=null){
                    confirmListener.cancel(this,view);
                }
                break;
            case R.id.two_button_two:
                if(confirmListener!=null){
                    confirmListener.confirm(this,view);
                }
                break;
        }
    }

    public void setComfirmText(String text){
        twoButtonTwo.setText(text);
    }

    public void setContentText(String content){
        textView.setText(content);
    }

    private ConfirmListener confirmListener;

    public void setOnDialogClickLisenrar(ConfirmListener confirmListener){
        this.confirmListener = confirmListener;
    }




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

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
