package com.pbids.sanqin.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.common.Const;
import com.pbids.sanqin.server.ISQBinder;
import com.pbids.sanqin.server.SanQinService;
import com.pbids.sanqin.utils.OkGoUtil;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * @author caiguoliang
 * @date on 2018/3/2 11:34
 * @desscribe 类描述:所有P层的父类
 * @remark 备注
 * @see
 */
public abstract class BaasePresenter <T extends BaaseView> implements ServiceConnection {

    //protected Reference viewRef;

    protected T mView;

    //
    protected Context mContext = null;

    protected ISQBinder binder;
    protected boolean mBound;
    //okgo
    CompositeDisposable mCompositeDisposable;
    /*
    public void attachView(T baseView){
        viewRef=new WeakReference(baseView);
    }

    public void detachView(){
        if(viewRef!=null){
            viewRef.clear();
            viewRef = null;
        }
    }
    */

    public void onCreate(T v, Context context) {
        mView = v;
        mContext = context;
        try {
            //绑定服务
            mContext.bindService(new Intent(mContext, SanQinService.class), this, context.BIND_AUTO_CREATE);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void onDestroy() {
        //detachView();
        //断开服务
        mContext.unbindService(this);
        dispose();
    }


    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        //连接服务
//        mService = ((SanQinService.SQBinder) iBinder).getService();
//        messenger = new Messenger(iBinder);
//        SanQinService mService = ((SanQinService.SQBinder) iBinder).getService();
//        binder = (ISQBinder)iBinder;
//        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
//        messenger = null;
        mBound = false;
    }

    public DisposableObserver<Response<String>> requestHttp(final String url, final HttpParams params, MHttpMethod method , final int requestCode){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","stringResponse: "+stringResponse);
                try {
                    HttpJsonResponse rescb =  JSON.parseObject(stringResponse.body(),HttpJsonResponse.class);
                    if(rescb.getStatus()== Const.OK){
                        int status = rescb.getStatus();
                        onHttpSuccess(status,requestCode, rescb );
                    }else {
                        onHttpError(rescb.getStatus(), requestCode,rescb.getCode(),rescb.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //mView.onHttpError(Const.LOST, requestCode,0,null);
                    onHttpError( Const.LOST, requestCode,Const.REQUEST_ERROR_JSON_DATA,"json data error");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                //mView.onHttpError(Const.LOST, requestCode,0,null);
                Log.d(BaasePresenter.class.getSimpleName(), e.getMessage());
                onHttpError( Const.LOST, requestCode,Const.REQUEST_ERROR_NETWOR_DISCONNECT,"网络未连接，请检查您的网络！");
            }

            @Override
            public void onComplete() {

            }
        };
        if(method.equals(MHttpMethod.GET)){
            //http get
            OkGoUtil.getStringObservableForGet(url,params,observer);
        }else if(method.equals(MHttpMethod.POST)){
            //http post
            OkGoUtil.getStringObservableForPost(url,params,observer);
        }
        addDisposable(observer);
        return observer;
    }


    //添加OkGo请求
    protected void addDisposable(Disposable disposable){
        if(null == mCompositeDisposable){
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    //取消所有的任务
    protected void dispose(){
        if(null != mCompositeDisposable) mCompositeDisposable.dispose();
    }

    //移除对应的OkGo的请求
    protected void removeDisposable(Disposable disposable){
        if(null != mCompositeDisposable){
            if(null != disposable){
                mCompositeDisposable.remove(disposable);
            }
        }
    }


    // 请求列表数据
    // HttpParams httpParams = new HttpParams();
    // httpParams.put("keyword",keyword);
    // httpParams.put("pageIndex",indexPage);
    //return Const.REQUEST_SUCCESS;

    public DisposableObserver<Response<String>> requestHttpPost(final String url, final HttpParams params, final int requestCode) {
        return requestHttp(url, params, MHttpMethod.POST, requestCode);
    }
    public DisposableObserver<Response<String>> requestHttpGet(final String url, final HttpParams params, final int requestCode) {
        return requestHttp(url, params, MHttpMethod.GET, requestCode);
    }

    //数据请求返回
    public void onHttpSuccess(int resultCode, int requestCode,  HttpJsonResponse rescb){

    }

    //数据请求错误
    public void onHttpError(int resultCode, int requestCode, int errorCode, String errorMessage){
//        mView.showToast(errorMessage + " code:"+errorCode);
        mView.showToast(errorMessage);
    }




}
