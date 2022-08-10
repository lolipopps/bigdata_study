package com.bigdata.util;

import com.bigdata.bean.BaseBean;
import com.bigdata.bean.Order;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: hyt
 */
@Slf4j
public class BeanGenUtil {


    void InsertIntoMysql(Class cls, int batchSize, int timeSleep) throws ClassNotFoundException, SQLException {
        String tableName = cls.getSimpleName();

        Field[] fields = cls.getFields();

        StringBuilder sql = new StringBuilder("INSERT INTO " + tableName.toLowerCase() + " (");

        for (Field field : fields) {
            sql.append(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName()) + ",");
        }
        sql.delete(sql.length() - 1, sql.length());

        sql.append(") values ");


        Method genDataMethod = null;
        Method strDataMethod = null;
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase("genBean")) {
                genDataMethod = method;
            }
            if (method.getName().equalsIgnoreCase("getString")) {
                strDataMethod = method;
            }
        }
        Class c = Class.forName(cls.getName());
        for (int i = 0; i < batchSize; i++) {
            try {
                Object data = genDataMethod.invoke(c);
                Object str = strDataMethod.invoke(data);
                sql.append(str.toString() + ",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(";");
        System.out.println(sql);
        MysqlUtil.executeUpdate(sql.toString());



    }

    public static void main(String[] aegs) throws ClassNotFoundException, SQLException {
        BeanGenUtil beanGenUtil = new BeanGenUtil();
        beanGenUtil.InsertIntoMysql(Order.class, 10, 20);
    }

}
