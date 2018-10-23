package com.pbids.sanqin.component;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.text.TextWatcher;

public class BankCardET extends android.support.v7.widget.AppCompatEditText implements View.OnFocusChangeListener,TextWatcher {

    public BankCardET(Context context) {
        super(context);
    }

    public BankCardET(Context context, AttributeSet attrs) {
        super(context,attrs);
        // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
        init();
    }

    public BankCardET(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();

    }

    private void init() {
        //限制输入类型
        this.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }
}
