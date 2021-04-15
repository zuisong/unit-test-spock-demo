# 开始构建
FROM maven:3-openjdk-8-slim As builder
MAINTAINER zuisong
USER root
RUN mkdir -p /mydata
COPY . /mydata
WORKDIR /mydata
RUN mvn clean test -q
# 构建完毕
#FROM frolvlad/alpine-java:jre8-slim
#MAINTAINER zuisong
#VOLUME /data
#WORKDIR /data
#COPY --from=builder /mydata/target/app.jar /home/app.jar
#EXPOSE 8080
#CMD ["java","-jar","/home/app.jar"]
