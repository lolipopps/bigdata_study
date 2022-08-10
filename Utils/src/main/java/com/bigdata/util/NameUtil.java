package com.bigdata.util;

import com.google.common.base.CaseFormat;

public class NameUtil {
    public static void main(String[] args) {
        // 变量小写连接线转小写驼峰
        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "user-name"));//userName
        // 变量小写连接线转小写下划线
        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_UNDERSCORE, "user-name"));//user_name
        // 变量小写下划线转小写驼峰
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "user_name"));//userName
        // 变量下划线转大写驼峰
        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "user_name"));//UserName
        // 变量小写驼峰转大写驼峰
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, "userName"));//UserName
        // 变量小写驼峰转小写下划线
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "userName"));//user_name
        // 变量小写驼峰转小写下划线
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "UserName"));//user_name
        // 变量小写驼峰转小写连接线
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "userName"));//user-name
    }
}
