Redis哨兵（Sentinel）模式（主从模式）
1.使用docker下载redis镜像，默认下载最redis最新版本，目前版本号为5.0.0，
如果需要其他版本请登录https://hub.docker.com/进行搜索
    docker pull redis
2.下载完毕后分别创建/usr/local/docker/redis、redis-6379-data、redis-6380-data、redis-6381-data、
sentinel-26379-data、sentinel-26380-data、sentinel-26381-data这7个文件夹

3./usr/local/docker/redis 目前下获取redis配置文件
   wget http://download.redis.io/redis-stable/redis.conf
  
4.复制3份命名为redis-6379.conf、redis-6380.conf、redis-6381.conf
     1.1）修改redis-6379.conf配置文件
        port 6379
        bind 127.0.0.1 #注释掉这部分，这是限制redis只能本地访问
        protected-mode no #默认yes，开启保护模式，限制为本地访问
        dir ./
        appendonly yes #redis持久化（可选）
        appendfilename appendonly.aof
        logfile "redis-6379.log"
        requirepass oopss1256 #外部密码
        masterauth oopss1256 #主从验证密码
     1.2）启动
        docker run -p 6379:6379 --name redis-6379 --restart=always -v /usr/local/docker/redis/redis-6379.conf:/etc/redis/redis-6379.conf -v /usr/local/docker/redis/redis-6379-data:/data -d redis redis-server /etc/redis/redis-6379.conf
     2.1）修改redis-6380.conf配置文件
            port 6380
            #bind 127.0.0.1 #注释掉这部分，这是限制redis只能本地访问
            protected-mode no #默认yes，开启保护模式，限制为本地访问
            dir ./
            appendonly yes #redis持久化（可选）
            appendfilename appendonly.aof
            logfile "redis-6380.log"
            slaveof 52.231.207.203 6379
            requirepass oopss1256 #外部密码
            masterauth oopss1256 #主从验证密码
     2.2）启动
            docker run -p 6380:6380 --name redis-6380 --restart=always -v /usr/local/docker/redis/redis-6380.conf:/etc/redis/redis-6380.conf -v /usr/local/docker/redis/redis-6380-data:/data -d redis redis-server /etc/redis/redis-6380.conf
     3.1）修改redis-6381.conf配置文件
                 port 6381
                 bind 127.0.0.1 #注释掉这部分，这是限制redis只能本地访问
                 protected-mode no #默认yes，开启保护模式，限制为本地访问
                 dir ./
                 appendonly yes #redis持久化（可选）
                 appendfilename appendonly.aof
                 logfile "redis-6381.log"
                 slaveof 52.231.207.203 6379
                 requirepass oopss1256 #外部密码
                 masterauth oopss1256 #主从验证密码
     3.2）启动
            docker run -p 6381:6381 --name redis-6381 --restart=always -v /usr/local/docker/redis/redis-6381.conf:/etc/redis/redis-6381.conf -v /usr/local/docker/redis/redis-6381-data:/data -d redis redis-server /etc/redis/redis-6381.conf

5.启动redis容器后，分别观察三个redis容器内部情况
[root@localhost redis]# docker exec -it redis-6379 /bin/bash
root@zhangqian527halbin:/data# redis-cli
127.0.0.1:6379> auth 123456
ok
127.0.0.1:6379> info replication

如下：主从配置成功
# Replication
role:master
connected_slaves:2
slave0:ip=52.231.207.203,port=6380,state=online,offset=98,lag=1
slave1:ip=52.231.207.203,port=6381,state=online,offset=98,lag=0
master_replid:ac0980a8c7eeed8b312fc7ac77ab91b3dc315a66
master_replid2:0000000000000000000000000000000000000000
master_repl_offset:98
master_repl_meaningful_offset:0
second_repl_offset:-1
repl_backlog_active:1
repl_backlog_size:1048576
repl_backlog_first_byte_offset:1
repl_backlog_histlen:98

6.配置哨兵模式
1.sentinel-26379.conf
    port 26379
    dir "/data"
    logfile "sentinel-26379.log"
    sentinel monitor mymaster 52.231.207.203 6379 2     #哨兵监控的主服务器名称为mymaster，ip为52.231.207.203，端口为6379，将这个主服务器标记为失效至少需要2个哨兵进程的同意
    sentinel down-after-milliseconds mymaster 10000     #指定哨兵在监控Redis服务时，当Redis服务在一个默认毫秒数内都无法回答时，单个哨兵认为的主观下线时间，默认为30000（30秒）
    sentinel failover-timeout mymaster 60000    #指定可以有多少个Redis服务同步新的主机，一般而言，这个数字越小同步时间越长，而越大，则对网络资源要求越高
    sentinel auth-pass mymaster oopss1256       # 哨兵的认证密码
2.sentinel-26380.conf
    port 26380
    dir "/data"
    logfile "sentinel-26380.log"
    sentinel monitor mymaster 52.231.207.203 6379 2
    sentinel down-after-milliseconds mymaster 10000
    sentinel failover-timeout mymaster 60000
    sentinel auth-pass mymaster oopss1256   
3.sentinel-26381.conf
    port 26381
    dir /data
    logfile "sentinel-26381.log"
    sentinel monitor mymaster 52.231.207.203 6379 2
    sentinel down-after-milliseconds mymaster 10000
    sentinel failover-timeout mymaster 60000
    sentinel auth-pass mymaster oopss1256
 启动命令
 docker run -p 26379:26379 --restart=always --name sentinel-26379 -v /usr/local/docker/redis/sentinel-26379.conf:/etc/redis/sentinel.conf -v /usr/local/docker/redis/sentinel-26379-data:/data -d redis redis-sentinel /etc/redis/sentinel.conf
 docker run -p 26380:26380 --restart=always --name sentinel-26380 -v /usr/local/docker/redis/sentinel-26380.conf:/etc/redis/sentinel.conf -v /usr/local/docker/redis/sentinel-26380-data:/data -d redis redis-sentinel /etc/redis/sentinel.conf
 docker run -p 26381:26381 --restart=always --name sentinel-26381 -v /usr/local/docker/redis/sentinel-26381.conf:/etc/redis/sentinel.conf -v /usr/local/docker/redis/sentinel-26381-data:/data -d redis redis-sentinel /etc/redis/sentinel.conf
 
 7.查看哨兵信息
     [root@localhost redis]# docker exec -it sentinel-26379 /bin/bash
     
     root@zhangqian527halbin:/data# redis-cli -p 26379
     
     127.0.0.1:26379>  info sentinel
     
     显示如下
     # Sentinel
     sentinel_masters:1
     sentinel_tilt:0
     sentinel_running_scripts:0
     sentinel_scripts_queue_length:0
     sentinel_simulate_failure_flags:0
     master0:name=mymaster,status=ok,address=52.231.207.203:6380,slaves=2,sentinels=3

结论
当把主redis停掉后，其中一台从redis就被推举为主redis。停掉的redis重启正常后作为从redis加入进去