package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.ui.view.pay.PayPsdInputView;
import com.pbids.sanqin.ui.view.pay.XNumberKeyboardView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/22.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:10
 * @desscribe 类描述:支付密码弹窗dialog
 * @remark 备注:
 * @see
 */
public class PayPasswordDialog extends BaseDialog implements XNumberKeyboardView.IOnKeyboardListener,PayPsdInputView.onPasswordListener {

    @Bind(R.id.pay_password_cancel)
    ImageView payPasswordCancel;
    @Bind(R.id.pay_password_payinputview)
    PayPsdInputView payPasswordPayinputview;
    @Bind(R.id.pay_password_keyboardview)
    XNumberKeyboardView payPasswordKeyboardview;
    Context mContext;

    public PayPasswordDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    void initView() {
        setContentView(R.layout.dialog_pay_password);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        setBottomUpAnimation();
        setGrayBottom();
        payPasswordKeyboardview.setIOnKeyboardListener(this);
        payPasswordPayinputview.setonPasswordListener(this);
    }

    @OnClick(R.id.pay_password_cancel)
    public void onViewClicked() {
        dismiss();
    }

    @Override
    public void onInsertKeyEvent(String text) {
        payPasswordPayinputview.append(text);
    }

    @Override
    public void onDeleteKeyEvent() {
        int start = payPasswordPayinputview.length() - 1;
        if (start >= 0) {
            payPasswordPayinputview.getText().delete(start, start + 1);
        }
    }

    public void clearPassword(){
        payPasswordPayinputview.setText("");
    }

    @Override
    public void onOver(String password) {
        if(onScanOverLisenear!=null){
            onScanOverLisenear.onOver(password);
        }
//        Toast.makeText(mContext,"密码："+password,Toast.LENGTH_SHORT).show();
    }

    OnScanOverLisenear onScanOverLisenear;

    public void setOnScanOverLisenear( OnScanOverLisenear onScanOverLisenear){
        this.onScanOverLisenear = onScanOverLisenear;
    }

    public interface OnScanOverLisenear{
        void onOver(String password);
    }
}