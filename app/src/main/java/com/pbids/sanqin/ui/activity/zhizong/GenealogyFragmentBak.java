package com.pbids.sanqin.ui.activity.zhizong;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.common.CommonGlideInstance;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.common.OnDialogClickListener;
import com.pbids.sanqin.common.ToolbarFragment;
import com.pbids.sanqin.model.entity.Catlog;
import com.pbids.sanqin.model.entity.GenealogyInfo;
import com.pbids.sanqin.model.entity.GenealogyInformation;
import com.pbids.sanqin.model.entity.GenealogyRank;
import com.pbids.sanqin.presenter.GenealogyBakPresenter;
import com.pbids.sanqin.presenter.GenealogyPresenter;
import com.pbids.sanqin.ui.view.AppToolBar;
import com.pbids.sanqin.ui.view.GenealogyCatalogPop;
import com.pbids.sanqin.ui.view.curl.CurlPage;
import com.pbids.sanqin.ui.view.curl.CurlView;
import com.pbids.sanqin.ui.view.pager.BaseReadView;
import com.pbids.sanqin.utils.NumberUtil;
import com.pbids.sanqin.utils.ScreenUtils;
import com.pbids.sanqin.utils.StatusBarUtils;
import com.pbids.sanqin.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:08
 * @desscribe 类描述:我的族谱界面
 * @remark 备注:
 * @see
 */
public class GenealogyFragmentBak extends ToolbarFragment implements AppToolBar.OnToolBarClickLisenear, GenealogyBakView {

    @Bind(R.id.pager_first_rangking)
    TextView pagerFirstRangking;
    @Bind(R.id.pager_first_rangking_image)
    ImageView pagerFirstRangkingImage;
    @Bind(R.id.pager_first_rangking_content)
    TextView pagerFirstRangkingContent;
    @Bind(R.id.pager_first_rangking_money)
    TextView pagerFirstRangkingMoney;
    @Bind(R.id.pager_first_rangking_up)
    ImageView pagerFirstRangkingUp;
    @Bind(R.id.pager_first_rangking_layout)
    RelativeLayout pagerFirstRangkingLayout;
    @Bind(R.id.pager_second_rangking)
    TextView pagerSecondRangking;
    @Bind(R.id.pager_second_rangking_image)
    ImageView pagerSecondRangkingImage;
    @Bind(R.id.pager_second_rangking_content)
    TextView pagerSecondRangkingContent;
    @Bind(R.id.pager_second_rangking_money)
    TextView pagerSecondRangkingMoney;
    @Bind(R.id.pager_second_rangking_up)
    ImageView pagerSecondRangkingUp;
    @Bind(R.id.pager_second_rangking_layout)
    RelativeLayout pagerSecondRangkingLayout;
    @Bind(R.id.pager_third_rangking)
    TextView pagerThirdRangking;
    @Bind(R.id.pager_third_rangking_image)
    ImageView pagerThirdRangkingImage;
    @Bind(R.id.pager_third_rangking_content)
    TextView pagerThirdRangkingContent;
    @Bind(R.id.pager_third_rangking_money)
    TextView pagerThirdRangkingMoney;
    @Bind(R.id.pager_third_rangking_up)
    ImageView pagerThirdRangkingUp;
    @Bind(R.id.pager_third_rangking_layout)
    RelativeLayout pagerThirdRangkingLayout;
    @Bind(R.id.pager_fourth_rangking)
    TextView pagerFourthRangking;
    @Bind(R.id.pager_fourth_rangking_image)
    ImageView pagerFourthRangkingImage;
    @Bind(R.id.pager_fourth_rangking_content)
    TextView pagerFourthRangkingContent;
    @Bind(R.id.pager_fourth_rangking_money)
    TextView pagerFourthRangkingMoney;
    @Bind(R.id.pager_fourth_rangking_up)
    ImageView pagerFourthRangkingUp;
    @Bind(R.id.pager_fourth_rangking_layout)
    RelativeLayout pagerFourthRangkingLayout;
    @Bind(R.id.rangking_layout)
    LinearLayout rangkingLayout;
    @Bind(R.id.information_page_layout)
    FrameLayout informationPageLayout;
    @Bind(R.id.tv2)
    View tv2;
    @Bind(R.id.tv3)
    View tv3;
    @Bind(R.id.tv4)
    View tv4;
    @Bind(R.id.tv5)
    View tv5;
    @Bind(R.id.tv6)
    View tv6;
    @Bind(R.id.tv7)
    View tv7;
    @Bind(R.id.tv8)
    View tv8;
    @Bind(R.id.tv9)
    View tv9;
    @Bind(R.id.tv10)
    View tv10;
    @Bind(R.id.tv11)
    View tv11;
    @Bind(R.id.family_book_mulu)
    RelativeLayout familyBookMulu;

