package com.shipout.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.shipout.dao.UserMapper
import com.shipout.entity.User
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.core.ValueOperations
import spock.lang.Specification
import spock.lang.Subject

@Subject(Userservice)
class UserserviceTest extends Specification {

    StringRedisTemplate stringRedisTemplate = Mock()

    UserMapper userMapper = Mock()

    Userservice userservice = new Userservice(
            userMapper: userMapper,
            stringRedisTemplate: stringRedisTemplate,
            objectMapper: new ObjectMapper()
    );

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
