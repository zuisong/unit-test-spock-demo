package com.shipout.service

import com.fasterxml.jackson.databind.*
import com.shipout.dao.*
import com.shipout.entity.*
import org.springframework.data.redis.core.*
import spock.lang.*

@Subject(UserService)
class UserserviceTest extends Specification {

    StringRedisTemplate stringRedisTemplate = Mock()

    UserMapper userMapper = Mock()

    UserService userservice = new UserService(
            userMapper: userMapper,
            stringRedisTemplate: stringRedisTemplate,
            objectMapper: new ObjectMapper()
    )

    def "测试查询用户"() {

        when:
        userservice.findUserById(1)

        then:
        stringRedisTemplate.opsForValue() >> Mock(ValueOperations) {
            1 * get(1) >> null
            1 * set('1', _, 10, _) >> null
        }

        1 * userMapper.selectById(1) >> new User(id: 1, name: "zhangsan")
        true

    }
}
