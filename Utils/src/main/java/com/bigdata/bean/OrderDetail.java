package com.bigdata.bean;

import com.bigdata.util.DataGenUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
public class OrderDetail extends BaseBean {

    final static List<String> statusNames = new ArrayList<>(Arrays.asList("创建", "运输", "下单", "完成", "取消"));

    Integer orderId;
    String status;
    Integer id;
    Date creeateTime;

    public static OrderDetail getData() {
        OrderDetail order = new OrderDetail();
        order.setOrderId(DataGenUtil.getRandomNumber(1, 100));
        order.setStatus(statusNames.get(DataGenUtil.getRandomNumber(0, statusNames.size() - 1)));
        order.setCreeateTime(new Date());
        return order;
    }
}
