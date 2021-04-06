package com.shipout.main;

import com.shipout.dao.UserMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@MapperScan("com.shipout.dao")
public class Main {

    public static void main(String[] args) throws Exception {


        ConfigurableApplicationContext applicationContext = SpringApplication.run(Main.class);

        UserMapper userMapper = applicationContext.getBean(UserMapper.class);

        userMapper.deleteById(1);

    }

}
