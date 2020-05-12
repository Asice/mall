package com.cloud.serviceadmin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 链路监控测试
 */
@RestController
@RequestMapping("/sleuth")
public class SleuthZipkinController {

    private Logger logger= LoggerFactory.getLogger(SleuthZipkinController.class);

    @Autowired
    RestTemplate restTemplate;


    @GetMapping("/zipkin")
    public String creatOrder() {
        String url = "http://127.0.0.1:18941/cart/add/C55668";
        String result = restTemplate.getForObject(url, String.class);
        System.out.println("添加购物车 :" + result);
        logger.info("添加购物车 :" + result);
        return result;
    }

}
