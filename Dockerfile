# 开始构建
FROM maven:3-openjdk-8-slim As builder
USER root
RUN mkdir -p /mydata
COPY . /mydata
WORKDIR /mydata
RUN mvn clean test package -DfinalName=app -q

# 构建完毕
FROM azul/zulu-openjdk:8
USER root
RUN mkdir -p /data
VOLUME /data
WORKDIR /data
COPY --from=builder /mydata/target/app.jar /home/app.jar
EXPOSE 8080
CMD ["java","-jar","/home/app.jar"]
