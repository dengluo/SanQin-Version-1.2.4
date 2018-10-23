package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.SelfAbsoluteLayout;
import com.pbids.sanqin.common.CustomPopView;
import com.pbids.sanqin.utils.AppUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:26
 * @desscribe 类描述:姓氏认证-手写板
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.AuthenticationFragment
 */
public class WacomView extends CustomPopView implements View.OnClickListener,SelfAbsoluteLayout.OnWriteChangeLisener {
    View mWacomViewLayout;

    ImageView wacomBg;

    TextView writeTv1;
    TextView writeTv2;
    TextView writeTv3;
    TextView writeTv4;
    TextView writeTv5;
    EditText writeNameEt;
    Button writeKeyboard;
    ImageView writeClearName;
    TextView writeClearHand;
    Button writeComfirm;
    SelfAbsoluteLayout selfAbsoluteLayout;

    public String surname;

    public WacomView(Context context) {
        super(context);
    }

    @Override
    public void childInit() {
        //ToDo OutOfMemoryError 低版本手机会抛出
        try {
            super.childInit();
            mWacomViewLayout = LayoutInflater.from(getContext()).inflate(R.layout.app_write_name_window, contentContainer);
            initView();
            initEvent();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void initView(){
        wacomBg = (ImageView) mWacomViewLayout.findViewById(R.id.write_name_wacom_bg);

        writeTv1 = (TextView) mWacomViewLayout.findViewById(R.id.app_write_name_tv1);
        writeTv2 = (TextView) mWacomViewLayout.findViewById(R.id.app_write_name_tv2);
        writeTv3 = (TextView) mWacomViewLayout.findViewById(R.id.app_write_name_tv3);
        writeTv4 = (TextView) mWacomViewLayout.findViewById(R.id.app_write_name_tv4);
        writeTv5 = (TextView) mWacomViewLayout.findViewById(R.id.app_write_name_tv5);
        writeNameEt = (EditText) mWacomViewLayout.findViewById(R.id.write_name_et);
        writeKeyboard = (Button) mWacomViewLayout.findViewById(R.id.write_name_keyboard);
        writeClearName = (ImageView) mWacomViewLayout.findViewById(R.id.write_name_clear_im);
        writeClearHand = (TextView) mWacomViewLayout.findViewById(R.id.write_name_clear_tv);
        writeComfirm = (Button) mWacomViewLayout.findViewById(R.id.write_name_comfirm);
        selfAbsoluteLayout = (SelfAbsoluteLayout) mWacomViewLayout.findViewById(R.id.write_layout);
        selfAbsoluteLayout.setOnWriteChangeLisener(this);
//        writeNameEt.addTextChangedListener(new ChinaWather(writeNameEt));

        InputFilter switchFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String regEx = "^[\\u4e00-\\u9fa5]*$";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(source.toString());
                if(m.matches()){
                    return source.toString();
                }else {
                    return "";
                }
            }
        };
        writeNameEt.setFilters(new InputFilter[]{switchFilter,new InputFilter.LengthFilter(2)});
    }

    public void initEvent(){
        Glide.with(getContext()).load(R.drawable.write_name_hand_bg).into(wacomBg);

        writeComfirm.setOnClickListener(this);
        writeClearHand.setOnClickListener(this);
        writeKeyboard.setOnClickListener(this);
        writeTv1.setOnClickListener(this);
        writeTv2.setOnClickListener(this);
        writeTv3.setOnClickListener(this);
        writeTv4.setOnClickListener(this);
        writeTv5.setOnClickListener(this);
        writeClearName.setOnClickListener(this);
    }

