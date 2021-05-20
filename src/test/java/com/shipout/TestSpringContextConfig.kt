package com.shipout

import com.baomidou.mybatisplus.annotation.*
import com.baomidou.mybatisplus.autoconfigure.*
import com.baomidou.mybatisplus.extension.plugins.*
import com.baomidou.mybatisplus.extension.plugins.inner.*
import com.zaxxer.hikari.*
import groovy.sql.*
import org.mybatis.spring.annotation.*
import org.springframework.beans.factory.annotation.*
import org.springframework.boot.autoconfigure.*
import org.springframework.boot.autoconfigure.condition.*
import org.springframework.boot.autoconfigure.gson.*
import org.springframework.boot.autoconfigure.jackson.*
import org.springframework.boot.autoconfigure.jdbc.*
import org.springframework.boot.autoconfigure.jooq.*
import org.springframework.boot.autoconfigure.transaction.*
import org.springframework.boot.context.properties.*
import org.springframework.boot.test.context.*
import org.springframework.context.annotation.*
import org.springframework.core.io.*
import org.springframework.jdbc.datasource.init.*
import org.testcontainers.containers.*
import org.testcontainers.utility.*
import javax.sql.*

/**
 * 单元测试专属 spring 容器配置
 * 专为 mybatis 启用
 */
@TestConfiguration
@EnableConfigurationProperties(DataSourceProperties::class)
@MapperScan(basePackages = ["com.shipout"])
class TestSpringContextConfig {
    @ImportAutoConfiguration(classes = [MybatisPlusAutoConfiguration::class, TransactionAutoConfiguration::class, JacksonAutoConfiguration::class, GsonAutoConfiguration::class, JooqAutoConfiguration::class])
    @TestConfiguration
    internal class AutoConfig

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置
     * MybatisConfiguration#useDeprecatedExecutor = false
     * 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    fun mybatisPlusInterceptor(): MybatisPlusInterceptor {
        val interceptor = MybatisPlusInterceptor()
        interceptor.addInnerInterceptor(PaginationInnerInterceptor(DbType.H2))
        return interceptor
    }

    @Bean
    fun sql(dataSource: DataSource): Sql {
        return Sql(dataSource)
    }

    @Bean
    @ConditionalOnExpression("#{'docker'.equals(environment.getProperty('test_env'))}")
    fun dockerMysqlDataSource(): DataSource {
        val dbContainer: JdbcDatabaseContainer<*> =
            MySQLContainer<MySQLContainer<*>>(
                DockerImageName
                    .parse("mysql/mysql-server")
                    .asCompatibleSubstituteFor("mysql")
            )
                .withDatabaseName("foo")
                .withUsername("foo")
                .withPassword("secret")
        dbContainer.start()
        val config = HikariConfig()
        config.jdbcUrl = dbContainer.jdbcUrl
        config.username = dbContainer.username
        config.password = dbContainer.password
        config.driverClassName = dbContainer.driverClassName
        return HikariDataSource(config)
    }

    // 数据源初始化
    @Bean
    @ConditionalOnExpression("#{!'docker'.equals(environment.getProperty('test_env'))}")
    fun h2DataSource(): DataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:h2:mem:c11n;MODE=mysql;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
        config.driverClassName = "org.h2.Driver"
        config.username = "sa"
        config.password = ""
        return HikariDataSource(config)
    }

    @Bean
    fun dataSourceInitializer(
        dataSource: DataSource,
        @Value("classpath:db-schema.sql") schemaScript: Resource?
    ): DataSourceInitializer {
        val initializer = DataSourceInitializer()
        initializer.setDataSource(dataSource)
        initializer.setDatabasePopulator(ResourceDatabasePopulator(schemaScript))
        return initializer
    }
}
