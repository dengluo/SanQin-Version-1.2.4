package com.pbids.sanqin.ui.activity.news;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pbids.sanqin.common.SanQinViewFooter;
import com.andview.refreshview.XRefreshView;
import com.lzy.okgo.model.HttpParams;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.BaseFragment;
import com.pbids.sanqin.common.BasePresenter;
import com.pbids.sanqin.presenter.NewsSearchPresenter;
import com.pbids.sanqin.ui.activity.zhizong.ZhiZongWebFragment;
import com.pbids.sanqin.ui.recyclerview.adapter.NewsSearchAdapter;
import com.pbids.sanqin.ui.recyclerview.adapter.base.GroupedRecyclerViewAdapter;
import com.pbids.sanqin.ui.recyclerview.holder.BaseViewHolder;
import com.pbids.sanqin.ui.view.OneTextTwoBtPop;
import com.pbids.sanqin.ui.view.WarpLinearLayout;
import com.pbids.sanqin.utils.AddrConst;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 15:04
 * @desscribe 类描述:首页资讯搜索界面
 * @remark 备注:
 * @see
 */
public class NewsSearchFragment extends BaseFragment implements NewsSearchView, GroupedRecyclerViewAdapter.OnChildClickListener {

    @Bind(R.id.news_title_search_et)
    EditText newsTitleSearchEt;
    @Bind(R.id.news_title_search_bt)
    Button newsTitleSearchBt;
    @Bind(R.id.news_title_search_lajitong)
    ImageView newsTitleSearchLajitong;
    @Bind(R.id.news_title_search_rv)
    RecyclerView newsTitleSearchRv;
    @Bind(R.id.news_title_search_history)
    WarpLinearLayout newsTitleSearchHistory;
    @Bind(R.id.home_xrefreshview)
    XRefreshView homeXrefreshview;
    @Bind(R.id.history_ly)
    LinearLayout historyLy;
    @Bind(R.id.main_content_area_ry)
    RelativeLayout mainContentAreaRy;
    @Bind(R.id.clear_title_img)
    ImageView clearTitleImg;  //点击清除标题
    //presenter
    NewsSearchPresenter newsSearchPresenter;
    //adapter
    NewsSearchAdapter newsSearchAdapter;

    private int currentIndexPage= 1;//页码
    private String keyword = null; //要查找的关键字
    private String lastKeyword = "";
    DisposableObserver observer;

    OneTextTwoBtPop deleteHistoryPop ;

    public static NewsSearchFragment newInstandce() {
        NewsSearchFragment newsSearchFragment = new NewsSearchFragment();
        Bundle bundle = new Bundle();
        newsSearchFragment.setArguments(bundle);
        return newsSearchFragment;
    }

