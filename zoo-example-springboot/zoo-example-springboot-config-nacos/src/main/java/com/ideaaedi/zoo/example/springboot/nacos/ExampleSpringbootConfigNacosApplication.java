package com.ideaaedi.zoo.example.springboot.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-feature-config-by-nacos
 */
@SpringBootApplication
public class ExampleSpringbootConfigNacosApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootConfigNacosApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.print(
                """
                启动项目后，可以这样按照以下步骤测试，并对比观察：
                1. 访问 localhost:8080/demo 查看返回的当前日志级别
                2. 去nacos上修改日志级别、用户名、年龄，如：
                   logging:
                     level:
                       com.ideaaedi.zoo.example.springboot.nacos.controller.DemoController: info
                   user-info:
                     name: 李四
                     age: 24
                   然后再访问 localhost:8080/demo 查看返回的当前日志级别
                3. 再去nacos上修改日志级别，如：
                   logging:
                     level:
                       com.ideaaedi.zoo.example.springboot.nacos.controller.DemoController: debug
                   user-info:
                     name: 王五
                     age: 30
                   然后再访问 localhost:8080/demo 查看返回的当前日志级别
                """
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
}
