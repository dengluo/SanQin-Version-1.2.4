package com.pbids.sanqin.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonGlideInstance;

import java.util.ArrayList;

/**
 * Created by pbids903 on 2017/12/7.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:25
 * @desscribe 类描述:广告页viewpager的适配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.BootPageFragment
 */
public class BootPageVPAdapter extends PagerAdapter{
    public int screenWidth;
    int screenHeigh;
    int[] drawables = {R.drawable.launchimage1,R.drawable.launchimage2,R.drawable.launchimage3};
    public ArrayList<View> mViews;
    Context mContext;
    public CountDownTimer countDownTimer;
    public BootPageVPAdapter(Context context,int[] drawables){
        this.mContext = context;
        this.drawables = drawables;
        DisplayMetrics dm = new DisplayMetrics();
       // 获取屏幕信息
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
       screenWidth = dm.widthPixels;
       screenHeigh = dm.heightPixels;
        Log.i("wzh","screenWidth: "+screenWidth);
        Log.i("wzh","screenHeigh: "+screenHeigh);
        mViews = new ArrayList<View>();
//        if(url!=null){
//            View view = LayoutInflater.from(mContext).inflate(R.layout.boot_vp_item,null,false);
//            mViews.insert(view);
//        }else{
            for(int i=0;i<drawables.length;i++){
                View view = LayoutInflater.from(mContext).inflate(R.layout.boot_vp_item,null,false);
                mViews.add(view);
            }
//        }
    }


    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
//        ImageView imageView = new ImageView(mContext);
        ViewHolder mViewHolder = new ViewHolder();
        View view = mViews.get(position);
        if(null == view.getTag()){
            mViewHolder.im = (ImageView) view.findViewById(R.id.boot_vp_item_im);
            new CommonGlideInstance().setImageViewBackgroundForUrl(
                    mContext,mViewHolder.im,drawables[position],R.drawable.loading,R.drawable.loading);
            view.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder) view.getTag();
        }

        container.addView(mViews.get(position));
        return mViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
        container.removeView(mViews.get(position));
    }

    class ViewHolder {
        ImageView im;
//        Button bt;
    }
}
