package com.shipout.dao

import com.fasterxml.jackson.databind.*
import com.shipout.*
import com.shipout.entity.*
import groovy.sql.*
import org.springframework.beans.factory.annotation.*
import org.springframework.test.context.*
import spock.lang.*

import javax.sql.*

@ContextConfiguration(classes = TestSpringContextConfig)
@Subject(UserMapper)
class UserMapperTest extends Specification {


    ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT)

    @Autowired
    DataSource dataSource

    @Autowired
    UserMapper userMapper
    @Shared
    Sql sql

    def setup() {
        sql = new Sql(dataSource)

        println "------- setup"
        sql.execute("""
        truncate table t_user;
    """)
    }


    def setupSpec() {

        // 设置每个测试类的环境
        // 每个测试类初始化时会被执行一遍
        println("-------- setupSpec")
    }


    def cleanup() {
        // 清理每个测试方法的环境，每个测试方法执行一次
        println "------- cleanup"
        // 删除每个单元测试内部的数据 避免数据互相干扰
        sql.execute("""
        truncate table t_user;
        """)
    }

    def cleanupSpec() {
        // 清理每个测试类的环境
        println("-------- cleanupSpec")
    }


    def "插入数据测试"() {


        def user = Stub(User)
        when:
        userMapper.insertUser(user)
        then:
        def rows = sql.rows("select * from t_user")
        rows.size() == 1
        println objectMapper.writeValueAsString(rows)
    }

    def "查询数据测试"() {

        // 这里可以初始 插入一些数据
        setup:
        sql.execute("""
        insert into t_user (name) values ('zhangsan'),('lisi');
        """)

        when:
        def users = userMapper.selectByMap(["name": "zhangsan"])

        then:
        println(users)
        users.size() == 1
        users.get(0).name == 'zhangsan'

    }

    def "批量插入测试"() {
        setup:
        def user1 = new User(name: "zhangsan1")
        def user2 = new User(name: "zhangsan2")
        def user3 = new User(name: "zhangsan3")

        when:
        userMapper.batchSaveUser([user1, user2, user3])


        then:
        def row = sql.firstRow("select count(*) as count from t_user ")
        row.count == 3

    }

}
