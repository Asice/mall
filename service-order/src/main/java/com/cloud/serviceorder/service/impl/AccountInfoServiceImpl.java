package com.cloud.serviceorder.service.impl;

import com.cloud.serviceorder.dao.IAccountInfoDao;
import com.cloud.serviceorder.dao.support.IBaseDao;
import com.cloud.serviceorder.entity.AccountInfo;
import com.cloud.serviceorder.service.AccountInfoService;
import com.cloud.serviceorder.service.support.impl.BaseServiceImpl;
import com.cloud.serviceorder.vo.AccountChangeEvent;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

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

    /**
     * mq实现的分布式事务（柔性一致性）
     * @param accountChangeEvent
     */
    @Transactional
    @Override
    public void addAccountInfoBalance(AccountChangeEvent accountChangeEvent) {
        logger.info("bank2更新本地账号，账号：{},金额：{}",accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());

        //幂等校验
        if(redisTemplate.hasKey(accountChangeEvent.getTxNo())){
            if(accountChangeEvent.getAmount() == 4){
                throw new RuntimeException("人为制造异常");
            }
            iAccountInfoDao.updateAccountBalanceByAccountNo(accountChangeEvent.getAccountNo(),accountChangeEvent.getAmount());
            logger.info("更新本地事务执行成功，本次事务号: {}", accountChangeEvent.getTxNo());
        }else{
            logger.info("更新本地事务执行失败，本次事务号: {}", accountChangeEvent.getTxNo());
        }

    }

    /**
     * seata实现的tcc分布式事务
     * @param userId
     * @param money
     */
    @Override
    public void increase(String userId, double money) {
        iAccountInfoDao.updateAccountBalanceByAccountNo(userId,money);
        if(money == 8){
            logger.info("人工回滚，金额_"+money);
            throw new RuntimeException("人工回滚");
        }else{
            logger.info("李四加钱成功+_"+money);
        }
    }

}
