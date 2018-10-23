package com.pbids.sanqin.utils;

import java.util.regex.Pattern;

/**
 * Created by pbids903 on 2017/11/20.
 */
/**
 * 账户相关属性验证工具
 *
 */

/**
 * @author 巫哲豪
 * @date on 2018/3/2 16:40
 * @desscribe 类描述:正则检查工具
 * @remark 备注:
 * @see
 */
public class ValidatorUtil {

        /**
         * 正则表达式：验证用户名
         */
        public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,20}$";

        /**
         * 正则表达式：验证密码
         */
        public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,20}$";

        /**
         * 正则表达式：验证手机号
         */
        public static final String REGEX_MOBILE = "^((17[0-9])|(14[0-9])|(18[0-9])|(19[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

        /**
         * 正则表达式：验证邮箱
         */
        public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

        /**
         * 正则表达式：验证汉字
         */
        public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]*$";
        /**
         * 正则表达式：验证汉字姓名
         */
        public static final String REGEX_CHINESE_NAME = "^[\u4e00-\u9fa5]{2,6}$";

        /**
         * 正则表达式：验证身份证
         */
        public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$)";


        public static final String REGEX_BANK_ID_CARD = "^([1-9]{1})(\\d{15,19})$";

        /**
         * 正则表达式：验证URL
         */
//        public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";
//        public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
         * 正则表达式：验证IP地址
         */
        public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

        /**
         * 校验用户名
         *
         * @param username
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isUsername(String username) {
            return Pattern.matches(REGEX_USERNAME, username);
        }

        /**
         * 校验密码
         *
         * @param password
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isPassword(String password) {
            return Pattern.matches(REGEX_PASSWORD, password);
        }

        /**
         * 校验手机号
         *
         * @param mobile
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isMobile(String mobile) {
            return Pattern.matches(REGEX_MOBILE, mobile);
        }

        /**
         * 校验邮箱
         *
         * @param email
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isEmail(String email) {
            return Pattern.matches(REGEX_EMAIL, email);
        }

        /**
         * 校验汉字
         *
         * @param chinese
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isChinese(String chinese) {
            return Pattern.matches(REGEX_CHINESE, chinese);
        }

        /**
         * 校验汉字姓名
         *
         * @param chinese
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isChineseName(String name) {
            return Pattern.matches(REGEX_CHINESE_NAME, name);
        }

        /**
         * 校验身份证
         *
         * @param idCard
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isIDCard(String idCard) {
            return Pattern.matches(REGEX_ID_CARD, idCard);
        }
        /**
         * 校验身份证
         *
         * @param idCard
         * @return 校验通过返回true，否则返回false
         */
        public static boolean isBankIDCard(String idCard) {
            return Pattern.matches(REGEX_BANK_ID_CARD, idCard);
        }

        /**
         * 校验URL
         *
         * @param url
         * @return 校验通过返回true，否则返回false
         */
//        public static boolean isUrl(String url) {
//            return Pattern.matches(REGEX_URL, url);
//        }

        /**
         * 校验IP地址
         *
         * @param ipAddr
         * @return
         */
        public static boolean isIPAddr(String ipAddr) {
            return Pattern.matches(REGEX_IP_ADDR, ipAddr);
        }

    /**
     * 校验数字
     * @param str
     * @return
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
       }
}
