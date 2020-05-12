package com.cloud.serviceadmin.controller;

import com.cloud.serviceadmin.service.SeataTransactionalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/seata")
public class SeataTransactionalController {

    private Logger logger= LoggerFactory.getLogger(SeataTransactionalController.class);

    @Autowired
    SeataTransactionalService seataTransactionalService;


    @GetMapping(value = "/transfer")
    public String transfer(@RequestParam("accountNo")String accountNo, @RequestParam("amount") double amount){
        seataTransactionalService.transferAmount(accountNo,amount);
        logger.info("转账开始" );
        return "transfer success";
    }

}
