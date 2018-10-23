package com.pbids.sanqin.ui.adapter;

import com.pbids.sanqin.R;

import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;

/**
 * 无用的
 */

public class SharedAdapter extends AuthorizeAdapter{
    public void onCreate() {
        System.out.println("> ShareSDKUIShell created!");
//获取标题栏控件
        TitleLayout llTitle = getTitleLayout();
//标题栏的文字修改
//        int resID= R.getStringRes(getActivity(), "second_title");//这个字段定义在strings.xml文件里面
        llTitle.getTvTitle().setText(R.string.second_title);
    }

    public void onDestroy() {
        System.out.println("> ShareSDKUIShell will be destroyed.");
    }
}
