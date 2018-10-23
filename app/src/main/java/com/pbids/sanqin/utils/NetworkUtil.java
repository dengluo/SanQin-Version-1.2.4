package com.pbids.sanqin.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by pbids903 on 2017/11/8.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:33
 * @desscribe 类描述:网络请求检查
 * @remark 备注:
 * @see
 */
public class NetworkUtil {
      /**
      * 检测当的网络（WLAN、3G/2G）状态 
      * @param context Context 
      * @return true 表示网络可用 
      */
            public static boolean isNetworkAvailable(Context context){
                ConnectivityManager connectivity = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivity != null) {
                    NetworkInfo info = connectivity.getActiveNetworkInfo();
                    if (info != null && info.isConnected())
                    {
                        // 当前网络是连接的
                        if (info.getState() == NetworkInfo.State.CONNECTED)
                        {
                            // 当前所连接的网络可用
                            return true;
                        }
                    }
                }
                return false;
            }

}
