package com.shipout


import groovy.sql.*
import org.testcontainers.containers.*
import org.testcontainers.spock.*
import org.testcontainers.utility.*
import spock.lang.*

@Testcontainers
class MysqlTestContainerTest extends Specification {
    Sql sql

    def setup() {
        JdbcDatabaseContainer dbContainer = new MySQLContainer(DockerImageName
                .parse("mysql/mysql-server")
                .asCompatibleSubstituteFor("mysql"))
                .withDatabaseName("foo")
                .withUsername("foo")
                .withPassword("secret")
        dbContainer.start()
        sql = Sql.newInstance(dbContainer.jdbcUrl, dbContainer.username, dbContainer.password)
    }

    def "waits until databases accepts jdbc connections"() {
        when:
        def res = sql.firstRow("select now()")

        then: "result is returned"
        println(res)
    }

    def "waits until databases accepts jdbc connections2"() {
        when:
        def res = sql.firstRow("select now() as now")

        then: "result is returned"
        println(res)
    }

}
