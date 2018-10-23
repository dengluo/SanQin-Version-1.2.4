package com.pbids.sanqin.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pbids.sanqin.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerSanqinGroupAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater li;
    private List<String> dataList;

    private ViewHolder vHolder;

    public SpinnerSanqinGroupAdapter(Context ctx, List<String> dataList) {
        this.mContext = ctx;
        this.li = LayoutInflater.from(ctx);
        this.dataList = dataList;
        if(this.dataList==null){
            this.dataList = new ArrayList<>();
        }
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView  = View.inflate(mContext, R.layout.sanqin_group_spinner_item, null);
            new ViewHolder(convertView);
        }
        // get convertView's holder
        vHolder = (ViewHolder) convertView.getTag();
        vHolder.tvName.setText(getItem(position) );
        return convertView;
    }

    class ViewHolder  {
        TextView tvName;


        public ViewHolder(View convertView){
            tvName = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(this);//set a viewholder
        }
    }


}
