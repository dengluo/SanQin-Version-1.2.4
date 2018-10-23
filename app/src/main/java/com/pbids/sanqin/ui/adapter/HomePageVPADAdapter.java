package com.pbids.sanqin.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 大概时旧的
 */

public class HomePageVPADAdapter extends PagerAdapter{
    ArrayList<View> mViews;

    public HomePageVPADAdapter(@NonNull ArrayList<View> views){
        this.mViews = views;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {//必须实现，实例化
        position = position % mViews.size();
        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
        position = position % mViews.size();
        container.removeView(mViews.get(position));
    }
}
