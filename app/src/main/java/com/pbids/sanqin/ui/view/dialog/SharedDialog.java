package com.pbids.sanqin.ui.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.pbids.sanqin.R;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.model.entity.NewsArticle;
import com.pbids.sanqin.utils.AddrConst;
import com.pbids.sanqin.utils.OkGoUtil;
import com.pbids.sanqin.utils.SharedUtils;
import com.pbids.sanqin.utils.eventbus.ShareArticleEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import io.reactivex.observers.DisposableObserver;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:12
 * @desscribe 类描述:分享布局dialog
 * @remark 备注:
 * @see
 */
public class SharedDialog extends BaseDialog {
    @Bind(R.id.news_shared_pop_weixin)
    LinearLayout newsSharedPopWeixin;
    @Bind(R.id.news_shared_pop_quan)
    LinearLayout newsSharedPopQuan;
    @Bind(R.id.news_shared_pop_weibo)
    LinearLayout newsSharedPopWeibo;
    @Bind(R.id.news_shared_pop_kongjian)
    LinearLayout newsSharedPopKongjian;
    @Bind(R.id.news_shared_pop_qq)
    LinearLayout newsSharedPopQq;

    NewsArticle newsArticle;
    Context mContext;
    @Bind(R.id.news_shared_pop_bt)
    Button newsSharedPopBt;
    private boolean isCancel = true;

    //转发量计数 --- 发送到服务器
    private void onFromNum(final String aid){
        final DisposableObserver<Response<String>> observer = new DisposableObserver<Response<String>>() {
            @Override
            public void onNext(@NonNull Response<String> stringResponse) {
                //Log.i("wzh","stringResponse: "+stringResponse);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                //修改首页
                ShareArticleEvent evt = new ShareArticleEvent();
                evt.setAid( Long.parseLong(aid));
                EventBus.getDefault().post(evt);
                //关闭分享
                dismiss();
            }
        };
        String url = AddrConst.SERVER_ADDRESS_NEWS + AddrConst.ADDRESS_ARTICLE_FORWARD;
        HttpParams params = new HttpParams();
        params.put("uid",MyApplication.getUserInfo().getUserId());
        params.put("id",aid);
        //http get
        OkGoUtil.getStringObservableForGet(url,params,observer);
    }

    PlatformActionListener listener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            //Log.i("wzh", "onComplete: " + "");
            //转发量加1
            onFromNum(newsArticle.getId()+"");
            // isCancel = true;
            // setClickable(true);
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            //Log.i("wzh", "onError: " + "" + throwable.getMessage());
            // isCancel = true;
            // setClickable(true);
        }

        @Override
        public void onCancel(Platform platform, int i) {
            // Log.i("wzh", "onCancel: " + "");
            // isCancel = true;
            // setClickable(true);
        }
    };

    public SharedDialog(@NonNull Context context, NewsArticle newsArticle) {
        super(context);
        mContext = context;
        this.newsArticle = newsArticle;
        setBottomUpAnimation();
        setGrayBottom();
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void initView() {
        setContentView(R.layout.pop_news_shared);
        ButterKnife.bind(this);
    }

    public void setClickable(boolean isClick) {
        newsSharedPopWeixin.setClickable(isClick);
        newsSharedPopQuan.setClickable(isClick);
        newsSharedPopKongjian.setClickable(isClick);
        newsSharedPopQq.setClickable(isClick);
        newsSharedPopBt.setClickable(isClick);
    }

    @OnClick({R.id.news_shared_pop_weixin, R.id.news_shared_pop_quan, R.id.news_shared_pop_weibo
            , R.id.news_shared_pop_kongjian, R.id.news_shared_pop_qq, R.id.news_shared_pop_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.news_shared_pop_weixin:
                //Log.i("wzh", "news_shared_pop_weixin");
                if (newsArticle != null) {
                    // isCancel = false;
                    //  setClickable(false);
                    SharedUtils.sharedWechat(mContext, newsArticle, listener);
                }
                break;
            case R.id.news_shared_pop_quan:
                // 分享到朋友圈
                //Log.i("wzh", "news_shared_pop_quan");
                if (newsArticle != null) {
                    // isCancel = false;
                    // setClickable(false);
                    SharedUtils.sharedWechatMoments(mContext, newsArticle, listener);
                }
                break;
            case R.id.news_shared_pop_weibo:
                if (newsArticle != null) {
                    SharedUtils.sharedSinaWeibo(mContext, newsArticle, listener);
                }
                //Log.i("wzh", "news_shared_pop_weibo");
                break;
            case R.id.news_shared_pop_kongjian:
                if (newsArticle != null) {
                    //  isCancel = false;
                    // setClickable(false);
                    SharedUtils.sharedQZone(mContext, newsArticle, listener);
                }
                //Log.i("wzh", "news_shared_pop_kongjian");
                break;
            case R.id.news_shared_pop_qq:
                //分享到QQ
                if (newsArticle != null) {
                    // isCancel = false;
                    // setClickable(false);
                    SharedUtils.sharedQQ(mContext, newsArticle, listener);
                }
                //Log.i("wzh", "news_shared_pop_qq");
                break;
            case R.id.news_shared_pop_bt:
                //返回
                dismiss();
                break;
        }
    }

    public NewsArticle getNewsArticle() {
        return newsArticle;
    }

    public void setNewsArticle(NewsArticle newsArticle) {
        this.newsArticle = newsArticle;
    }

}
