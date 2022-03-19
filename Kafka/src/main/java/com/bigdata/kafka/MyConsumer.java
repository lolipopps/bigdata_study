package com.bigdata.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;

public class MyConsumer {

    public static void readFromKafka(String topic) throws InterruptedException {
        Properties props = null;
        props = KafkaConfig.buildKafkaProps();
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList(topic));
        System.out.println("---------开始消费---------");

        while (true) {
            int messageNo = 1;
            try {
                ConsumerRecords<String, String> msgList = consumer.poll(100000);
                if (null != msgList && msgList.count() > 0) {
                    for (ConsumerRecord<String, String> record : msgList) {
                        //消费100条就打印 ,但打印的数据不一定是这个规律的
                        System.out.println("---------开始消费---------： " + record.value());
                        if (messageNo % 2 == 0) {
                            System.out.println(messageNo + "=======receive: key = " + record.key() + ", value = " + record.value() + " offset===" + record.offset());
                        }
                        //当消费了1000条就退出
                        if (messageNo % 100 == 0) {
                            break;
                        }
                        messageNo++;
                    }
                } else {
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        readFromKafka("order_cnt");
    }
}
