package com.shipout

import io.lettuce.core.*
import org.testcontainers.containers.*
import org.testcontainers.spock.*
import spock.lang.*

@Testcontainers
@Requires({ env.test_env == 'docker' })
class RedisTestContainerTest extends Specification {
    RedisClient redisClient

    static int redis_port = 6379

    def setup() {

        def container = new GenericContainer("redis:6-alpine")
                .withExposedPorts(redis_port)

        container.start()

        redisClient = RedisClient.create(RedisURI.create(
                container.host,
                container.getMappedPort(redis_port)
        ))
    }

    def "redis 连接测试"() {
        given: "连接redis"
        def redisConnection = redisClient.connect()
        def redisCommands = redisConnection.sync()
        when:
        def l = redisCommands.incr("chenjian")
        then:
        l == 1
        noExceptionThrown()

    }
}
