package com.pbids.sanqin.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.utils.ScreenUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2018/3/14.
 */

public class SplashFragment extends BaseFragment {

    @Bind(R.id.splash_iv)
    ImageView splashIv;

    public static SplashFragment newInstance(){
        SplashFragment fragment = new SplashFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_splash,container,false);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView() {
        new CommonGlideInstance().setImageViewBackgroundForUrl(
                _mActivity,splashIv,R.drawable.launchimage_,ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight(),true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startWithPop(BootPageFragment.newInstance());
            }
        },2000);
    }
}
