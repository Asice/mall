package com.cloud.serviceorder.controller;

import com.cloud.serviceorder.service.AccountInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountInfoService accountInfoService;

    /**
     * 加钱
     * @param userId 用户id
     * @param money 金额
     * @return
     */
    @RequestMapping("increase")
    public String increase(@RequestParam("accountNo") String userId, @RequestParam("amount") double money){
        accountInfoService.increase(userId,money);
        return "Account decrease success";
    }


}
