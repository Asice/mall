package com.cloud.serviceorder.dao;

import com.cloud.serviceorder.dao.support.IBaseDao;
import com.cloud.serviceorder.entity.AccountInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountInfoDao extends IBaseDao<AccountInfo, Long> {

    @Modifying
    @Query("update AccountInfo a set a.accountBalance=a.accountBalance+?2 where  a.accountNo=?1")
    int updateAccountBalanceByAccountNo(String accountNo, double accountBalance);


}
