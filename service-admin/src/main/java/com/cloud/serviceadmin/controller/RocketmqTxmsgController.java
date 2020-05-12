package com.cloud.serviceadmin.controller;

import com.cloud.serviceadmin.service.AccountInfoService;
import com.cloud.serviceadmin.vo.AccountChangeEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * RocketmqTxmsg 最终一致性事务
 */
@RestController
@RequestMapping("/rocketmq")
public class RocketmqTxmsgController {

    @Autowired
    private AccountInfoService accountInfoService;


    @GetMapping(value = "/transfer")
    public String transfer(@RequestParam("accountNo")String accountNo, @RequestParam("amount") Double amount){
        String tx_no = UUID.randomUUID().toString();
        AccountChangeEvent accountChangeEvent = new AccountChangeEvent(accountNo,amount,tx_no);
        accountInfoService.sendUpdateAccountBalance(accountChangeEvent);
        return "转账成功";
    }

}
