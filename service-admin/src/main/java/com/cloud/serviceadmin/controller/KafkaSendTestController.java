package com.cloud.serviceadmin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * 链路监控测试
 */
@RestController
@RequestMapping("/kakfa")
public class KafkaSendTestController {

    private Logger logger= LoggerFactory.getLogger(KafkaSendTestController.class);


    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @GetMapping("/send")
    public String creatOrder() {
        final int count=10_000;
        new Thread(()->{
            ExecutorService executorService = Executors.newFixedThreadPool(count);
            IntStream.range(0, count).forEach(i -> {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                kafkaTemplate.send("springcloud-logs-consumer-test","kafka发送消息"+new Date());
            });
        }).start();


        logger.info("kafka发送消息" +new Date());
        return "Success";
    }

}
