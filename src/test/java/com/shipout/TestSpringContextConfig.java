package com.shipout;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;

import static org.springframework.core.io.support.ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX;

/**
 * 单元测试专属 spring 容器配置
 * 专为 mybatis 启用
 */
@MapperScan(basePackages = {"com.shipout.dao"})
@EnableTransactionManagement
public class TestSpringContextConfig {

    @Autowired
    private DataSource dataSource;

    // 配置mybatis
    @Bean
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean() throws IOException {

        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();

        String packageXMLConfigPath = CLASSPATH_ALL_URL_PREFIX + "/mapper/**/*.xml";

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        sessionFactoryBean.setMapperLocations(resolver.getResources(packageXMLConfigPath));
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setMapUnderscoreToCamelCase(false);
        sessionFactoryBean.setConfiguration(configuration);
        sessionFactoryBean.setDataSource(dataSource);

        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(paginationInterceptor());
        Interceptor[] plugins = {mybatisPlusInterceptor};
        sessionFactoryBean.setPlugins(plugins);

        return sessionFactoryBean;
    }

    public PaginationInnerInterceptor paginationInterceptor() {
        // 开启 count 的 join 优化,只针对 left join !!!
        PaginationInnerInterceptor interceptor = new PaginationInnerInterceptor();
        return interceptor;
    }

    // 数据源初始化
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:c11n;MODE=mysql;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        config.setDriverClassName("org.h2.Driver");
        config.setUsername("sa");
        config.setPassword("");

        return new HikariDataSource(config);
    }

    @Bean
    DataSourceInitializer dataSourceInitializer(
            @Value("classpath:schema-h2.sql") Resource schemaScript
    ) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(schemaScript));
        return initializer;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }

}
