package cn.armory.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by desin on 2017/2/15.
 */

public class TimeUtils {
    private static TimeUtils timeUtils;

    public static TimeUtils newInsantce() {
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
        String time = df.format(new Date());
        return time;
    }

    /**
     * 获取当前时间 格式为yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getTypeNewTime(String timeType, String mTime) throws ParseException {
        SimpleDateFormat sdr = new SimpleDateFormat(timeType, Locale.CHINA);
        Date date;
        String times = null;
        try {
            date = sdr.parse(mTime);
            long l = date.getTime();
            String stf = String.valueOf(l);
            times = stf.substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

    /**
     * 获取当前时间
     *
     * @param TimeType 时间格式
     * @return
     */
    public static String getNewTime(String TimeType) {
        SimpleDateFormat df = new SimpleDateFormat(TimeType);//设置日期格式
        String time = df.format(new Date());
        return time;
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
            return diff / 1000;
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
    public static String getMonth(long second) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        String time = df.format(new Date(second*1000));
        return time;
    }

    /**
     * 时间戳转换 格式为yyyy-MM
     *
     * @return
     */
    public static String getMonth(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM");//设置日期格式
        String time = df.format(new Date(milSecond));
        return time;
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd
     *
     * @return
     */
    public static String getDate(long second) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df.format(new Date(second*1000));
        return time;
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd
     *
     * @return
     */
    public static String getDate(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df.format(new Date(Long.parseLong(milSecond)*1000));
        return time;
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd
     *
     * @return
     */
    public static String getMonthDate(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("MM.dd");//设置日期格式
        String time = df.format(new Date(Long.parseLong(milSecond)*1000));
        return time;
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String getDateTime(long milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        String time = df.format(new Date(milSecond*1000));
        return time;
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd HH:mm
     *
     * @return
     */
    public static String getDateTime(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");//设置日期格式
        String time = df.format(new Date(Long.parseLong(milSecond)*1000));
        return time;
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateTimeSecond(long milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");//设置日期格式
        String time = df.format(new Date(milSecond*1000));
        return time;
    }

    /**
     * 时间戳转换 格式为yyyy-MM-dd HH:mm:SS
     *
     * @return
     */
    public static String getDateTimeSecond(String milSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");//设置日期格式
        String time = df.format(new Date(Long.parseLong(milSecond)*1000));
        return time;
    }

    public static String getDate(long second, int daysAfter) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String time = df.format(new Date(second*1000 + 1000 * 60 * 60 * 24 * daysAfter));
        return time;
    }

    /**
     * 时间戳转换 格式为HH:mm
     *
     * @return
     */
    public static String getTime(long timeSecond) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");//设置时间格式
        String time = df.format(new Date(timeSecond*1000));
        return time;
    }

    /**
     * 时间戳转换
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        milSecond = milSecond * 1000;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);//这个是你要转成后的时间的格式
        String sd = sdf.format(new Date(milSecond));   // 时间戳转换成时间
        return sd;
    }

    public static int getTimeInSecond(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();//日历类的实例化
        calendar.set(year, month - 1, day);//设置日历时间，月份必须减一
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    public static int getTimeInSecond(int year, int month) {
        Calendar calendar = Calendar.getInstance();//日历类的实例化
        calendar.set(year, month - 1,1);//设置日历时间，月份必须减一
        return (int) (calendar.getTimeInMillis() / 1000);
    }

    public static String getChinaDateTime(long timeSecond) {
        SimpleDateFormat df = new SimpleDateFormat("MM月dd日 HH:mm");//设置日期格式
        String time = df.format(new Date(timeSecond*1000));
        return time;
    }

    public static String getChinaDate(long timeSecond) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");//设置日期格式
        String time = df.format(new Date(((long) timeSecond)*1000));
        return time;
    }
}
