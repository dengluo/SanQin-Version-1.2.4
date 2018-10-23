package com.pbids.sanqin.component;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 输入金额   保留两位小数
 */

public class AmountET extends android.support.v7.widget.AppCompatEditText  implements View.OnFocusChangeListener, TextWatcher {

    public AmountET(Context context) {
        super(context);
        init();
    }

    // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
    public AmountET(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public AmountET(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.addTextChangedListener(this);
        //限制输入类型
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        int selectionStart = getSelectionStart();
        int selectionEnd = getSelectionEnd();
        if (!isOnlyPointNumber(editable.toString()) && editable.length() > 0) {
            //删除多余输入的字（不会显示出来）
            editable.delete(selectionStart - 1, selectionEnd);
            setText(editable);
            setSelection(editable.length());
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    //验证输入合法 0.33 保留两位小数
    protected boolean isOnlyPointNumber(String number) {
        Pattern pattern = Pattern.compile("^\\d+\\.?\\d{0,2}$");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }
    //是否通过
    public boolean checkPass(){
        return isOnlyPointNumber(getText().toString());
    }


}
