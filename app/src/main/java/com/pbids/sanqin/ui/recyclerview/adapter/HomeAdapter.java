package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseActivity;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.ui.activity.HomePageActivity;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.adapter.BaseViewPagerAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.AutoScrollViewPager;
import com.pbids.sanqin.ui.view.PopupAdView;
import com.pbids.sanqin.ui.view.ViewPagerScroller;
import com.pbids.sanqin.utils.TimeUtil;

import java.util.List;

/**
 * 旧的
 */

public class HomeAdapter extends GroupedRecyclerViewAdapter {
    List<NewsArticle> bannerList;
    List<NewsInformation> newsInformations;

    Context mContext;
    AutoScrollViewPager mViewPager;
    BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener;

    public HomeAdapter(Context context, List<NewsArticle> bannerList, List<NewsInformation> newsInformations) {
        super(context);
        mContext = context;
        this.bannerList = bannerList;
        this.newsInformations = newsInformations;
    }

    public List<NewsInformation> getNewsInformations() {
        return newsInformations;
    }

    @Override
    public int getGroupCount() {
        return newsInformations ==null?0:newsInformations.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return newsInformations ==null?0:newsInformations.get(groupPosition).getList().size();
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
    public int getChildLayout(int viewType) {
        Log.i("wzw","viewType: "+viewType);
        switch (viewType){
            case NewsArticle.ONE_TYPE:
                return R.layout.hp_news_one;
            case NewsArticle.TWO_TYPE:
                return R.layout.hp_news_ten;
            case NewsArticle.THREE_TYPE:
                return R.layout.hp_news_three;
            case NewsArticle.FOUR_TYPE:
                return R.layout.hp_news_two;
            case NewsArticle.FIVE_TYPE:
                return R.layout.hp_news_five;
//            case NewsArticle.SIX_TYPE:
//                break;
            case NewsArticle.SEVEN_TYPE:
                return R.layout.hp_news_eight;
//            case NewsArticle.EIGHT_TYPE:
//                break;
            case NewsArticle.NINE_TYPE:
                return R.layout.hp_news_seven;
            case NewsArticle.FOURTEEN_TYPE:
                return R.layout.hp_news_nine;
//            case NewsArticle.TWENTY_TYPE:
//                break;
            case NewsArticle.TWENTY_ONE_TYPE:
               return R.layout.hp_news_two;
        }
        return R.layout.hp_news_one;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        mViewPager = holder.get(R.id.viewPager);
        initBanner(bannerList);
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public int getChildViewType(int groupPosition, int childPosition) {
        return newsInformations==null?0:newsInformations.get(groupPosition).getList().get(childPosition).getViewType();
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        NewsInformation newsInformation = newsInformations.get(groupPosition);
        NewsArticle newsArticle = newsInformation.getList().get(childPosition);
        int viewType = newsArticle.getViewType();
        if(newsInformation.getIsmore()==1 && childPosition==(newsInformation.getList().size()-1)||
                (newsInformations.size()-1)==groupPosition && (newsInformation.getList().size()-1)==childPosition){
            Log.i("wzh","groupPosition :"+groupPosition);
            Log.i("wzh","childPosition :"+childPosition);
            Log.i("wzh","newsInformation.getIsmore() :"+newsInformation.getIsmore());

            View view = holder.get(R.id.home_item_decoration);
            view.setVisibility(View.GONE);
        }else{
            View view = holder.get(R.id.home_item_decoration);
            view.setVisibility(View.VISIBLE);
        }
//        Log.i("wzh","viewType: ");
        switch (viewType){
            case NewsArticle.ONE_TYPE:
                bindingOneType(mContext,newsArticle,holder);
                break;
            case NewsArticle.TWO_TYPE:
                bindingTwoType(mContext,newsArticle,holder);
                break;
            case NewsArticle.THREE_TYPE:
                bindingThreeType(mContext,newsArticle,holder);
                break;
            case NewsArticle.FOUR_TYPE:
                bindingFourType(mContext,newsArticle,holder);
                break;
            case NewsArticle.FIVE_TYPE:
                bindingFiveType(mContext,newsArticle,holder);
                break;
//            case NewsArticle.SIX_TYPE:
//                bindingSixType(mContext,newsArticle,holder);
//                break;
            case NewsArticle.SEVEN_TYPE:
                bindingSevenType(mContext,newsArticle,holder);
                break;
//            case NewsArticle.EIGHT_TYPE:
//                bindingEightType(mContext,newsArticle,holder);
//                break;
            case NewsArticle.NINE_TYPE:
                bindingNineType(mContext,newsArticle,holder);
                break;
            case NewsArticle.FOURTEEN_TYPE:
                bindingFourteenType(mContext,newsArticle,holder);
                break;
//            case NewsArticle.TWENTY_TYPE:
//                bindingTwentyType(mContext,newsArticle,holder);
//                break;
            case NewsArticle.TWENTY_ONE_TYPE:
                bindingFourType(mContext,newsArticle,holder);
//                bindingTwentyOneType(mContext,newsArticle,holder);
                break;
            default:
                bindingOneType(mContext,newsArticle,holder);
                break;
        }
    }

    private void bindingOneType(Context context, NewsArticle information, BaseViewHolder holder) {
        ImageView newsPicture = holder.get(R.id.home_im_first_picture);

        bindingCommonData(information,holder);
        if(information.getLitpicList().size()>0){
            new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,newsPicture,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//            Glide.with(mContext)
//                    .load(information.getLitpicList().get(0)).override(
//                    (int)mContext.getResources().getDimension(R.dimen.dp_146)
//                    ,(int)mContext.getResources().getDimension(R.dimen.dp_100))
//                    .placeholder(R.drawable.loading).error(R.drawable.loading)
//                    .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture);
        }
    }

    private void bindingTwoType(Context context,NewsArticle information,BaseViewHolder holder) {
        ImageView newsPicture1 =  holder.get(R.id.home_im_third_image1);
        ImageView newsPicture2 =  holder.get(R.id.home_im_third_image2);

        bindingCommonData(information,holder);
        int pictureSize = information.getLitpicList().size();
        if(pictureSize>0) {
            new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//            Glide.with(mContext).load(information.getLitpicList().get(0)).override(
//                    (int) mContext.getResources().getDimension(R.dimen.dp_165)
//                    , (int) mContext.getResources().getDimension(R.dimen.dp_112))
//                    .placeholder(R.drawable.loading).error(R.drawable.loading)
//                    .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture1);
            if (pictureSize > 1) {
                new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,newsPicture2,information.getLitpicList().get(1),R.drawable.loading,R.drawable.loading);
//                Glide.with(mContext).load(information.getLitpicList().get(1)).override(
//                        (int) mContext.getResources().getDimension(R.dimen.dp_165)
//                        , (int) mContext.getResources().getDimension(R.dimen.dp_112))
//                        .placeholder(R.drawable.loading).error(R.drawable.loading)
//                        .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture2);
            }
        }
    }

    private void bindingThreeType(Context context,NewsArticle information,BaseViewHolder holder) {
        ImageView newsPicture1 = holder.get(R.id.home_im_third_image1);
        ImageView newsPicture2 = holder.get(R.id.home_im_third_image2);
        ImageView newsPicture3 = holder.get(R.id.home_im_third_image3);

        bindingCommonData(information,holder);
        int pictureSize = information.getLitpicList().size();
        if(pictureSize>0){
            new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//            Glide.with(context).load(information.getLitpicList().get(0)).override(
//                    (int)context.getResources().getDimension(R.dimen.dp_110)
//                    ,(int)context.getResources().getDimension(R.dimen.dp_80))
//                    .placeholder(R.drawable.loading).error(R.drawable.loading)
//                    .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture1);
            if(pictureSize>1){
                new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,newsPicture2,information.getLitpicList().get(1),R.drawable.loading,R.drawable.loading);
//                Glide.with(context).load(information.getLitpicList().get(1)).override(
//                        (int)context.getResources().getDimension(R.dimen.dp_110)
//                        ,(int)context.getResources().getDimension(R.dimen.dp_80))
//                        .placeholder(R.drawable.loading).error(R.drawable.loading)
//                        .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture2);
            }
            if(pictureSize>2){
                new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,newsPicture3,information.getLitpicList().get(2),R.drawable.loading,R.drawable.loading);
//                Glide.with(context).load(information.getLitpicList().get(2)).override(
//                        (int)context.getResources().getDimension(R.dimen.dp_110)
//                        ,(int)context.getResources().getDimension(R.dimen.dp_80))
//                        .placeholder(R.drawable.loading).error(R.drawable.loading)
//                        .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture3);
            }
        }
    }

    private void bindingFourType(Context context,NewsArticle information,BaseViewHolder holder) {
        ImageView newsPlayImageBg = holder.get(R.id.home_im_second_play_bg);//第二页
        ImageView newsPlayImage = holder.get(R.id.home_im_second_play);

        bindingCommonData(information,holder);
        int viewType = information.getViewType();
        if(viewType==NewsArticle.FOUR_TYPE){
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPlayImage,R.drawable.information_icon_video_defalt,R.drawable.loading,R.drawable.loading);
//            Glide.with(context).load(R.drawable.information_icon_play_default).override(
//                    (int)context.getResources().getDimension(R.dimen.dp_100)
//                    ,(int)context.getResources().getDimension(R.dimen.dp_100)).into(newsPlayImage);
        }else if(viewType==NewsArticle.TWENTY_ONE_TYPE){
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPlayImage,R.drawable.information_icon_video_defalt);
//            Glide.with(context).load(R.drawable.information_icon_video_defalt).override(
//                    (int)context.getResources().getDimension(R.dimen.dp_100)
//                    ,(int)context.getResources().getDimension(R.dimen.dp_100)).into(newsPlayImage);
        }
        if(information.getLitpicList().size()>0){
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPlayImageBg,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//            Glide.with(context).load(information.getLitpicList().get(0)).override(
//                    (int)context.getResources().getDimension(R.dimen.dp_336)
//                    ,(int)context.getResources().getDimension(R.dimen.dp_160))
//                    .placeholder(R.drawable.loading).error(R.drawable.loading)
//                    .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPlayImageBg);
        }
    }

    private void bindingFiveType(Context context,NewsArticle information,BaseViewHolder holder) {
        TextView newsContent = holder.get(R.id.home_news_content);

        LinearLayout indexLayoutParent = holder.get(R.id.home_news_index_layout_parent);
        ImageView newsPicture1 = holder.get(R.id.home_im_five_image);

        TextView newsTime = holder.get(R.id.home_im_time);
//        Button button = holder.get(R.id.home_im_five_bt);

        newsContent.setText(information.getTitle());
        addIndexs(indexLayoutParent,information.getTags().trim(),context,information);
        if(information.getLitpicList().size()>0){
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//            Glide.with(context).load(information.getLitpicList().get(0)).override(
//                    (int)context.getResources().getDimension(R.dimen.dp_336)
//                    ,(int)context.getResources().getDimension(R.dimen.dp_120))
//                    .placeholder(R.drawable.loading).error(R.drawable.loading)
//                    .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture1);
        }
        newsTime.setText(TimeUtil.getTimeFormatText(information.getCreateTime()));
    }

    private void bindingSixType(Context context,NewsArticle information,BaseViewHolder holder) {}

    private void bindingSevenType(Context context, NewsArticle information, BaseViewHolder holder) {
        TextView title = holder.get(R.id.home_im_eight_content);
        ImageView newsPicture1= holder.get(R.id.home_im_eight_image);
        TextView newsTime = holder.get(R.id.home_im_time);
        ImageView button = holder.get(R.id.home_im_ad_bt);

        title.setText(information.getTitle());
        if(information.getLitpicList().size()>0){
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//            Glide.with(context).load(information.getLitpicList().get(0)).override(
//                    (int)context.getResources().getDimension(R.dimen.dp_336)
//                    ,(int)context.getResources().getDimension(R.dimen.dp_160))
//                    .placeholder(R.drawable.loading).error(R.drawable.loading)
//                    .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture1);
        }
        newsTime.setText(TimeUtil.getTimeFormatText(information.getCreateTime()));
        final NewsArticle newsArticle = information;
        final PopupAdView popupAdView = new PopupAdView((BaseActivity)context);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAdView.showUp(v);
//                    PopupAdView popupAdView = new PopupAdView((Activity) context);
//                    popupAdView.showUp(v);
            }
        });
    }

    private void bindingEightType(Context context,NewsArticle information,BaseViewHolder holder) {}

    private void bindingNineType(Context context,NewsArticle information,BaseViewHolder holder) {
        TextView newsContent = holder.get(R.id.home_news_content);
        ImageView newsPicture1 = holder.get(R.id.home_im_seven_imge1);
        ImageView newsPicture2 = holder.get(R.id.home_im_seven_imge2);
        ImageView newsPicture3 = holder.get(R.id.home_im_seven_imge3);

        newsContent.setText(information.getTitle());
        int pictureSize = information.getLitpicList().size();
        if(pictureSize>0){
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//            Glide.with(context).load(information.getLitpicList().get(0)).override(
//                    (int)context.getResources().getDimension(R.dimen.dp_110)
//                    ,(int)context.getResources().getDimension(R.dimen.dp_80))
//                    .placeholder(R.drawable.loading).error(R.drawable.loading)
//                    .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture1);
            if(pictureSize>1){
                new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture2,information.getLitpicList().get(1),R.drawable.loading,R.drawable.loading);
//                Glide.with(context).load(information.getLitpicList().get(1)).override(
//                        (int)context.getResources().getDimension(R.dimen.dp_110)
//                        ,(int)context.getResources().getDimension(R.dimen.dp_80))
//                        .placeholder(R.drawable.loading).error(R.drawable.loading)
//                        .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture2);
                if(pictureSize>2){
                    new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture3,information.getLitpicList().get(2),R.drawable.loading,R.drawable.loading);
//                    Glide.with(context).load(information.getLitpicList().get(2)).override(
//                            (int)context.getResources().getDimension(R.dimen.dp_110)
//                            ,(int)context.getResources().getDimension(R.dimen.dp_80))
//                            .placeholder(R.drawable.loading).error(R.drawable.loading)
//                            .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsPicture3);
                }
            }
        }
    }

    private void bindingFourteenType(Context context,NewsArticle information,BaseViewHolder holder) {
        ImageView newsNinePicture = holder.get(R.id.hp_news_nine_image);//第九页
        ImageView newsNineAdBt = holder.get(R.id.hp_news_nine_image_ad_bt);

        int pictureSize = information.getLitpicList().size();
        if(pictureSize>0){
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsNinePicture,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//            Glide.with(context).load(information.getLitpicList().get(0)).override(
//                    (int)context.getResources().getDimension(R.dimen.dp_360)
//                    ,(int)context.getResources().getDimension(R.dimen.dp_100))
//                    .placeholder(R.drawable.loading).error(R.drawable.loading)
//                    .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsNinePicture);
            if(pictureSize>1){
                new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsNineAdBt,R.drawable.information_buttom_guanggao_defalt,R.drawable.loading,R.drawable.loading);
//                Glide.with(context).load(R.drawable.information_buttom_guanggao_defalt).override(
//                        (int)context.getResources().getDimension(R.dimen.dp_54)
//                        ,(int)context.getResources().getDimension(R.dimen.dp_15))
//                        .placeholder(R.drawable.loading).error(R.drawable.loading)
//                        .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(newsNineAdBt);
            }
        }
        final PopupAdView popupAdView = new PopupAdView((BaseActivity) context);
        newsNineAdBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAdView.showUp(v);
            }
        });
    }

    private void bindingTwentyType(Context context,NewsArticle information,BaseViewHolder holder) {}

    private void bindingTwentyOneType(Context context,NewsArticle information,BaseViewHolder holder) {}


    public void bindingCommonData(NewsArticle information, BaseViewHolder holder){
        ImageView newsIcon = (ImageView) holder.get(R.id.home_news_icon);//1到3
        TextView newsTitle = (TextView) holder.get(R.id.home_news_title);
        TextView newsContent = (TextView) holder.get(R.id.home_news_content);

        LinearLayout indexLayoutParent = (LinearLayout) holder.get(R.id.home_news_index_layout_parent);

        TextView newsTime = (TextView) holder.get(R.id.home_im_time);
        TextView newsPeople = (TextView) holder.get(R.id.home_im_people);
        TextView newsForward = (TextView) holder.get(R.id.home_im_forward);
        TextView newsReward = (TextView) holder.get(R.id.home_im_reward);
        new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,newsIcon,information.getIcon(),R.drawable.loading,R.drawable.loading);
