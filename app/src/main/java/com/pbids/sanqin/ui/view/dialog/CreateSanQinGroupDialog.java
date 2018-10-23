package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.ui.adapter.SpinnerSanqinGroupAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:09
 * @desscribe 类描述: 创建三亲的分组弹窗
 * @remark 备注:
 * @see
 */
public class CreateSanQinGroupDialog extends BaseDialog {

    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.et_call)
    EditText etCall;
    @Bind(R.id.two_button_one)
    Button twoButtonOne;
    @Bind(R.id.two_button_two)
    Button twoButtonTwo;
    @Bind(R.id.spin_call)
    Spinner spinCall;  //下拉

    private String callName ;
    private List<String> dataList;
    private SpinnerSanqinGroupAdapter mAdapter;

    //初始化称呼
    public CreateSanQinGroupDialog(@NonNull Context context,String callName) {
        super(context);
        this.callName = callName;
        //this.spinCall.set

        dataList = new ArrayList<>();
        mAdapter = new SpinnerSanqinGroupAdapter(context,dataList);
        //适配器
       // mAdapter= new ArrayAdapter<String>(context, R.layout.sanqin_group_spinner_select, dataList);
        //设置样式
        //mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinCall.setAdapter(mAdapter);

        //选择item的选择点击监听事件
        spinCall.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    //取已选择的结果
    public String getSelectCall(){
        return spinCall.getSelectedItem().toString();
    }

    @Override
    void initView() {
        setContentView(R.layout.pop_create_san_qin_group);
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


    //设置下拉内容
    public void setSpinCall(List<String> list){
        dataList.clear();
        dataList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    //设置称呼
    public void setCallName (String callName){
        this.etCall.setText(callName);
    }

    private OnDialogClickListener onDialogClickLisenrar;

    public void setOnDialogClickLisenrar(OnDialogClickListener onDialogClickLisenrar){
        this.onDialogClickLisenrar = onDialogClickLisenrar;
    }

}
