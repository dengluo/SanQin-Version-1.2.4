package com.pbids.sanqin.ui.pay.union_pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseActivity;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.view.dialog.PaymentModeDialog;

/**
 * @author caiguoliang
 * @date on 2018/6/4
 * @desscribe 类描述: 银生宝支付
 * @remark 备注:
 *
 */

public class UnionPayActivity extends BaaseActivity<UnionPayPresenter> {

    private String payMode ;
    private String totalAmount;
    private String aid;
    public String purpose ; //支付用途

    @Override
    public UnionPayPresenter initPresenter() {
        return new UnionPayPresenter();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_union_pay);
        Intent intent = getIntent();
        if(intent==null){
            finish();
        }

        payMode = intent.getStringExtra("pay");
        totalAmount = intent.getStringExtra("totalAmount");
        purpose = intent.getStringExtra("purpose");
        aid = intent.getStringExtra("aid");
        Log.d("cgl","aid:"+aid+ "  pay mode:"+payMode +"   totalAmount:"+totalAmount + " uid:" +MyApplication.getUserInfo().getUserId()+  " token:"+ MyApplication.getUserInfo().getToken());

        if (aid == null || "".equals(aid) || !PaymentModeDialog.PAYMENT_YINLIAN.contains(payMode)) {
            showToast("参数错误");
            finish();
        }
        loadRootFragment(R.id.fl_container, MeUnionPayFragment.instance(payMode,totalAmount,aid));
        init();
        
    }

    private void init() {

    }

    // back
    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        //super.onBackPressedSupport();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            finish();
        }
    }
}