    public String getSurname() {
        return writeNameEt.getText().toString().trim();
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    private void inputSurname(TextView textView){
        if("".equals(textView.getText())){
            return;
        }
        String surname = ""+writeNameEt.getText()+textView.getText();
        writeNameEt.setText(surname);
        //textView.setText("");
        selfAbsoluteLayout.reset_recognize();
        //设置光标
        writeNameEt.setSelection(writeNameEt.getText().toString().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.app_write_name_tv1:
                inputSurname(writeTv1);
                /*if(!"".equals(writeTv1.getText())){
                    writeNameEt.setText(""+writeNameEt.getText()+writeTv1.getText());
                    writeTv1.setText("");
                    selfAbsoluteLayout.reset_recognize();
                }*/
                break;
            case R.id.app_write_name_tv2:
                inputSurname(writeTv2);
                /*if(!"".equals(writeTv2.getText())){
                    writeNameEt.setText(""+writeNameEt.getText()+writeTv2.getText());
                    writeTv2.setText("");
                    selfAbsoluteLayout.reset_recognize();
                }*/
                break;
            case R.id.app_write_name_tv3:
                inputSurname(writeTv3);
                /*if(!"".equals(writeTv3.getText())){
                    writeNameEt.setText(""+writeNameEt.getText()+writeTv3.getText());
                    writeTv3.setText("");
                    selfAbsoluteLayout.reset_recognize();
                }*/
                break;
            case R.id.app_write_name_tv4:
                inputSurname(writeTv4);
                /*if(!"".equals(writeTv4.getText())){
                    writeNameEt.setText(""+writeNameEt.getText()+writeTv4.getText());
                    writeTv4.setText("");
                    selfAbsoluteLayout.reset_recognize();
                }*/
                break;
            case R.id.app_write_name_tv5:
                inputSurname(writeTv5);
               /* if(!"".equals(writeTv5.getText())){
                    writeNameEt.setText(""+writeNameEt.getText()+writeTv5.getText());
                    writeTv5.setText("");
                    selfAbsoluteLayout.reset_recognize();
                }*/
                break;
            case R.id.write_name_keyboard:
//                hideKeyBoard();
                AppUtils.showSoftInputFromWindow(getContext());
                break;
            case R.id.write_name_clear_im:
                if(!"".equals(writeNameEt.getText())){
                    writeNameEt.setText("");
                }
                break;
            case R.id.write_name_clear_tv:
                //清除
                selfAbsoluteLayout.reset_recognize();
                break;
            case R.id.write_name_comfirm:
                if("".equals(writeNameEt.getText().toString().trim())){
                    Toast.makeText(getContext(),"请输入姓氏",Toast.LENGTH_SHORT).show();
                    return;
                }
                onConfirmClickLisenear.onConfirmClick();
                break;
            default:
                break;
        }
    }

    OnConfirmClickLisenear onConfirmClickLisenear;

    public void setOnConfirmClickLisenear(OnConfirmClickLisenear onConfirmClickLisenear){
        this.onConfirmClickLisenear = onConfirmClickLisenear;
    }

    @Override
    public void writeChange(char[] mResult) {
        //Log.i("wzh","writeChange=========");
        if(null == mResult || mResult.length ==0){
            return;
        }
        for(int j=0;j<mResult.length;j++){
            //Log.i("wzh","write: "+String.valueOf(mResult[j]));
        }
        for(int i=0;i<5;i++){
            String text  = String.valueOf(mResult[i]);
            switch (i){
                case 0:
                    writeTv1.setText(text);
                    break;
                case 1:
                    writeTv2.setText(text);
                    break;
                case 2:
                    writeTv3.setText(text);
                    break;
                case 3:
                    writeTv4.setText(text);
                    break;
                case 4:
                    writeTv5.setText(text);
                    break;
                default:
                    break;
            }
        }
    }

    public interface OnConfirmClickLisenear {
        void onConfirmClick();
    }

    class ChinaWather implements TextWatcher {

        //监听改变的文本框
        private EditText editText;


        public ChinaWather(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {
            String editable = editText.getText().toString();
            if (stringFilter(editable)) {
                editText.setText(editable);
                //设置新的光标所在位置
                editText.setSelection(editable.length());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {


        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

    }

    public static boolean stringFilter(String str) throws PatternSyntaxException {
        // 只允许字母和数字
        String regEx = "^[\\u4e00-\\u9fa5]*$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        Log.i("wzh","m.matches(): "+m.matches());
        return m.matches();
    }
}
