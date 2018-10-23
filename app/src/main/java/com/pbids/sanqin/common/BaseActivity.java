package com.pbids.sanqin.common;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.pbids.sanqin.R;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:34
 * @desscribe 类描述:基础Activity，大部分Activity的父类
 * @remark 备注
 * @see
 */
public abstract class BaseActivity extends SupportActivity implements AbstractBaseView{

    private static final String TAG = "BaseActivity";

    BasePresenter mBasePresenter;
    CompositeDisposable mCompositeDisposable;
    // 再点一次退出程序时间设置
    protected static final long WAIT_TIME = 2000L;
    protected long TOUCH_TIME = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //ToDo java.lang.NoSuchMethodException
        try {
            super.onCreate(savedInstanceState);
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
            mBasePresenter = initPresenter();
            if(null!=mBasePresenter){
                mBasePresenter.attachView(this);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!=mBasePresenter){
            mBasePresenter.detachView();
        }
    }

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

    public abstract BasePresenter initPresenter();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        BaseFragment fragment = get
        FragmentManager fm = getSupportFragmentManager();
        int len = fm.getFragments().size();
        for(Fragment frag : fm.getFragments()){
            handleResult(frag, requestCode, resultCode, data);
        }
        /*int index = requestCode >> 16;
        if (index != 0) {
            index--;
            if (fm.getFragments() == null || index < 0 || index >= fm.getFragments().size()) {
                Log.w(TAG, "Activity result fragment index out of range: 0x" + Integer.toHexString(requestCode));
                return;
            }
            Fragment frag = fm.getFragments().get(index);
            if (frag == null) {
                Log.w(TAG, "Activity result no fragment exists for index: 0x" + Integer.toHexString(requestCode));
            } else {
                handleResult(frag, requestCode, resultCode, data);
            }
            return;
        }*/

    }
    /**
     * 递归调用，对所有子Fragement生效
     *
     * @param frag
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        frag.onActivityResult(requestCode  , resultCode, data);
        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }
}
