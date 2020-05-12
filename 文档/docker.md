1.docker安装
    https://blog.csdn.net/TangXuZ/article/details/100082144
    准备工作
        yum install -y yum-utils device-mapper-persistent-data lvm2
    添加Docker的存储库
        yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
    安装Docker-ce
        yum install -y docker-ce
    启动Docker
        systemctl start docker
    测试运行 hello-world
        docker run hello-world
docker安装mysql
https://blog.csdn.net/weixin_40461281/article/details/92610876
docker制作镜像
https://blog.csdn.net/qq_25591191/article/details/100066656
springboot 加上不然时区不对
ENTRYPOINT ["java", "-jar", "-Duser.timezone=GMT+8","/app.jar"]


linux
docker build -t wechat-app:v1.1 .
docker run -d -p 8888:80 wechat-app:v1.1
查看日志
docker logs --tail=10 0b1d30d13944