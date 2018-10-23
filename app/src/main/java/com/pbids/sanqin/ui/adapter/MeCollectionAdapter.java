package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.ui.activity.HomePageActivity;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbids903 on 2017/12/20.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:43
 * @desscribe 类描述:我的收藏list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeCollectionFragment
 */
public class MeCollectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<NewsArticle> newsArticles;
    Context context;
    public int dagouVisible=0;
    ArrayList<NewsArticle> deleteNewsArticles = new ArrayList<>();
    String idsStr= "";
    String type="";

    public MeCollectionAdapter(List<NewsArticle> newsArticles, Context context){
        this.newsArticles = newsArticles;
        this.context = context;
    }

    public void updateView(List<NewsArticle> newsArticles){
        this.newsArticles = newsArticles;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_video,parent,false);
        RecyclerView.ViewHolder myViewHolder = new MeCollectionViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((MeCollectionViewHolder)holder).bindingData(newsArticles.get(position),context);
    }

    public void setType(String type){
        this.type = type;
    }

    @Override
    public int getItemCount() {
        return newsArticles.size();
    }
//    ArrayList<NewsArticle> newsArticles
    public void setDagouVisible(){
        if(dagouVisible==0){
            dagouVisible=1;
        }else if(dagouVisible==1){
            dagouVisible=0;
        }
//        this.newsArticles.clear();
//        this.newsArticles.addAll(newsArticles);
        this.notifyDataSetChanged();
//        notifyItemRangeChanged(0,getItemCount());
//        notifyItemInserted(getItemCount());
    }

    public String getDeleteNewsArticleIds(){
        String str="";
        for(int i=0;i<deleteNewsArticles.size();i++){
            if(i==0){
                str= str+deleteNewsArticles.get(0).getId();
            }else{
                str= str+","+deleteNewsArticles.get(i).getId();
            }
        }
        return str;
    }

    public List<NewsArticle> getDeleteNewsArticles(){
        return deleteNewsArticles;
    }

    public void clearDeleteNewsArticleIds(){
        deleteNewsArticles.clear();
    }

    public List<NewsArticle> getNewsArticles() {
        return newsArticles;
    }

    public void setNewsArticles(ArrayList<NewsArticle> newsArticles) {
        this.newsArticles = newsArticles;
    }

    public class MeCollectionViewHolder extends RecyclerView.ViewHolder{
        ImageView collectionDagou;
        TextView collectionTitle;
        TextView collectionRead;
        TextView collectionReward;
        TextView collectionForward;
        TextView tv1;
        TextView tv2;
        TextView tv3;

        ImageView collectionImage;
        ImageView collectionPlay;
        Button collectionCampaign;
        LinearLayout collectionIndexLayout;
        View itemView;
        public MeCollectionViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            collectionDagou = (ImageView) itemView.findViewById(R.id.collection_dagou);
            collectionTitle = (TextView) itemView.findViewById(R.id.collection_title);
            collectionRead = (TextView) itemView.findViewById(R.id.collection_read);
            collectionReward = (TextView) itemView.findViewById(R.id.collection_reward);
            collectionForward = (TextView) itemView.findViewById(R.id.collection_forward);

            tv1 = (TextView) itemView.findViewById(R.id.tv1);
            tv2 = (TextView) itemView.findViewById(R.id.tv2);
            tv3 = (TextView) itemView.findViewById(R.id.tv3);

            collectionImage = (ImageView) itemView.findViewById(R.id.collection_image);
            collectionPlay = (ImageView) itemView.findViewById(R.id.collection_play);

            collectionCampaign = (Button) itemView.findViewById(R.id.collection_campaign);
            collectionIndexLayout = (LinearLayout) itemView.findViewById(R.id.collection_index_layout);
        }

        public void bindingData(final NewsArticle information, final Context context){
            if("history".equals(type)){
                collectionRead.setVisibility(View.GONE);
                collectionReward.setVisibility(View.GONE);
                collectionForward.setVisibility(View.GONE);
                tv1.setVisibility(View.GONE);
                tv2.setVisibility(View.GONE);
                tv3.setVisibility(View.GONE);
            }
            collectionTitle.setText(information.getTitle());
            collectionRead.setText(""+information.getClickNum());
            collectionReward.setText(""+information.getRewordNum());
            collectionForward.setText(""+information.getFromNum());
            if(information.getLitpicList().size()>0){
                new CommonGlideInstance().setImageViewBackgroundForUrl(context,collectionImage,information.getLitpicList().get(0),R.drawable.loading,R.drawable.loading);
//                Glide.with(context).load(information.getLitpicList().get(0)).override(
//                        (int)context.getResources().getDimension(R.dimen.dp_100)
//                        ,(int)context.getResources().getDimension(R.dimen.dp_63))
//                        .placeholder(R.drawable.loading).error(R.drawable.loading)
//                        .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(collectionImage);
            }
            Log.i("wzh","dagouVisible: "+dagouVisible);
            if(dagouVisible==1){
                collectionDagou.setVisibility(View.VISIBLE);
            }else if(dagouVisible ==0){
                collectionDagou.setVisibility(View.GONE);
            }
            collectionDagou.setSelected(false);
            if(collectionIndexLayout.getChildCount()==0){
                addIndexs(collectionIndexLayout,context,information);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(collectionDagou.getVisibility() == View.VISIBLE){
                        if(!collectionDagou.isSelected()){
                            collectionDagou.setSelected(true);
                            deleteNewsArticles.add(information);
                        }else{
                            deleteNewsArticles.remove(information);
                            collectionDagou.setSelected(false);
                        }
                        return;
                    }
                    ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
                    fragment.getArguments().putString("link",information.getLink());
                    ((HomePageActivity)context).start(fragment);
                }
            });
//            collectionDagou.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(!collectionDagou.isSelected()){
//                        collectionDagou.setSelected(true);
//                        deleteNewsArticles.insert(information);
//                    }else{
//                        deleteNewsArticles.remove(information);
//                        collectionDagou.setSelected(false);
//                    }
//                }
//            });
            switch (information.getViewType()){
                case 4:
                    collectionPlay.setVisibility(View.VISIBLE);
                    break;
                case 5:
                    collectionCampaign.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    public void addIndexs(LinearLayout indexLayoutParent,Context context,NewsArticle information){
        //Log.i("wzh","information.getSurname(): "+information.getSurname());
        //Log.i("wzh","tags: "+information.getTags());
        String tags = information.getTags();
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
}
