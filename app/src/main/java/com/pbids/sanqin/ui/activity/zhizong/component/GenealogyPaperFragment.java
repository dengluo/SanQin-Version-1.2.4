package com.pbids.sanqin.ui.activity.zhizong.component;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pbids.sanqin.R;
import com.pbids.sanqin.base.BaaseFragment;
import com.pbids.sanqin.model.entity.Catlog;
import com.pbids.sanqin.model.entity.GenealogyInformation;
import com.pbids.sanqin.presenter.GenealogyPaperPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author caiguoliang
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:我的族谱界面 ->组件-翻页
 * @remark 备注:
 * @see
 */

@SuppressLint("ValidFragment")
public class GenealogyPaperFragment extends BaaseFragment<GenealogyPaperPresenter> {

    @Bind(R.id.paper_genealogy)
    GenealogyPaper mPaper;

//    GenealogyView genealogyView;

    private GenealogyPaperAdapter adapter;

    private GenealogyInfoFragment infoFragment; //信息
    private GenealogyCatalogFragment catalogFragment; //目录
    private GenealogyInformation genealogyInformation;

    public static final String DATA = "genealogy";

    /*public GenealogyPaperFragment(GenealogyView genealogyView){
        this.genealogyView = genealogyView;
    }*/

    public static GenealogyPaperFragment newInstance(GenealogyInformation genealogyInformation){
        GenealogyPaperFragment fragment = new GenealogyPaperFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DATA,genealogyInformation);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected GenealogyPaperPresenter initPresenter() {
        return new GenealogyPaperPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_genealogy_paper, container, false);
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    //获取数据
    private void initData() {
        Bundle arguments = getArguments();
        genealogyInformation = (GenealogyInformation) arguments.getSerializable(DATA);
    }

    public void setPage(int n){
        if (n != mPaper.getCurrentItem()) {
            mPaper.setCurrentItem(n);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

    public void initView() {
        //init papers
        //**** 优化启动速度 初始化显示第一个 完成再后创建其他
        infoFragment = GenealogyInfoFragment.newInstance(genealogyInformation);
        adapter = new GenealogyPaperAdapter( getChildFragmentManager() , new ArrayList<Fragment>());
        mPaper.setAdapter(adapter);

        adapter.addPaper(infoFragment);
        //adapter.notifyDataSetChanged();
        if (genealogyInformation != null) {
            if (genealogyInformation.getGenealogy()>0) {
                //有内容
                catalogFragment = GenealogyCatalogFragment.newInstance(genealogyInformation);
                adapter.addPaper(catalogFragment);
                List<Catlog> catlogs = genealogyInformation.getGenealogy_list();
                for (int i = 0; i < catlogs.size(); i++) {
                    Catlog item = catlogs.get(i);
                    GenealogyWebFragment webFragment = new GenealogyWebFragment(item);
                    adapter.addPaper(webFragment);
                }
            }
        }
        adapter.notifyDataSetChanged();
        //初始化显示第一个页面
        mPaper.setCurrentItem(0);


       /* //延时创建其它界面
        Flowable.timer(300, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {

                if (genealogyView == null) {
                    return;
                }
                GenealogyInformation infoData = genealogyView.getData();
                if (infoData.getGenealogy()>0) {
                    //有内容
                    catalogFragment = new GenealogyCatalogFragment(genealogyView);
                    adapter.addPaper(catalogFragment);
                    List<Catlog> catlogs = infoData.getGenealogy_list();
                    for (int i = 0; i < catlogs.size(); i++) {
                        Catlog item = catlogs.get(i);
                        GenealogyWebFragment webFragment = new GenealogyWebFragment(item);
                        adapter.addPaper(webFragment);
                    }
                    //线程不能直接更新ui
                    _mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

            }
        });*/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mPaper!=null){
            mPaper.removeAllViews();
            mPaper = null;
        }
        ButterKnife.unbind(this);
    }

}
