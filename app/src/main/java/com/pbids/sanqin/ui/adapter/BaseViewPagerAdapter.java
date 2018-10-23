package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.view.AutoViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caik on 2016/10/11.
 */
//自动轮播viewpager的适配器

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:22
 * @desscribe 类描述:自动轮播viewpager的适配器
 * @remark 备注:
 * @see AutoViewPager
 */
public abstract class BaseViewPagerAdapter<T> extends PagerAdapter implements ViewPager.OnPageChangeListener{

    private static final String TAG = "BaseViewPagerAdapter";

    private List<String> data = new ArrayList<String>();
    private List<NewsArticle> articles = new ArrayList<NewsArticle>();

    private Context mContext;
    private AutoViewPager mView;

    private OnAutoViewPagerItemClickListener listener;

    public BaseViewPagerAdapter(List<String> t) {
        this.data = t;
    }

    public BaseViewPagerAdapter(Context context) {
        this.mContext = context;
    }


    public BaseViewPagerAdapter(Context context, OnAutoViewPagerItemClickListener listener) {
        this.mContext = context;
        this.listener = listener;
    }

    public BaseViewPagerAdapter(Context context, List<NewsArticle> articles, OnAutoViewPagerItemClickListener listener) {
        this.mContext = context;
        this.listener = listener;
        this.articles = articles;
    }

    public BaseViewPagerAdapter(Context context, List<String> data) {
        this.mContext = context;
        this.data = data;
    }

//    public BaseViewPagerAdapter(Context context, List<String> data,OnAutoViewPagerItemClickListener listener) {
//        this.mContext = context;
//        this.data = data;
//        this.listener = listener;
//    }

    public void init(AutoViewPager viewPager,BaseViewPagerAdapter adapter){
        mView = viewPager;
        mView.setAdapter(this);
        mView.addOnPageChangeListener(this);

        if (articles == null || articles.size() == 0){
            return;
        }
        //设置初始为中间，这样一开始就能够往左滑动了
        int position = Integer.MAX_VALUE/2 - (Integer.MAX_VALUE/2) % getRealCount();
        mView.setCurrentItem(position);

        if (!mView.isStart()) {
            mView.start();
            mView.updatePointView(getRealCount());
        }

    }

    public void setListener(OnAutoViewPagerItemClickListener listener) {
        this.listener = listener;
    }

//    public void insert(String t){
//
//        if (mView.getAdapter() == null) {
//            throw new RuntimeException("必须先设置Adapter");
//        }
//
//        if (articles == null) {
//            articles = new ArrayList<>();
//        }
//        int currentItem = mView.getCurrentItem() % getRealCount();
//        articles.insert(t);
//        //先停止自动滚动
//        mView.onStop();
//        Log.d(TAG, "当前显示第" + currentItem + "/" + mView.getCurrentItem());
////        notifyDataSetChanged();
//        mView.updatePointView(getRealCount(),currentItem);
//        //重新开始自动滚动
//        mView.onResume();
//
//    }

    public void add(List<NewsArticle> list){
        if (articles == null) {
            articles = new ArrayList<>();
        }

        articles.addAll(list);

        notifyDataSetChanged();

        mView.start();
        mView.updatePointView(getRealCount());
    }

    public void init(List<NewsArticle> list){

        if (articles == null) {
            articles = new ArrayList<>();
        }

        articles.clear();
        articles.addAll(list);

        notifyDataSetChanged();

        if (!mView.isStart()) {
            mView.start();
            mView.updatePointView(getRealCount());
        }

    }

    @Override
    public int getCount() {
        return (articles == null || articles.size() == 0 ) ? 0 : Integer.MAX_VALUE;
    }

    public int getRealCount(){
        return articles == null ? 0 : articles.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ImageView view = (ImageView) object;
        container.removeView(view);
        view = null;
    }
    ViewGroup viewGroup;
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView view = (ImageView) LayoutInflater.from(mContext)
                .inflate(R.layout.hp_point_imager,container,false);
        loadImage(view,position, articles.get(position % getRealCount()).getLitpicList().get(0));
        container.addView(view);
        if(viewGroup==null){
            viewGroup = container;
        }
        //设置标题(不知道为什么标题跟图片对不上，所以做了如下处理，有大神看到帮忙看看。。。)
//        if (mView.getSubTitle() != null){
//            if (position == 0){
//                setSubTitle(mView.getSubTitle(),position,data.get((getRealCount() - 1)));
//            }else {
//                setSubTitle(mView.getSubTitle(),position,data.get((position - 1) % getRealCount()));
//            }
//
//        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mView.onStop();
                if (listener != null) {
                    listener.onItemClick(position % getRealCount(),articles.get(position % getRealCount()));
                }
                mView.onResume();
            }
        });

        return view;
    }

    public void clear(){
        if(viewGroup!=null){
            for(int i=0;i<viewGroup.getChildCount();i++){
                    ImageView img = (ImageView) viewGroup.getChildAt(i);
                    if (img != null) {
                        Drawable drawable = img.getDrawable();
                        if (drawable != null) {
                            if (drawable instanceof BitmapDrawable) {
                                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                                Bitmap bitmap = bitmapDrawable.getBitmap();
                                if (bitmap != null) {
                                    bitmap.recycle();
                                    bitmap = null;
                                }
                            }
                        }
                    }
                    img = null;
            }
        }
    };

    public abstract void loadImage(ImageView view,int position,String t);
    public abstract void setSubTitle(TextView textView,int position,String t);

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        Log.i("wzh","getRealCount: "+getRealCount());
        if(getRealCount()==0){
            return;
        }
        mView.onPageSelected(position % getRealCount());
        if (mView.getSubTitle() != null){
            if (position == 0){
                setSubTitle(mView.getSubTitle(),position,articles.get(position % getRealCount()).getTitle());
            }else {
                setSubTitle(mView.getSubTitle(),position,articles.get(position % getRealCount()).getTitle());
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface OnAutoViewPagerItemClickListener<T> {
        void onItemClick(int position, T t);
    }

}
