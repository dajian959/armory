package cn.armory.common.utils;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by desin on 2017/2/15.
 */
@SuppressLint({"SimpleDateFormat"})
public class TimeUtils {
    private static TimeUtils timeUtils;
    private static final int S = 1000;

    public static TimeUtils newInstance() {
        if (timeUtils == null) {
            timeUtils = new TimeUtils();
        }
        return timeUtils;
    }

    /**
     * 获取当前时间 格式为yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getNewTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        return df.format(new Date());
    }

    /**
     * 获取当前时间
     *
     * @param TimeType 时间格式
     * @return
     */
    public static String getNewTime(String TimeType) {
        SimpleDateFormat df = new SimpleDateFormat(TimeType);//设置日期格式
        return df.format(new Date());
    }

    /**
     * 获取两个时间只差
     *
     * @param oldTime  老时间
     * @param newTime  现在时间
     * @param timeType 时间格式
     * @return 秒
     */
    public static long getTimeDiff(String oldTime, String newTime, String timeType) {
        DateFormat df = new SimpleDateFormat(timeType);
        try {
            Date d1 = df.parse(oldTime);
            Date d2 = df.parse(newTime);
            long diff = d2.getTime() - d1.getTime();
            return diff / S;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String secToTime(String ss) {
        if (ss == null || ss.equals(""))
            return "";
        int time = Integer.parseInt(ss);
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 时间戳转换 格式为yyyy-MM
     *
     * @return
     */
    public static String formatToMonth(long second) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        return df.format(new Date(second * S));
    }

    /**
     * 时间戳转换 格式为yyyy-MM
     *
     * @return
     */
    public static String formatToMonth(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        return df.format(new Date(Long.parseLong(milSecond) * S));
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd
     *
     * @return
     */
    public static String formatToDay(long second) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date(second * S));
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd
     *
     * @return
     */
    public static String formatToDay(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date(Long.parseLong(milSecond) * S));
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd
     *
     * @return
     */
    public static String formatOnlyMouthAndDay(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("MM.dd");//设置日期格式
        return df.format(new Date(Long.parseLong(milSecond) * S));
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String formatToMinute(long milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        return df.format(new Date(milSecond * S));
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String formatToMinute(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        return df.format(new Date(Long.parseLong(milSecond) * S));
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String formatToSecond(long milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");//设置日期格式
        return df.format(new Date(milSecond * S));
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd HH:mm:SS
     *
     * @return
     */
    public static String formatToSecond(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");//设置日期格式
        return df.format(new Date(Long.parseLong(milSecond) * S));
    }

    public static String formatToDayAfter(long second, int daysAfter) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        return df.format(new Date(second * S + S * 60 * 60 * 24 * daysAfter));
    }

    /**
     * 时间戳转换 格式为HH:mm
     *
     * @return
     */
    public static String formatOnlyHourAndMinute(long timeSecond) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置时间格式
        return df.format(new Date(timeSecond * S));
    }

    /**
     * 时间戳转换
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String formatDateWithType(long milSecond, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);//这个是你要转成后的时间的格式
        return sdf.format(new Date(milSecond * S));
    }

    /**
     * 时间戳转换
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String formatDateWithType(String milSecond, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);//这个是你要转成后的时间的格式
        return sdf.format(new Date(Long.parseLong(milSecond) * S));
    }

    public static int convertDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();//日历类的实例化
        calendar.set(year, month - 1, day);//设置日历时间，月份必须减一
        return (int) (calendar.getTimeInMillis() / S);
    }

    public static int convertDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();//日历类的实例化
        calendar.set(year, month - 1, 1);//设置日历时间，月份必须减一
        return (int) (calendar.getTimeInMillis() / S);
    }

    public static String formatWithChinaToMinute(long timeSecond) {
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");//设置日期格式
        return df.format(new Date(timeSecond * S));
    }

    public static String formatWithChinaToMinute(String timeSecond) {
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");//设置日期格式
        return df.format(new Date(Long.parseLong(timeSecond) * S));
    }

    public static String formatWithChinaToDay(long timeSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
        return df.format(new Date(((long) timeSecond) * S));
    }

    public static String formatWithChinaToDay(String timeSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
        return df.format(new Date(Long.parseLong(timeSecond) * S));
    }

    public static String formatWithChina(long time) {
        long now = System.currentTimeMillis();
        long span = now - time;
        if (span < 0L) {
            return String.format("%tc", time);
        } else if (span < 3600000L) {
            return String.format(Locale.getDefault(), "%d分钟前", span / 60000L);
        } else if (span < 86400000L) {
            return String.format(Locale.getDefault(), "%d小时前", span / 3600000L);
        } else {
            long wee = getWeeOfToday();
            return time < wee && time >= wee - 86400000L ? "昨天" : String.format("%tF", time);
        }
    }

    private static long getWeeOfToday() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

}
