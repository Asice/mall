package com.cloud.serviceadmin.service;

import com.cloud.serviceadmin.entity.AccountInfo;
import com.cloud.serviceadmin.service.support.IBaseService;
import com.cloud.serviceadmin.vo.AccountChangeEvent;

public interface SeataTransactionalService extends IBaseService<AccountInfo,Long> {

    void  transferAmount(String  accountNo,double amount);

}
