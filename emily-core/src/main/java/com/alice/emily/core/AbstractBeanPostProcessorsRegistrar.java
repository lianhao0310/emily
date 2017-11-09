package com.alice.emily.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import javax.annotation.Nonnull;

/**
 * Created by lianhao on 2017/6/10.
 */
public abstract class AbstractBeanPostProcessorsRegistrar
        implements ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware {

    protected ConfigurableListableBeanFactory beanFactory;
    protected Environment environment;

    @Override
    public void setBeanFactory(@Nonnull BeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof ConfigurableListableBeanFactory) {
            this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
        }
    }

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(@Nonnull AnnotationMetadata annotationMetadata,
                                        @Nonnull BeanDefinitionRegistry registry) {
        if (this.beanFactory == null) {
            return;
        }

        doRegisterBeanDefinitions(annotationMetadata, registry);
    }

    protected abstract void doRegisterBeanDefinitions(AnnotationMetadata annotationMetadata,
                                                      BeanDefinitionRegistry registry);
}
