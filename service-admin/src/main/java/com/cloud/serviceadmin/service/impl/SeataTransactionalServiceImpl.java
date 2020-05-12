package com.cloud.serviceadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.serviceadmin.entity.AccountInfo;
import com.cloud.serviceadmin.feign.AccountApi;
import com.cloud.serviceadmin.jpa.dao.IAccountInfoDao;
import com.cloud.serviceadmin.jpa.dao.support.IBaseDao;
import com.cloud.serviceadmin.service.AccountInfoService;
import com.cloud.serviceadmin.service.SeataTransactionalService;
import com.cloud.serviceadmin.service.support.impl.BaseServiceImpl;
import com.cloud.serviceadmin.vo.AccountChangeEvent;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
public class SeataTransactionalServiceImpl extends BaseServiceImpl<AccountInfo,Long> implements SeataTransactionalService {

    private Logger logger= LoggerFactory.getLogger(SeataTransactionalServiceImpl.class);

    @Autowired
    private IAccountInfoDao iAccountInfoDao;

    @Resource
    private AccountApi accountApi;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



    @Override
    public IBaseDao<AccountInfo, Long> getBaseDao() {
        return iAccountInfoDao;
    }


    /**
     * 扣钱，然后通知【service-order】服务的李四加钱
     * @param accountNo
     * @param amount
     */
    @Override
    @GlobalTransactional(name = "fsp-create-order",rollbackFor = Exception.class)
    public void transferAmount(String  accountNo,double amount) {
        //本地方法，扣钱
        iAccountInfoDao.updateAccountBalanceByAccountNo(accountNo,amount*-1);
        //远程调用李四【accountNo=2】加钱
        accountApi.increase("2",amount);
        logger.info("调用转账");
    }


}
