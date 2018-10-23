package com.pbids.sanqin.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.District;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;

import java.util.ArrayList;

/**
 * Created by pbids903 on 2017/12/15.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:40
 * @desscribe 类描述:城市选择list适配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeCitySelectorFragment
 */
public class MeCityCityAdapter extends RecyclerView.Adapter<MeCityCityAdapter.MeCityCityViewHolder>{
    ArrayList<District> mDistrictList;
    OnItemClickListenerUtil onItemClickListenerUtil;

    public MeCityCityAdapter(ArrayList<District> mDistrictList){
        this.mDistrictList = mDistrictList;
        onItemClickListenerUtil = new OnItemClickListenerUtil();
    }

    @Override
    public MeCityCityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_city_item,parent,false);
        return new MeCityCityViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListenerUtil.OnItemClickListener onItemClickListener){
        onItemClickListenerUtil.setOnItemClickListener(onItemClickListener);
    }

    @Override
    public void onBindViewHolder(MeCityCityViewHolder holder, final int position) {
        holder.textView.setText(mDistrictList.get(position).getDistrictName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListenerUtil.mOnItemClickListener.onClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mDistrictList!=null){
            return mDistrictList.size();
        }else {
            return 0;
        }
    }

    public class MeCityCityViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MeCityCityViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.me_city_item_tv);
        }
    }
}
