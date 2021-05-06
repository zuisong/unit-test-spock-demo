package com.shipout;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

/**
 * 单元测试专属 spring 容器配置
 * 专为 mybatis 启用
 */
@TestConfiguration
@EnableConfigurationProperties({
        DataSourceProperties.class
})
@MapperScan(basePackages = "com.shipout")
public class TestSpringContextConfig {

    @ImportAutoConfiguration(classes = {
            MybatisPlusAutoConfiguration.class,
            TransactionAutoConfiguration.class,
            JacksonAutoConfiguration.class,
            GsonAutoConfiguration.class,
            JooqAutoConfiguration.class,
    })
    @TestConfiguration
    static class AutoConfig {

    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));
        return interceptor;
    }

    @Bean
    DataSource dataSource() {

        JdbcDatabaseContainer<?> dbContainer = new MySQLContainer<>(DockerImageName
                .parse("mysql/mysql-server")
                .asCompatibleSubstituteFor("mysql"))
                .withDatabaseName("foo")
                .withUsername("foo")
                .withPassword("secret");

        dbContainer.start();

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbContainer.getJdbcUrl());
        config.setUsername(dbContainer.getUsername());
        config.setPassword(dbContainer.getPassword());
        config.setDriverClassName(dbContainer.getDriverClassName());
        return new HikariDataSource(config);

    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(
            DataSource dataSource,
            @Value("classpath:schema-h2.sql") Resource schemaScript
    ) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(schemaScript));
        return initializer;
    }

}
