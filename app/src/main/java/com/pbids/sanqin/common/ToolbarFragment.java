package com.pbids.sanqin.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.ui.view.AppToolBar;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 11:37
 * @desscribe 类描述:拥有标题栏的fragment
 * @remark 备注:
 * @see
 */

public abstract class ToolbarFragment extends BaseFragment{
    private RelativeLayout llRoot;
    private AppToolBar toolBar;
    private LinearLayout content;
    private LinearLayout noDataLayout;
    private ImageView noDataIv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        if(llRoot==null){
            llRoot = (RelativeLayout) LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_main_title, container,false);
            toolBar = (AppToolBar) llRoot.findViewById(R.id.toolbar);
            content = (LinearLayout) llRoot.findViewById(R.id.content);
            noDataLayout = (LinearLayout) llRoot.findViewById(R.id.no_data_layout);
            noDataIv = (ImageView) llRoot.findViewById(R.id.no_data_iv);
//        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        lp.addRule(RelativeLayout.BELOW, R.id.toolbar);
            View childView =this.onToolBarCreateView(inflater,container,savedInstanceState);
            setToolBar(toolBar);
            content.addView(childView);
//        if (null != llRoot)
//            llRoot.addView(childView, lp);
//        }
        return llRoot;
    }

    public void setToolBarVisiable(){
        toolBar.setVisibility(View.VISIBLE);
    }

    public void setToolBarGone(){
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
//        toolBar.setLayoutParams(params);
        toolBar.setVisibility(View.GONE);
    }

    public void setContentLayoutVisible(){
        if(content.getVisibility() == View.GONE){
            content.setVisibility(View.VISIBLE);
        }
    }

    public void setNoDataLayoutVisible(){
//        if(noDataLayout.getVisibility() == View.GONE){
//            noDataLayout.setVisibility(View.GONE);

        noDataIv.setVisibility(View.VISIBLE);
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,noDataIv,R.drawable.me_icon_zhanwushujuxianshi_default);
//            Glide.with(_mActivity).load(R.drawable.me_icon_zhanwushujuxianshi_default).override(
//                    (int)_mActivity.getResources().getDimension(R.dimen.dp_170)
//                    ,(int)_mActivity.getResources().getDimension(R.dimen.dp_120)).into(noDataIv);
//        }
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
