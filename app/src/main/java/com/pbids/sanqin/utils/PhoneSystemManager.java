package com.pbids.sanqin.utils;

/**
 * Created by pbids903 on 2018/1/23.
 */

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:36
 * @desscribe 类描述:手机操作系统检测
 * @remark 备注:
 * @see
 */
public class PhoneSystemManager {

    public static final String SYS_EMUI = "sys_emui";// 华为
    public static final String SYS_MIUI = "sys_miui";// 小米
    public static final String SYS_FLYME = "sys_flyme";// 魅族
    public static final String SYS_NORMAL = "sys_normal";// 一般市面手机


    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_EMUI_API_LEVEL = "ro.build.hw_emui_api_level";
    private static final String KEY_EMUI_VERSION = "ro.build.version.emui";
    private static final String KEY_EMUI_CONFIG_HW_SYS_VERSION = "ro.confg.hw_systemversion";

    public static String getTelPhoneSystem() {
        String SYS = SYS_NORMAL;
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            if (prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null) {
                SYS = SYS_MIUI;//小米
            } else if (prop.getProperty(KEY_EMUI_API_LEVEL, null) != null
                    || prop.getProperty(KEY_EMUI_VERSION, null) != null
                    || prop.getProperty(KEY_EMUI_CONFIG_HW_SYS_VERSION, null) != null) {
                SYS = SYS_EMUI;//华为
            } else if (getMeizuFlymeOSFlag().toLowerCase().contains("flyme")) {
                SYS = SYS_FLYME;//魅族
            }
        } catch (IOException e) {
            e.printStackTrace();
            return SYS;
        }
        return SYS;
    }

    public static String getMeizuFlymeOSFlag() {
        return getSystemProperty("ro.build.display.id", "");
    }

    private static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }


    /** 设置透明状态栏目
     setMiuiStatusBarDarkMode(this, true);
     setMeizuStatusBarDarkIcon(this, true);
     **/
    public static boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view_donate_records.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                activity.getWindow().setAttributes(lp);
                result = true;
            } catch (Exception e) {
            }
        }
        return result;
    }

    /**
     * 处理华为手机虚拟按键和底部导航遮挡问题
     * 出现原因-
     * getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
     * getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
     * 加：
     * -android::fitsSystemWindows="true"
     * 去除-FLAG_TRANSLUCENT_NAVIGATION
     * -仍旧不行使用本类-在setContentView(……
     * if (AndroidWorkaround.checkDeviceHasNavigationBar(this))
     * {
     * AndroidWorkaround.assistActivity(findViewById(android.R.id.content));
     * }
     *
     */
    public static class AndroidWorkaround {

        public static void assistActivity(View content) {
            new AndroidWorkaround(content);
        }

        private View mChildOfContent;
        private int usableHeightPrevious;
        private ViewGroup.LayoutParams frameLayoutParams;

        private AndroidWorkaround(View content) {
            mChildOfContent = content;
            mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    possiblyResizeChildOfContent();
                }
            });
            frameLayoutParams = mChildOfContent.getLayoutParams();
        }

        private void possiblyResizeChildOfContent() {
            int usableHeightNow = computeUsableHeight();
            if (usableHeightNow != usableHeightPrevious) {

                frameLayoutParams.height = usableHeightNow;
                mChildOfContent.requestLayout();
                usableHeightPrevious = usableHeightNow;
            }
        }

        private int computeUsableHeight() {
            Rect r = new Rect();
            mChildOfContent.getWindowVisibleDisplayFrame(r);
            return (r.bottom);
        }

        /** 获取虚拟功能键高度 */
        public static int getVirtualBarHeigh(Context context) {

            int vh = 0;

            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();

            try {

                @SuppressWarnings("rawtypes")
                Class c = Class.forName("android.view_donate_records.Display");

                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);

                method.invoke(display, dm);
                vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return vh;
        }

        public static boolean checkDeviceHasNavigationBar(Context context) {
            boolean hasNavigationBar = false;
            Resources rs = context.getResources();
            int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
            if (id > 0) {
                hasNavigationBar = rs.getBoolean(id);
            }
            try {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride)) {
                    hasNavigationBar = false;
                } else if ("0".equals(navBarOverride)) {
                    hasNavigationBar = true;
                }
            } catch (Exception e) {

            }
            return hasNavigationBar;
        }
    }
}
