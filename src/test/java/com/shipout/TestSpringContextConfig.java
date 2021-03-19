package com.shipout;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * 单元测试专属 spring 容器配置
 * 专为 mybatis 启用
 */
@ComponentScan(basePackages = {"com.shipout.dao",})
@MapperScan(basePackages = {"com.shipout.dao"})
@EnableTransactionManagement
public class TestSpringContextConfig {


    @Autowired
    private DataSource dataSource;

    // 配置mybatis
    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() throws IOException {

        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();

        String packageXMLConfigPath = CLASSPATH_ALL_URL_PREFIX + "/mapper/*.mapper.xml";

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sessionFactoryBean.setMapperLocations(resolver.getResources(packageXMLConfigPath));
        sessionFactoryBean.setDataSource(dataSource);

        return sessionFactoryBean;

    }

    // 数据源初始化
    @Bean
    public DataSource dataSource() {

        HikariDataSource dataSource = new HikariDataSource();

        dataSource.setJdbcUrl("jdbc:p6spy:h2:mem:c11n;MODE=mysql;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        dataSource.setDriverClassName(com.p6spy.engine.spy.P6SpyDriver.class.getName());
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        return dataSource;
    }


    // 初始化数据库结构和数据
    @Bean
    InitializingBean initDB() {
        return () -> {
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            Resource sqlScriptResource = new ClassPathResource("schema-h2.sql");
            populator.addScript(sqlScriptResource);
            DatabasePopulatorUtils.execute(populator, dataSource);
        };
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }


}
