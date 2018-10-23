package com.pbids.sanqin.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.pbids.sanqin.model.entity.InviteInfo;
import com.pbids.sanqin.model.entity.NewsArticle;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by pbids903 on 2017/12/4.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:37
 * @desscribe 类描述:SharedSdk工具类
 * @remark 备注:
 * @see
 */
public class SharedUtils {

    public static void qqGetInformation(PlatformActionListener listener) {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        if (qq.isAuthValid()) {
            qq.removeAccount(true);
//            return;
        }
        //使用SSO授权，通过客户单授权
        qq.SSOSetting(false);
        qq.setPlatformActionListener(listener);
//authorize与showUser单独调用一个即可
//        qq.authorize();//单独授权,OnComplete返回的hashmap是空的
        qq.showUser(null);//授权并获取用户信息
//移除授权
//weibo.removeAccount(true);
    }

    public static void weixinGetInformation(PlatformActionListener listener) {
        //Log.i("wzh","weixinGetInformation: "+"");
        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
//        if (weixin.isAuthValid()) {
//            weixin.removeAccount(true);
//            return;
//        }
        //移除授权
        weixin.removeAccount(true);
        //使用SSO授权，通过客户单授权
        weixin.SSOSetting(false);
        //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        weixin.setPlatformActionListener(listener);
        //authorize与showUser单独调用一个即可
        //        weixin.authorize();//单独授权,OnComplete返回的hashmap是空的
        weixin.showUser(null);//授权并获取用户信息

    }


    public static void weixinoperationInfomation(PlatformActionListener listener){
        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
         //回调信息，可以在这里获取基本的授权返回的信息，但是注意如果做提示和UI操作要传到主线程handler里去执行
        weixin.setPlatformActionListener(listener);
        //authorize与showUser单独调用一个即可
        weixin.authorize();//要功能不要数据，在监听oncomplete中不会返回用户数据
        //想要移除授权状态，在想移除的地方执行下面的方法即可
        //weibo.removeAccount(true);
    }

    //分享qq
    public static void sharedQQ(Context context, NewsArticle newsArticle,PlatformActionListener listener){
        OnekeyShare oks = new OnekeyShare();
//        oks.setImageUrl("http://firicon.fir.im/baa18a6d779c597888d685f1159070df5b4f2912");

        if(newsArticle.getLitpicList().size()>0){
            oks.setImageUrl(newsArticle.getLitpicList().get(0));
        }else{
            oks.setImageUrl("http://app.huaqinchi.com:8083/static/img/app_icon.png");
        }
        if(newsArticle.getLink()!=null){
            oks.setTitleUrl(newsArticle.getLink());
        }
        oks.setText(newsArticle.body); //内容
        oks.setTitle(newsArticle.getTitle());
        oks.setPlatform(QQ.NAME);
        oks.setCallback(listener);

//oks.setPlatform(QZone.NAME);
        oks.show(context);
    }

    //微信分享
    public static void sharedWechat(Context context, NewsArticle newsArticle,PlatformActionListener listener){
        OnekeyShare oks = new OnekeyShare();
        if(newsArticle.getLitpicList().size()>0){
            oks.setImageUrl(newsArticle.getLitpicList().get(0));
        }
        oks.setText(newsArticle.body); //内容
        oks.setTitle(newsArticle.getTitle());
        oks.setUrl(newsArticle.getLink());
//        oks.setImageArray((String[])newsArticle.getLitpicList().toArray());
//        oks.setComment(newsArticle.body);
        oks.setPlatform(Wechat.NAME);
        oks.setCallback(listener);
        oks.show(context);
    }

    //微信分享
    public static void sharedWechat(Context context, InviteInfo inviteInfo,PlatformActionListener listener){
        OnekeyShare oks = new OnekeyShare();
        if (inviteInfo != null) {
            oks.setImageUrl("http://appimg.hicloud.com/hwmarket/files/application/icon144/3119dcba80924c328ee7c1565429a1a2.png");
            oks.setText(inviteInfo.getContent());
            oks.setTitle(inviteInfo.getTitle());
            oks.setUrl(inviteInfo.getLink());
        }
        oks.setPlatform(Wechat.NAME);
        oks.setCallback(listener);
        oks.show(context);
    }

    //微信分享
    public static void sharedWechat(Context context, String imageUrl , PlatformActionListener listener){
        if(imageUrl!=null){
            OnekeyShare oks = new OnekeyShare();
            oks.setImageUrl(imageUrl);
            oks.setPlatform(Wechat.NAME);
            oks.setCallback(listener);
            oks.show(context);
        }
    }

