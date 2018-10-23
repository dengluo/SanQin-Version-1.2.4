package com.pbids.sanqin.utils;

//import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.google.zxing.common.StringUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.pbids.sanqin.BuildConfig;
import com.pbids.sanqin.NotificationAdmain;
import com.pbids.sanqin.R;
import com.pbids.sanqin.ui.activity.BootPageActivity;
import com.pbids.sanqin.utils.eventbus.NimSystemMessageEvent;

import java.io.File;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:28
 * @desscribe 类描述:检查更新util
 * @remark 备注:
 * @see
 */
public class CheckUpdateUtil {
//    private static NotificationCompat.Builder builder;
//    private static NotificationManager manager;
    private static final int UPDATE_ID = 0;

    private static NotificationAdmain updateNotification ;
    private static final int UPDATE_NOTIFICATION_ID=156211;
    public static String NOTIFICATION_UPDATE = "NOTIFICATION_UPDATE";

   /**
     * 检查更新
     *
     * @param context
     * @return
     */
/*
    public static boolean checkUpdate(Context context) {
        int systemVersionCode = 2;
        int versionCode = AppUtils.getVersionCode(context);
        if (versionCode > systemVersionCode) {
            return false;
        }else{
            return true;
        }
        // 版本号转换为数组
    }
*/
   public static Intent getIntent(Context context) {
       Intent intent = new Intent(context, BootPageActivity.class);
       intent.putExtra(NOTIFICATION_UPDATE, NOTIFICATION_UPDATE);

       return intent;
   }

   public interface OnStartUpdateListener{
       void onUpdate(File file);
       void onFraction(int fraction);
    }

    private static int smallIcon = R.drawable.app_logo;
    private static String ticker = "正在下载更新";
    //显示更新弹窗
    public static void showUpdatePop(final Context context, String url, final OnStartUpdateListener listener){
        File file = FileUtil.getFile(context,FileUtil.PATH_DOWNLOAD);
        String fileDir = file.getAbsolutePath();
        //Log.i("wzh","fileDir: "+fileDir);
        //if(checkUpdate(context)){
            OkGo.<File>get(url).tag(context).execute(new FileCallback(fileDir,"huaqinchi.apk") {
                @Override
                public void onStart(Request<File, ? extends Request> request) {
                    super.onStart(request);
//                    if (updateNotification == null) {
//                        updateNotification = new NotificationAdmain(context, UPDATE_NOTIFICATION_ID);
//                    }
//                    //创建通知栏下载提示
//                    updateNotification.normal_notification(getIntent(context), smallIcon, ticker, "华亲池", "正在下载更新...");
                }

                @Override
                public void onSuccess(Response<File> response) {
//                    updateNotification.clear();//取消通知栏下载提示
//                    manager.cancel(UPDATE_ID);//取消通知栏下载提示
                    //下载成功后自动安装apk并打开
                    File file = response.body();
                    Log.v("cgl","download complete:"+file.getPath()+" size:"+file.getName());
                    if(listener!=null){
                        listener.onUpdate(file);
                    }
                }

                @Override
                public void downloadProgress(Progress progress){
                    super.downloadProgress(progress);
                    int fraction = (int)(progress.fraction*100);
                    if(listener!=null){
                        listener.onFraction(fraction);
                    }
                    Log.i("fraction",fraction+"");
                  /*  builder.setProgress(100, fraction, false)//更新进度
                            .setContentText(fraction + "%");
                    manager.notify(UPDATE_ID, builder.build());*/

                }
            });
        //}else{
       //     Log.i("wzh","已经是最新版本");
       // }
    }

    public static void installAPK(Context context,File file){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            Uri contentUri = FileProvider.getUriForFile(context, "com.pbids.sanqin.fileprovider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        }else{
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        //android.os.Process.killProcess(android.os.Process.myPid());
        context.startActivity(intent);
    }



}
