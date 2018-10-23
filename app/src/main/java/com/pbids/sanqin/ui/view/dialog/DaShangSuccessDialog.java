package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.pbids.sanqin.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pbids903 on 2018/1/19.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:08
 * @desscribe 类描述:打赏成功dialog
 * @remark 备注:
 * @see
 */
public class DaShangSuccessDialog extends BaseDialog {
    @Bind(R.id.pay_success_bt)
    Button paySuccessBt;

    public DaShangSuccessDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    void initView() {
        setContentView(R.layout.pop_news_pay_success);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.pay_success_bt)
    public void onViewClicked() {
        dismiss();
    }
}
