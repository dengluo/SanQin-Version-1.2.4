package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.text.InputFilter;
import android.util.Log;
import android.widget.TextView;

import com.pbids.sanqin.R;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.model.entity.CampaignEnrollEntity;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.List;

/**
 * Created by pbids903 on 2018/1/30.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:04
 * @desscribe 类描述:活动报名详情list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollOverFragment
 */
public class CampaignEnrollOverAdapter extends GroupedRecyclerViewAdapter {
    List<CampaignEnrollEntity> entities;
    private int cound=0;
    InputFilter nameFilter;
    InputFilter sexFilter;

    public CampaignEnrollOverAdapter(Context context, List<CampaignEnrollEntity> entities) {
        super(context);
        this.entities = entities;
    }

    public List<CampaignEnrollEntity> getEntities() {
        return entities;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return entities==null?0:entities.size();
//        return cound;
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
        return R.layout.adapter_campaign_enroll_over;
    }

    @Override
    public void onBindHeaderViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindFooterViewHolder(BaseViewHolder holder, int groupPosition) {

    }

    @Override
    public void onBindChildViewHolder(BaseViewHolder holder, int groupPosition, final int childPosition) {
        Log.i("wzh","childPosition: "+childPosition);
        CampaignEnrollEntity enrollEntity = entities.get(childPosition);

        TextView nameTv = holder.get(R.id.name_tv);
        TextView idcardTv =holder.get(R.id.idcard_tv);
        TextView sexTv =holder.get(R.id.sex_tv);

        nameTv.setText(enrollEntity.getUsername());
        idcardTv.setText(enrollEntity.getIdNumber());
        sexTv.setText(enrollEntity.getSex());
    }
}
