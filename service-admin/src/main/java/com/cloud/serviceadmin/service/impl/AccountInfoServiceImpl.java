package com.cloud.serviceadmin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cloud.serviceadmin.entity.AccountInfo;
import com.cloud.serviceadmin.jpa.dao.IAccountInfoDao;
import com.cloud.serviceadmin.jpa.dao.support.IBaseDao;
import com.cloud.serviceadmin.service.AccountInfoService;
import com.cloud.serviceadmin.service.support.impl.BaseServiceImpl;
import com.cloud.serviceadmin.vo.AccountChangeEvent;
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
public class AccountInfoServiceImpl extends BaseServiceImpl<AccountInfo,Long> implements AccountInfoService {

    private Logger logger= LoggerFactory.getLogger(AccountInfoServiceImpl.class);

    @Autowired
    private IAccountInfoDao iAccountInfoDao;

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Autowired
    private RedisTemplate<String, Object> redisTemplate;



    @Override
    public IBaseDao<AccountInfo, Long> getBaseDao() {
        return iAccountInfoDao;
    }


    @Override
    public void sendUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        //构建消息体
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountChange",accountChangeEvent);
        Message<String> message = MessageBuilder.withPayload(jsonObject.toJSONString()).build();
        TransactionSendResult sendResult = rocketMQTemplate.sendMessageInTransaction("producer_group_txmsg_bank1", "topic_txmsg", message, null);

        logger.info("send transcation message body={},result={}",message.getPayload(),sendResult.getSendStatus());
    }

    @Transactional
    @Override
    public void doUpdateAccountBalance(AccountChangeEvent accountChangeEvent) {
        logger.info("开始更新本地事务，事务号：{}",accountChangeEvent.getTxNo());
        iAccountInfoDao.updateAccountBalanceByAccountNo(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount()*-1);

        //为幂等作准备
        redisTemplate.opsForValue().set(accountChangeEvent.getTxNo(),accountChangeEvent.getAmount());

        if(accountChangeEvent.getAmount() == 2){
            throw new RuntimeException("bank1更新本地事务时抛出异常");
        }
        logger.info("结束更新本地事务，事务号：{}",accountChangeEvent.getTxNo());
    }
}
