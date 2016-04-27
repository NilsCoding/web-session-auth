package com.github.nilscoding.sessionauth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Just some simple common utility functions
 * @author NilsCoding
 */
public class Utils {
    
    private Utils() { }
    
    /**
     * Checks if the given String is empty
     * @param str   string to check
     * @return  true if null/empty, false if not empty
     */
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        return (str.trim().length() == 0);
    }
    
    /**
     * Returns the given String if it is not empty, or the default String otherwise
     * @param str   string
     * @param defaultStr    default string
     * @return  string if not empty or default string otherwise
     */
    public static String notEmptyOrDefault(String str, String defaultStr) {
        return ((isEmpty(str) == false) ? str : defaultStr);
    }
    
    /**
     * Checks if the given object is logically true or not
     * @param obj   object to check
     * @return  true or false
     */
    public static boolean isTrue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof CharSequence) {
            String tmpStr = ((CharSequence)obj).toString().toLowerCase();
            return (tmpStr.equals("1") || tmpStr.equals("true") || tmpStr.equals("yes") || tmpStr.equals("y") || tmpStr.equals("ja") || tmpStr.equals("j"));
        }
        if (obj instanceof Boolean) {
            return (((Boolean)obj).booleanValue());
        }
        if (obj instanceof Number) {
            return (((Number)obj).intValue() == 1);
        }
        return false;
    }
    
    /**
     * Parses the given String to int
     * @param str   string to parse
     * @param defaultValue  default value to return on error
     * @return  parsed value or default value on error
     */
    public static int parseInt(String str, int defaultValue) {
        if (isEmpty(str) == true) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception ex) {
            return defaultValue;
        }
    }
    
    /**
     * Searches for the given regex and returns the matching (specified) group string
     * @param str   source string
     * @param regex regex to find
     * @param group group, suggested: 1
     * @param defaultValue  default value to return
     * @return  matching group value, or default value
     */
    public static String getRegexStr(String str, String regex, int group, String defaultValue) {
        if (isEmpty(str) == true) {
            return defaultValue;
        }
        if (group < 1) {
            group = 1;
        }
        try {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            if (m.find()) {
                String foundValueStr = m.group(group);
                return foundValueStr;
            }
        } catch (Exception ex) {
        }
        return defaultValue;
    }
    
    /**
     * Searches for the given regex and returns the matching (specified) group as an int value
     * @param str   source string
     * @param regex regex to find
     * @param group group, suggested: 1
     * @param defaultValue  default value to return
     * @return  matching group value as int, or default value
     */
    public static int getRegexInt(String str, String regex, int group, int defaultValue) {
        if (isEmpty(str) == true) {
            return defaultValue;
        }
        if (group < 1) {
            group = 1;
        }
        try {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(str);
            if (m.find()) {
                String foundValueStr = m.group(group);
                return parseInt(foundValueStr, defaultValue);
            }
        } catch (Exception ex) {
        }
        return defaultValue;
    }
    
    /**
     * Creates an instance by name, making sure that it is of the requested type
     * @param <T>   type
     * @param classname class name
     * @param resultType    result type (can be an interface)
     * @return  instance or null
     */
    public static <T> T createByName(String classname, Class<T> resultType) {
        if ((isEmpty(classname) == true) || (resultType == null)) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(classname, true, Thread.currentThread().getContextClassLoader());
            if (resultType.isAssignableFrom(clazz)) {
                return (T)clazz.newInstance();
            }
        } catch (Exception ex) {
        }
        return null;
    }
    
}
