package com.pbids.sanqin.common;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.utils.PickerViewAnimateUtil;
import com.bigkoo.pickerview.view.BasePickerView;
import com.pbids.sanqin.R;

/**
 * 公用的popview，但是后来pop我大部分都改成了dialog，所以pop用的相对少
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:35
 * @desscribe 类描述:公用的popview
 * @remark 但是后来pop我大部分都改成了dialog，所以pop用的相对少
 * @see
 */

public class CustomPopView {

//    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
//    );
    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.BOTTOM
    );

    private Context context;
    protected ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//附加View 的 根View

    private OnDismissListener onDismissListener;
    private boolean dismissing;

    private Animation outAnim;
    private Animation inAnim;
    private boolean isShowing;
    private int gravity = Gravity.BOTTOM;
    private boolean isAnimation = true;

    public CustomPopView(Context context){
        this(context,null);
    }

    public CustomPopView(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public CustomPopView(Context context, AttributeSet attrs, int defStyle){
        this.context = context;
        initViews();
        init();
        initEvents();
        childInit();
    }

    public void childInit() {

    }

    public void setIsAnimation(boolean isAnimation){
        this.isAnimation = isAnimation;
    }

    protected void initViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        decorView = (ViewGroup) ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_basepickerview, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        contentContainer.setLayoutParams(params);
    }

    public void setRootViewColor(int color){
        rootView.setBackgroundColor(color);
    }

    protected void init() {
        inAnim = getInAnimation();
        outAnim = getOutAnimation();
    }
    protected void initEvents() {
    }
    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        decorView.addView(view);
        if(isAnimation){
            contentContainer.startAnimation(inAnim);
        }
    }
    /**
     * 添加这个View到Activity的根视图
     */
    public void show() {
        if (isShowing()) {
            return;
        }
        isShowing = true;
        onAttached(rootView);
    }
    /**
     * 检测该View是不是已经添加到根视图
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        return rootView.getParent() != null || isShowing;
    }

    public void setBgColor(int color){
        rootView.setBackgroundColor(color);
    }

    public void dismiss() {
        if (dismissing) {
            return;
        }

        dismissing = true;

        if(!isAnimation){
            dismissImmediately();
        }

        //消失动画
        outAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                decorView.post(new Runnable() {
                    @Override
                    public void run() {
                        dismissImmediately();
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if(isAnimation){
            contentContainer.startAnimation(outAnim);
        }
    }

    public void dismissImmediately() {
        //从activity根视图移除
        decorView.removeView(rootView);
        isShowing = false;
        dismissing = false;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(CustomPopView.this);
        }
    }
    public Animation getInAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, true);
        return AnimationUtils.loadAnimation(context, res);
    }

    public Animation getOutAnimation() {
        int res = PickerViewAnimateUtil.getAnimationResource(this.gravity, false);
        return AnimationUtils.loadAnimation(context, res);
    }

    public CustomPopView setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }

    public CustomPopView setCancelable(boolean isCancelable) {
        View view = rootView.findViewById(R.id.outmost_container);

        if (isCancelable) {
            view.setOnTouchListener(onCancelableTouchListener);
        }
        else{
            view.setOnTouchListener(null);
        }
        return this;
    }
    /**
     * Called when the user touch on black overlay in order to dismiss the dialog
     */
    private final View.OnTouchListener onCancelableTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };

    public View findViewById(int id){
        return contentContainer.findViewById(id);
    }


    public Context getContext() {
        return context;
    }

}
