package com.pbids.sanqin.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseActivity;
import com.pbids.sanqin.base.CommonPresenter;

/**
 * @author caiguoliang
 * @date on 2018/3/2 15:13
 * @desscribe 类描述:姓氏认证界面
 * @remark 备注:
 * @see
 */

public class AuthenticationActivity extends BaaseActivity<CommonPresenter> {

    @Override
    public CommonPresenter initPresenter() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initView();
    }

    private void initView() {
        loadRootFragment(R.id.fl_container, AuthenticationFragment.newInstance());
    }
}
