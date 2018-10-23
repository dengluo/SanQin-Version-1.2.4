package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CustomPopView;

/**
 * Created by pbids903 on 2017/12/25.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:17
 * @desscribe 类描述:pop的http请求加载中
 * @remark 备注:现在都改为dialog
 * @see
 */
public class MeLoadingPop extends CustomPopView {
    TextView textView;
    public MeLoadingPop(Context context,String text) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.pop_me_loading,contentContainer);
        textView = (TextView) view.findViewById(R.id.pop_me_loading_tv);
        textView.setText(text);
    }

    public void setTextView(String textView){
        this.textView.setText(textView);
    }

}
