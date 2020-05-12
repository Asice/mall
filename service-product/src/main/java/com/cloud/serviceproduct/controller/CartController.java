package com.cloud.serviceproduct.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/cart")
public class CartController {

    private Logger logger= LoggerFactory.getLogger(CartController.class);

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/add/{cartId}")
    public String addToCart(@PathVariable("cartId") String cartId) {
        String url = "http://127.0.0.1:18931/api/order/create";
        String result = restTemplate.getForObject(url, String.class);
        System.out.println("生产订单 :" + result);
        logger.info("生产订单"+result);
        return result;
    }


}
