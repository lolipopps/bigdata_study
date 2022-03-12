package com.bigdata.kafka;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

@Slf4j
public class MyProduce {

    static class MyKafkaProducer implements Runnable {

        KafkaProducer producer;
        String topic;

        MyKafkaProducer(String topic) {
            Properties props = null;
            props = KafkaConfig.buildKafkaProps();
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            this.producer = new KafkaProducer<String, String>(props);
            this.topic = topic;
        }

        @SneakyThrows
        @Override
        public void run() {
            int num = 1;
            while (true) {
                String a = DataGenUtil.getString(10);
                log.info("发送数据: " + a);
                ProducerRecord record = new ProducerRecord<String, String>(topic, null, null, a);
                producer.send(record);
                Thread.sleep(100);
                producer.flush();
                num++;
                if (num == 1000) {
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MyKafkaProducer p = new MyKafkaProducer("test");
        p.run();
    }
}
