package com.pbids.sanqin.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import com.pbids.sanqin.model.entity.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pbids903 on 2017/12/12.
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:27
 * @desscribe 类描述:应用商店util
 * @remark 备注:
 * @see
 */
public class AppMarketUtil {
    /**
     * 获取已安装应用商店的包名列表
     * 获取有在AndroidManifest 里面注册<category android:name="android.intent.category.APP_MARKET" />的app
     * @param context
     * @return
     */
    public static ArrayList<String> getInstallAppMarkets(Context context) {
        //默认的应用市场列表，有些应用市场没有设置APP_MARKET通过隐式搜索不到
        Log.i("wzh","context: "+context);
        ArrayList<String>  pkgList = new ArrayList<>();
        pkgList.add("com.tencent.android.qqdownloader");//腾讯应用宝
        pkgList.add("com.qihoo.appstore");              //360手机助手
        pkgList.add("com.baidu.appsearch");             //百度手机助手
        pkgList.add("com.xiaomi.market");               //小米应用商店
        pkgList.add("com.huawei.appmarket");            //华为应用商店
        pkgList.add("com.wandoujia.phoenix2");          //豌豆荚
        pkgList.add("com.dragon.android.pandaspace");  //91手机助手
        pkgList.add("com.hiapk.marketpho");             //安智应用商店
        pkgList.add("com.yingyonghui.market");          //应用汇
        pkgList.add("com.tencent.qqpimsecure");         //QQ手机管家
        pkgList.add("com.mappn.gfan");                   // 机锋应用市场
        pkgList.add("com.pp.assistant");                 // PP手机助手
        pkgList.add("com.oppo.market");                  // OPPO应用商店
        pkgList.add("cn.goapk.market");                  //GO市场
        pkgList.add("zte.com.market");                   // 中兴应用商店
        pkgList.add("com.yulong.android.coolmart");     //宇龙Coolpad应用商店
        pkgList.add("com.lenovo.leos.appstore");        // 联想应用商店
        pkgList.add("com.coolapk.market");               //  cool市场
        ArrayList<String> pkgs = new ArrayList<String>();
        if (context == null)
            return pkgs;
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.APP_MARKET");
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> infos = pm.queryIntentActivities(intent, 0);
        Log.i("wzh","infos.size(): "+infos.size());
        if (infos == null || infos.size() == 0) {
            return pkgList;
        }
        int size = infos.size();
        for (int i = 0; i < size; i++) {
            String pkgName = "";
            try {
                ActivityInfo activityInfo = infos.get(i).activityInfo;
                pkgName = activityInfo.packageName;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(pkgName))
                pkgs.add(pkgName);

            Log.i("wzh","pkgName: "+pkgName);

        }
        //取两个list并集,去除重复
        pkgList.removeAll(pkgs);
        pkgs.addAll(pkgList);
        return pkgs;
    }

    /**
     * 过滤出已经安装的包名集合
     * @param context
     * @param pkgs 待过滤包名集合
     * @return 已安装的包名集合
     */
    public static ArrayList<AppInfo> getFilterInstallMarkets(Context context,ArrayList<String> pkgs) {
//        appInfos.clear();
        ArrayList<String> appList = new ArrayList<String>();
        ArrayList<AppInfo> appInfos = new ArrayList<AppInfo>();

        if (context == null || pkgs == null || pkgs.size() == 0)
            return appInfos;
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> installedPkgs = pm.getInstalledPackages(0);
        int li = installedPkgs.size();
        int lj = pkgs.size();
        for (int j = 0; j < lj; j++) {
            for (int i = 0; i < li; i++) {
                String installPkg = "";
                String checkPkg = pkgs.get(j);
                PackageInfo packageInfo = installedPkgs.get(i);
                try {
                    installPkg = packageInfo.packageName;

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(installPkg))
                    continue;
                if (installPkg.equals(checkPkg)) {
                    // 如果非系统应用，则添加至appList,这个会过滤掉系统的应用商店，如果不需要过滤就不用这个判断
//                    if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        //将应用相关信息缓存起来，用于自定义弹出应用列表信息相关用
                        AppInfo appInfo = new AppInfo();
                        appInfo.setAppName(packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString());
                        appInfo.setAppIcon(packageInfo.applicationInfo.loadIcon(context.getPackageManager()));
                        appInfo.setPackageName(packageInfo.packageName);
                        appInfo.setVersionCode(packageInfo.versionCode);
                        appInfo.setVersionName(packageInfo.versionName);
                        appInfos.add(appInfo);
                        appList.add(installPkg);
                    Log.i("wzh","packageName: "+packageInfo.packageName);
//                    }
                    break;
                }

            }
        }
        return appInfos;
    }

    /**
     * 跳转到应用市场app详情界面
     * @param appPkg App的包名
     * @param marketPkg 应用市场包名
     */
    public static void launchAppDetail(Context context,String appPkg, String marketPkg) {
        try {

            if (TextUtils.isEmpty(appPkg))
                return;
//            Uri uri;
//            if(Build.VERSION.SDK_INT>Build.VERSION_CODES.N){
//                contentUri = FileProvider.getUriForFile(context, "com.pbids.sanqin.fileprovider", file);
//            }
            Uri uri = Uri.parse("market://details?id=" + appPkg);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if (!TextUtils.isEmpty(marketPkg))
                intent.setPackage(marketPkg);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
