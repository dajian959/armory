package cn.armory.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串校验相关工具类
 */

public class CheckStringUtils {
    private CheckStringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0 || "null".contentEquals(s);
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(final String s) {
        return s == null || s.trim().length() == 0 || "null".equals(s.trim());
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(final String s) {
        if (s == null || "null".equals(s))
            return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 正则：手机号（简单）
     */
    public static final String REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$";
    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8), 135, 136, 137, 138, 139, 147, 150, 151, 152, 157, 158, 159, 178, 182, 183, 184, 187, 188, 198</p>
     * <p>联通：130, 131, 132, 145, 155, 156, 166, 171, 175, 176, 185, 186</p>
     * <p>电信：133, 153, 173, 177, 180, 181, 189, 199</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(16[6])|(17[0,1,3,5-8])|(18[0-9])|(19[8,9]))\\d{8}$";
    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 验证手机号（简单）
     *
     * @param s 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileSimple(final CharSequence s) {
        return isMatch(REGEX_MOBILE_SIMPLE, s);
    }

    /**
     * 验证手机号（精确）
     *
     * @param s 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(final CharSequence s) {
        return isMatch(REGEX_MOBILE_EXACT, s);
    }

    /**
     * 验证身份证号码18位
     *
     * @param s 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard(final CharSequence s) {
        return isMatch(REGEX_ID_CARD18, s);
    }

    /**
     * 验证邮箱
     *
     * @param s 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(final CharSequence s) {
        return isMatch(REGEX_EMAIL, s);
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param s     要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(final String regex, final CharSequence s) {
        return !isEmpty(s) && Pattern.matches(regex, s);
    }

    /**
     * 判断该字符串是否为中文
     */
    public static boolean isChinese(String s) {
        if (s == null || "".equals(s))
            return false;
        for (int i = 0; i < s.length(); i++) {
            int n = (int) s.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断输入是否是表情
     */
    public static boolean containsEmoji(final CharSequence s) {
        if (isEmpty(s))
            return false;
        int len = s.length();
        for (int i = 0; i < len; i++) {
            char hs = s.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (s.length() > 1) {
                    char ls = s.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d
                        || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (s.length() > 1 && i < s.length() - 1) {
                    char ls = s.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA)
                || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)));
    }


    /**
     * 定义script的正则表达式
     */
    private static final String REGEX_SCRIPT = "<script[^>]*?>[\\s\\S]*?<\\/script>";
    /**
     * 定义style的正则表达式
     */
    private static final String REGEX_STYLE = "<style[^>]*?>[\\s\\S]*?<\\/style>";
    /**
     * 定义HTML标签的正则表达式
     */
    private static final String REGEX_HTML = "<[^>]+>";
    /**
     * 定义空格回车换行符
     */
    private static final String REGEX_SPACE = "\\s*|\t|\r|\n";

    public static String delHTMLTag(String htmlStr) {
        // 过滤script标签
        Pattern p_script = Pattern.compile(REGEX_SCRIPT, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        // 过滤style标签
        Pattern p_style = Pattern.compile(REGEX_STYLE, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        // 过滤html标签
        Pattern p_html = Pattern.compile(REGEX_HTML, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        // 过滤空格回车标签
        Pattern p_space = Pattern.compile(REGEX_SPACE, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll("");
        return htmlStr.trim(); // 返回文本字符串
    }
}
