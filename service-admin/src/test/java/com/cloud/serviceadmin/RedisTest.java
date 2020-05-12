package com.cloud.serviceadmin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    public void setTest() {
        redisTemplate.opsForValue().set("one","中文");

        redisTemplate.opsForHash().put("hash","key","value");

        new Thread(()->{
            ExecutorService executorService = Executors.newFixedThreadPool(1000);
            IntStream.range(0, 1000).forEach(i -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executorService.execute(() -> redisTemplate.opsForValue().increment("num", 1));
            });
        }).start();

        System.out.println("end");
    }
}
