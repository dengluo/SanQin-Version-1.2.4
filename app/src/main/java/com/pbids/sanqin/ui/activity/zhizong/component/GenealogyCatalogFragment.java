package com.pbids.sanqin.ui.activity.zhizong.component;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseFragment;
import com.pbids.sanqin.model.entity.Catlog;
import com.pbids.sanqin.model.entity.GenealogyInformation;
import com.pbids.sanqin.presenter.GenealogyCatalogPresenter;
import com.pbids.sanqin.utils.eventbus.OnClickGenealogCatalogEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author caiguoliang
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:我的族谱界面 ->组件-排行
 * @remark 备注:
 * @see
 */

@SuppressLint("ValidFragment")
public class GenealogyCatalogFragment extends BaaseFragment<GenealogyCatalogPresenter> implements GenealogyCatalogView {

    @Bind(R.id.tv_catalog)
    TextView tvCatalog;


    List<TextView> catologTvList;
    private GenealogyInformation genealogyInformation;

//    GenealogyView genealogyView;
//
//
//    //instance
//
//    public GenealogyCatalogFragment(GenealogyView genealogyView){
//        this.genealogyView = genealogyView;
//    }

    public static GenealogyCatalogFragment newInstance(GenealogyInformation genealogyInformation){
        GenealogyCatalogFragment fragment = new GenealogyCatalogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GenealogyPaperFragment.DATA,genealogyInformation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public GenealogyCatalogPresenter initPresenter() { return  new GenealogyCatalogPresenter(); }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_genealogy_catalog, container, false);
        initData();
        //设置背景
        ImageView mbg = (ImageView)view.findViewById(R.id.img_mbg);
        Glide.with(_mActivity).load(R.drawable.new_bg_mulu_default).into(mbg);

        ButterKnife.bind(this, view);
        catologTvList = new ArrayList<>();

        catologTvList.add((TextView) view.findViewById(R.id.tv6));
        catologTvList.add((TextView) view.findViewById(R.id.tv5));
        catologTvList.add((TextView) view.findViewById(R.id.tv4));
        catologTvList.add((TextView) view.findViewById(R.id.tv3));
        catologTvList.add((TextView) view.findViewById(R.id.tv2));

        catologTvList.add((TextView) view.findViewById(R.id.tv11));
        catologTvList.add((TextView) view.findViewById(R.id.tv10));
        catologTvList.add((TextView) view.findViewById(R.id.tv9));
        catologTvList.add((TextView) view.findViewById(R.id.tv8));
        catologTvList.add((TextView) view.findViewById(R.id.tv7));

        initView();
        return view;
    }

    private void initData() {
        Bundle arguments = getArguments();
        genealogyInformation = (GenealogyInformation) arguments.getSerializable(GenealogyPaperFragment.DATA);
    }


    public void initView() {
        //字体初始化
        initTypeface();

        //设置目录字体
        tvCatalog.getPaint().setTypeface(typeface);

        for (int i=0;i<catologTvList.size();i++){
            TextView tv = catologTvList.get(i);
            tv.setVisibility(View.INVISIBLE);
            //设置字体
            tv.getPaint().setTypeface(typeface);
        }
        //viewHeight = ScreenUtils.getScreenHeight() - (int) getResources().getDimension(R.dimen.dp_40) - StatusBarUtils.getStatusBarHeight(_mActivity);
        //pageFactory = new PageFactory(_mActivity,ScreenUtils.getScreenWidth(),viewHeight,50);
        if (genealogyInformation.getGenealogy()>0){
            List<Catlog> catlogs = genealogyInformation.getGenealogy_list();
            for(int i=0;i<catlogs.size();i++) {
                Catlog item = catlogs.get(i);
                if(i<catologTvList.size()){
                    TextView tv = catologTvList.get(i);
                    tv.setText(item.getCatlog());
                    tv.setVisibility(View.VISIBLE);

                    final int p = i;
                    final Catlog catlog = item;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //点击目录
                            OnClickGenealogCatalogEvent evt = new OnClickGenealogCatalogEvent();
                            evt.setPosition(p+2);
                            evt.setCatlog(catlog);
                            EventBus.getDefault().post(evt);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
