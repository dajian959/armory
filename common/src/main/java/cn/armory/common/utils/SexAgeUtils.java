package cn.armory.common.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SexAgeUtils {
    /**
     * 通过身份证号码获取性别、年龄
     */
    public static Map<String, String> getSexAge(String certificateNo) {
        Map<String, String> map = new HashMap<>();
        if (CSUtils.isEmpty(certificateNo)) {
            map.put("sex", "3");
            map.put("age", "0");
            return map;
        }

        int length = certificateNo.length();
        if (length == 15) {
            map.put("sex", Integer.parseInt(certificateNo.substring(length - 3)) % 2 == 0 ? "0" : "1");
            map.put("age", getAge("19" + certificateNo.substring(6, 8), certificateNo.substring(8, 10), certificateNo.substring(10, 12)));
        } else if (length == 18) {
            map.put("sex", Integer.parseInt(certificateNo.substring(length - 4, length - 1)) % 2 == 0 ? "0" : "1");
            map.put("age", getAge(certificateNo.substring(6, 10), certificateNo.substring(10, 12), certificateNo.substring(12, 14)));
        }

        return map;
    }

    /**
     * 获取年龄（周岁）
     *
     * @param year  年
     * @param month 月
     * @param day   日
     * @return 年龄
     */
    private static String getAge(String year, String month, String day) {
        // 得到当前时间的年、月、日
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayNow = cal.get(Calendar.DATE);

        // 用当前年月日减去出生年月日
        int yearMinus = yearNow - Integer.parseInt(year);
        int monthMinus = monthNow - Integer.parseInt(month);
        int dayMinus = dayNow - Integer.parseInt(day);

        int age = yearMinus;// 先大致赋值
        if (age <= 0) {
            age = 0;
        } else if (monthMinus < 0) {
            age = age - 1;
        } else if (monthMinus == 0 && dayMinus < 0) {
            age = age - 1;
        }

        return age + "";
    }
}
