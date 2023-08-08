package com.hello.springboot.config;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Order
@Configuration
@EnableConfigurationProperties(MyNacosConfigurationResolver.EmployeeProperties.class)
public class MyNacosConfigurationResolver implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        EmployeeProperties employeeProperties = applicationContext.getBean(EmployeeProperties.class);
        log.info("容器刷新，当前获取到nacos的配置是:{}", employeeProperties);
    }

    @RefreshScope
    @Data
    @ConfigurationProperties(prefix = "employee")
    public static class EmployeeProperties {
        private String name;
        private String age;
    }

    @Order
    @Component
    @RequiredArgsConstructor
    public static class NacosConfigurationRefreshListener implements ApplicationListener<RefreshScopeRefreshedEvent> {

        private final BeanFactory beanFactory;

        @Override
        public void onApplicationEvent(@NonNull RefreshScopeRefreshedEvent refreshScopeRefreshedEvent) {
            EmployeeProperties employeeProperties = beanFactory.getBean(EmployeeProperties.class);
            log.info("nacos发布刷新，获取到的配置对象是:{}", employeeProperties);
        }
    }

}
