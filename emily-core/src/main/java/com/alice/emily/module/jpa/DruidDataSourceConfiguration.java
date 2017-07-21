package com.alice.emily.module.jpa;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DatabaseDriver;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableConfigurationProperties({DataSourceProperties.class})
@ConditionalOnClass(DruidDataSource.class)
public class DruidDataSourceConfiguration {

    @Bean
    @ConditionalOnProperty(name = "spring.datasource.type",
            havingValue = "com.alibaba.druid.pool.DruidDataSource",
            matchIfMissing = true)
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DruidDataSource dataSource(DataSourceProperties properties) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.determineDriverClassName());
        dataSource.setUrl(properties.determineUrl());
        dataSource.setUsername(properties.determineUsername());
        dataSource.setPassword(properties.determinePassword());
        DatabaseDriver databaseDriver = DatabaseDriver.fromJdbcUrl(properties.determineUrl());
        String validationQuery = databaseDriver.getValidationQuery();
        if (validationQuery != null) {
            dataSource.setTestOnBorrow(true);
            dataSource.setValidationQuery(validationQuery);
        }
        return dataSource;
    }

    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnProperty(name = "emily.druid.stat.enable", havingValue = "true")
    @ConditionalOnBean(DruidDataSource.class)
    @EnableConfigurationProperties(DruidProperties.class)
    class DruidStatViewConfiguration {

        @Bean
        public ServletRegistrationBean druidStatViewServlet(DruidProperties properties) {
            ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), properties.getMappings());
            bean.addInitParameter(StatViewServlet.PARAM_NAME_ALLOW, properties.getAllow());
            bean.addInitParameter(StatViewServlet.PARAM_NAME_DENY, properties.getDeny());
            bean.addInitParameter(StatViewServlet.PARAM_NAME_USERNAME, properties.getUsername());
            bean.addInitParameter(StatViewServlet.PARAM_NAME_PASSWORD, properties.getPassword());
            bean.addInitParameter(StatViewServlet.PARAM_NAME_RESET_ENABLE, String.valueOf(properties.isResetEnable()));
            return bean;
        }

        @Bean
        public FilterRegistrationBean druidStatFilter(DruidProperties properties) {
            FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
            filterRegistrationBean.addUrlPatterns("/*");
            List<String> exclusions = Lists.newArrayList(properties.getMappings());
            Collections.addAll(exclusions, "*.js", "*.gif", "*.jpg", "*.png", "*.css", "*.ico");
            filterRegistrationBean.addInitParameter(WebStatFilter.PARAM_NAME_EXCLUSIONS, Joiner.on(",").join(exclusions));
            return filterRegistrationBean;
        }
    }
}