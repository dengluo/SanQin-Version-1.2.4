package com.pbids.sanqin.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.ui.view.AppToolBar;

public abstract class BaaseToolBarFragment<P extends BaasePresenter> extends BaaseFragment<P> implements AppToolBar.OnToolBarClickLisenear {

    protected RelativeLayout llRoot;
    protected AppToolBar toolBar;
    protected LinearLayout content;
    protected LinearLayout noDataLayout;
    protected ImageView noDataIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        llRoot = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_main_title, container,false);
        toolBar = (AppToolBar) llRoot.findViewById(R.id.toolbar);
        content = (LinearLayout) llRoot.findViewById(R.id.content);
        noDataLayout = (LinearLayout) llRoot.findViewById(R.id.no_data_layout);
        noDataIv = (ImageView) llRoot.findViewById(R.id.no_data_iv);
        View childView =this.onToolBarCreateView(inflater,container,savedInstanceState);
        setToolBar(toolBar);
        content.addView(childView);
        return llRoot;
    }

    public void setToolBarVisiable(){
        toolBar.setVisibility(View.VISIBLE);
    }

    public void setToolBarGone(){
        toolBar.setVisibility(View.GONE);
    }

    public void setContentLayoutVisible(){
        if(content.getVisibility() == View.GONE){
            content.setVisibility(View.VISIBLE);
        }
    }

    public void setNoDataLayoutVisible(){
        noDataIv.setVisibility(View.VISIBLE);
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,noDataIv,R.drawable.me_icon_zhanwushujuxianshi_default);
    }

    public void setContentLayoutGone(){
        if(content.getVisibility() == View.VISIBLE){
            content.setVisibility(View.GONE);
        }
    }

    public LinearLayout getContentLayout(){
        return content;
    }
    public RelativeLayout getToolFragmentRootLayout(){return llRoot;};

    public abstract View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
    public abstract void setToolBar(AppToolBar toolBar);



}
