package com.pbids.sanqin.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbids.sanqin.R;

/**
 * Created by pbids903 on 2017/12/5.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:58
 * @desscribe 类描述:公众话题订阅列表list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.news.NewsTopicListFragment
 */
public class NewsTopicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    RecyclerView recyclerView;

    @Override
    public int getItemViewType(int position) {
        if(position<2){
            return 0;
        }else{
            return 1;
        }
//        return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType==0){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_topic_list_item,parent,false);
            return new NewsTop1ViewHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_topic_list_item2,parent,false);
            return new NewsTop2ViewHolder(view);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class NewsTop1ViewHolder extends RecyclerView.ViewHolder{

        public NewsTop1ViewHolder(View itemView) {
            super(itemView);
        }
    }
    public class NewsTop2ViewHolder extends RecyclerView.ViewHolder{

        public NewsTop2ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
