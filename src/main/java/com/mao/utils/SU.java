package com.mao.utils;

/**
 * 字符串方面：StringUtils简写
 * @author mao by 11:14 2019/6/18
 */
public class SU {

    /**
     * 判断字符串是否为空
     * 仿写Common-lang3
     * @param cs 字符串
     * @return boolean
     */
    public static boolean isEmpty(CharSequence cs){
        return cs == null || cs.length() == 0;
    }

    /**
     * 判断字符串是否不为空
     * 仿写Common-lang3
     * @param cs 字符串
     * @return boolean
     */
    public static boolean isNotEmpty(CharSequence cs){
        return !isEmpty(cs);
    }

}