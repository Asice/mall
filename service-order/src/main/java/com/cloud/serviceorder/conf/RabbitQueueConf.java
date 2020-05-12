package com.cloud.serviceorder.conf;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//相当于初始化一个queue
@Configuration
public class RabbitQueueConf {
    @Bean
    public Queue queue(){
        return new Queue("order");
    }
}
