package com.cloud.serviceadmin;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.client.producer.SendResult;


public class Producer {

    public static void main(String[] args) throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroupName");

        producer.setNamesrvAddr("52.231.207.203:9876");
        producer.start();

        for (int i = 0; i < 10; i++)
            try {
                {
                    Message msg = new Message("TopicTest",
                            "TagA",
                            "OrderID188",
                            "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
                    SendResult sendResult = producer.send(msg);
                    System.out.printf("%s%n", sendResult);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        //producer.shutdown();
    }
}