    private BaseReadView mPageWidget;
    GenealogyPresenter genealogyPresenter;
    private String surname = "";
    private String surnameId = "";
    CurlView mCurlView;
    ArrayList<ViewGroup> groups = new ArrayList<ViewGroup>();
    int index = 0;
    int viewHeight;
    RelativeLayout muluCover;
    PageFactory pageFactory;
    List<Integer> integers = new ArrayList<>();
    View catalogView;


    public static GenealogyFragmentBak newInstance() {
        GenealogyFragmentBak fragment = new GenealogyFragmentBak();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public BasePresenter initPresenter() {
        //return genealogyPresenter = new GenealogyBakPresenter(this);
        return null;
    }

    @Override
    public View onToolBarCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        ButterKnife.bind(this, view);
        mCurlView = (CurlView) view.findViewById(R.id.family_book_curview);
        initView();
        return view;
    }

    @Override
    public void setToolBar(AppToolBar toolBar) {
        toolBar.setOnToolBarClickLisenear(this);
        toolBar.setLeftArrowCenterBook(_mActivity);
    }


    public void initView() {
        viewHeight = ScreenUtils.getScreenHeight() - (int) getResources().getDimension(R.dimen.dp_40) - StatusBarUtils.getStatusBarHeight(_mActivity);

        pageFactory = new PageFactory(_mActivity,ScreenUtils.getScreenWidth(),viewHeight,50);

        surname = getArguments().getString("surname");
        surnameId = getArguments().getString("surnameId");
//        Log.i("wzh","");
        if (surnameId.equals("") || surname.equals("")) {
            return;
        }
        Flowable.timer(300, TimeUnit.MILLISECONDS).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long aLong) throws Exception {
                HttpParams params = new HttpParams();
                params.put("surid", surnameId);
//        genealogyPresenter.submitInformation("http://192.168.5.32:9092/surname/get?surid=2", params, "");
               // genealogyPresenter.submitInformation(MyApplication.SERVER_ADDRESS_NEWS +MyApplication.ADDRESS_GENEALOGY, params, "");
            }
        });

