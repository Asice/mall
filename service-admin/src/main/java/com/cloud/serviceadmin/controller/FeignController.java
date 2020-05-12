package com.cloud.serviceadmin.controller;

import com.cloud.serviceadmin.service.feign.FeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign")
public class FeignController {


    @Autowired
    private FeignService feignService;

    @GetMapping("/{orderNo}")
    public ResponseEntity<?> getOrderByNo(@PathVariable("orderNo")String orderNo){
        try {
            return feignService.getOrderByNo(orderNo);
        }catch (Exception e){
            //todo: do something
            e.printStackTrace();
            return new ResponseEntity<>("请求失败！", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
