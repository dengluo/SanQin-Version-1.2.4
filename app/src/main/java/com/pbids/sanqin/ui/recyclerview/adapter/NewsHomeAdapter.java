package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.ui.adapter.BaseViewPagerAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AutoScrollViewPager;
import com.pbids.sanqin.ui.view.ViewPagerScroller;

import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:05
 * @desscribe 类描述:首页的资讯列表list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.ZhiZongFragment
 */
public class NewsHomeAdapter extends BaseNewsAdapter{

    List<NewsArticle> bannerList;
    List<NewsInformation> newsInformations;

    Context mContext;
    AutoScrollViewPager mViewPager;
    BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener;

    public NewsHomeAdapter(Context context, List<NewsArticle> bannerList, List<NewsInformation> newsInformations) {
        super(context, bannerList, newsInformations);
        this.bannerList= bannerList;
        this.newsInformations = newsInformations;
        mContext = context;
    }

    @Override
    public boolean hasHeader(int groupPosition) {
        return newsInformations==null?false:groupPosition==0?true:false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return newsInformations==null?false:newsInformations.get(groupPosition).getIsmore()==1?true:false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.home_viewpager_hear;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return R.layout.hp_news_more;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        mViewPager = holder.get(R.id.viewPager);
        initBanner(bannerList);
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        super.onBindChildViewHolder(holder, groupPosition, childPosition);
        NewsInformation newsInformation = newsInformations.get(groupPosition);
        NewsArticle newsArticle = newsInformation.getList().get(childPosition);
        if(newsInformation.getIsmore()==1 && childPosition==(newsInformation.getList().size()-1)||
                (newsInformations.size()-1)==groupPosition && (newsInformation.getList().size()-1)==childPosition){
            View view = holder.get(R.id.home_item_decoration);
            view.setVisibility(View.GONE);
        }else{
            View view = holder.get(R.id.home_item_decoration);
            view.setVisibility(View.VISIBLE);
        }
    }

    BaseViewPagerAdapter mBaseViewPagerAdapter;
    public void initBanner(List<NewsArticle> banners) {
        mBaseViewPagerAdapter = new BaseViewPagerAdapter<String>(mContext, listener) {
            @Override
            public void loadImage(ImageView view, int position, String url) {
                new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,view,url,R.drawable.loading,R.drawable.loading);
                view.setTag(url);
            }

            @Override
            public void setSubTitle(TextView textView, int position, String s) {
                textView.setText(s);
            }
        };
        mViewPager.setAdapter(mBaseViewPagerAdapter);
        if(banners!=null){
            new ViewPagerScroller(mContext).initViewPagerScroll(mViewPager.getViewPager());
            mBaseViewPagerAdapter.add(banners);
            mViewPager.getViewPager().setCurrentItem(Integer.MAX_VALUE / 2);
        }
    }

    public void setOnAutoViewPagerItemClickListener(BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener){
        this.listener = listener;
    }

    public void clearQuote(){
        if(mViewPager!=null){
            mViewPager.setAdapter(null);
            mBaseViewPagerAdapter.clear();
            mBaseViewPagerAdapter = null;
            mViewPager = null;
        }
    }
}
