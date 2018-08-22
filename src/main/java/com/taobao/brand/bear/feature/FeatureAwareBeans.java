package com.taobao.brand.bear.feature;

import com.taobao.brand.bear.domain.TQLFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jinshuan.li 2017/12/23 10:02
 */
@Slf4j
@Configuration
public class FeatureAwareBeans {

    public static ApplicationContext appContext;

    @Bean
    public ApplicationContextAware applicationContextAware() {

        return applicationContext -> {

            appContext = applicationContext;
            TQLFacade jinshuan = FeatureAwareBeans.appContext.getBean(TQLFacade.class, "jinshuan");
            jinshuan = FeatureAwareBeans.appContext.getBean(TQLFacade.class, "jinshuan2");
            log.info("applicationContext:{}", applicationContext);
        };
    }

    @Bean
    public ApplicationEventPublisherAware applicationEventPublisherAware() {

        return applicationEventPublisher -> {

        };
    }

    @Bean
    public BeanClassLoaderAware beanClassLoaderAware() {

        return classLoader -> {
            log.info("classLoader.{}", classLoader);
        };
    }

    @Bean
    public BeanFactoryAware beanFactoryAware() {
        return beanFactory -> {

        };
    }

    @Bean
    public BeanPostProcessor beanPostProcessor() {

        /**
         * 该接口会在doGetBean 前后被调用
         */
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

                System.currentTimeMillis();
                return bean;
            }

            @Override
            public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

                return bean;
            }
        };
    }

}
