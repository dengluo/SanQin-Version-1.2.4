package com.pbids.sanqin.ui.activity.me;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.FeedErrorType;
import com.pbids.sanqin.presenter.MeFeedbackPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.WarpLinearLayout;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.ButtonUtil;
import com.pbids.sanqin.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2017/11/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:23
 * @desscribe 类描述:我的设置-反馈界面
 * @remark 备注:
 * @see
 */
public class MeFeedbackFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MeFeedbackView{

    @Bind(R.id.me_feedback_et)
    EditText meFeedbackEt;
    @Bind(R.id.me_feedback_bt)
    Button meFeedbackBt;
    @Bind(R.id.me_feedback_fixed)
    WarpLinearLayout meFeedbackFixed;

    MeFeedbackPresenter meFeedbackPresenter;
    List<String> errorStrs = new ArrayList<>();
    List<String> feedbackTypeIds = new ArrayList<>();

    public static MeFeedbackFragment newInstance() {
        MeFeedbackFragment fragment = new MeFeedbackFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view_donate_records = inflater.inflate(R.layout.me_feedback, container, false);
//        ButterKnife.bind(this, view_donate_records);
//        return view_donate_records;
//    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.me_feedback, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        HttpParams httpParams = new HttpParams();
        addDisposable(meFeedbackPresenter.submitInfotmation(
                AddrConst.SERVER_ADDRESS_USER+ AddrConst.ADDRESS_FEEDBACK_QUERYTYPE,httpParams,"2"));
        ButtonUtil.setOnClickFalse(meFeedbackBt);
        meFeedbackEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!meFeedbackEt.getText().toString().trim().equals("")){
                    ButtonUtil.setOnClickTrue(meFeedbackBt);
                }else{
                    ButtonUtil.setOnClickFalse(meFeedbackBt);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftTextCenterTextTitle("取消","意见反馈",_mActivity);
    }

    @Override
    public BasePresenter initPresenter() {
        return meFeedbackPresenter = new MeFeedbackPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.me_feedback_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            case R.id.me_title_left_layout:
//                pop();
//                break;
            case R.id.me_feedback_bt:
                hideSoftInput();
                HttpParams params = new HttpParams();
                params.put("userId",MyApplication.getUserInfo().getUserId());
                params.put("content",meFeedbackEt.getText().toString().trim());
                params.put("feedbackTypeIds", StringUtil.convertToDatabaseValue(feedbackTypeIds,","));
                getLoadingPop("正在提交").show();
                addDisposable(meFeedbackPresenter.submitInfotmation(
                        AddrConst.SERVER_ADDRESS_USER + AddrConst.ADDRESS_ME_FEEDBACK,params,"1"));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
        }
    }

    @Override
    public void onHttpSuccess(String type) {
        getLoadingPop(null).dismiss();
        if("1".equals(type)){
            Toast.makeText(_mActivity,"提交成功!",Toast.LENGTH_SHORT).show();
            pop();
        }
    }

    @Override
    public void onHttpError(String type) {
        getLoadingPop(null).dismiss();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getFeedErrorTypes(List<FeedErrorType> errorTypes) {
        if(errorTypes.size()<=0){
            return;
        }
        errorStrs.clear();
        meFeedbackFixed.removeAllViews();
        Log.i("wzh","errorTypes: "+errorTypes.toString());
        Resources res = _mActivity.getResources();
        int size = (int)res.getDimension(R.dimen.dp_3);
        for(int i=0;i<errorTypes.size();i++){
            final FeedErrorType feedErrorType = errorTypes.get(i);
            final TextView textView = new TextView(_mActivity);
            textView.setText(feedErrorType.getTypeName());
            textView.setGravity(Gravity.CENTER);
            textView.setPadding(size,size,size,size);
            textView.setBackground(res.getDrawable(R.drawable.selector_feedback_index));
            textView.setSelected(false);
            textView.setTextColor(Color.parseColor("#A0A0A0"));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
//            if(i!=0){
//                textView
//            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(textView.isSelected()){
                        textView.setSelected(false);
                        textView.setTextColor(Color.parseColor("#A0A0A0"));
                        errorStrs.remove(feedErrorType.getTypeName());
                        feedbackTypeIds.remove(""+feedErrorType.getId());
                    }else{
                        textView.setTextColor(Color.parseColor("#FFFFFF"));
                        textView.setSelected(true);
                        errorStrs.add(feedErrorType.getTypeName());
                        feedbackTypeIds.add(""+feedErrorType.getId());
                    }
                }
            });
            meFeedbackFixed.addView(textView);
        }
//        R.layout.view_feedback_type
    }
}
