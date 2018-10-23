package com.pbids.sanqin.ui.activity.me;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.pbids.sanqin.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author : 上官名鹏
 *         Description : 海报生成过程Dialog
 *         Date :Create in 2018/8/3 17:57
 *         Modified By :
 */

public class PostDownDialog extends Dialog {

    @Bind(R.id.post_down_step_iv)
    ImageView postDownStepIv;
    private Context mContext;

    public PostDownDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public PostDownDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = View.inflate(mContext, R.layout.dialog_post_down, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setCanceledOnTouchOutside(false);
    }

    public ImageView getPostDownStepIv() {
        return postDownStepIv;
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);

    }
}
