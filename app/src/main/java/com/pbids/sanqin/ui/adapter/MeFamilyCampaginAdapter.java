package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;

import java.util.List;



/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:43
 * @desscribe 类描述:家族活动list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeFamilyCampaignFragment
 */
public class MeFamilyCampaginAdapter extends RecyclerView.Adapter<MeFamilyCampaginAdapter.MeFamilyCampaginViewHolder> {
    List<NewsArticle> newsArticles;
    Context context;
    OnItemClickListenerUtil clickListenerUtil;
    public MeFamilyCampaginAdapter(List<NewsArticle> newsArticles, Context context){
        this.context = context;
        this.newsArticles = newsArticles;
        clickListenerUtil = new OnItemClickListenerUtil();
    }

    @Override
    public MeFamilyCampaginViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_family_campagin_rv_item,parent,false);
        return new MeFamilyCampaginViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeFamilyCampaginViewHolder holder, final int position) {
        holder.bindingDate(newsArticles.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListenerUtil.mOnItemClickListener!=null){
                    clickListenerUtil.mOnItemClickListener.onClick(v,position);
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

    public class MeFamilyCampaginViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView content;
        TextView time;
        TextView number;
        Button button;
        View itemView;

        public MeFamilyCampaginViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            title = (TextView) itemView.findViewById(R.id.me_family_campagin_item_title);
            content = (TextView) itemView.findViewById(R.id.me_family_campagin_item_content);
            time = (TextView) itemView.findViewById(R.id.me_family_campagin_item_time);
            number = (TextView) itemView.findViewById(R.id.me_family_campagin_item_number);
            button = (Button) itemView.findViewById(R.id.me_family_campagin_item_bt);
        }

        public void bindingDate(NewsArticle newsArticle){
            title.setText(newsArticle.getTitle());
//            String ss = "描述东东的基本信息";
            content.setText("活动简介:"+newsArticle.getSubTitle());
            time.setText("活动时间:"+newsArticle.getStartTimeFormat()+"-"+newsArticle.getOverTimeFormat());
            number.setText(newsArticle.getPepleNum()+"");

            if(newsArticle.getFinish()==0){
                button.setText("进行中");
                button.setBackgroundResource(R.drawable.selector_app_comfirm);
            }else if(newsArticle.getFinish()==1){
                button.setText("已结束");
                button.setBackgroundResource(R.drawable.selector_app_cancel);
            }
        }
    }
}
