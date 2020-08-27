package cn.armory.common.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * 手机相关工具类
 */

public class PhoneUtils {
    private PhoneUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    /**
     * 获取手机IMEI码
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.READ_PHONE_STATE"/>}</p>
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public static String getIMEI(Context context){
        String imei = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                imei = tm.getDeviceId();
            }else {
                Method method = tm.getClass().getMethod("getImei");
                imei = (String) method.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imei;
    }

    /**
     * Return the sim operator name.
     *
     * @return the sim operator name
     */
    public static String getSimOperatorName() {
        TelephonyManager tm =
                (TelephonyManager) ACUtils.getApplicationContext().getSystemService(TELEPHONY_SERVICE);
        return tm != null ? tm.getSimOperatorName() : "";
    }

    /**
     * 获取Sim卡运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 移动网络运营商名称
     *          <br>{@code "中国电信"}</br>
     *          <br>{@code "中国移动"}</br>
     *          <br>{@code "中国联通"}</br>
     *          <br>{@code "未知"}</br>
     */
    public static String getSimOperatorByMnc() {
        TelephonyManager tm = (TelephonyManager) ACUtils.getApplicationContext()
                .getSystemService(TELEPHONY_SERVICE);
        String operator = tm != null ? tm.getSimOperator() : null;
        if (operator == null) return "未知";
        @SuppressLint("MissingPermission") String imsi = tm.getSubscriberId();
        switch (operator) {
            case "46000":
            case "46002":
            case "46007":
                return "中国移动";
            case "46001":
            case "46006":
                return "中国联通";
            case "46003":
            case "46005":
            case "46011":
                return "中国电信";
            default:
                return "未知";
        }
    }

    public static String getSubscriberId() {
        TelephonyManager tm = (TelephonyManager) ACUtils.getApplicationContext()
                .getSystemService(TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String imsi = tm != null ? tm.getSubscriberId() : null;
        if (imsi == null) return "未知";
        if(imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
            return "中国移动";
        } else if(imsi.startsWith("46001") || imsi.startsWith("46006")) {
            return "中国联通";
        } else if(imsi.startsWith("46003") || imsi.startsWith("46005") || imsi.startsWith("46011")) {
            return "中国电信";
        } else {
            return "未知";
        }
    }

    /**
     * 获取Sim卡运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 移动网络运营商名称
     *          <br>{@code "1"：中国电信}</br>
     *          <br>{@code "2"：中国移动}</br>
     *          <br>{@code "3"：中国联通}</br>
     *          <br>{@code "4"：未知}</br>
     */
    public static String getSimOperatorCode() {
        String operatorName = getSimOperatorByMnc();
        switch (operatorName) {
            case "中国电信":
                return "1";
            case "中国移动":
                return "2";
            case "中国联通":
                return "3";
            default:
                return "4";
        }
    }

    /**
     * 判断手机系统是否是小米MIUI
     */
    public static boolean isMIUI() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    /**
     * 判断手机系统是否是魅族Flyme
     */
    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return line;
    }
}
