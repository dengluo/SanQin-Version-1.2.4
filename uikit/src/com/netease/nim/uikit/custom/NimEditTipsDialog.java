package com.netease.nim.uikit.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.R;

/**
 * Created by pbids920 on 2018/7/25.
 */

public class NimEditTipsDialog extends Dialog {

    private Context mContext;

    private TextView tipsDescTv;

    private LinearLayout editLin;

    private TextView editTitleTv;

    private EditText editEt;

    private Button cancleBt;

    private Button okBt;

    public NimEditTipsDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view  = View.inflate(mContext, R.layout.nim_custom_dialog_system_tips,null);
        //设置去除ActionBar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(view);
        initViews(view);
    }

    private void initViews(View view) {
        tipsDescTv = view.findViewById(R.id.min_custom_dialog_tips_desc);
        editLin = view.findViewById(R.id.min_custom_dialog_edit_lin);
        editTitleTv = view.findViewById(R.id.min_custom_dialog_title_tv);
        editEt = view.findViewById(R.id.min_custom_dialog_edit_et);
        cancleBt = view.findViewById(R.id.min_custom_dialog_cancle_bt);
        cancleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListenEvent.cancle(v);
            }
        });
        okBt = findViewById(R.id.min_custom_dialog_ok_bt);
        okBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonListenEvent.ok(v);
            }
        });
    }

    public void setTitle(String title){
        editTitleTv.setText(title);
    }

    public Button getCancle(){
        return cancleBt;
    }

    public void setOkBtTitle(String title){
        okBt.setText(title);
    }

    public String getEditContent(){
        return editEt.getText().toString().trim();
    }

    public void setHitContent(String tips){
        editEt.setHint(tips);
    }

    public static final int EDITINFO = 1;
    public static final int CHOSES = 0;

    public void setViewType(int type){
        if (type == EDITINFO) {
            editLin.setVisibility(View.VISIBLE);
        }else if (type == CHOSES){
            tipsDescTv.setVisibility(View.VISIBLE);
        }
    }

    public void setDescTitle(String title){
        tipsDescTv.setText(title);
    }

    public interface ButtonListenEvent{
        void cancle(View v);
        void ok(View v);
    }

    public ButtonListenEvent buttonListenEvent;

    public void setButtonListenEvent(ButtonListenEvent buttonListenEvent) {
        this.buttonListenEvent = buttonListenEvent;
    }
}
