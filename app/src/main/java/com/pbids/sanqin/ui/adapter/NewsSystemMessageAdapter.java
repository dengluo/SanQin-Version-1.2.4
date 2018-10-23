package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.SystemMessage;
import com.pbids.sanqin.ui.recyclerview.adapter.base.SwipeGroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.group.NewsSystemMessageGroup;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.recyclerview.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:48
 * @desscribe 类描述:消息列表list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.news.NewsSystemMassageFragment
 */
public class NewsSystemMessageAdapter extends SwipeGroupedRecyclerViewAdapter {

    private RecyclerView mRecyclerView;
    private Context mContext;

    private List<NewsSystemMessageGroup> groupList = new ArrayList<>();
    NewsSystemMessageGroup messageGroup = new NewsSystemMessageGroup();
    List<SystemMessage>  messageList = new ArrayList<>();

    public NewsSystemMessageAdapter(Context context) {
        super(context);
        this.mContext = context;
        this.messageGroup.setLists(messageList);
        this.groupList.add(messageGroup);
    }

    public List<SystemMessage> getMessageList(){
        return  messageList;
    }

    @Override
    public int getGroupCount() {
        return null==groupList?0:groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupList ==null?0:groupList.get(groupPosition).getLists().size();
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
        return R.layout.news_system_massage_item;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, int childPosition) {

        final SystemMessage message = getMessage(groupPosition,childPosition);
        TextView txtContent =  holder.get(R.id.txt_content);
        ImageView readFag =  holder.get(R.id.img_news_read_fag);
        txtContent.setText(message.getTitle());
        // 处理未读标记
        if(message.getIsread()){
            readFag.setVisibility(View.INVISIBLE);
        }else {
            readFag.setVisibility(View.VISIBLE);
        }

        //增加标签和姓氏显示
        ImageView surnameIconIv = holder.get(R.id.surname_icon_iv);
        TextView tagsTv = holder.get(R.id.tags_tv);
        String surnameIcon = message.getSurnameIcon();
        String tagsStr = message.getTags();
        String[] tags = null;
        if(tagsStr!=null&&!tagsStr.equals("")){
            tags = message.getTags().split(",");
        }
        if(surnameIcon!=null&&!surnameIcon.equals("")){
            surnameIconIv.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(message.getSurnameIcon()).into(surnameIconIv);
        }else{
            surnameIconIv.setVisibility(View.GONE);
        }
//        if (tags != null||!tags.equals("")) {
        if (tags != null) {
            tagsTv.setVisibility(View.VISIBLE);
            if (tags.length > 0) {
                tagsTv.setText(tags[0]);
            }
        }else{
            tagsTv.setVisibility(View.GONE);
        }

        SwipeLayout swipeLayout = holder.get(R.id.swip_me_topic_subscribe_list_item);
        swipeLayout.setCustomOnClickListener(new SwipeLayout.CustomOnClickListener() {
            @Override
            public void onClick() {
//					Log.i("wzh","click=-=-===");
                closeAllItems();
            }
        });

        LinearLayout deleteLin = holder.get(R.id.delete_lin);
        deleteLin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeAllItems();
                sideslipDelelte.sideslipDelete(v,message);
            }
        });

        mItemManger.bindView(holder.itemView,getPositionForChild(groupPosition,childPosition));


    }

    public SystemMessage getMessage(int groupPosition, int childPosition){
        return groupList.get(groupPosition).getLists().get(childPosition);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swip_me_topic_subscribe_list_item;
    }

    private SideslipDelelte sideslipDelelte;

    public void setSideslipDelelteListener(SideslipDelelte sideslipDelelte) {
        this.sideslipDelelte = sideslipDelelte;
    }

    public interface SideslipDelelte{
        void sideslipDelete(View view,SystemMessage message);
    }
}
