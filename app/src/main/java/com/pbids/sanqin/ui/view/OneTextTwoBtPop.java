package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CustomPopView;

/**
 * Created by pbids903 on 2017/12/19.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:19
 * @desscribe 类描述:一个textview两个按钮的pop
 * @remark 备注:
 * @see
 */
public class OneTextTwoBtPop extends CustomPopView {
    TextView textView;
    Button cancelButton;
    Button comfirmlButton;
    LinearLayout cancelLin;
    String text;


    public OneTextTwoBtPop(Context context,String text) {
        super(context);
        this.text = text;
        initView(context);
    }

    public void initView(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.pop_one_text_two_button, contentContainer);
        textView = (TextView) view.findViewById(R.id.textView);
        cancelButton = (Button) findViewById(R.id.two_button_one);
        comfirmlButton = (Button) findViewById(R.id.two_button_two);
        cancelLin = (LinearLayout) findViewById(R.id.cancel_lin);
        if(text!=null){
            textView.setText(text);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLisenear.cancel();
            }
        });
        comfirmlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLisenear.comfirm();
            }
        });
    }

    public void setContentText(String content){
        textView.setText(content);
    }

    public OnOneTextTwoBtPopClickLisenear mLisenear;

    public void setOnOneTextTwoBtPopClickLisenear(OnOneTextTwoBtPopClickLisenear lisenear){
        this.mLisenear = lisenear;
    }

    public interface OnOneTextTwoBtPopClickLisenear{
        public void comfirm();
        public void cancel();
    }

    public LinearLayout getCancelLin() {
        return cancelLin;
    }
}
