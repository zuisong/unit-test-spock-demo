package com.shipout

import io.lettuce.core.*
import org.testcontainers.containers.*
import org.testcontainers.spock.*
import spock.lang.*

@Testcontainers
class RedisTestContainerTest extends Specification {
    RedisClient redisClient

    def setup() {
        def container = new GenericContainer("redis:6-alpine")
                .withExposedPorts(6379)

        container.start()

        redisClient = RedisClient.create(RedisURI.create(
                container.host,
                container.getMappedPort(6379))
        )
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
