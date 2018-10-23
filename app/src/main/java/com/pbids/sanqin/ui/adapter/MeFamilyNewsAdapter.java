package com.pbids.sanqin.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:44
 * @desscribe 类描述:家族资讯list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeFamilyNewsFragment
 */
public class MeFamilyNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<NewsArticle> newsArticles;
    private OnItemClickListenerUtil clickListenerUtil;

    public MeFamilyNewsAdapter(List<NewsArticle> newsArticles){
        this.newsArticles = newsArticles;
        if(this.newsArticles==null){
            this.newsArticles = new ArrayList<>();
        }
        clickListenerUtil = new OnItemClickListenerUtil();
    }

    public List<NewsArticle> getNewsArticles(){
        return this.newsArticles;
    }

    @Override
    public MeFamilyNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_family_item,parent,false);
        return new MeFamilyNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ((MeFamilyNewsViewHolder)holder).bindingDate(newsArticles.get(position));
        ((MeFamilyNewsViewHolder)holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListenerUtil.mOnItemClickListener!=null){
                    clickListenerUtil.mOnItemClickListener.onClick(v,position);
                    NewsArticle arc = newsArticles.get(position);
                    arc.setClickNum(arc.getClickNum()+1);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListenerUtil.OnItemClickListener onItemClickListener){
        clickListenerUtil.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }

    public class MeFamilyNewsViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView read;
        TextView shared;
        TextView carsh;
        View itemView;

        public MeFamilyNewsViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = (TextView) itemView.findViewById(R.id.me_family_title);
            read = (TextView) itemView.findViewById(R.id.me_family_read);
            shared = (TextView) itemView.findViewById(R.id.me_family_shared);
            carsh = (TextView) itemView.findViewById(R.id.me_family_carsh);
        }

        public void bindingDate(NewsArticle newsArticle){
            title.setText("" + newsArticle.getTitle());
            read.setText("" + newsArticle.getClickNum());
            shared.setText("" + newsArticle.getFromNum());
            carsh.setText("" + newsArticle.getRewordNum());
        }
    }
}
