package com.ideaaedi.zoo.example.springboot.sse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-artifact-sse
 */
@SpringBootApplication
public class ExampleSpringbootSseApplication {
    
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleSpringbootSseApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.printf(
                """
                启动项目后，可通过
                   1. 浏览器访问 http://localhost:%s/demo 直接访问测试
                   2. 浏览器打开 src/main/resources/sse/sse-demo.html 代码链接测试
                   3. 浏览器打开 src/main/resources/sse/sse-demo-with-header.html 代码传递请求头测试
                """
                , applicationContext.getEnvironment().getProperty("server.port", "8080")
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
}
