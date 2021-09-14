package com.bigdata.hbase;

import javafx.util.Pair;
import org.apache.hadoop.hbase.CompareOperator;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.util.Arrays;
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
