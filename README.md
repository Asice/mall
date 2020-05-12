基于SpringCloud微服务搭建的商城系统

系统架构图
![系统架构图](https://github.com/Asice/mall/blob/master/%E6%96%87%E6%A1%A3/%E7%B3%BB%E7%BB%9F%E6%9E%B6%E6%9E%84.jpg)

重点实现功能
![重点实现功能](https://github.com/Asice/mall/blob/master/%E6%96%87%E6%A1%A3/%E9%87%8D%E7%82%B9%E5%AE%9E%E7%8E%B0.JPG)

几个服务地址

    Eureka----http://52.231.207.203:8802/
    Rabbit----http://52.231.207.203:15672/（账号密码都是guest）
    Zipkin----http://52.231.207.203:9411/zipkin/ （通过官方的docker image运行，不需要自己建zipkin-server）
        RestTemplate调用：http://52.231.207.203:9411/zipkin/traces/e9b16e10f768efe9
        Feign远程调用：http://52.231.207.203:9411/zipkin/traces/90623893627becef
    kafka----http://52.231.207.203:9000/
    kafka集群搭建https://www.jianshu.com/p/e324ceabf494

实现功能

    1.redis哨兵模式的连接【service-admin】LettuceRedisConfig.java
    2.feign远程调用和ribbon负载均衡【service-admin调用service-order（起2个来测试负载均衡）】
    3.RabbitMQ异步通信，
        (1) 【service-order】RabbitQueueConf.java创建一个Queue
        (2) 【service-order】OrderService.create生产订单信息
        (3) 【service-admin】RabbitMQConsumer.process消费
4.sleuth+zipkin链路监控（日志通过RabbitMQ转存，【后期换成kafka，kafka比较专业】）

    (1) 启动zipkin服务器
        docker run --name rabbit-zipkin --restart=always -d -p 9411:9411 --link rabbitmq -e RABBIT_ADDRESSES=rabbitmq:5672 -e RABBIT_USER=guest -e RABBIT_PASSWORD=guest openzipkin/zipkin
    (2) 配置文件和pom.xml的jar
          #zipkin
          zipkin:
            base-url: http://${server.host}:9411/
            enabled: true
            sender:
              type: rabbit
          #接口默认全部采样
          sleuth:
            sampler:
              probability: 1
    (3)结论，不管通过feign远程调用还是RestTemplate调用，都能记录下来
4.日志系统（ELK）

    (1)docker安装
        先在宿主机执行 sysctl -w vm.max_map_count=262144
        然后(elk安装最新版不成功，就指定版本安装) 
            docker run -p 5601:5601 -p 9200:9200 -p 5044:5044 -it --name elk sebp/elk:670
    (2)修改Logstash配置（改为从kafka消费日志）
            进入elk容器：docker exec -it <container-name> /bin/bash
            修改配置文件：vim /etc/logstash/conf.d/02-beats-input.conf
                input {
                    kafka {
                    	bootstrap_servers => ["52.231.207.203:9092"]  #kafka服务器地址
                		topics => "springcloud-logs-elk" #主题
                	}
                }
                output {
                	elasticsearch {
                		hosts => ["localhost:9200"] #elasticsearch地址
                	}
                }
    (3) logback-spring.xml配置KafkaAppender发送日志到kafka
4.分布式事务

    (1)基于RocketMQ实现最终一致性（通过redis key存储【事务号】来实现消息的幂等）
        【service-admin】ProducerTxmsgListener事务的回调和回查
        【service-order】TxmsgConsumer事务消费
            1.请求成功:http://127.0.0.1:8901/rocketmq/transfer?accountNo=1&amount=10
            2.发送端本地抛异常，事务回滚（发送端和消费端都不执行）:http://127.0.0.1:8901/rocketmq/transfer?accountNo=1&amount=2
            3.消费端抛异常（消费端自己会有重试机制），消费端事务回滚，上游发送端不回滚（这里要有补偿机制，如人工介入）:http://127.0.0.1:8901/rocketmq/transfer?accountNo=1&amount=4
    
    (2)基于阿里seata-server的AT分布式事务
            安装seata-server，具体看【文档/seata-server.md】
            (1)正常调用
                 【http://127.0.0.1:8901/seata/transfer?accountNo=1&amount=5】
                    {
                         Begin new global transaction [52.231.207.203:8091:3709393370316800]
                         [52.231.207.203:8091:3709393370316800] commit status: Committed
                         onMessage:xid=52.231.207.203:8091:3709393370316800,branchId=3709489545707521,branchType=AT,resourceId=jdbc:mysql://52.231.207.203:3306/bank1,applicationData=null
                         Branch committing: 52.231.207.203:8091:3709393370316800 3709489545707521 jdbc:mysql://52.231.207.203:3306/bank1 null
                         Branch commit result: PhaseTwo_Committed    
                     }
            (2)被调方回滚， @GlobalTransactional注解上的本地事务也回滚
                【http://127.0.0.1:8901/seata/transfer?accountNo=1&amount=8】
                Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is com.netflix.hystrix.exception.HystrixRuntimeException: AccountApi#increase(String,double) failed and no fallback available.] with root cause

暂未实现
    
    1.（redis，mq，elk日志系统）等等独立出来一个common模块
    2.网关OAuth2.0鉴权和路由
