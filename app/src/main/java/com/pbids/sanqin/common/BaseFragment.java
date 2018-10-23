package com.pbids.sanqin.common;

import android.app.Dialog;
import android.content.ContentProvider;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.netease.nim.uikit.business.eventbus.InviteMessage;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaasePresenter;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.me.MeAuthenticationFragment;
import com.pbids.sanqin.ui.activity.me.MeBindingNumberFragment;
import com.pbids.sanqin.ui.activity.me.MeFragment;
import com.pbids.sanqin.ui.view.WriteCoverPopView;
import com.pbids.sanqin.ui.view.dialog.LoadingDialog;
import com.pbids.sanqin.ui.view.dialog.OneTextTwoBtDialog;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:34
 * @desscribe 类描述:基础的Fragment，大部分Fragment都继承它
 * @remark 备注
 * @see
 */
public abstract class BaseFragment extends SupportFragment implements AbstractBaseView{
    BasePresenter mBasePresenter;
    private CompositeDisposable mCompositeDisposable;
//    private MeLoadingPop meLoadingPop;
    private LoadingDialog loadingDialog;

    //绑定银行卡页面
    public static final int TOBANKTYPE = 1;
    //新人邀请页面
    public static final int TOINVTITETYPE = 2;
    //宗人群页面
    public static final int TOTEAMTYPE = 3;
    //实名认证
    public static final int TORENLNAMETYPE = 4;
    //默认
    public static final int DEFAULTTYPE = 0;

    public static final String TYPEFRAGMENT = "typeFragment";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBasePresenter = initPresenter();
        if(mBasePresenter!=null){
            mBasePresenter.attachView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispose();
        if(mBasePresenter!=null){
            mBasePresenter.detachView();
            mBasePresenter = null;
        }
    }

    public LoadingDialog getLoadingPop(String s){
        if(loadingDialog==null){
            loadingDialog = new LoadingDialog(_mActivity,s);
            loadingDialog.setCancelable(false);
            loadingDialog.setCanceledOnTouchOutside(false);
//            meLoadingPop = new MeLoadingPop(_mActivity,s);
//            meLoadingPop.setIsAnimation(false);
//            meLoadingPop.setCancelable(false);
        }
        if(s!=null){
            loadingDialog.setText(s);
        }
        return loadingDialog;
    }

    public void dismiss(){
        if(loadingDialog!=null){
            if(loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        if(loadingDialog!=null && loadingDialog.isShowing()){
            return true;
        }
        return super.onBackPressedSupport();
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

    public abstract BasePresenter initPresenter();

    OnPayPasswordSetLisenear onPayPasswordSetLisenear;

    public interface OnPayPasswordSetLisenear{
        void setOver();
    }

    public OnPayPasswordSetLisenear getOnPayPasswordSetLisenear(){
        return onPayPasswordSetLisenear;
    }

    public void setOnPayPasswordSetLisenear(OnPayPasswordSetLisenear onPayPasswordSetLisenear){
        this.onPayPasswordSetLisenear = onPayPasswordSetLisenear;
    }

    public void toast(String str) {
        Toast.makeText(_mActivity,str,Toast.LENGTH_LONG).show();
    }

    /**
     * 实名认证弹窗
     * @param fragment 从我的页面跳转需要从MainFragment跳转
     * @param toFragment 判断用户是需要跳哪个页面 1 新人邀请，2 银行卡，3 宗人群
     */

    protected void showRealNameDialog(final Fragment fragment, final int toFragment){
        final OneTextTwoBtDialog realNameDialog = new OneTextTwoBtDialog(_mActivity);
        realNameDialog.setGrayCenter();
        realNameDialog.setComfirmText("前往");
        realNameDialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
            @Override
            public void confirm(View view) {
                realNameDialog.dismiss();
                if(fragment instanceof MeFragment){
                    ((MainFragment) getParentFragment()).start(MeAuthenticationFragment.newInstance(toFragment));
                }else{
                    start(MeAuthenticationFragment.newInstance(toFragment));
                }
            }

            @Override
            public void cancel(View view) {
                realNameDialog.dismiss();
            }
        });
        realNameDialog.setContentText("您还未进行实名认证是否前往实名认证页面？");
//        realNameDialog.setContentText("您尚未实名认证，完成认证将获得更多福利！是否前往实名认证界面？");
        TextView contentTv = realNameDialog.getTextView();
        Button twoButtonOne = realNameDialog.getTwoButtonOne();
        Button twoButtonTwo = realNameDialog.getTwoButtonTwo();
        contentTv.setTextSize(14);
        twoButtonOne.setTextSize(15);
        twoButtonTwo.setTextSize(15);
        realNameDialog.show();
    }

    /**
     * 弹出绑定手机号码
     */
    protected void showBindPhoneDialog(final Fragment fragment, final int toFragment){
        final OneTextTwoBtDialog valPhoneDialog = new OneTextTwoBtDialog(_mActivity);
        valPhoneDialog.setGrayCenter();
        valPhoneDialog.setComfirmText("前往");
        valPhoneDialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
            @Override
            public void confirm(View view) {
                valPhoneDialog.dismiss();
                if(fragment instanceof MeFragment){
                    ((MainFragment) getParentFragment()).start(MeBindingNumberFragment.newInstance(toFragment));
                }else{
                    start(MeBindingNumberFragment.newInstance(toFragment));
                }
            }
            @Override
            public void cancel(View view) {
                valPhoneDialog.dismiss();
            }
        });
        valPhoneDialog.setContentText("您还未绑定手机号码，是否前往手机绑定页面?");
        TextView contentTv = valPhoneDialog.getTextView();
        Button twoButtonOne = valPhoneDialog.getTwoButtonOne();
        Button twoButtonTwo = valPhoneDialog.getTwoButtonTwo();
        contentTv.setTextSize(14);
        twoButtonOne.setTextSize(15);
        twoButtonTwo.setTextSize(15);
        valPhoneDialog.show();
    }

    /**
     * 新人邀请对话框
     */
    protected void showInviteDialog(){
        final OneTextTwoBtDialog oneTextTwoBtDialog = new OneTextTwoBtDialog(_mActivity);
        oneTextTwoBtDialog.setGrayCenter();
        oneTextTwoBtDialog.setComfirmText("前往");
        oneTextTwoBtDialog.setOnDialogClickLisenrar(new OnDialogClickListener() {
            @Override
            public void confirm(View view) {
                EventBus.getDefault().post(new InviteMessage(DEFAULTTYPE));
                oneTextTwoBtDialog.dismiss();
            }

            @Override
            public void cancel(View view) {
                oneTextTwoBtDialog.dismiss();
            }
        });
        oneTextTwoBtDialog.setContentText("您已完成实名认证，前往新人邀请页面邀请更多好友加入吧！每成功邀请一个好友可获得现金奖励哦！");
        oneTextTwoBtDialog.show();
        TextView contentTv = oneTextTwoBtDialog.getTextView();
        Button twoButtonOne = oneTextTwoBtDialog.getTwoButtonOne();
        Button twoButtonTwo = oneTextTwoBtDialog.getTwoButtonTwo();
        contentTv.setTextSize(13);
        twoButtonOne.setTextSize(13);
        twoButtonTwo.setTextSize(13);
    }

    /**
     * 判断是否有网络
     * @param context
     * @return
     */
    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public void showDialog(Dialog dialog, double width, double height) {
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager m = getActivity().getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = window.getAttributes();
        p.height = (int) (height);
        p.width = (int) (d.getWidth() * width);
        window.setAttributes(p);
    }



}
