package com.shipout


import groovy.sql.*
import org.testcontainers.containers.*
import org.testcontainers.spock.*
import org.testcontainers.utility.*
import spock.lang.*

@Testcontainers
@Requires({ env.test_env == 'docker' })
class MysqlTestContainerTest extends Specification {

    @Shared
    JdbcDatabaseContainer dbContainer = new MySQLContainer(DockerImageName
            .parse("zuisong-docker.pkg.coding.net/mirrors/docker/mysql/mysql-server")
            .asCompatibleSubstituteFor("mysql"))
            .withDatabaseName("foo")
            .withUsername("foo")
            .withPassword("secret")
    Sql sql = Sql.newInstance(dbContainer.jdbcUrl, dbContainer.username, dbContainer.password)


    def "waits until databases accepts jdbc 1"() {
        when:
        def res = sql.firstRow(" select now() as now ")

        then: "result is returned"
        println(res)
    }

    def "waits until databases accepts jdbc"() {
        when:
        def res = sql.firstRow(" select now() as now ")

        then: "result is returned"
        println(res)
    }

}
