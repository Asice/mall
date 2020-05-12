1.docker pull rocketmqinc/rocketmq
2.docker run -d -p 9876:9876 -v /usr/local/docker/rocketmq/store:/root/store 
    -v /usr/local/docker/rocketmq/logs:/root/logs --name mqnamesrv -e "MAX_POSSIBLE_HEAP=100000000" rocketmqinc/rocketmq sh mqnamesrv
3.