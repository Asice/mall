package com.cloud.serviceadmin.service;

import com.cloud.serviceadmin.entity.AccountInfo;
import com.cloud.serviceadmin.service.support.IBaseService;
import com.cloud.serviceadmin.vo.AccountChangeEvent;

public interface AccountInfoService extends IBaseService<AccountInfo,Long> {

    void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

    void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent);

}
