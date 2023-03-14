package cn.armory.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 栏相关工具类
 */

public class BarUtils {
    private static final String TAG_COLOR = "TAG_COLOR";
    private static final int TAG_OFFSET = -123;

    private BarUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity activity
     * @param color    状态栏颜色值
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     */
    public static void setStatusBarColor(@NonNull final Activity activity, @ColorInt final int color, boolean dark) {
        setStatusBarColor(activity, null, color, dark);
    }

    /**
     * 设置状态栏颜色
     *
     * @param activity  activity
     * @param viewGroup viewGroup
     * @param color     状态栏颜色值
     * @param dark      是否把状态栏文字及图标颜色设置为深色
     */
    public static void setStatusBarColor(@NonNull final Activity activity, ViewGroup viewGroup, @ColorInt final int color, boolean dark) {
        if (Build.VERSION.SDK_INT < 19) return;
        int statusBarColor = color;
        //设置状态栏透明
        setTranslucentStatus(activity);
        //隐藏自定义的状态栏view
        hideColorView(activity);
        //如果传入的viewGroup为null，直接为activity 增加 MarginTop 为状态栏高度
//        if(viewGroup == null) addMarginTopEqualStatusBarHeight(activity);

        if (PhoneUtils.isMIUI()) {
            //小米手机设置状态栏图标文字颜色为深色还是白色
            setStatusBarModeByMIUI(activity, dark);
        } else if (PhoneUtils.isFlyme()) {
            //魅族手机设置状态栏图标文字颜色为深色还是白色
            setStatusBarModeByFlyme(activity, dark);
        } else {
            if (Build.VERSION.SDK_INT >= 23) {
                //其余手机且系统版本高于6.0，设置状态栏图标文字颜色为深色还是白色
                setStatusBarMode(activity, dark);
            }
            if (statusBarColor == Color.WHITE && Build.VERSION.SDK_INT <= 22) {
                //手机系统小于6.0的手机时，如果设置状态栏为白色，那么状态栏上的按钮和文字也是白色，影响观看
                //这里强制转换为 #CCCCCC 灰色系
                statusBarColor = 0xffcccccc;
            }
        }

        if (viewGroup != null) {
            addStatusBarColor(viewGroup, statusBarColor);
        } else {
            addStatusBarColor(activity, statusBarColor);
        }
    }

    /**
     * 隐藏状态栏
     *
     * @param activity activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     */
    public static void hideStatusBar(@NonNull final Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT < 19) return;
        setTranslucentStatus(activity);
        subtractMarginTopEqualStatusBarHeight(activity);
        hideColorView(activity);
        if (PhoneUtils.isMIUI()) {
            setStatusBarModeByMIUI(activity, dark);
        } else if (PhoneUtils.isFlyme()) {
            setStatusBarModeByFlyme(activity, dark);
        } else if (Build.VERSION.SDK_INT >= 23) {
            setStatusBarMode(activity, dark);
        }
    }

    private static void addStatusBarColor(final ViewGroup viewGroup, final int color) {
        viewGroup.addView(createColorStatusBarView(viewGroup.getContext(), color), 0);
    }

    private static void addStatusBarColor(final Activity activity, final int color) {
        ViewGroup parent = (ViewGroup) activity.findViewById(android.R.id.content);
        View fakeStatusBarView = parent.findViewWithTag(TAG_COLOR);
        if (fakeStatusBarView != null) {
            if (fakeStatusBarView.getVisibility() == View.GONE) {
                fakeStatusBarView.setVisibility(View.VISIBLE);
            }
            fakeStatusBarView.setBackgroundColor(color);
        } else {
            parent.addView(createColorStatusBarView(parent.getContext(), color));
        }
    }

    /**
     * 绘制一个和状态栏一样高的颜色矩形
     */
    private static View createColorStatusBarView(final Context context, final int color) {
        View statusBarView = new View(context);
        statusBarView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
        statusBarView.setBackgroundColor(color);
        statusBarView.setTag(TAG_COLOR);
        return statusBarView;
    }

    /**
     * 为 activity 增加 MarginTop 为状态栏高度
     *
     * @param activity activity
     */
    private static void addMarginTopEqualStatusBarHeight(@NonNull Activity activity) {
        View view = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        addMarginTopEqualStatusBarHeight(view);
    }

    /**
     * 为 view 增加 MarginTop 为状态栏高度
     *
     * @param view view
     */
    private static void addMarginTopEqualStatusBarHeight(@NonNull View view) {
        Object haveSetOffset = view.getTag(TAG_OFFSET);
        if (haveSetOffset != null && (Boolean) haveSetOffset) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin + getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin);
        view.setTag(TAG_OFFSET, true);
    }

    /**
     * 为 activity 减少 MarginTop 为状态栏高度
     *
     * @param activity activity
     */
    private static void subtractMarginTopEqualStatusBarHeight(@NonNull Activity activity) {
        View view = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        subtractMarginTopEqualStatusBarHeight(view);
    }

    /**
     * 为 view 减少 MarginTop 为状态栏高度
     *
     * @param view view
     */
    private static void subtractMarginTopEqualStatusBarHeight(@NonNull View view) {
        Object haveSetOffset = view.getTag(TAG_OFFSET);
        if (haveSetOffset == null || !(Boolean) haveSetOffset) return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        layoutParams.setMargins(layoutParams.leftMargin,
                layoutParams.topMargin - getStatusBarHeight(),
                layoutParams.rightMargin,
                layoutParams.bottomMargin);
        view.setTag(TAG_OFFSET, false);
    }

    private static void hideColorView(final Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        View fakeStatusBarView = decorView.findViewWithTag(TAG_COLOR);
        if (fakeStatusBarView == null) return;
        fakeStatusBarView.setVisibility(View.GONE);
    }

    /**
     * 获取状态栏高度(px)
     *
     * @return 状态栏高度px
     */
    public static int getStatusBarHeight() {
        Resources resources = ACUtils.getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 设置页面全屏
     * <p>PS：一定要在setContentView之前调用，否则报错</p>
     *
     * @param activity 当前Activity
     */
    @Deprecated
    public static void setFullScreen(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置状态栏透明
     *
     * @param activity 当前Activity
     */
    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity) {
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 6.0以上设置Android状态栏的字体颜色
     *
     * @param activity 当前Activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     */
    public static void setStatusBarMode(Activity activity, boolean dark) {
        Window window = activity.getWindow();
        if (window == null) return;
        if (Build.VERSION.SDK_INT >= 23) {
            if (dark) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
        }
    }

    public static boolean supportStatusBarLightMode(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return BarUtils.setStatusBarModeByFlyme(activity, true)
                    || BarUtils.setStatusBarModeByMIUI(activity, true)
                    || Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
        }
        return false;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param activity 当前Activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setStatusBarModeByFlyme(Activity activity, boolean dark) {
        boolean result = false;
        if (activity != null) {
            try {
                Window window = activity.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
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
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 需要MIUIV6以上
     *
     * @param activity 当前Activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean setStatusBarModeByMIUI(Activity activity, boolean dark) {
        boolean result = false;
        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (Build.VERSION.SDK_INT >= 23) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
