package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.component.AmountET;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.presenter.PayPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

import static android.text.InputType.TYPE_CLASS_NUMBER;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:11
 * @desscribe 类描述:支付打赏金额dialog
 * @remark 备注:
 * @see
 */
public class RewardDialog extends BaseDialog {

    @Bind(R.id.news_cash_pop_10_iv)
    ImageView newsCashPop10Iv;
    @Bind(R.id.news_cash_pop_30_iv)
    ImageView newsCashPop30Iv;
    @Bind(R.id.news_cash_pop_50_iv)
    ImageView newsCashPop50Iv;
    @Bind(R.id.news_cash_pop_100_iv)
    ImageView newsCashPop100Iv;
    @Bind(R.id.news_cash_pop_500_iv)
    ImageView newsCashPop500Iv;
    @Bind(R.id.news_cash_pop_2000_iv)
    ImageView newsCashPop2000Iv;
    @Bind(R.id.pop_cash_minus)
    ImageView popCashMinus;
    @Bind(R.id.pop_cash_add)
    ImageView popCashAdd;
    @Bind(R.id.pop_cash_et)
    AmountET popCashEt;

    @Bind(R.id.pop_cash_bt)
    Button popCashBt;

    PayPresenter dialogPresenter;
    DisposableObserver observer;
    Context mContext;
    OnDialogClickListener onDialogClickLisenrar;
    String type;
    private float maxNumber=0;

    private String outFromat = "%.2f";

    public String toastMsg = "请输入金额";

    public RewardDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public void initView() {
        setContentView(R.layout.pop_news_cash);
        ButterKnife.bind(this);
        setGrayBottom();
        setBottomUpAnimation();
        initData();
    }


    public void setMaxNumber(float number){
        this.maxNumber = number;
    }

    //只能数据数字设置
    public void setInputType(int type){
        popCashEt.setInputType(type)  ;
    }

    //
    public void setOutFromat(String out){
        this.outFromat = out;
    }


    private void initData() {
        popCashEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(maxNumber!=0){
                    String input =  charSequence.toString();
                    if(input==null || input.equals("")){
                        return;
                    }
                    try{
                        float number = Float.valueOf(charSequence.toString());
                        if(number>maxNumber){
                            if("friend".equals(type)){
                                popCashEt.setText(((int)maxNumber)+"");
                            }else{
                                popCashEt.setText(""+maxNumber);
                            }
                        }
                    }catch (Exception e){
                        //popCashEt.setText("");
                    }

                }
                popCashEt.setSelection(popCashEt.getText().length());
//                number
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    public void setHitAndButtonText(String hitText, String buttonText){
        popCashEt.setHint(hitText);
        popCashBt.setText(buttonText);
    }

    public void setType(String type){
        this.type = type;
//        if("cash".equals(type)){
//            popCashEt.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            EditTextUtils.afterDotTwo(popCashEt);
//        }else if("friend".equals(type)){
//            popCashEt.setInputType(InputType.TYPE_CLASS_NUMBER);
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDetachedFromWindow() {
//        popCashEt.setFocusable(false);
//        popCashEt.setFocusableInTouchMode(false);
//        ButterKnife.unbind(this);
        super.onDetachedFromWindow();
    }

    public void setOnDialogClickLisenrar(OnDialogClickListener onDialogClickLisenrar){
        this.onDialogClickLisenrar = onDialogClickLisenrar;
    }

    @OnClick({R.id.news_cash_pop_10_iv, R.id.news_cash_pop_30_iv, R.id.news_cash_pop_50_iv,
            R.id.news_cash_pop_100_iv, R.id.news_cash_pop_500_iv, R.id.news_cash_pop_2000_iv,
            R.id.pop_cash_minus, R.id.pop_cash_add, R.id.pop_cash_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_cash_pop_10_iv:
                //popCashEt.setText("10");
                updateCashET(10);
                break;
            case R.id.news_cash_pop_30_iv:
                //popCashEt.setText("30");
                updateCashET(30);
                break;
            case R.id.news_cash_pop_50_iv:
                //popCashEt.setText("50");
                updateCashET(50);
                break;
            case R.id.news_cash_pop_100_iv:
                //popCashEt.setText("100");
                updateCashET(100);
                break;
            case R.id.news_cash_pop_500_iv:
                updateCashET(500);
                //popCashEt.setText("500");
                break;
            case R.id.news_cash_pop_2000_iv:
                updateCashET(2000);
                //popCashEt.setText("2000");
                break;
            case R.id.pop_cash_minus:
                //减数字
                if(!"".equals(popCashEt.getText().toString().trim())){
                    float minusCash = Float.valueOf(popCashEt.getText().toString().trim());
                    if(minusCash>1) {
                        minusCash -=1;
                    }
                    //popCashEt.setText(""+minusCash);
                    updateCashET(minusCash);
                }
                break;
            case R.id.pop_cash_add:
                //加数字
                if(!"".equals(popCashEt.getText().toString().trim())){
                    //int addCash = Integer.valueOf(popCashEt.getText().toString().trim());
                    float addCash = Float.valueOf(popCashEt.getText().toString().trim());
                    //popCashEt.setText(""+(addCash+1));
                    updateCashET(addCash+1);
                }else{
                    //popCashEt.setText("1");
                    updateCashET(1);
                }
                break;
            case R.id.pop_cash_bt:
                //点击赏按钮
                if(popCashEt!=null){
                    String cashStr = popCashEt.getText().toString();
                    if (cashStr == null || cashStr.isEmpty()) {
                        Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                    } else {
                        float cash = Float.valueOf(cashStr.trim());
                        //金额不能为0
                        if (cash > 0) {
                            view.setTag(popCashEt.getText().toString().trim());
                            if (onDialogClickLisenrar != null) {
                                onDialogClickLisenrar.confirm(view);
                            }
                        } else {
                            Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                }
                break;
        }
    }

    private void updateCashET(float addCash){
        if(popCashEt!=null){
            popCashEt.setText(String.format(outFromat,addCash));
        }
    }

}
