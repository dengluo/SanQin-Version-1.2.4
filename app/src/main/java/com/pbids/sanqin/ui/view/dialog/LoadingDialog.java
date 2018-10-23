package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.pbids.sanqin.R;

/**
 * Created by pbids903 on 2018/1/14.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:08
 * @desscribe 类描述:调用http请求时，加载中布局
 * @remark 备注:
 * @see
 */
public class LoadingDialog extends BaseDialog{
    String text;
    TextView textView;
    public LoadingDialog(@NonNull Context context,String text) {
        super(context);
        this.text = text;
    }

    public void setText(String text){
        if(null!=text){
            textView.setText(text);
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.pop_me_loading);
        textView = (TextView) findViewById(R.id.pop_me_loading_tv);
        textView.setTextColor(getContext().getResources().getColor(R.color.white));
        if(text!=null){
            textView.setText(text);
        }
        this.setCanceledOnTouchOutside(false);
    }
}
