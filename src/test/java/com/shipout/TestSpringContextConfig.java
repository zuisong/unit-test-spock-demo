package com.shipout;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.sql.init.SqlInitializationAutoConfiguration;
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
            SqlInitializationAutoConfiguration.class,
            DataSourceAutoConfiguration.class,
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

    @Autowired
    DataSource dataSource;

}
