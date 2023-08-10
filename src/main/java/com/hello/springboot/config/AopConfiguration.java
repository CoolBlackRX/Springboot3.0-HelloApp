package com.hello.springboot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

/**
 * AOP配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AopConfiguration {
    private final Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    /**
     * 打印 接口 请求日志，打印请求参数和响应
     */
    @Bean
    public AspectJExpressionPointcutAdvisor aspectJExpressionPointcutAdvisor() {
        AspectJExpressionPointcutAdvisor aspectJExpressionPointcutAdvisor = new AspectJExpressionPointcutAdvisor();
        // 配置日志拦截切面表达式
        aspectJExpressionPointcutAdvisor.setExpression("execution(public * com.hello.springboot.controller..*.*(..))");
        ObjectMapper objectMapper = jackson2ObjectMapperBuilder.build();
        aspectJExpressionPointcutAdvisor.setAdvice(new ControllerRequestResponseLogPrinter(objectMapper));
        log.info("aop接口请求初始化完成");
        return aspectJExpressionPointcutAdvisor;
    }

    @RequiredArgsConstructor
    static class ControllerRequestResponseLogPrinter implements MethodInterceptor {

        private final ObjectMapper objectMapper;

        @Nullable
        @Override
        public Object invoke(@NotNull MethodInvocation invocation) throws Throwable {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            Assert.notNull(servletRequestAttributes, "拦截请求为空,请检查切面日志表达式");
            HttpServletRequest request = servletRequestAttributes.getRequest();
            String stopWatchId = UUID.randomUUID().toString();
            Map<String, Object> before = Maps.newHashMap();
            before.put("id", stopWatchId);
            before.put("url", request.getRequestURI());
            before.put("params", Arrays.toString(invocation.getArguments()));
            log.info("请求拦截日志:\n{}", objectMapper.writeValueAsString(before));
            // 方法执行
            StopWatch stopWatch = new StopWatch(stopWatchId);
            stopWatch.start();
            Object proceed = invocation.proceed();
            stopWatch.stop();
            Map<String, Object> after = Maps.newHashMap();
            after.put("id", stopWatch.getId());
            after.put("response", proceed);
            after.put("time", stopWatch.getTotalTimeMillis() + "ms");
            log.info("请求拦截日志:\n{}", objectMapper.writeValueAsString(after));
            return proceed;
        }
    }
}
