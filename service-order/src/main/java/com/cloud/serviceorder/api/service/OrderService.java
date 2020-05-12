package com.cloud.serviceorder.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Service
public class OrderService {

    private Logger logger= LoggerFactory.getLogger(OrderService.class);

    @Value("${server.port}")
    private String port;

    @Autowired
    private AmqpTemplate rabbitmqTemplate;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public ResponseEntity<?> getOrderByNo(String orderNo) throws InterruptedException {
        if(redisTemplate.hasKey("3588458664400011")){
            //Thread.sleep(3_000);
            logger.info("Feign通过("+port+")调用了该接口,状态码200");
            return  new ResponseEntity<>("找到订单3588458664400011", HttpStatus.OK);
        }else{
            logger.info("Feign通过("+port+")调用了该接口,状态码500");
            return  new ResponseEntity<>("没有该订单", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 生产订单到mq
     * @return
     */
    public ResponseEntity<?> create() {
        new Thread(()->{
            ExecutorService executorService = Executors.newFixedThreadPool(10000);
            IntStream.range(0, 10000).forEach(i -> {
                Date now=new Date();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.execute(() -> rabbitmqTemplate.convertAndSend("order",now.getTime()));
            });
        }).start();
        return  new ResponseEntity<>("开始生产", HttpStatus.OK);
    }
}
