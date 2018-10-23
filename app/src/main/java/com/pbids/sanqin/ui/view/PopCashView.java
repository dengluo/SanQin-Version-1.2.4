package com.pbids.sanqin.ui.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CustomPopView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/13.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:21
 * @desscribe 类描述:打赏金额弹窗
 * @remark 备注:改为dialog
 * @see
 */
public class PopCashView extends CustomPopView {

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
    EditText popCashEt;
    @Bind(R.id.pop_cash_bt)
    Button popCashBt;

    public PopCashView(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.pop_news_cash, contentContainer);

    }


    @OnClick({R.id.news_cash_pop_10_iv, R.id.news_cash_pop_30_iv, R.id.news_cash_pop_50_iv,
            R.id.news_cash_pop_100_iv, R.id.news_cash_pop_500_iv, R.id.news_cash_pop_2000_iv,
            R.id.pop_cash_minus, R.id.pop_cash_add, R.id.pop_cash_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_cash_pop_10_iv:
                Log.i("wzh","news_cash_pop_10_iv");
                break;
            case R.id.news_cash_pop_30_iv:
                break;
            case R.id.news_cash_pop_50_iv:
                break;
            case R.id.news_cash_pop_100_iv:
                break;
            case R.id.news_cash_pop_500_iv:
                break;
            case R.id.news_cash_pop_2000_iv:
                break;
            case R.id.pop_cash_minus:
                break;
            case R.id.pop_cash_add:
                break;
            case R.id.pop_cash_bt:
                break;
        }
    }
}
