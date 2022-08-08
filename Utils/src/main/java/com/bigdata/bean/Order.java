package com.bigdata.bean;

import com.bigdata.util.DataGenUtil;
import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class Order implements BaseBean {

    final static List<String> currencies = new ArrayList<>(Arrays.asList("Dollar", "Euro", "Yuan"));

    final static List<String> itemNames = new ArrayList<>(Arrays.asList("苹果", "报纸", "牛奶", "橘子", "豆腐"));

    public Integer orderId;

    public String item;

    public String currency;

    public Double amount;

    public Date orderTime;


    public static Order genBean() {
        Order order = new Order();
        order.setOrderId(DataGenUtil.getRandomNumber(1, 100));
        order.setItem(currencies.get(DataGenUtil.getRandomNumber(0, currencies.size() - 1)));
        order.setCurrency(itemNames.get(DataGenUtil.getRandomNumber(0, itemNames.size() - 1)));
        order.setAmount(DataGenUtil.fakerWithCN.number().randomDouble(100, 1, 100));
        order.setOrderTime(new Date());
        return order;
    }



}
