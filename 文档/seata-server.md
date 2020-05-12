1.docker安装
    docker run --name seata-server \
            -p 8091:8091 \
            -e SEATA_IP=服务器外网Ip \
            -e SEATA_PORT=8091 \
            seataio/seata-server
2.进入容器：docker exec -it seata-server /bin/bash
3.安装vim，这个容器里面没有vi和vim
    apt-get update
    apt-get install vim
4.修改【/seata-server/resources】下的file.conf和registry.conf
    file.conf
        mode="db"
        修改mysql的信息
        最后面加上配置service：
                service {
                  vgroup_mapping.fsp_tx_group = "default"
                  default.grouplist = "服务器ip:8091"
                  enableDegrade = false
                  disable = false
                  max.commit.retry.timeout = "-1"
                  max.rollback.retry.timeout = "-1"
                }
    registry.conf
        type="eureka"，然后修改eureka地址为服务中心地址
5.重启容器；docker restart seata-server
6.在eureka配置中心能看到【DEFAULT】的注册信息，并且id为外网地址，则【seata-server】配置成功


参考连接
http://seata.io/en-us/docs/ops/deploy-by-docker.html
https://github.com/seata/seata-samples/tree/master/springcloud-eureka-feign-mybatis-seata
