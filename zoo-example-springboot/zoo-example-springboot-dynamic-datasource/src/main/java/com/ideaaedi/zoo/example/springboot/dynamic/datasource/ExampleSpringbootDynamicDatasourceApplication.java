package com.ideaaedi.zoo.example.springboot.dynamic.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-artifact-dynamic-datasource
 */
@SpringBootApplication
public class ExampleSpringbootDynamicDatasourceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootDynamicDatasourceApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.print(
                """
                可通过单元测试进行测试观察
                """
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
}
