#运行此项目还需要基于java镜像
FROM java:8
ADD *.jar app.jar
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8 --spring.profiles.active=peer1","/app.jar"]