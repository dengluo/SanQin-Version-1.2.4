package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.NewsTopicSubscribe;
import com.pbids.sanqin.ui.recyclerview.adapter.base.SwipeGroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.group.NewsTopicSubscribeGroup;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.recyclerview.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:48
 * @desscribe 类描述:我订阅的公众号消息列表list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.news.NewsSystemMassageFragment
 */
public class MeTopicSubscribeAdapter extends SwipeGroupedRecyclerViewAdapter  implements SectionIndexer {

    //private RecyclerView mRecyclerView;

    private List<NewsTopicSubscribeGroup> groupList = new ArrayList<>();
    //关注的标签组
    NewsTopicSubscribeGroup tagsGroup = new NewsTopicSubscribeGroup();
    List<NewsTopicSubscribe> tagsList = new ArrayList<>();

    //关注的姓氏组
    NewsTopicSubscribeGroup surnameGroup = new NewsTopicSubscribeGroup();
    List<NewsTopicSubscribe>  surnameList = new ArrayList<>();

    public MeTopicSubscribeAdapter(Context context) {
        super(context);

        surnameGroup.setLists(surnameList);
        surnameGroup.setTitle("姓氏");
        groupList.add(surnameGroup);

        tagsGroup.setLists(tagsList);
        tagsGroup.setTitle("话题");
        groupList.add(tagsGroup);
    }

    public List<NewsTopicSubscribe> getTagsList(){
        return tagsList;
    }

	public List<NewsTopicSubscribe> getSurnamesList(){
		return surnameList;
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
        NewsTopicSubscribeGroup item = groupList.get(groupPosition);
        if(null==item.getTitle()||item.getTitle().isEmpty()){
            return false;
        }
        return true;
    }

    @Override
    public boolean hasFooter(int groupPosition) {
        return false;
    }

    @Override
    public int getHeaderLayout(int viewType) {
        return R.layout.me_topic_subscribe_group_header;
    }

    @Override
    public int getFooterLayout(int viewType) {
        return 0;
    }

    @Override
    public int getChildLayout(int viewType) {
        return R.layout.me_topic_subscribe_list_item;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {
        TextView groupHeader =  holder.get(R.id.tv_title);
        groupHeader.setText(groupList.get(groupPosition).getTitle());
    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }
	@Override
	public int getSwipeLayoutResourceId(int i) {
		return R.id.swip_me_topic_subscribe_list_item;
	}

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, final int groupPosition, final int childPosition) {
        NewsTopicSubscribe message = getMessage(groupPosition,childPosition);
        TextView tvName =  holder.get(R.id.tv_name);
//        ImageView readFag =  holder.get(R.id.img_news_read_fag);
//        readFag.setVisibility(View.INVISIBLE);
		if(message.getTopicType()==NewsTopicSubscribe.TOPIC_TYPE_SURNAME){
			tvName.setText(message.getOrganization()+"宗亲会");
		}else{
			tvName.setText(message.getTitle());
		}

		View topLine = holder.get(R.id.top_line);
		if(childPosition==0){
			topLine.setVisibility(View.VISIBLE);
		}else{
			topLine.setVisibility(View.GONE);
		}

		SwipeLayout swipeLayout =  holder.get(R.id.swip_me_topic_subscribe_list_item);
		swipeLayout.setCustomOnClickListener(new SwipeLayout.CustomOnClickListener() {
			@Override
			public void onClick() {
//					Log.i("wzh","click=-=-===");
				closeAllItems();
			}
		});
		//delete
		LinearLayout delete =  holder.get(R.id.message_center_item_delete);
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				closeAllItems();
				NewsTopicSubscribe sel = getMessage(groupPosition,childPosition);
				deleteListItemListener.deleteItem(sel);
			}
		});

		mItemManger.bindView(holder.itemView,getPositionForChild(groupPosition,childPosition));
    }

    public NewsTopicSubscribe getMessage(int groupPosition, int childPosition){
        return groupList.get(groupPosition).getLists().get(childPosition);
    }


	@Override
	public Object[] getSections() {
		return new Object[0];
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		return -1;
	}

	@Override
	public int getSectionForPosition(int i) {
		return -1;
	}

	private DeleteListItemListener deleteListItemListener;
	public void setDeleteListItemListener(DeleteListItemListener delitem){
		deleteListItemListener = delitem;
	}

	public interface DeleteListItemListener {
    	void deleteItem(NewsTopicSubscribe one);
	}
}
