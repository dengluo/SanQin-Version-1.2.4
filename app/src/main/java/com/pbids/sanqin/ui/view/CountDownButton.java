package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by pbids903 on 2017/11/20.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:16
 * @desscribe 类描述:验证码自定义view
 * @remark 备注:
 * @see
 */
public class CountDownButton extends android.support.v7.widget.AppCompatTextView{

    CountDownTimer countDownTimer;

    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }

    public void startCountDown(){
        this.setClickable(false);
        if(countDownTimer == null){
            countDownTimer = new CountDownTimer(60000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    CountDownButton.this.setText("发送验证码(" +millisUntilFinished/1000+ ")");
                }

                @Override
                public void onFinish() {
                    CountDownButton.this.setText("发送验证码");
                    CountDownButton.this.setClickable(true);
                }
            };
        }
        countDownTimer.start();
    }
}
