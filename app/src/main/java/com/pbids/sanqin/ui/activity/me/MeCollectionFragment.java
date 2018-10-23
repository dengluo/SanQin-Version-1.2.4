package com.pbids.sanqin.ui.activity.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.db.SystemMessageManager;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.presenter.MeCollectionPresenter;
import com.pbids.sanqin.ui.adapter.MeCollectionAdapter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.eventbus.DeleteArticleFavorItemEvent;
import com.pbids.sanqin.utils.eventbus.SystemMessageHandleEvent;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pbids903 on 2017/11/29.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 14:19
 * @desscribe 类描述:我的收藏界面
 * @remark 备注:
 * @see
 */
public class MeCollectionFragment extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear,MeCollectionView{

    @Bind(R.id.me_collection_rv)
    RecyclerView meCollectionRv;

    MeCollectionPresenter meCollectionPresenter;


    public static MeCollectionFragment newInstance() {
        MeCollectionFragment fragment = new MeCollectionFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    View rootView;
    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.me_collection, container, false);
            ButterKnife.bind(this, rootView);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        EventBus.getDefault().register(this);
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterTextTitleRightImage("我的收藏",_mActivity);
    }

    //RelativeLayout relativeLayout;
    public void initView() {
        setContentLayoutGone();

        //list
        this.newsArticles = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        commonNewsAdapter = new MeCollectionAdapter(newsArticles, _mActivity);
        meCollectionRv.setLayoutManager(manager);
        meCollectionRv.setAdapter(commonNewsAdapter);
        meCollectionRv.addItemDecoration(new HorizontalDividerItemDecoration.Builder(_mActivity)
                .color(_mActivity.getResources().getColor(R.color.main_status_color))
                .sizeResId(R.dimen.dp_10)
                .build());

        HttpParams params = new HttpParams();
        params.put("uid",MyApplication.getUserInfo().getUserId());
        addDisposable(meCollectionPresenter.submitInformation(AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_FAVOR_LIST,params,"-2"));

    }

    @Override
    public BasePresenter initPresenter() {
        return meCollectionPresenter = new MeCollectionPresenter(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dispose();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    //收藏事件接收
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteItemEvent(DeleteArticleFavorItemEvent event){
        System.out.print(event);
        if(event!=null&& event.getArticle()!=null){
            if(event.getStatus()==1){
                //添加
                this.newsArticles.add(0,event.getArticle());
            }else {
                //删除
                for(NewsArticle item:newsArticles){
                    if(item.getId()== event.getArticle().getId()){
                        this.newsArticles.remove(item);
                        break;
                    }
                }
            }
            commonNewsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_right_layout:
                if(newsArticles==null || newsArticles.size()==0){
                    return;
                }
                final TextView titleText1 = (TextView) v.findViewById(R.id.me_title_right_text);
                final ImageView imageView = (ImageView) v.findViewById(R.id.me_title_right_image);
                if(titleText1.getVisibility() == View.INVISIBLE){
                    titleText1.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                }else{
                    titleText1.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.VISIBLE);
                }
                //Log.i("wzh","meCollectionRv: "+meCollectionRv.hashCode());
                //Log.i("wzh","commonNewsAdapter.newsArticles: "+commonNewsAdapter.newsArticles.hashCode());
                // int visibility = ((TextView)v).getVisibility();
                // if(visibility == View.VISIBLE){
                String ids = commonNewsAdapter.getDeleteNewsArticleIds();
                if(!ids.equals("")){
                    getLoadingPop("正在删除").show();
                    HttpParams params = new HttpParams();
                    params.put("uid",MyApplication.getUserInfo().getUserId());
                    params.put("ids",commonNewsAdapter.getDeleteNewsArticleIds());
                    Log.i("wzh","ids: "+commonNewsAdapter.getDeleteNewsArticleIds());
                    addDisposable(meCollectionPresenter.submitInformationDelete(AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_FAVOR_GET,params,"10"));
                    //Log.i("wzh","delete");
                }
                // }
                commonNewsAdapter.setDagouVisible();

                break;
        }
    }

    @Override
    public void onHttpSuccess(String type) {
        dismiss();
        if(type.equals("10")){
            List<NewsArticle> deleteNewsArticles = commonNewsAdapter.getDeleteNewsArticles();
            for(int i=0;i<deleteNewsArticles.size();i++){
                commonNewsAdapter.getNewsArticles().remove(deleteNewsArticles.get(i));
                deleteNewsArticles.get(i);
            }
            //Log.i("wzh","commonNewsAdapter.getNewsArticles():"+commonNewsAdapter.getNewsArticles().size());
            commonNewsAdapter.notifyDataSetChanged();
            commonNewsAdapter.clearDeleteNewsArticleIds();

   /*         for(int i=0;i<this.newsArticles.size();i++){
                commonNewsAdapter.getNewsArticles().remove(this.newsArticles.get(i));
            }
            commonNewsAdapter.notifyDataSetChanged();
            commonNewsAdapter.clearDeleteNewsArticleIds();*/
        }
    }

    @Override
    public void onHttpError(String type) {
        dismiss();
        if(type.equals("10")){
            commonNewsAdapter.clearDeleteNewsArticleIds();
        }
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    MeCollectionAdapter commonNewsAdapter;
    List<NewsArticle> newsArticles;

    @Override
    public void getNewsArticles(List<NewsArticle> newsArticles) {
        if(newsArticles.size()>0){
            setContentLayoutVisible();
        }else{
            setNoDataLayoutVisible();
            return;
        }
        this.newsArticles.clear();
        this.newsArticles.addAll(newsArticles) ;
        commonNewsAdapter.notifyDataSetChanged();

//        meCollectionRv.setNestedScrollingEnabled(false);
//        meCollectionRv.setHasFixedSize(true);
//        DividerItemDecoration decoration = new DividerItemDecoration(_mActivity,DividerItemDecoration.VERTICAL_LIST);
//        decoration.setDivider(_mActivity.getResources().getDrawable(R.drawable.divider_zh_rv));
//        decoration.setColor(_mActivity.getResources().getColor(R.color.main_status_color));
//        decoration.setmDividerHeight((int)_mActivity.getResources().getDimension(R.dimen.dp_10));

//        meCollectionRv.setFocusable(false);
//        meCollectionRv.setFocusableInTouchMode(false);
//        meCollectionRv.requestFocus();
//        meCollectionRv
    }
//    OnClearClickLisenear onClearClickLisenear;
//
//
//    public interface OnClearClickLisenear{
//        void setDagouVisible();
//        String getIds();
//    }
}
