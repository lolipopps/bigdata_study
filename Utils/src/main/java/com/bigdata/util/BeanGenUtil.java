package com.bigdata.util;

import com.bigdata.bean.BaseBean;
import com.bigdata.bean.Order;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: hyt
 */
@Slf4j
public class BeanGenUtil {


    void InsertIntoMysql(Class cls, int batchSize, int timeSleep) throws ClassNotFoundException {
        String tableName = cls.getSimpleName();

        Integer fieldSize = cls.getFields().length;
        StringBuilder sql = new StringBuilder("INSERT INTO " +  tableName.toLowerCase() + " VALUES (");
        for (int i = 0;i<fieldSize;i++){
            sql.append("?,");
        }
        sql.delete(sql.length()-1,sql.length());
        sql.append(")");
        System.out.println(sql);
        log.info("插入 " + tableName + "数据");
        Method genDataMethod = null;
        Method getDataMethod = null;
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase("genBean")) {
                genDataMethod = method;
            }
            if (method.getName().equalsIgnoreCase("getString")) {
                genDataMethod = method;
            }
        }
        Class c = Class.forName(cls.getName());
        for (int i = 0; i < batchSize; i++) {
            try {
                Object data = genDataMethod.invoke(c);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] aegs) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        BeanGenUtil beanGenUtil = new BeanGenUtil();
        beanGenUtil.InsertIntoMysql(Order.class, 10, 20);
    }

}
