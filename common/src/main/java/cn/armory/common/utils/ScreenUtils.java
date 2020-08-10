package cn.armory.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

/**
 * 屏幕相关工具类
 */

public class ScreenUtils {
    private ScreenUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return int 屏幕宽px
     */
    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) ACUtils.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getRealMetrics(dm);// 给白纸设置宽高
        }
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return int 屏幕高px
     */
    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager) ACUtils.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
        if (windowManager != null) {
            windowManager.getDefaultDisplay().getRealMetrics(dm);// 给白纸设置宽高
        }
        return dm.heightPixels;
    }

    /**
     * 设置页面全屏
     * <p>PS：一定要在setContentView之前调用，否则报错</p>
     *
     * @param activity 当前Activity
     */
    public static void setFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置为横屏.
     *
     * @param activity The activity.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public static void setLandscape(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * 设置为竖屏.
     *
     * @param activity The activity.
     */
    @SuppressLint("SourceLockedOrientationActivity")
    public static void setPortrait(@NonNull final Activity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 判断是否是横屏.
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isLandscape() {
        return ACUtils.getApplicationContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * 判断是否是竖屏.
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isPortrait() {
        return ACUtils.getApplicationContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * 判断是否为平板.
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isTablet() {
        return (ACUtils.getApplicationContext().getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

}
