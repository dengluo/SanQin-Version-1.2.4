package com.pbids.sanqin.ui.recyclerview.adapter;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.pbids.sanqin.R;
import com.pbids.sanqin.common.OnOccurrenceLisenear;
import com.pbids.sanqin.component.IdCardET;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.model.entity.CampaignEnrollEntity;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pbids903 on 2018/1/30.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:03
 * @desscribe 类描述:活动报名的随从人员list设配器
 * @remark 备注:
 * @see com.pbids.sanqin.ui.activity.zhizong.CampaignEnrollFragment
 */
public class CampaignEnrollAdapter extends GroupedRecyclerViewAdapter {
    List<CampaignEnrollEntity> entities;
    private int cound=0;
    InputFilter nameFilter;
    InputFilter sexFilter;
    Context mContext;

    public CampaignEnrollAdapter(Context context
//            , List<CampaignEnrollEntity> entities
    ) {
        super(context);
        mContext = context;
        entities = new ArrayList<>();
        nameFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String regEx = "^[\\u4e00-\\u9fa5]*$";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(source.toString());
                if(m.matches()){
                    return source.toString();
                }else {
                    return "";
                }
            }
        };
        sexFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
//                String regEx = "^[\\u4e00-\\u9fa5]*$";
//                Pattern p = Pattern.compile(regEx);
//                Matcher m = p.matcher(source.toString());

                if("男".equals(source.toString()) || "女".equals(source.toString())){
                    return source.toString();
                }else {
                    return "";
                }
            }
        };
    }

    public List<CampaignEnrollEntity> getEntities() {
        return entities;
    }

    public void addData() {
//      在list中添加数据，并通知条目加入一条
        entities.add(new CampaignEnrollEntity());
        notifyDataSetChanged();
//        notifyItemInserted(entities.size()-1);
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
        return R.layout.adapter_campaign_enroll;
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

        final EditText nameEt = holder.get(R.id.name_et);
        final IdCardET idcardEt =holder.get(R.id.idcard_et);
//        final EditText sexEt =holder.get(R.id.sex_et);
        final Spinner sexSpinner = holder.get(R.id.sex_spinner);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(mContext, R.array.sex, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpinner.setAdapter(spinnerAdapter);



        View view = holder.get(R.id.delete);

        nameEt.setFilters(new InputFilter[]{nameFilter,new InputFilter.LengthFilter(6)});
//        sexEt.setFilters(new InputFilter[]{sexFilter,new InputFilter.LengthFilter(1)});
        Log.i("wzh","enrollEntity.getName():"+ enrollEntity.getUsername());
        Log.i("wzh","enrollEntity.getIdcard():"+ enrollEntity.getIdcard());
        Log.i("wzh","enrollEntity.getSex():"+ enrollEntity.getSex());
        Log.i("wzh","enrollEntity:"+ enrollEntity.toString());
        nameEt.setText(enrollEntity.getUsername());
        idcardEt.setText(enrollEntity.getIdcard());
        switch (enrollEntity.getSex()){
            case "男":
                sexSpinner.setSelection(0);
                break;
            case "女":
                sexSpinner.setSelection(1);
                break;
        }
//        sexEt.setText(enrollEntity.getSex());
        final int finalChildPosition = childPosition;
//        TextWatcher nameTextWatcher =  new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(childPosition>=entities.size()){
//                    return;
//                }
//                Log.i("wzh","finalChildPosition:"+ finalChildPosition);
//                CampaignEnrollEntity enrollEntity = entities.get(finalChildPosition);
//                enrollEntity.setName(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        };
//        nameEt.addTextChangedListener(nameTextWatcher);
//        TextWatcher idcardTextWatcher =  new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(childPosition>=entities.size()){
//                    return;
//                }
//                CampaignEnrollEntity enrollEntity = entities.get(finalChildPosition);
//                enrollEntity.setIdcard(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        };
//        idcardEt.addTextChangedListener(idcardTextWatcher);
//        TextWatcher sexTextWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(childPosition>=entities.size()){
//                    return;
//                }
//                CampaignEnrollEntity enrollEntity = entities.get(finalChildPosition);
//                enrollEntity.setSex(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        };
//        sexEt.addTextChangedListener(sexTextWatcher);

        nameEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(childPosition>=entities.size()){
                    return;
                }
                if(!hasFocus){
                    CampaignEnrollEntity enrollEntity = entities.get(finalChildPosition);
                    enrollEntity.setUsername(nameEt.getText().toString().trim());
                }
            }
        });

        idcardEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(childPosition>=entities.size()){
                    return;
                }
                if(!hasFocus){
                    CampaignEnrollEntity enrollEntity = entities.get(finalChildPosition);
                    enrollEntity.setIdcard(idcardEt.getText().toString().trim());
                }
            }
        });

        sexSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    CampaignEnrollEntity enrollEntity = entities.get(finalChildPosition);
                    switch (i){
                        case 0:
                            enrollEntity.setSex("男");
                            break;
                        case 1:
                            enrollEntity.setSex("女");
                            break;
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        sexEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if(childPosition>=entities.size()){
//                    return;
//                }
//                if(!hasFocus){
//                    CampaignEnrollEntity enrollEntity = entities.get(finalChildPosition);
//                    enrollEntity.setSex(sexEt.getText().toString().trim());
//                }
//            }
//        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("wzh","entities: "+entities.size());
                Log.i("wzh","finalChildPosition: "+finalChildPosition);
//                nameEt.removeTextChangedListener(nameTextWatcher);
//                idcardEt.removeTextChangedListener(idcardTextWatcher);
//                sexEt.removeTextChangedListener(sexTextWatcher);
                onOccurrenceLisenear.onOccurrence();
                entities.remove(finalChildPosition);
//                CampaignEnrollAdapter.this.notifyItemRemoved(finalChildPosition);
                CampaignEnrollAdapter.this.notifyItemRangeChanged(0,entities.size()-1);
                notifyDataSetChanged();
                onOccurrenceLisenear.onOccurrenceAfter();
            }
        });
    }

    public OnOccurrenceLisenear onOccurrenceLisenear;

    public void setOnOccurrenceLisenear(OnOccurrenceLisenear onOccurrenceLisenear) {
        this.onOccurrenceLisenear = onOccurrenceLisenear;
    }
}
