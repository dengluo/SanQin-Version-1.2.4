package com.pbids.sanqin.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.widget.Toast;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseView;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author caiguoliang
 * @date on 2018/3/2 11:34
 * @desscribe 类描述:基础Activity，大部分Activity的父类
 * @remark 备注
 * @see
 */
public abstract class BaaseActivity<P extends BaasePresenter > extends SupportActivity implements BaaseView  {

    P  mPresenter;
    //CompositeDisposable mCompositeDisposable;
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        mPresenter = initPresenter();
        if(null!= mPresenter){
            //mPresenter.attachView(this);
            mPresenter.onCreate( this,this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!= mPresenter){
            mPresenter.onDestroy();
            //mPresenter.detachView();
        }
    }
/*
    //添加OkGo请求
    public void addDisposable(Disposable disposable){
        if(null == mCompositeDisposable){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    //取消所有的任务
    public void dispose(){
        if(null != mCompositeDisposable) mCompositeDisposable.dispose();
    }

    //移除对应的OkGo的请求
    public void removeDisposable(Disposable disposable){
        if(null != mCompositeDisposable){
            if(null != disposable){
                mCompositeDisposable.remove(disposable);
            }
        }
    }
*/
    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public abstract P initPresenter();

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // open web page
    @Override
    public void openWebFragment(String url){
        ZhiZongWebFragment fragment1 = ZhiZongWebFragment.newInstance();
        fragment1.getArguments().putString("link", url);
        start(fragment1);
    }
}
