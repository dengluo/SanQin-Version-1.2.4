package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
//import com.bumptech.glide.request.animation.DrawableCrossFadeFactory;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseActivity;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.model.entity.NewsInformation;
import com.pbids.sanqin.ui.activity.HomePageActivity;
import com.pbids.sanqin.ui.activity.MainFragment;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.PopupAdView;
import com.pbids.sanqin.utils.TimeUtil;

import java.util.List;


public class BaseNewsAdapter extends GroupedRecyclerViewAdapter {


    protected List<NewsArticle> bannerList;
    protected List<NewsInformation> newsInformations;

    Context mContext;

    public BaseNewsAdapter(Context context, List<NewsArticle> bannerList, List<NewsInformation> newsInformations) {
        super(context);
        mContext = context;
        this.bannerList = bannerList;
        this.newsInformations = newsInformations;
    }

    public BaseNewsAdapter(Context context,List<NewsInformation> newsInformations){
        super(context);
        mContext = context;
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
        return false;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return 0;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
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
//        mViewPager = holder.get(R.id.viewPager);
//        initBanner(bannerList);
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public int getChildViewType(int groupPosition, int childPosition) {
        if (newsInformations == null || groupPosition >= newsInformations.size()) {
            return 0;
        }
        List<NewsArticle> childList = newsInformations.get(groupPosition).getList();
        if (childList == null || childPosition >= childList.size()) {
            return 0;
        }
        return  childList.get(childPosition).getViewType();
    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {
        NewsInformation newsInformation = newsInformations.get(groupPosition);
        int size = newsInformation.getList().size();
        if(size!=0){
            NewsArticle newsArticle = newsInformation.getList().get(childPosition);
            int viewType = newsArticle.getViewType();
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
                    bindingFiveType(mContext,newsArticle,holder,groupPosition,childPosition);
                    break;
//            case NewsArticle.SIX_TYPE:
//                bindingSixType(mContext,newsArticle,holder);
//                break;
                case NewsArticle.SEVEN_TYPE:
                    bindingSevenType(mContext,newsArticle,holder,groupPosition,childPosition);
                    break;
//            case NewsArticle.EIGHT_TYPE:
//                bindingEightType(mContext,newsArticle,holder);
//                break;
                case NewsArticle.NINE_TYPE:
                    bindingNineType(mContext,newsArticle,holder);
                    break;
                case NewsArticle.FOURTEEN_TYPE:
                    bindingFourteenType(mContext,newsArticle,holder,groupPosition,childPosition);
                    break;
//            case NewsArticle.TWENTY_TYPE:
//                bindingTwentyType(mContext,newsArticle,holder);
//                break;
                case NewsArticle.TWENTY_ONE_TYPE:
                    bindingFourType(mContext,newsArticle,holder);
//                bindingTwentyOneType(mContext,newsArticle,holder);
                    break;
                default:
                    bindingOneType(mContext, newsArticle, holder);
                    break;
            }
        }else{
            //TODO 暂时解决闪退问题,多线程问题，可正常加载数据
//            Toast.makeText(mContext,"没有数据，请重新搜索！",Toast.LENGTH_SHORT).show();
        }
    }

    private void bindingOneType(Context context, NewsArticle information, BaseViewHolder holder) {
        ImageView newsPicture = holder.get(R.id.home_im_first_picture);

        bindingCommonData(information,holder);
        if(information.getLitpicList().size()>0){
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(
                    context,newsPicture,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);

        }
    }

    private void bindingTwoType(Context context,NewsArticle information,BaseViewHolder holder) {
        ImageView newsPicture1 =  holder.get(R.id.home_im_third_image1);
        ImageView newsPicture2 =  holder.get(R.id.home_im_third_image2);

        bindingCommonData(information,holder);
        int pictureSize = information.getLitpicList().size();
        if(pictureSize>0) {
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);

            if (pictureSize > 1) {
                //设置图片
                new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture2,information.getLitpicList().get(1),R.drawable.loading,R.drawable.loading);
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
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
            if(pictureSize>1){
                //设置图片
                new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture2,information.getLitpicList().get(1),R.drawable.loading,R.drawable.loading);
            }
            if(pictureSize>2){
                //设置图片
                new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPicture3,information.getLitpicList().get(2),R.drawable.loading,R.drawable.loading);
            }
        }
    }

    private void bindingFourType(Context context,NewsArticle information,BaseViewHolder holder) {
        ImageView newsPlayImageBg = holder.get(R.id.home_im_second_play_bg);//第二页
        ImageView newsPlayImage = holder.get(R.id.home_im_second_play);

        bindingCommonData(information,holder);
        int viewType = information.getViewType();
        if(viewType==NewsArticle.FOUR_TYPE){
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPlayImage,R.drawable.information_icon_play_default);
        }else if(viewType==NewsArticle.TWENTY_ONE_TYPE){
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(context,newsPlayImage,R.drawable.information_icon_video_defalt);
        }
        if(information.getLitpicList().size()>0){
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(
                    context,newsPlayImageBg,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
        }
    }

    private void bindingFiveType(Context context, NewsArticle information
            , final BaseViewHolder holder, final int groupPosition, final int childPosition) {
        TextView newsContent = holder.get(R.id.home_news_content);

        LinearLayout indexLayoutParent = holder.get(R.id.home_news_index_layout_parent);
        ImageView newsPicture1 = holder.get(R.id.home_im_five_image);

        TextView newsTime = holder.get(R.id.home_im_time);
        Button button = holder.get(R.id.home_im_five_bt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getOnChildClickListener()!=null){
                    getOnChildClickListener().onChildClick(BaseNewsAdapter.this,holder,groupPosition,childPosition);
                }
            }
        });

        newsContent.setText(information.getTitle());
        addIndexs(indexLayoutParent,information.getTags().trim(),context,information);
        if(information.getLitpicList().size()>0){
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(
                    context,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
        }
        newsTime.setText(TimeUtil.getTimeFormatText(information.getCreateTime()));
    }

    private void bindingSixType(Context context,NewsArticle information,BaseViewHolder holder) {}

    private void bindingSevenType(final Context context, final NewsArticle information
            , BaseViewHolder holder, final int groupPosition, final int childPosition) {
        TextView title = holder.get(R.id.home_im_eight_content);
        ImageView newsPicture1= holder.get(R.id.home_im_eight_image);
        TextView newsTime = holder.get(R.id.home_im_time);
        ImageView button = holder.get(R.id.home_im_ad_bt);

        title.setText(information.getTitle());
        if(information.getLitpicList().size()>0){
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(
                    context,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);

        }
        newsTime.setText(TimeUtil.getTimeFormatText(information.getCreateTime()));
        final NewsArticle newsArticle = information;
        final PopupAdView popupAdView = new PopupAdView((BaseActivity)context);
        popupAdView.setOnDialogClickLisenrar(new OnDialogClickListener() {
            @Override
            public void confirm( View view) {
                popupAdView.dismiss();
                final SharedPreferences sp = context.getSharedPreferences("sanqin", Context.MODE_PRIVATE);
                String ad_not_interest = sp.getString("ad_not_interest","");
                if(ad_not_interest.equals("")){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ad_not_interest",information.getId()+"");
                    editor.commit();
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ad_not_interest",ad_not_interest+","+information.getId());
                    editor.commit();
                }

                int position = getPositionForChild(groupPosition,childPosition);
                newsInformations.get(groupPosition).getList().remove(childPosition);
                notifyItemRemoved(position);
                if (position != getItemCount()) {
                    notifyItemRangeChanged(position, getItemCount() - position);
                }
                if(newsInformations.get(groupPosition).getList().size()==0){
                    newsInformations.remove(groupPosition);
                    notifyItemRemoved(position-1);
                    if (position != getItemCount()) {
                        notifyItemRangeChanged(position-1, getItemCount() - position);
                    }
                }
            }

            @Override
            public void cancel(View view) {
                popupAdView.dismiss();
            }
        });
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
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(
                    context,newsPicture1,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);

            if(pictureSize>1){
                //设置图片
                new CommonGlideInstance().setImageViewBackgroundForUrl(
                        context,newsPicture2,information.getLitpicList().get(1),R.drawable.loading,R.drawable.loading);

                if(pictureSize>2){
                    //设置图片
                    new CommonGlideInstance().setImageViewBackgroundForUrl(
                            context,newsPicture3,information.getLitpicList().get(2),R.drawable.loading,R.drawable.loading);

                }
            }
        }
    }

    private void bindingFourteenType(final Context context, final NewsArticle information, BaseViewHolder holder
            , final int groupPosition, final int childPosition) {
        ImageView newsNinePicture = holder.get(R.id.hp_news_nine_image);//第九页
        ImageView newsNineAdBt = holder.get(R.id.hp_news_nine_image_ad_bt);

        int pictureSize = information.getLitpicList().size();
        if(pictureSize>0){
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(
                    context,newsNinePicture,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);

            if(pictureSize>1){
                //设置图片
                new CommonGlideInstance().setImageViewBackgroundForUrl(
                        context,newsNineAdBt,R.drawable.information_buttom_guanggao_defalt);

            }
        }
        final PopupAdView popupAdView = new PopupAdView((BaseActivity) context);
        popupAdView.setOnDialogClickLisenrar(new OnDialogClickListener() {
            @Override
            public void confirm(View view) {
                popupAdView.dismiss();
                final SharedPreferences sp = context.getSharedPreferences("sanqin", Context.MODE_PRIVATE);
                String ad_not_interest = sp.getString("ad_not_interest","");
                if(ad_not_interest.equals("")){
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ad_not_interest",information.getId()+"");
                    editor.commit();
                }else{
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ad_not_interest",ad_not_interest+","+information.getId());
                    editor.commit();
                }

                int position = getPositionForChild(groupPosition,childPosition);
                newsInformations.get(groupPosition).getList().remove(childPosition);
                notifyItemRemoved(position);
                if (position != getItemCount()) {
                    notifyItemRangeChanged(position, getItemCount() - position);
                }
                if(newsInformations.get(groupPosition).getList().size()==0){
                    newsInformations.remove(groupPosition);
                    notifyItemRemoved(position-1);
                    if (position != getItemCount()) {
                        notifyItemRangeChanged(position-1, getItemCount() - position);
                    }
                }
            }

            @Override
            public void cancel(View view) {
                popupAdView.dismiss();
            }
        });
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



        //@ liang  添加
        //姓氏和姓氏图标
        String organization = information.getOrganization();
        String surnameIcon = information.getSurnameIcon();
        if(null==organization || organization.isEmpty() || null==surnameIcon || surnameIcon.isEmpty()){
            newsIcon.setVisibility(View.GONE);
            newsTitle.setVisibility(View.GONE);
        }else{
            newsIcon.setVisibility(View.VISIBLE);
            newsTitle.setVisibility(View.VISIBLE);
            //标题
            newsTitle.setText(information.getOrganization());
            //设置图片
            new CommonGlideInstance().setImageViewBackgroundForUrl(
                    mContext,newsIcon,information.getSurnameIcon(),R.drawable.me_avatar_moren_default,R.drawable.me_avatar_moren_default);

        }



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
        Log.i("wzh","information.getSurname(): "+information.getSurname());
        Log.i("wzh","tags: "+information.getTags());
        if(indexLayoutParent.getChildCount()!=0){
//            indexLayoutParent.removeViewAt();
//            Log.i("wzh","indexLayoutParent.getChildCount(): "+indexLayoutParent.getChildCount());
//            for(int i=0;i<indexLayoutParent.getChildCount();i++){
//
//            }
            indexLayoutParent.removeAllViews();
//            return;
        }
        if("".equals(tags.trim()) && "".equals(information.getSurname())){
            return;
        }
        if(!"".equals(information.getSurname()) && !"".equals(tags)){
            tags = information.getSurname()+","+tags;
        }else if("".equals(tags) && !"".equals(information.getSurname())){
            tags = information.getSurname();
        }

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

}