    @Override
    public BasePresenter initPresenter() {
        return newsSearchPresenter = new NewsSearchPresenter(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_title_search, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }



    public void initView() {
        newsSearchAdapter = new NewsSearchAdapter(_mActivity, newsSearchPresenter.getNewsInformation());
        newsSearchAdapter.setOnChildClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        newsTitleSearchRv.setAdapter(newsSearchAdapter);
        newsTitleSearchRv.setLayoutManager(manager);
        //newsTitleSearchRv.setVisibility(View.GONE);
        clearTitleImg.setVisibility(View.GONE);
        initXRefreshView();
        //下拉刷新视图效果
        homeXrefreshview.setCustomFooterView(new SanQinViewFooter(_mActivity));

        //显示搜索历史
        newsTitleSearchHistory.removeAllViews();
        showHistoryView();

        InputFilter switchFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                String regEx = "^[\\u4e00-\\u9fa5a-zA-Z0-9‘“”+-_()<>?/|‘“”；：“;:'\"]*$";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(source.toString());
                if(m.matches()){
                    return source.toString();
                }else {
                    return "";
                }
            }
        };
        newsTitleSearchEt.setFilters(new InputFilter[]{switchFilter,new InputFilter.LengthFilter(40)});
        //文本框
        newsTitleSearchEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(null==historyLy){
                    return;
                }
                if(b){
                    //显示输入历史
                    historyLy.setVisibility(View.VISIBLE);
                }else {
                    //隐藏输入历史
                    historyLy.setVisibility(View.GONE);
                }
                showClearTitleBtn();
            }
        });
        newsTitleSearchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                showClearTitleBtn();
            }
        });


        //显示键盘
        showSoftInput(newsTitleSearchEt);

        //确认删除
        deleteHistoryPop = new OneTextTwoBtPop( getContext(),"确认删除全部历史记录？");
        deleteHistoryPop.setOnOneTextTwoBtPopClickLisenear(new OneTextTwoBtPop.OnOneTextTwoBtPopClickLisenear() {
            @Override
            public void comfirm() {
                deleteHistoryPop.dismiss();
                clearKeywordHistoryToDb();
                newsTitleSearchHistory.removeAllViews();
            }

            @Override
            public void cancel() {
                deleteHistoryPop.dismiss();
            }
        });

        //阻止点击历史记录 响应文章列表事件
        historyLy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (newsTitleSearchEt != null) {
                    newsTitleSearchEt.clearFocus();
                    hideSoftInput(); //隐藏键盘
                }
                return true;
            }
        });

    }
    private void showClearTitleBtn(){
        if(null==newsTitleSearchEt){
            return;
        }
        if(newsTitleSearchEt.getText().toString().length()<1){
            clearTitleImg.setVisibility(View.GONE);
        }else {
            clearTitleImg.setVisibility(View.VISIBLE);
        }
    }

    private TextView addTextViewToHistory(String item_str){
        //TextView txt_item = new TextView(getContext(), null, R.style.news_search_fragment_history_textview);
        TextView txt_item = new TextView(new ContextThemeWrapper(getActivity(), R.style.news_search_fragment_history_textview));
        txt_item.setText(item_str);
        txt_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView txt_item = (TextView) view;
                keyword = txt_item.getText().toString();
                newsTitleSearchEt.setText(keyword);
                newsTitleSearchEt.clearFocus();
                hideSoftInput();
                currentIndexPage = 1;
                getLoadingPop("loading...").show();
                newsSearchPresenter.clearNewsInfo();//清除数据
                loadNewsList(keyword, currentIndexPage);
            }
        });
        newsTitleSearchHistory.addView(txt_item);
        return txt_item;
    }

    //显示历史搜索
    private void showHistoryView(){
        String his_keywords = getHistoryDb();
        if("".equals(his_keywords)){
            return;
        }
        List<String> key_list = Arrays.asList(his_keywords.split(","));
        for (int i = 0; i < key_list.size(); i++) {
            String item_str = key_list.get(i);
            addTextViewToHistory(item_str);
        }
    }

    //取历史搜索数据
    private String getHistoryDb(){
        newsTitleSearchHistory.removeAllViews();
        SharedPreferences sp = getContext().getSharedPreferences("sanqin_news_search_history", Context.MODE_PRIVATE);
        return sp.getString("keywords", "");
    }

    //保存搜索搜索历史
    private void saveKeywordHistoryToDb(String keyword){
        SharedPreferences sp = getContext().getSharedPreferences("sanqin_news_search_history", Context.MODE_PRIVATE);
        String keywords_str = sp.getString("keywords", "");
        List<String> key_list = Arrays.asList(keywords_str.split(","));
        //不重复添加
        for (int i = 0; i < key_list.size(); i++) {
            String item_str = key_list.get(i);
            if(item_str.equals(keyword)){
                return;
            }
        }
        addTextViewToHistory(keyword);
        SharedPreferences.Editor db_history = sp.edit();
        if(keywords_str.isEmpty()){
            keywords_str  = keyword;
        }else{
            keywords_str  = keywords_str+","+keyword;
        }
        keywords_str = keywords_str.trim();//删除空格
        db_history.putString("keywords",keywords_str);
        db_history.commit();
    }

    //保存搜索搜索历史
    private void clearKeywordHistoryToDb(){
        SharedPreferences sp = getContext().getSharedPreferences("sanqin_news_search_history", Context.MODE_PRIVATE);
        SharedPreferences.Editor db_history = sp.edit();
        db_history.putString("keywords","");
        db_history.commit();
    }

    public void initXRefreshView() {
//        homeXrefreshview.setSilenceLoadMore(true);
        //设置刷新完成以后，headerview固定的时间
        homeXrefreshview.setPinnedTime(0);
        homeXrefreshview.setMoveForHorizontal(true);
        homeXrefreshview.setPullRefreshEnable(false);// 设置是否可以下拉刷新
        homeXrefreshview.setPullLoadEnable(false); // 设置是否可以上拉加载更多
//        homeXrefreshview.setCustomFooterView(new XRefreshViewFooter(_mActivity));
        homeXrefreshview.setCustomFooterView(new SanQinViewFooter(_mActivity));
//        homeXrefreshview.setCustomHeaderView(new XRefreshViewHeader(_mActivity));
//        homeXrefreshview.setCustomHeaderView(new SanQinViewHeader(_mActivity));
//        homeXrefreshview.setAutoLoadMore(true);
        homeXrefreshview.enableReleaseToLoadMore(true);
        homeXrefreshview.enableRecyclerViewPullUp(true);
        homeXrefreshview.enablePullUpWhenLoadCompleted(true);
        homeXrefreshview.setPinnedContent(true);
        // homeXrefreshview.enablePullUp(true);
        homeXrefreshview.enablePullUp(false);
        // homeXrefreshview.ena
        //设置静默加载时提前加载的item个数
        // xefreshView1.setPreLoadCount(4);
        //设置Recyclerview的滑动监听

        homeXrefreshview.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        homeXrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {

            }

            //加载更新
            @Override
            public void onLoadMore(boolean isSilence) {
                if (observer != null) {
                    removeDisposable(observer);
                    currentIndexPage++;
                    loadNewsList( keyword, currentIndexPage);
                }
            }
        });
    }

    //刷新新闻列表数据
    public void loadNewsList(String keyword , int indexPage) {
        lastKeyword = keyword;
        // 请求列表数据
        HttpParams httpParams = new HttpParams();
        httpParams.put("keyword",keyword);
        httpParams.put("pageIndex",indexPage);
        observer = newsSearchPresenter.submit(AddrConst.SERVER_ADDRESS_NEWS+"/search/find",httpParams,"-3");
        addDisposable(observer);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        hideSoftInput(); //收起键盘
    }


    @OnClick({R.id.me_title_left_layout, R.id.news_title_search_bt, R.id.news_title_search_lajitong,R.id.clear_title_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.me_title_left_layout:
                //返回
                pop();
                break;
            case R.id.news_title_search_bt:
                //搜索文章
                keyword = newsTitleSearchEt.getText().toString();
                if(keyword == null || keyword.isEmpty()){
                    Toast.makeText(_mActivity,"请输入要查找的内容",Toast.LENGTH_SHORT).show();
                    return;
                }
                //不能输入标点符号
                if(keyword.contains(",")||keyword.contains("，")){
                    Toast.makeText(_mActivity,"搜索不能包含逗号",Toast.LENGTH_SHORT).show();
                    return;
                }
                //save history
                saveKeywordHistoryToDb(keyword);
                newsTitleSearchEt.clearFocus();
                hideSoftInput(); //收起键盘
                if (keyword.equals(lastKeyword)) {
                    return;
                }
                getLoadingPop("loading...").show();
                newsSearchPresenter.clearNewsInfo();//清除数据
                homeXrefreshview.setPullLoadEnable(false); // 设置是否可以上拉加载更多
                currentIndexPage = 1;
                // 请求列表数据
                loadNewsList(keyword,currentIndexPage);
                break;
            case R.id.news_title_search_lajitong:
                //删除历史记录
                hideSoftInput(); //收起键盘
                deleteHistoryPop.show();
                break;
            case R.id.clear_title_img:
                //删除输入的文本
                newsTitleSearchEt.setText("");
                break;
        }
    }

    //数据加载成功
    @Override
    public void onHttpSuccess(String type) {
        if(homeXrefreshview!=null){
            homeXrefreshview.setLoadComplete(false);
            homeXrefreshview.stopRefresh();
        }
        newsSearchAdapter.setLoaded(true);
        switch (type){
            case "-3":
                dismiss(); //隐藏加载中
                break;
        }
    }

    //数据加载失败
    @Override
    public void onHttpError(String type) {
        dismiss(); //隐藏加载中
        homeXrefreshview.stopRefresh();
        homeXrefreshview.stopLoadMore();
        Toast.makeText(_mActivity,type,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onChildClick(GroupedRecyclerViewAdapter adapter, BaseViewHolder holder, int groupPosition, int childPosition) {
        //Toast.makeText(_mActivity,"onChildClick",Toast.LENGTH_SHORT).show();
        ZhiZongWebFragment fragment = ZhiZongWebFragment.newInstance();
        fragment.getArguments().putString("link",newsSearchPresenter.getNewsInformation().get(groupPosition).getList().get(childPosition).getLink());
        start(fragment);
    }

    @Override
    public NewsSearchAdapter getNewsSearchAdapter() {
        return newsSearchAdapter;
    }

    @Override
    public void ListLoadDataNum(int loadNum) {
        if(homeXrefreshview==null){
            return;
        }
        homeXrefreshview.setLoadComplete(true);
        if (loadNum > 0) {
            homeXrefreshview.setPullLoadEnable(true);
        } else {
            homeXrefreshview.setPullLoadEnable(false);
        }
    }
}
