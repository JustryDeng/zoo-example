package com.ideaaedi.zoo.example.springboot.i18n.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-feature-i18n
 */
@SpringBootApplication
public class ExampleSpringbootI18nApplication {
    
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleSpringbootI18nApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.print(
                """
                启动项目后，可以这样测试
                方式一：借助接口工具（如postman），发送请求"http://localhost:8080/demo"，并在请求头中指定lang来设定语言
                方式一：借助curl指令
                    英文：   curl -X GET -H "lang:en_US"  "http://localhost:8080/demo"
                    简体中文：curl -X GET -H "lang:zh_CN"  "http://localhost:8080/demo"
                    繁体中文：curl -X GET -H "lang:zh_TW"  "http://localhost:8080/demo"
                """
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
}
