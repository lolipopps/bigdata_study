package com.bigdata.hbase;

import org.junit.Test;

import java.util.List;


public class ZookeeperUtilsTest {
    private static final String TABLE_NAME = "class";
    private static final String TEACHER = "teacher";
    private static final String STUDENT = "student";

    @Test
    public void getZnodeData() {
        ZookeeperUtils zk = new ZookeeperUtils();
        List<String> table = zk.getChildren("/");
        System.out.println("路径列表:" + table);
    }


}
