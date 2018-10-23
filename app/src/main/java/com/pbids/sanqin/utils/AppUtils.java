package com.pbids.sanqin.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.google.gson.GsonBuilder;
import com.pbids.sanqin.common.MyApplication;
import com.pbids.sanqin.utils.eventbus.PermissionEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;


/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:27
 * @desscribe 类描述:apputil
 * @remark 备注:
 * @see
 */
public class AppUtils {

    private static Context mContext;
    private static Thread mUiThread;

    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void init(Context context) {
        mContext = context;
        mUiThread = Thread.currentThread();
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static AssetManager getAssets() {
        return mContext.getAssets();
    }

    public static Resources getResource() {
        return mContext.getResources();
    }

    public static boolean isUIThread() {
        return Thread.currentThread() == mUiThread;
    }

    public static void runOnUI(Runnable r) {
        sHandler.post(r);
    }

    public static void runOnUIDelayed(Runnable r, long delayMills) {
        sHandler.postDelayed(r, delayMills);
    }

    public static void removeRunnable(Runnable r) {
        if (r == null) {
            sHandler.removeCallbacksAndMessages(null);
        } else {
            sHandler.removeCallbacks(r);
        }
    }


    public static <V> V readAreaJson(Context context, Class<V> type) {
        InputStreamReader inputReader;
        try {
            inputReader = new InputStreamReader(context
                    .getResources().getAssets().open("administrative_planning.json"));//administrative_planning
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                sb.append(line);
            }
            GsonBuilder builder = new GsonBuilder();
            return builder.create().fromJson(sb.toString(), type);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //拍照上传图片使用,同时需要照相机权限和写文件权限
    public static boolean checkTakePhotoPermission(Activity context, int requestCode){
        if (ContextCompat.checkSelfPermission(context,  Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ||ContextCompat.checkSelfPermission(context,  Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA
                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionEvent.REQUEST_CODE_TAKE_PICTURE_PESSION);
            // 没有权限，申请权限。
            return false;
        }else{
            // 有权限了，去放肆吧。
            return true;
        }
    }

    //使用系统相册上传图片使用,同时需要读文件权限和写文件权限
    public static boolean checkOpenAlemPermission(Activity context, int requestCode){
        if (ContextCompat.checkSelfPermission(context,  Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context,  Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    ,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionEvent.REQUEST_CODE_IMAGES_PESSION);
            return false;
        }else{
            return true;
        }
    }

    public static boolean checkCameraPermission(Activity context, int requestCode){
        if (ContextCompat.checkSelfPermission(context,  Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, PermissionEvent.REQUEST_CODE_CAMERA);
            // 没有权限，申请权限。
            return false;
        }else{
            // 有权限了，去放肆吧。
            return true;
        }
    }
    public static boolean checkLocationPermission(Activity context){
        if (ContextCompat.checkSelfPermission(context,  Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PermissionEvent.REQUEST_CODE_LOCATION);
            // 没有权限，申请权限。
            return false;
        }else{
            // 有权限了，去放肆吧。
            return true;
        }
    }


    public static boolean checkWechatMomentsPermission(Activity context, int requestCode){
        if (ContextCompat.checkSelfPermission(context,  Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context,  Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            return false;
        }else{
            return true;
        }
    }

    public static boolean checkReadSelfPermission(Activity context,int requestCode){
        if (ContextCompat.checkSelfPermission(context,  Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
            // 没有权限，申请权限。
            return false;
        }else{
            // 有权限了，去放肆吧。
            return true;
        }
    }

    public static void showSoftInputFromWindow(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }
    }

    public static int[] getScreenSize(Context context) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics outMetrics = new DisplayMetrics();
    wm.getDefaultDisplay().getMetrics(outMetrics);
    return new int[]{outMetrics.widthPixels, outMetrics.heightPixels};
    }

    public boolean isNewVersion(Context context){
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int currentVersion = info.versionCode;
        SharedPreferences prefs = context.getSharedPreferences("app_info",0);
        int lastVersion = prefs.getInt("VERSION_KEY", 0);
        if (currentVersion > lastVersion) {
            //如果当前版本大于上次版本，该版本属于第一次启动
            //将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
            prefs.edit().putInt("VERSION_KEY",currentVersion).commit();
        } else {//不是第一次启动，跳过引导页直接到欢迎界面
//            Intent intent = new Intent();
//            startActvity(intent);
        }
        return true;
    }

    //版本名
    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    //版本号
    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private static PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);

            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        //int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 获取自己手机的ipv4地址
     * @return
     */
    public static String getIp() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
                        .hasMoreElements();) {
                    InetAddress inetAddress = ipAddr.nextElement();
                    // ipv4地址
                    if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address){
//                        InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
                        Log.e("wzh", "ipv4=" + inetAddress.getHostAddress());
                        return inetAddress.getHostAddress();
                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//                        if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address){
////                        InetAddressUtils.isIPv4Address(inetAddress.getHostAddress())) {
//                            Log.e("wzh", "ipv4=" + inetAddress.getHostAddress());
//                            return inetAddress.getHostAddress();
//                        }
//                    }else{
//                        if(!inetAddress.isLoopbackAddress() && Inetaddressutils.isIPv4Address(inetAddress.getHostAddress())){
//                            Log.e("wzh", "ipv4=" + inetAddress.getHostAddress());
//                            return inetAddress.getHostAddress();
//                        }
//                    }

                }
            }
        } catch (Exception ex) {

        }
        return "192.168.1.0";
    }

    /**
     * 解决小米手机上获取图片路径为null的情况
     * @param intent
     * @return
     */
    public static Uri getUri(android.content.Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr = MyApplication.getApplication().getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[] { MediaStore.Images.ImageColumns._ID },
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }

}