    //分享到微信朋友圈
    public static void sharedWechatMoments(Context context, NewsArticle newsArticle,PlatformActionListener listener){
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        Platform.ShareParams shareParams = new  Platform.ShareParams();
        if(newsArticle.getLitpicList().size()>0){
            shareParams.setImageUrl(newsArticle.getLitpicList().get(0));
        }
        shareParams.setTitle(newsArticle.getTitle());
        shareParams.setText(newsArticle.body); //内容
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
//        shareParams.setShareType(Platform.SHARE_IMAGE);
        shareParams.setUrl(newsArticle.getLink());
        shareParams.setComment(newsArticle.body);
        shareParams.setTitleUrl(newsArticle.getLink());

        platform.setPlatformActionListener(listener);
        platform.share(shareParams);
    }

    //分享到微信朋友圈
    public static void sharedWechatMoments(Context context, InviteInfo inviteInfo,PlatformActionListener listener){
        Wechat.ShareParams shareParams = new Wechat.ShareParams();
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
//        Platform.ShareParams shareParams = new  Platform.ShareParams();
        if(inviteInfo!=null){
            shareParams.setImageData(inviteInfo.getImg());
            shareParams.setText(inviteInfo.getContent());
            shareParams.setTitle(inviteInfo.getTitle());
            shareParams.setShareType(Platform.SHARE_WEBPAGE);
            shareParams.setUrl(inviteInfo.getLink());
        }
        platform.setPlatformActionListener(listener);
        platform.share(shareParams);
    }

    //分享到微信朋友圈
    public static void sharedWMoments(Context context, Bitmap imageUrl , PlatformActionListener listener){
        Wechat.ShareParams shareParams = new Wechat.ShareParams();
        Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
        if(imageUrl!=null){
//        Platform.ShareParams shareParams = new  Platform.ShareParams();
            shareParams.setImageData(imageUrl);
            shareParams.setShareType(Platform.SHARE_WEBPAGE);
        }
        platform.setPlatformActionListener(listener);
        platform.share(shareParams);
    }
    //分享qq空间
    public static void sharedQZone(Context context, NewsArticle newsArticle,PlatformActionListener listener){
        OnekeyShare oks = new OnekeyShare();
        if(newsArticle.getLitpicList().size()>0){
            oks.setImageUrl(newsArticle.getLitpicList().get(0));
        }
        if(newsArticle.getLink()!=null){
            oks.setTitleUrl(newsArticle.getLink());
        }
        oks.setText(newsArticle.body); //内容
        oks.setTitle(newsArticle.getTitle());
        oks.setPlatform(QZone.NAME);
        oks.setCallback(listener);
        oks.show(context);
    }

    //分享微博
    public static void sharedSinaWeibo(Context context, NewsArticle newsArticle,PlatformActionListener listener){
        OnekeyShare oks = new OnekeyShare();
        if(newsArticle.getLitpicList().size()>0){
            oks.setImageUrl(newsArticle.getLitpicList().get(0));
        }
        if(newsArticle.getLink()!=null){
            String pageLink =newsArticle.getLink();
            oks.setText( newsArticle.body +" " +pageLink ); //内容
            oks.setTitleUrl(newsArticle.getLink());
        }else {
            oks.setText(newsArticle.body); //内容
        }
        oks.setTitle(newsArticle.getTitle());
        oks.setPlatform(SinaWeibo.NAME);
        oks.setCallback(listener);
        oks.show(context);
    }


    //分享微博
    public static void sharedSinaWeibo(Context context, InviteInfo inviteInfo, PlatformActionListener listener){
        OnekeyShare oks = new OnekeyShare();
        if(inviteInfo != null){
            oks.setText(inviteInfo.getContent()+inviteInfo.getLink());
//            oks.setTitle(inviteInfo.getTitle()+inviteInfo.getLink());
            oks.setUrl("");
            oks.setImageUrl(inviteInfo.getImgUrl());
        }
        oks.setPlatform(SinaWeibo.NAME);
        oks.setCallback(listener);
        oks.show(context);
    }

    //分享微博
    public static void sharedSinaWeibo(Context context,String imageUrl , PlatformActionListener listener){
        if(imageUrl!=null){
            OnekeyShare oks = new OnekeyShare();
            oks.setImageUrl(imageUrl);
            oks.setPlatform(SinaWeibo.NAME);
            oks.setCallback(listener);
            oks.show(context);
        }
    }
}
