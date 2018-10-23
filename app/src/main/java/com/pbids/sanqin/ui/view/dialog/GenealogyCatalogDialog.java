package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.utils.ScreenUtils;
import com.pbids.sanqin.utils.SharedUtils;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:12
 * @desscribe 类描述:分享布局dialog
 * @remark 备注:
 * @see
 */
public class GenealogyCatalogDialog extends BaseDialog {

    Context mContext;

    public GenealogyCatalogDialog(@NonNull Context context) {
        super(context);
        mContext = context;
//        setBottomUpAnimation();
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.GenealogyCatalog);
        window.getDecorView().setPadding(0, (int)mContext.getResources().getDimension(R.dimen.dp_40), 0, 0);
        WindowManager.LayoutParams attr = window.getAttributes();
        if (attr != null) {
            attr.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            attr.width = ViewGroup.LayoutParams.MATCH_PARENT;
            attr.gravity = Gravity.TOP;//设置dialog 在布局中的位置

            window.setAttributes(attr);
        }
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void initView() {
        setContentView(R.layout.pop_genealogy_catalog);
        ButterKnife.bind(this);
    }

}
