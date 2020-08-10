package cn.armory.common.utils;

import android.content.Context;

/**
 * 尺寸相关工具类
 */
public final class DensityUtils {

    private DensityUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(final float dpValue) {
        final float scale = ACUtils.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int dp2px(Context context, final float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(final float pxValue) {
        final float scale = ACUtils.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(final float spValue) {
        final float fontScale = ACUtils.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int sp2px(Context context, final float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(final float pxValue) {
        final float fontScale = ACUtils.getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * px转weex
     *
     * @param pxValue px值
     * @return wp值
     */
    public static int px2wp(final float pxValue) {
        final double scale = ScreenUtils.getScreenWidth() / 750.0;
        return (int) (pxValue / scale + 0.5f);
    }
}