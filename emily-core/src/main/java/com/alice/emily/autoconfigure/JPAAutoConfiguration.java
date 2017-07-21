package com.alice.emily.autoconfigure;

import com.alice.emily.module.jpa.DruidDataSourceConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;

/**
 * Created by liupin on 2017/3/2.
 */
@Configuration
@ConditionalOnClass(EntityManager.class)
@Import(DruidDataSourceConfiguration.class)
public class JPAAutoConfiguration {

}
