package com.shipout

import org.mybatis.spring.annotation.*
import org.springframework.boot.*
import org.springframework.boot.autoconfigure.*

@SpringBootApplication
@MapperScan("com.shipout.dao")
class Main {

}

fun main() {
    SpringApplication.run(Main::class.java)
}
