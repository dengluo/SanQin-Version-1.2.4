package com.pbids.sanqin.ui.view.letter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.pbids.sanqin.R;

import java.util.List;

public class SortAdapter extends BaseSwipeAdapter implements SectionIndexer {
	private List<SortModel> sortModels = null;
	private Context mContext;
	
	public SortAdapter(Context mContext, List<SortModel> list){
		this.mContext = mContext;
		this.sortModels = list;
	}
	
	public void updateListView(List<SortModel> list){
		this.sortModels = list;
		notifyDataSetChanged();
	}

	public int getCount(){
		return this.sortModels.size();
	}

	public Object getItem(int position) {
		return sortModels.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getSwipeLayoutResourceId(int i) {
		return R.id.zongquan_firends_list_item_swip;
	}

	@Override
	public View generateView(int i, ViewGroup viewGroup) {
		return LayoutInflater.from(mContext).inflate(R.layout.zongquan_friends_list_item, null);
	}

	@Override
	public void fillValues(int position, View convertView) {
//		final SortModel mContent = sortModels.get(position);
//		TextView name = (TextView) convertView.findViewById(R.id.zongquan_fried_list_name);
//		TextView letter = (TextView) convertView.findViewById(R.id.catalog);
//		int section = getSectionForPosition(position);
//
//		if(position == getPositionForSection(section)){
//			letter.setVisibility(View.VISIBLE);
//			letter.setText(mContent.getSortLetters());
//		}else{
//			letter.setVisibility(View.GONE);
//		}
//
//		name.setText(this.sortModels.get(position).getName());
//		convertView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//			}
//		});
	}

//	public View getView(final int position, View view_donate_records, ViewGroup arg2) {
//		ViewHolder viewHolder = null;
//		final SortModel mContent = sortModels.get(position);
//		if (view_donate_records == null) {
//			viewHolder = new ViewHolder();
//			view_donate_records = LayoutInflater.from(mContext).inflate(R.layout.zongquan_friends_list_item, null);
//			viewHolder.tvTitle = (TextView) view_donate_records.findViewById(R.id.zongquan_fried_list_name);
//			viewHolder.tvLetter = (TextView) view_donate_records.findViewById(R.id.catalog);
//			view_donate_records.setTag(viewHolder);
//		} else {
//			viewHolder = (ViewHolder) view_donate_records.getTag();
//		}
//
//		int section = getSectionForPosition(position);
//
//		if(position == getPositionForSection(section)){
//			viewHolder.tvLetter.setVisibility(View.VISIBLE);
//			viewHolder.tvLetter.setText(mContent.getSortLetters());
//		}else{
//			viewHolder.tvLetter.setVisibility(View.GONE);
//		}
//
//		viewHolder.tvTitle.setText(this.sortModels.get(position).getName());
//
//		return view_donate_records;
//
//	}



	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}


	public int getSectionForPosition(int position) {
		return sortModels.get(position).getSortLetters().charAt(0);
	}

	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = sortModels.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		
		return -1;
	}
	
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}