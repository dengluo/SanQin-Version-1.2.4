package com.pbids.sanqin.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 输入金额   保留两位小数
 */

public class IdCardET extends android.support.v7.widget.AppCompatEditText  implements View.OnFocusChangeListener, TextWatcher {

    /**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";


    public IdCardET(Context context) {
        super(context);
        init();
    }

    // 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
    public IdCardET(Context context, AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public IdCardET(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.addTextChangedListener(this);
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

    //验证身份证
    protected boolean isOnlyPointNumber(String number) {
        Pattern pattern = Pattern.compile("(^\\d{0,18}$)|(^\\d{14,17}([0-9]|X|x)$)");
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    //是否通过
    public boolean checkPass(){
        return isOnlyPointNumber(getText().toString());
    }

    /**
     * 校验身份证
     *
     * @return 校验通过返回true，否则返回false
     */
    public boolean isIDCard() {
        String idCard =  getText().toString();
        if(idCard==null){
            return false;
        }
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }
}
