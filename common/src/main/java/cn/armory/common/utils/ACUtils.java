package cn.armory.common.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * 可获取 ApplicationContext 对象.
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
            e.printStackTrace();

        }

        throw new NullPointerException("u should init first");
    }
}
