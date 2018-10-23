package com.pbids.sanqin.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.view.VerticalImageSpan;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pbids903 on 2017/12/6.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:39
 * @desscribe 类描述:我的活动list适配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeCampaignFragment
 */
public class MeCampaignListAdapter extends RecyclerView.Adapter<MeCampaignListAdapter.MeCanpaignViewHolder>{
    ArrayList<NewsArticle> newsArticles;
    OnItemClickListenerUtil clickListenerUtil;
    public MeCampaignListAdapter(ArrayList<NewsArticle> newsArticles){
        this.newsArticles = newsArticles;
        clickListenerUtil = new OnItemClickListenerUtil();
    }

    @Override
    public MeCampaignListAdapter.MeCanpaignViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_campaign_list_item,parent,false);
        return new MeCanpaignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeCampaignListAdapter.MeCanpaignViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListenerUtil.mOnItemClickListener!=null){
                    clickListenerUtil.mOnItemClickListener.onClick(v,position);
                }
            }
        });
        holder.bandingData(newsArticles.get(position),position);
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }

    public void setOnItemClickListener(OnItemClickListenerUtil.OnItemClickListener onItemClickListener){
        clickListenerUtil.setOnItemClickListener(onItemClickListener);
    }

    public ArrayList<NewsArticle> getNewsArticles(){
        return newsArticles;
    }

    public void updateList(ArrayList<NewsArticle> newsArticles){
        this.newsArticles = newsArticles;
        notifyDataSetChanged();
    }

    public class MeCanpaignViewHolder extends RecyclerView.ViewHolder{
        TextView content;
        TextView time;
        View itemView;
        TextView name;
        TextView itemBt;

        public MeCanpaignViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            content = (TextView) itemView.findViewById(R.id.me_campaign_list_item_content);
            time = (TextView) itemView.findViewById(R.id.me_campaign_list_item_time);
            name = (TextView) itemView.findViewById(R.id.me_campaign_list_item_name);
            itemBt = (TextView) itemView.findViewById(R.id.me_campaign_list_item_bt);
        }

        public void bandingData(NewsArticle newsArticle, final int position){
            Drawable drawable = null;
            newsArticle.getFinish();
            if(newsArticle.getFinish()==1){
                drawable=itemView.getContext().getResources().getDrawable(R.drawable.huodong_icon_yijieshu_default);
            }else{
                if(newsArticle.getIsPay()==1){
                    drawable=itemView.getContext().getResources().getDrawable(R.drawable.huodong_icon_yizhifu_default);
                }else{
                    drawable=itemView.getContext().getResources().getDrawable(R.drawable.huodong_icon_shenhezhong_default);
                }
            }
            SpannableStringBuilder builder = new SpannableStringBuilder();
            int startPoint = builder.length();
            builder.append("[abstract]");//占位使用
            int endPoint = builder.length();
//            drawable=itemView.getContext().getResources().getDrawable(R.drawable.huodong_icon_shenhezhong_default);
            drawable.setBounds(0, 0, (int)itemView.getContext().getResources().getDimension(R.dimen.dp_38),
                    (int)itemView.getContext().getResources().getDimension(R.dimen.dp_20));
            VerticalImageSpan span = new VerticalImageSpan(drawable);
            builder.setSpan(span, startPoint, endPoint, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(" ");
            builder.append(newsArticle.getTitle());
            content.setText(builder);

            name.setText(newsArticle.getWriter());
//            content.setText(newsArticle.getTitle());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            time.setText(simpleDateFormat.format(new Date(newsArticle.getCreateTime())));

            itemBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListenerUtil.mOnItemClickListener.onLongClick(v,position);
                }
            });
        }
    }
}
