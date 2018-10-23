package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.ui.activity.me.InviteTips;

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
public class OneTextTwoBtDialog extends BaseDialog {

    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.two_button_one)
    Button twoButtonOne;
    @Bind(R.id.two_button_two)
    Button twoButtonTwo;

    public OneTextTwoBtDialog(@NonNull Context context) {
        super(context);
    }

    @Override
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
                if(onDialogClickLisenrar!=null){
                    onDialogClickLisenrar.cancel(view);
                }
                break;
            case R.id.two_button_two:
                if(onDialogClickLisenrar!=null){
                    onDialogClickLisenrar.confirm(view);
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

    public TextView getTextView() {
        return textView;
    }

    public Button getTwoButtonOne() {
        return twoButtonOne;
    }

    public Button getTwoButtonTwo() {
        return twoButtonTwo;
    }

    private OnDialogClickListener onDialogClickLisenrar;

    public void setOnDialogClickLisenrar(OnDialogClickListener onDialogClickLisenrar){
        this.onDialogClickLisenrar = onDialogClickLisenrar;
    }

}
