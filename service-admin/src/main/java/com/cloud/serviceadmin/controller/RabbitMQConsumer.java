package com.cloud.serviceadmin.controller;

import com.cloud.serviceadmin.service.feign.FeignService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Component
public class RabbitMQConsumer {


    @RabbitListener(queues = "order")
    public void process(long orderNo){
        System.out.println("Receiver:" + orderNo);
    }

}
