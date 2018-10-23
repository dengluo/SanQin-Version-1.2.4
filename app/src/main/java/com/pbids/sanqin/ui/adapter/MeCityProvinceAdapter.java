package com.pbids.sanqin.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.model.entity.Province;
import com.pbids.sanqin.utils.OnItemClickListenerUtil;

import java.util.ArrayList;

/**
 * Created by pbids903 on 2017/12/15.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:41
 * @desscribe 类描述:省份选择list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.me.MeProvinceSelectorFragment
 */
public class MeCityProvinceAdapter extends RecyclerView.Adapter<MeCityProvinceAdapter.MeCityProvinceViewHolder>{
    ArrayList<Province> mProvinceNameList;
    OnItemClickListenerUtil clickListenerUtil;

    public MeCityProvinceAdapter(ArrayList<Province> provinceNameList){
        mProvinceNameList = provinceNameList;
        clickListenerUtil = new OnItemClickListenerUtil();
    }

    public ArrayList<Province> getmProvinceNameList() {
        return mProvinceNameList;
    }

    public void setmProvinceNameList(ArrayList<Province> mProvinceNameList) {
        this.mProvinceNameList = mProvinceNameList;
    }

    @Override
    public MeCityProvinceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.me_city_item,parent,false);

        return new MeCityProvinceViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListenerUtil.OnItemClickListener onItemClickListener){
        clickListenerUtil.setOnItemClickListener(onItemClickListener);
    }


    @Override
    public void onBindViewHolder(MeCityProvinceViewHolder holder, final int position) {
        holder.textView.setText(mProvinceNameList.get(position).getProvinceName());
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clickListenerUtil.mOnItemClickListener!=null){
                    clickListenerUtil.mOnItemClickListener.onClick(v,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mProvinceNameList!=null){
            return mProvinceNameList.size();
        }else{
            return 0;
        }
    }

    public class MeCityProvinceViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MeCityProvinceViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.me_city_item_tv);
        }
    }


}
