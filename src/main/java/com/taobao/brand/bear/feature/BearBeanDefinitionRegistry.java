package com.taobao.brand.bear.feature;

import com.taobao.brand.bear.service.AoguilinService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * @author jinshuan.li 25/03/2018 08:34
 */
@Component
public class BearBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(
        BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        System.out.println("fff");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
        throws BeansException {

        System.out.println(configurableListableBeanFactory);
        DefaultListableBeanFactory defaultListableBeanFactory
            = (DefaultListableBeanFactory)configurableListableBeanFactory;

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(
            AoguilinService.class);

        defaultListableBeanFactory.registerBeanDefinition("aoguiLinService", beanDefinitionBuilder.getBeanDefinition());
    }
}
