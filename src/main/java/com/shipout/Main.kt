package com.shipout

import com.shipout.dao.*
import org.mybatis.spring.annotation.*
import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@SpringBootApplication
@MapperScan("com.shipout.dao")
class Main {

}

fun main(args: Array<String>) {
    val applicationContext = SpringApplication.run(Main::class.java)
    val userMapper = applicationContext.getBean(UserMapper::class.java)
    userMapper.deleteById(1)
}
