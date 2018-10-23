package com.pbids.sanqin.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by liang on 2018/3/8.
 */

public class CommonGlideInstance {

	//Glide加载图片带占位符，带错误图，可以跳过内存缓存
	public void setImageViewBackgroundForUrl(Context context,ImageView imageView
			, String url, int placeholder, int error,boolean hasSize){
		RequestOptions requestOptions = new RequestOptions();
		if(hasSize = true){
			requestOptions.override(placeholder,error);
		}else{
			requestOptions.placeholder(placeholder);
			requestOptions.placeholder(error);
		}

		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(url).apply(requestOptions).into(imageView);
	}

	//Glide加载图片带占位符，带错误图，不可以跳过内存缓存
	public void setImageViewBackgroundForUrl(Context context,ImageView imageView,String url, int placeholder, int error){
		RequestOptions requestOptions = new RequestOptions();
		if(placeholder!=0){
			requestOptions.placeholder(placeholder);
//            requestOptions.transform();
		}
		if(error!=0){
			requestOptions.error(error);
		}
		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(url).apply(requestOptions).into(imageView);
	}

	//Glide加载图片带占位符，带错误图，不可以跳过内存缓存
	public void setImageViewBackgroundForUrl(Context context,ImageView imageView,int res, int placeholder, int error){
		RequestOptions requestOptions = new RequestOptions();
		if(placeholder!=0){
			requestOptions.placeholder(placeholder);
//            requestOptions.transform();
		}
		if(error!=0){
			requestOptions.error(error);
		}
		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(res).apply(requestOptions).into(imageView);
	}

	//Glide加载图片不带占位符，带错误图，不可以跳过内存缓存
	public void setImageViewBackgroundForUrl(Context context,ImageView imageView,String url){
		RequestOptions requestOptions = new RequestOptions();
//        if(placeholder!=0){
//            requestOptions.placeholder(placeholder);
////            requestOptions.transform();
//        }
//        if(error!=0){
//            requestOptions.error(error);
//        }
		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(url).apply(requestOptions).into(imageView);
	}

	//Glide加载图片不带占位符，带错误图，不可以跳过内存缓存
	public void setImageViewBackgroundForUrl(Context context,ImageView imageView,int res){
		RequestOptions requestOptions = new RequestOptions();
//        if(placeholder!=0){
//            requestOptions.placeholder(placeholder);
////            requestOptions.transform();
//        }
//        if(error!=0){
//            requestOptions.error(error);
//        }
		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(res).apply(requestOptions).into(imageView);
	}

	public void setImageViewBackgroundForUrl(Context context,ImageView imageView,int res,boolean isGif){
		RequestOptions requestOptions = new RequestOptions();
//        if(placeholder!=0){
//            requestOptions.placeholder(placeholder);
////            requestOptions.transform();
//        }
//        if(error!=0){
//            requestOptions.error(error);
//        }
		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(res).apply(requestOptions).into(imageView);
	}

	//Glide加载图片不带占位符，带错误图，不可以跳过内存缓存
	public void setImageViewBackgroundForUrl(Context context,ImageView imageView,int res,int w,int h,boolean hasSize){
		RequestOptions requestOptions = new RequestOptions();
//        if(placeholder!=0){
//            requestOptions.placeholder(placeholder);
////            requestOptions.transform();
//        }
//        if(error!=0){
//            requestOptions.error(error);
//        }
		requestOptions.override(w,h);
		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(res).apply(requestOptions).into(imageView);
	}

	//Glide加载图片不带占位符，带错误图，不可以跳过内存缓存
	public void setImageViewBackgroundForUrl(Context context,ImageView imageView, String url, RequestListener listener){
		RequestOptions requestOptions = new RequestOptions();
//        if(placeholder!=0){
//            requestOptions.placeholder(placeholder);
////            requestOptions.transform();
//        }
//        if(error!=0){
//            requestOptions.error(error);
//        }
		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(url).apply(requestOptions).listener(listener).into(imageView);
	}

	//Glide加载图片不带占位符，带错误图，不可以跳过内存缓存
	public void setImageViewBackgroundForUrl(Context context, ImageView imageView, Bitmap bitmap){
		RequestOptions requestOptions = new RequestOptions();
//        if(placeholder!=0){
//            requestOptions.placeholder(placeholder);
////            requestOptions.transform();
//        }
//        if(error!=0){
//            requestOptions.error(error);
//        }
		requestOptions.skipMemoryCache(false);
		Glide.with(context).load(bitmap).apply(requestOptions).into(imageView);
	}
}
