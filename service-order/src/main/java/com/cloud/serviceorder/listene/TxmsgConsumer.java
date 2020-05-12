package com.cloud.serviceorder.listene;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cloud.serviceorder.service.AccountInfoService;
import com.cloud.serviceorder.vo.AccountChangeEvent;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "topic_txmsg",consumerGroup = "producer_group_txmsg_bank1")
public class TxmsgConsumer implements RocketMQListener<String> {

    private Logger logger= LoggerFactory.getLogger(TxmsgConsumer.class);

    @Autowired
    private AccountInfoService accountInfoService;

    @Override
    public void onMessage(String s) {

        logger.info("开始消费消息:{}",s);
        //解析消息为对象
        final JSONObject jsonObject = JSON.parseObject(s);
        AccountChangeEvent accountChangeEvent = JSONObject.parseObject(jsonObject.getString("accountChange"),AccountChangeEvent.class);

        //调用service增加账号金额
        accountChangeEvent.setAccountNo("2");
        accountInfoService.addAccountInfoBalance(accountChangeEvent);
    }
}
