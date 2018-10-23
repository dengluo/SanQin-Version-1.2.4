package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonGlideInstance;

import java.util.ArrayList;

/**
 * Created by pbids903 on 2017/11/11.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:39
 * @desscribe 类描述:博古架弹窗viewpager适配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.MainFragment
 */
public class HomePageVPNameAdapter extends PagerAdapter{

    ArrayList<View> mViews = new ArrayList<>();
    Context mContext;
    OnVpNameClickLisener vpNameClickLisener;
    ArrayList<String> surnameIds;
    ArrayList<String> surnames;

    public HomePageVPNameAdapter(Context context,ArrayList<String> surnames,ArrayList<String> surnameIds){
        this.mContext = context;
        this.surnameIds = surnameIds;
        this.surnames = surnames;
        initView();
    }//220

    private void initView() {
        mViews.clear();
        for (int i = 0; i < surnames.size(); i++) {
            View mNameVPItem = LayoutInflater.from(mContext).inflate(R.layout.hp_vp_name_scond, null);
            mViews.add(mNameVPItem);
        }
    }

    public void updateInfomation(ArrayList<String> surnames,ArrayList<String> surnameIds){
        this.surnameIds = surnameIds;
        this.surnames = surnames;
        initView();
//        notifyDataSetChanged();
       // Log.i("wzh","notifyDataSetChanged");
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
    public Object instantiateItem(ViewGroup container, final int position) {//必须实现，实例化
        ViewHolder mViewHolder;
        final View view = mViews.get(position);

        mViewHolder = new ViewHolder();
        mViewHolder.im = (ImageView)view.findViewById(R.id.hp_vp_name_im);
        mViewHolder.historyPeople = (ImageView)view.findViewById(R.id.hp_history_people);
        mViewHolder.customs = (ImageView)view.findViewById(R.id.hp_customs);
        mViewHolder.reilc = (ImageView)view.findViewById(R.id.hp_history_reilc);
        mViewHolder.nowPeople = (ImageView)view.findViewById(R.id.hp_now_people);
        mViewHolder.jiaxun = (ImageView)view.findViewById(R.id.hp_jiaxun);
        mViewHolder.name = (TextView)view.findViewById(R.id.hp_vp_name_name);
        mViewHolder.fanily_news = (ImageView)view.findViewById(R.id.hp_fanily_news);
        mViewHolder.nameLayout = (LinearLayout) view.findViewById(R.id.hp_vp_name_layout);


        Resources resources = mContext.getResources();
//        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/华文行楷.ttf");
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/汉仪颜楷繁.ttf");
        //Log.i("wzh","mViewHolder.name.getText().length(): "+mViewHolder.name.getText().length());
        if(surnames.get(position).length()==1){
            mViewHolder.name.setLineSpacing(0f, 1.5f);
        }
        mViewHolder.name.setTextSize(resources.getDimension(R.dimen.sp_11));

        mViewHolder.name.setEms(1);
        mViewHolder.name.getPaint().setTypeface(typeface);
        String s = (String)view.getTag();
        //Log.i("wzh","name: "+s);
        mViewHolder.name.setText(surnames.get(position)+"府");
//        mViewHolder.name.setText("速度"+"府");

        new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,mViewHolder.im,R.drawable.bogujia_bg);
        new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,mViewHolder.historyPeople,R.drawable.bogujia_button_jiazuhuihuang_defalt);
        new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,mViewHolder.customs,R.drawable.bogujia_button_jiafeng_defalt);
        new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,mViewHolder.reilc,R.drawable.renjie2x);
        new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,mViewHolder.nowPeople,R.drawable.bogujia_button_jiashirenwen_defalt);
        new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,mViewHolder.jiaxun,R.drawable.zhixing2x);
        new CommonGlideInstance().setImageViewBackgroundForUrl(mContext,mViewHolder.fanily_news,R.drawable.bogujia_button_jiazuyiji_defalt);

        mViewHolder.nameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != vpNameClickLisener){
                    vpNameClickLisener.nameClick(v,surnames.get(position),surnameIds.get(position));
                }
            }
        });

        mViewHolder.historyPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != vpNameClickLisener){
                    vpNameClickLisener.otherClick(view);
                }
            }
        });
        mViewHolder.customs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != vpNameClickLisener){
                    vpNameClickLisener.otherClick(view);
                }
            }
        });
        mViewHolder.reilc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != vpNameClickLisener){
                    vpNameClickLisener.otherClick(view);
                }
            }
        });
        mViewHolder.jiaxun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != vpNameClickLisener){
                    vpNameClickLisener.otherClick(view);
                }
            }
        });
        mViewHolder.fanily_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != vpNameClickLisener){
                    vpNameClickLisener.otherClick(view);
                }
            }
        });
        mViewHolder.nowPeople.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null != vpNameClickLisener){
                    vpNameClickLisener.otherClick(view);
                }
            }
        });

        container.addView(mViews.get(position));

        return mViews.get(position);
    }

    public void setVpNameClickLisener(OnVpNameClickLisener vpNameClickLisener){
        this.vpNameClickLisener = vpNameClickLisener;
    }

    public interface OnVpNameClickLisener {
        void nameClick(View v,String surname,String surnameId);
        void otherClick(View v);
    }

    class ViewHolder {
        ImageView im;
        ImageView historyPeople;
        ImageView customs;
        ImageView reilc;
        ImageView nowPeople;
        ImageView jiaxun;
        TextView name;
        ImageView fanily_news;
        LinearLayout nameLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {//必须实现，销毁
        if(position>=mViews.size()){
            return;
        }
        container.removeView(mViews.get(position));
    }

}