//        Glide.with(mContext).load(information.getIcon()).override(
//                (int) mContext.getResources().getDimension(R.dimen.dp_24)
//                ,(int)  mContext.getResources().getDimension(R.dimen.dp_24))
//                .placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default)
//                .dontAnimate().into(newsIcon);
        newsTitle.setText(information.getWriter());
        newsContent.setText(information.getTitle());
        newsTime.setText(TimeUtil.getTimeFormatText(information.getCreateTime()));
        newsPeople.setText(""+information.getClickNum());
        newsForward.setText(""+information.getFromNum());
        newsReward.setText(""+information.getRewordNum());
//        String[] indexs=null;
        String tags = information.getTags().trim();
        addIndexs(indexLayoutParent,tags,mContext,information);
    }

    public void addIndexs(LinearLayout indexLayoutParent,String tags,Context context,NewsArticle information){
        if(indexLayoutParent.getChildCount()!=0){
//            indexLayoutParent.removeViewAt();
//            Log.i("wzh","indexLayoutParent.getChildCount(): "+indexLayoutParent.getChildCount());
//            for(int i=0;i<indexLayoutParent.getChildCount();i++){
//                indexLayoutParent.removeViewAt(i);
//            }
            return;
        }
        if("".equals(tags.trim()) && "".equals(information.getSurname())){
            return;
        }
        if(!"".equals(information.getSurname()) && !"".equals(tags)){
            tags = information.getSurname()+","+tags;
        }else if("".equals(tags) && !"".equals(information.getSurname())){
            tags = information.getSurname();
        }
//        Log.i("wzh","information.getSurname(): "+information.getSurname());
//        Log.i("wzh","tags: "+tags);
        String[] indexs = tags.split("[,]");
        for (int j = 0; j < indexs.length; j++) {
            View indexView = LayoutInflater.from(context).inflate(R.layout.hp_news_index_layout, null);
            TextView indexText = (TextView) indexView.findViewById(R.id.hp_news_index_text);
            indexText.setText(indexs[j]);
            LinearLayout.LayoutParams params
                    = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (j != 0) {
                params.setMargins((int) context.getResources().getDimension(R.dimen.dp_4), 0, 0, 0);
                indexView.setLayoutParams(params);
            }
            indexLayoutParent.addView(indexView);
        }
    }

    public void startFragmentForLink(String url){
        ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
        fragment.getArguments().putString("link",url);
        HomePageActivity homePageActivity = (HomePageActivity)mContext;
        MainFragment mainFragment = homePageActivity.findFragment(MainFragment.class);
        mainFragment.start(fragment);
    }

    BaseViewPagerAdapter mBaseViewPagerAdapter;
    public void initBanner(List<NewsArticle> banners) {
        mBaseViewPagerAdapter = new BaseViewPagerAdapter<String>(mContext, listener) {
            @Override
            public void loadImage(ImageView view, int position, String url) {
                new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,view,url,R.drawable.loading,R.drawable.loading);
//                Glide.with(mContext).load(url).override((int)mContext.getResources().getDimension(R.dimen.dp_360)
//                        ,(int)mContext.getResources().getDimension(R.dimen.dp_180))
//                        .placeholder(R.drawable.loading).error(R.drawable.loading)
//                        .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(view);
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
        mViewPager.setAdapter(null);
        mBaseViewPagerAdapter.clear();
        mBaseViewPagerAdapter = null;
        mViewPager = null;
    }

//    BaseViewPagerAdapter.OnAutoViewPagerItemClickListener listener = new BaseViewPagerAdapter.OnAutoViewPagerItemClickListener<NewsArticle>() {
//
//        @Override
//        public void onItemClick(int position, NewsArticle newsArticle) {
////                Toast.makeText(ZhiZongFragment.this.getActivity(),
////                        position + " ========= "+ url, Toast.LENGTH_SHORT).show();
////            MyApplication.getUserInfo().toString();
//            ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
//            fragment.getArguments().putString("link",newsArticle.getLink());
//
////            ((MainFragment) getParentFragment()).start(fragment);
////            start(fragment);
//            Log.i("wzh","newsArticle: "+newsArticle.toString());
//        }
//    };
}
