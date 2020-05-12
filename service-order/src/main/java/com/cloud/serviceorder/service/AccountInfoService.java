package com.cloud.serviceorder.service;


import com.cloud.serviceorder.entity.AccountInfo;
import com.cloud.serviceorder.service.support.IBaseService;
import com.cloud.serviceorder.vo.AccountChangeEvent;

public interface AccountInfoService extends IBaseService<AccountInfo,Long> {

    void addAccountInfoBalance(AccountChangeEvent accountChangeEvent);


    void increase(String userId, double money);
}
