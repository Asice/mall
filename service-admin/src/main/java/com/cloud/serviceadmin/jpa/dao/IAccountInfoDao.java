package com.cloud.serviceadmin.jpa.dao;

import com.cloud.serviceadmin.jpa.dao.support.IBaseDao;
import com.cloud.serviceadmin.entity.AccountInfo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IAccountInfoDao extends IBaseDao<AccountInfo, Long> {

    @Modifying
    @Query("update AccountInfo a set a.accountBalance=a.accountBalance+?2 where  a.accountNo=?1")
    int updateAccountBalanceByAccountNo(String accountNo,double accountBalance);


}