//        catalogView = LayoutInflater.from(_mActivity).
//                inflate(R.layout.pop_genealogy_catalog,null,false);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0, (int)_mActivity.getResources().getDimension(R.dimen.dp_40),0,0);
//        catalogView.setLayoutParams(params);
//        getToolFragmentRootLayout().addView(catalogView);
//        catalogView.setVisibility(View.GONE);
//        catalogView = LayoutInflater.from(_mActivity).
//                inflate(R.layout.pop_genealogy_catalog,null,false);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.setMargins(0, (int)_mActivity.getResources().getDimension(R.dimen.dp_40),0,0);
//        catalogView.setLayoutParams(params);
//        getToolFragmentRootLayout().addView(catalogView);
//        catalogView.setVisibility(View.GONE);
    }

    private ArrayList<ViewGroup> initListViews(ArrayList<Catlog> catlogs, GenealogyInfo genealogyInfo) {
        ArrayList<ViewGroup> viewGroups = new ArrayList<ViewGroup>();
        LayoutInflater factory = LayoutInflater.from(_mActivity);
        ViewGroup page1 = (ViewGroup) factory.inflate(R.layout.pager_first, null);
        TextView page1Tv = (TextView) page1.findViewById(R.id.pager_first_name);
        TextView page1Tv1 = (TextView) page1.findViewById(R.id.pager_first_familiy_number);
        TextView page1Tv2 = (TextView) page1.findViewById(R.id.pager_first_fund_number);
        TextView page1Tv3 = (TextView) page1.findViewById(R.id.pager_first_time_number);
        Typeface typeface = Typeface.createFromAsset(_mActivity.getAssets(), "fonts/wenyue.ttf");
        page1Tv.getPaint().setTypeface(typeface);

        page1Tv.setText(genealogyInfo.getSurname()+"氏族谱");
        page1Tv1.setText(genealogyInfo.getPeopleNum()+"人");
        page1Tv2.setText(NumberUtil.scalerWan(genealogyInfo.getWealth()));
        page1Tv3.setText(TimeUtil.getGenealogyTimeFormat(genealogyInfo.getCreateTime()));

        ArrayList<TextView> catlogsPage = new ArrayList<>();
        ArrayList<View> catlogsCoverPage = new ArrayList<>();

        ViewGroup page2 = (ViewGroup) factory.inflate(R.layout.pager_second, null);
        TextView page2Tv1 = (TextView) page2.findViewById(R.id.tv1);
        TextView page2Tv2 = (TextView) page2.findViewById(R.id.tv2);
        TextView page2Tv3 = (TextView) page2.findViewById(R.id.tv3);
        TextView page2Tv4 = (TextView) page2.findViewById(R.id.tv4);
        TextView page2Tv5 = (TextView) page2.findViewById(R.id.tv5);
        TextView page2Tv6 = (TextView) page2.findViewById(R.id.tv6);
        TextView page2Tv7 = (TextView) page2.findViewById(R.id.tv7);
        TextView page2Tv8 = (TextView) page2.findViewById(R.id.tv8);
        TextView page2Tv9 = (TextView) page2.findViewById(R.id.tv9);
        TextView page2Tv10 = (TextView) page2.findViewById(R.id.tv10);
        TextView page2Tv11 = (TextView) page2.findViewById(R.id.tv11);

        catlogsPage.add(page2Tv2);
        catlogsPage.add(page2Tv3);
        catlogsPage.add(page2Tv4);
        catlogsPage.add(page2Tv5);
        catlogsPage.add(page2Tv6);
        catlogsPage.add(page2Tv7);
        catlogsPage.add(page2Tv8);
        catlogsPage.add(page2Tv9);
        catlogsPage.add(page2Tv10);
        catlogsPage.add(page2Tv11);

        catlogsCoverPage.add(tv2);
        catlogsCoverPage.add(tv3);
        catlogsCoverPage.add(tv4);
        catlogsCoverPage.add(tv5);
        catlogsCoverPage.add(tv6);
        catlogsCoverPage.add(tv7);
        catlogsCoverPage.add(tv8);
        catlogsCoverPage.add(tv9);
        catlogsCoverPage.add(tv10);
        catlogsCoverPage.add(tv11);

        page2Tv1.getPaint().setTypeface(typeface);
//        page2Tv2.getPaint().setTypeface(typeface);
//        page2Tv3.getPaint().setTypeface(typeface);
//        page2Tv4.getPaint().setTypeface(typeface);
//        page2Tv5.getPaint().setTypeface(typeface);
//        page2Tv6.getPaint().setTypeface(typeface);
//        page2Tv7.getPaint().setTypeface(typeface);
//        page2Tv8.getPaint().setTypeface(typeface);
//        page2Tv9.getPaint().setTypeface(typeface);
//        page2Tv10.getPaint().setTypeface(typeface);
//        page2Tv11.getPaint().setTypeface(typeface);
        for(int i=0;i<catlogs.size();i++) {
            TextView page2tv = catlogsPage.get(i);
            page2tv.setVisibility(View.VISIBLE);
            page2tv.setText(catlogs.get(i).getCatlog());
            page2tv.getPaint().setTypeface(typeface);

            View view = catlogsCoverPage.get(i);
            view.setVisibility(View.VISIBLE);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    pageFactory.getVectorsSize()
                    Log.i("wzh","finalI: "+finalI);
                    int k = 0;
                    for(int j=0;j<finalI;j++){
                        k+=integers.get(j);
                        Log.i("wzh","integers.get(j): "+integers.get(j));
                    }
                    Log.i("wzh","k: "+k);
//                    mCurlView.setCurrentIndex(finalI+2);
                    mCurlView.setCurrentIndex(k+2);
                    mCurlView.onDrawFrame();
                }
            });
        }

        viewGroups.add(page1);
        viewGroups.add(page2);

        Log.i("wzh","catlogs.size(): "+catlogs.size());
        for(int i=0;i<catlogs.size();i++){
            TextView page2tv = catlogsPage.get(i);
            page2tv.getPaint().setTypeface(typeface);
            String body = catlogs.get(i).getGenealogy().getBody();
            String textStr = Html.fromHtml(body).toString();
            int j=0;
            while (true){
                textStr = pageFactory.pageDown(textStr);
                j++;
                if(textStr.length()<=0){
                    integers.add(j);
                    break;
                }
            }
//            ViewGroup view = (ViewGroup) factory.inflate(R.layout.catlog_text_layout, null);
//            TextView textView = (TextView) view.findViewById(R.id.catlog_text);
//            textView.setText(Html.fromHtml(catlogs.get(i).getGenealogy().getBody()));
//            viewGroups.insert(view);
        }
        Log.i("wzh","viewGroups: "+viewGroups.size());
        return viewGroups;
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCurlView != null) {
            mCurlView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurlView != null) {
            mCurlView.onResume();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_left_layout:
                pop();
                break;
            case R.id.main_family_book_1:
                mCurlView.setZOrderOnTop(true);
//                mCurlView.setZOrderMediaOverlay(true);
                informationPageLayout.setVisibility(View.VISIBLE);
                rangkingLayout.setVisibility(View.INVISIBLE);
                mCurlView.setVisibility(View.VISIBLE);
                break;
            case R.id.main_family_book_2:
                mCurlView.setZOrderOnTop(true);
//                mCurlView.setZOrderMediaOverlay(false);
                mCurlView.setVisibility(View.INVISIBLE);
                informationPageLayout.setVisibility(View.INVISIBLE);
                rangkingLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.main_right_layout:
//                GenealogyCatalogDialog catalogDialog = new GenealogyCatalogDialog(_mActivity);
//                catalogDialog.show();
//                mCurlView.setZOrderOnTop(true);
//                mCurlView.setZOrderMediaOverlay(true);
//                catalogView.setVisibility(View.VISIBLE);
                if(mCatlogs==null){
                    return;
                }
                if(informationPageLayout.getVisibility() == View.INVISIBLE){
                    return;
                }
                final GenealogyCatalogPop catalogPop = new GenealogyCatalogPop(_mActivity,mCatlogs,null);
                /*
                catalogPop.setOnDialogClickLisenrar(new OnDialogClickListener() {
                    @Override
                    public void confirm(View view) {
                        switch (view.getId()){
                            case R.id.tv1:
//                    mCurlView.setCurrentIndex(finalI+2);
                                mCurlView.setCurrentIndex(getPagePosition(0)+2);
                                mCurlView.onDrawFrame();
                                break;
                            case R.id.tv2:
                                mCurlView.setCurrentIndex(getPagePosition(1)+2);
                                mCurlView.onDrawFrame();
                                break;
                            case R.id.tv3:
                                mCurlView.setCurrentIndex(getPagePosition(2)+2);
                                mCurlView.onDrawFrame();
                                break;
                            case R.id.tv4:
                                mCurlView.setCurrentIndex(getPagePosition(3)+2);
                                mCurlView.onDrawFrame();
                                break;
                            case R.id.tv5:
                                mCurlView.setCurrentIndex(getPagePosition(4)+2);
                                mCurlView.onDrawFrame();
                                break;
                            case R.id.tv6:
                                mCurlView.setCurrentIndex(getPagePosition(5)+2);
                                mCurlView.onDrawFrame();
                                break;
                            case R.id.tv7:
                                mCurlView.setCurrentIndex(getPagePosition(6)+2);
                                mCurlView.onDrawFrame();
                                break;
                        }
                        catalogPop.dismiss();
                    }

                    @Override
                    public void cancel(View view) {

                    }
                });
                */
                catalogPop.showDown(v);
                break;
        }
    }

    public int getPagePosition(int position){
        int k = 0;
        for(int j=0;j<position;j++){
            k+=integers.get(j);
        }
        return k;
    }

    @Override
    public void onHttpSuccess(String type) {

    }

    @Override
    public void onHttpError(String type) {

    }

    @Override
    public void updataInformation(GenealogyInformation genealogyInformation) {
        Log.i("wzh", "genealogyInformation: " + genealogyInformation.toString());
        initRank(genealogyInformation.getRank());
        initBook(genealogyInformation.getGenealogy_list(), genealogyInformation.getInfo());
    }

    ArrayList<Catlog> mCatlogs;

    private void initBook(ArrayList<Catlog> catlogs, GenealogyInfo genealogyInfo) {
        mCatlogs = catlogs;
        groups.addAll(initListViews(catlogs, genealogyInfo));
//        mCurlView = new CurlView(_mActivity);
        mCurlView.setPageProvider(new PageProvider());
        mCurlView.setSizeChangedObserver(new SizeChangedObserver());
        mCurlView.setCurrentIndex(index);
        mCurlView.setOnIndexChanageLisenear(new CurlView.OnIndexChanageLisenear() {
            @Override
            public void indexChangeLisenear(int index) {
                GenealogyFragmentBak.this.index = index;

//                if(index==1){
//                    mCurlView.setZOrderOnTop(true);
//                    mCurlView.setZOrderMediaOverlay(true);
//                    familyBookMulu.setVisibility(View.VISIBLE);
//                }else{
//                    mCurlView.setZOrderOnTop(true);
//                    mCurlView.setZOrderMediaOverlay(false);
//                    familyBookMulu.setVisibility(View.GONE);
//                }
            }
        });
//        informationPageLayout.addView(mCurlView);
//        mCurlView.setBackgroundColor(getResources().getColor(R.color.white));
//        mCurlView.setBackgroundColor(0xFFFFFF);
//        mCurlView.onResume();
    }

    private void initRank(ArrayList<GenealogyRank> genealogyRanks) {
        int touxiangSize = (int) getResources().getDimension(R.dimen.dp_40);
        if (genealogyRanks.size() == 0) {
            return;
        } else if (genealogyRanks.size() == 1) {
            pagerSecondRangkingLayout.setVisibility(View.INVISIBLE);
            pagerThirdRangkingLayout.setVisibility(View.INVISIBLE);
            pagerFourthRangkingLayout.setVisibility(View.INVISIBLE);
            GenealogyRank genealogyRank = genealogyRanks.get(0);
            initFirstRank(genealogyRank, touxiangSize);
        } else if (genealogyRanks.size() == 2) {
            pagerThirdRangkingLayout.setVisibility(View.INVISIBLE);
            pagerFourthRangkingLayout.setVisibility(View.INVISIBLE);
            GenealogyRank genealogyRank1 = genealogyRanks.get(0);
            initFirstRank(genealogyRank1, touxiangSize);
            GenealogyRank genealogyRank2 = genealogyRanks.get(1);
            initSecondRank(genealogyRank2, touxiangSize);
        } else if (genealogyRanks.size() == 3) {
            pagerFourthRangkingLayout.setVisibility(View.INVISIBLE);
            GenealogyRank genealogyRank1 = genealogyRanks.get(0);
            initFirstRank(genealogyRank1, touxiangSize);
            GenealogyRank genealogyRank2 = genealogyRanks.get(1);
            initSecondRank(genealogyRank2, touxiangSize);
            GenealogyRank genealogyRank3 = genealogyRanks.get(2);
            initThirdRank(genealogyRank3, touxiangSize);
        } else if (genealogyRanks.size() == 4) {
            GenealogyRank genealogyRank1 = genealogyRanks.get(0);
            initFirstRank(genealogyRank1, touxiangSize);
            GenealogyRank genealogyRank2 = genealogyRanks.get(1);
            initSecondRank(genealogyRank2, touxiangSize);
            GenealogyRank genealogyRank3 = genealogyRanks.get(2);
            initThirdRank(genealogyRank3, touxiangSize);
            GenealogyRank genealogyRank4 = genealogyRanks.get(3);
            initFourthRank(genealogyRank4, touxiangSize);
        }
    }

    public void initFirstRank(GenealogyRank genealogyRank, int touxiangSize) {
        pagerFirstRangking.setText("" + genealogyRank.getRank());
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,pagerFirstRangkingImage,genealogyRank.getSurnameIcon(),R.drawable.me_avatar_moren_default,R.drawable.me_avatar_moren_default);
//        Glide.with(_mActivity).load(genealogyRank.getSurnameIcon()).override(touxiangSize, touxiangSize)
//                .placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default)
//                .animate(new DrawableCrossFadeFactory<GlideDrawable>())
//                .into(pagerFirstRangkingImage);
        pagerFirstRangkingContent.setText(genealogyRank.getOrganize());
        pagerFirstRangkingMoney.setText(NumberUtil.scalerWan(genealogyRank.getWealth()));
        initRankTrend(genealogyRank.getTrend(), pagerFirstRangkingUp);
    }

    public void initSecondRank(GenealogyRank genealogyRank, int touxiangSize) {
        pagerSecondRangking.setText("" + genealogyRank.getRank());
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,pagerSecondRangkingImage,genealogyRank.getSurnameIcon(),R.drawable.me_avatar_moren_default,R.drawable.me_avatar_moren_default);
//        Glide.with(_mActivity).load(genealogyRank.getSurnameIcon()).override(touxiangSize, touxiangSize).placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default)
//                .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(pagerSecondRangkingImage);
        pagerSecondRangkingContent.setText(genealogyRank.getOrganize());
        pagerSecondRangkingMoney.setText(NumberUtil.scalerWan(genealogyRank.getWealth()));
        initRankTrend(genealogyRank.getTrend(), pagerSecondRangkingUp);
    }

    public void initThirdRank(GenealogyRank genealogyRank, int touxiangSize) {
        pagerThirdRangking.setText("" + genealogyRank.getRank());
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,pagerThirdRangkingImage,genealogyRank.getSurnameIcon(),R.drawable.me_avatar_moren_default,R.drawable.me_avatar_moren_default);
//        Glide.with(_mActivity).load(genealogyRank.getSurnameIcon()).override(touxiangSize, touxiangSize).placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default)
//                .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(pagerThirdRangkingImage);
        pagerThirdRangkingContent.setText(genealogyRank.getOrganize());
        pagerThirdRangkingMoney.setText(NumberUtil.scalerWan(genealogyRank.getWealth()));
        initRankTrend(genealogyRank.getTrend(), pagerThirdRangkingUp);
    }

    public void initFourthRank(GenealogyRank genealogyRank, int touxiangSize) {
        pagerFourthRangking.setText("" + genealogyRank.getRank());
        new CommonGlideInstance().setImageViewBackgroundForUrl(_mActivity,pagerFourthRangkingImage,genealogyRank.getSurnameIcon(),R.drawable.me_avatar_moren_default,R.drawable.me_avatar_moren_default);
//        Glide.with(_mActivity).load(genealogyRank.getSurnameIcon()).override(touxiangSize, touxiangSize).placeholder(R.drawable.me_avatar_moren_default).error(R.drawable.me_avatar_moren_default)
//                .animate(new DrawableCrossFadeFactory<GlideDrawable>()).into(pagerFourthRangkingImage);
        pagerFourthRangkingContent.setText(genealogyRank.getOrganize());
        pagerFourthRangkingMoney.setText(NumberUtil.scalerWan(genealogyRank.getWealth()));
        initRankTrend(genealogyRank.getTrend(), pagerFourthRangkingUp);
    }

    public void initRankTrend(int trend, ImageView imageView) {
        if (trend == -1) {
            Glide.with(_mActivity).load(R.drawable.new_icon_xiangxia_default).into(imageView);
        } else if (trend == 1) {
            Glide.with(_mActivity).load(R.drawable.new_icon_xiangshang_default).into(imageView);
        } else if (trend == 0) {
            imageView.setVisibility(View.INVISIBLE);
        }
    }

    private void layoutView(View v, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
//        int measuredHeight = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    /**
     * Bitmap provider.
     */
    private class PageProvider implements CurlView.PageProvider {

        @Override
        public int getPageCount() {
            return groups.size()+pageFactory.getVectorsSize();
        }

        private Bitmap loadBitmap(int width, int height, int index) {
            Bitmap b = Bitmap.createBitmap(width, height,
                    Bitmap.Config.RGB_565);
            b.eraseColor(0xFFFFFFFF);
            Canvas c = new Canvas(b);

            if(index ==0 || index ==1){
                ViewGroup group = groups.get(index);
                layoutView(group, ScreenUtils.getScreenWidth(), viewHeight);
                group.draw(c);
            }else{
                pageFactory.onDraw(c,index,integers,mCatlogs);
            }

            return b;
        }


        @Override
        public void updatePage(CurlPage page, int width, int height, int index) {
            Log.i("wzh","index: "+index);
            Log.i("wzh","mCurlView.getCurrentIndex(): "+mCurlView.getCurrentIndex());
            Bitmap front = loadBitmap(width, height, index);
            page.setTexture(front, CurlPage.SIDE_BOTH);
//            switch (index) {
//                case 0: {
//                    Bitmap front = loadBitmap(width, height, 0);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
////                    page.setColor(Color.rgb(180, 180, 180), CurlPage.SIDE_BACK);
//                    break;
//                }
//                case 1: {
//                    Bitmap front = loadBitmap(width, height, 1);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
////                    page.setColor(Color.rgb(180, 180, 180), CurlPage.SIDE_BACK);
//                    break;
//                }
//                case 2: {
//                    Bitmap front = loadBitmap(width, height, 2);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//                case 3: {
//                    Bitmap front = loadBitmap(width, height, 3);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//                case 4:{
//                    Bitmap front = loadBitmap(width, height, 4);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//                case 5:{
//                    Bitmap front = loadBitmap(width, height, 5);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//                case 6:{
//                    Bitmap front = loadBitmap(width, height, 6);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//                case 7:{
//                    Bitmap front = loadBitmap(width, height, 7);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//                case 8:{
//                    Bitmap front = loadBitmap(width, height, 8);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//                case 9:{
//                    Bitmap front = loadBitmap(width, height, 9);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//                case 10:{
//                    Bitmap front = loadBitmap(width, height, 10);
//                    page.setTexture(front, CurlPage.SIDE_BOTH);
//                    break;
//                }
//            }
        }
    }

    /**
     * CurlView size changed observer.
     */
    private class SizeChangedObserver implements CurlView.SizeChangedObserver {
        @Override
        public void onSizeChanged(int w, int h) {
            mCurlView.setViewMode(CurlView.SHOW_ONE_PAGE);
        }
    }
}
