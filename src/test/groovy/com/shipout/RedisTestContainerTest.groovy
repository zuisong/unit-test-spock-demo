package com.shipout


import org.springframework.data.redis.connection.*
import org.springframework.data.redis.connection.lettuce.*
import org.springframework.data.redis.core.*
import org.testcontainers.containers.*
import org.testcontainers.spock.*
import spock.lang.*

@Testcontainers
@Requires({ env.test_env == 'docker' })
class RedisTestContainerTest extends Specification {


    static int redis_port = 6379

    def setup() {

        def container = new GenericContainer("redis:6-alpine")
                .withExposedPorts(redis_port)

        container.start()

        RedisConnectionFactory connectionFactory = new LettuceConnectionFactory(container.host, container.getMappedPort(redis_port))
        redisTemplate = new StringRedisTemplate(connectionFactory)
    }
    StringRedisTemplate redisTemplate

    def "redis 连接测试"() {
        given: "连接redis"

        when:
        def l = redisTemplate.opsForValue().increment("chenjian")
        then:
        l == 1
        noExceptionThrown()

    }
}
