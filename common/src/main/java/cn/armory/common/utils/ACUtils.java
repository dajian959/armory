package cn.armory.common.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;


/**
 * Context相关.
 */

public class ACUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private ACUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static void init(@NonNull final Context context) {
        if (mContext == null) {
            ACUtils.mContext = context.getApplicationContext();
        }
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getApplicationContext() {
        if (mContext != null) {
            return mContext;
        }
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object at = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(at);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            init((Application) app);
            return mContext;
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }

        throw new NullPointerException("u should init first");
    }


    /**
     * 判断App是否是Debug版本
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isAppDebug(Context context) {

        if (CSUtils.isSpace(context.getPackageName())) {
            return false;
        }
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e.getMessage());
            return false;
        }
    }

    /**
     * 获取版本号名称
     *
     * @return version
     */
    public static String getVersionName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return verName;
        }
        return verName;
    }
}
