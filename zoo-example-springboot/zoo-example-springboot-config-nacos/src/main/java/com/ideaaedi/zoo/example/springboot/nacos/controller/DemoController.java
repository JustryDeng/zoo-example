package com.ideaaedi.zoo.example.springboot.nacos.controller;


import ch.qos.logback.classic.Logger;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RefreshScope
@RestController
public class DemoController {
    
    @Value("${user-info.age:18}")
    private Integer age;
    
    @Resource
    private UserProperties userProperties;
    
    @GetMapping("/demo")
    public String demo() {
        ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
        Logger logger = (ch.qos.logback.classic.Logger)iLoggerFactory.getLogger(DemoController.class.getName());
        log.info(" info 日志");
        log.debug("debug 日志");
        return "当前日志级别：" + logger.getLevel() + ", 姓名：" + userProperties.getName() + ", 年龄：" + age;
    }
    
    
    @Data
    @Component
    @RefreshScope
    @ConfigurationProperties(prefix = "user-info")
    public static class UserProperties {
        
        private String name = "张三";
    }
}
