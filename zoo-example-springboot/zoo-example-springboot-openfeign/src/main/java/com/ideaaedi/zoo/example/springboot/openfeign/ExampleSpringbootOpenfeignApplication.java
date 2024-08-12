package com.ideaaedi.zoo.example.springboot.openfeign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-artifact-openfeign
 */
@SpringBootApplication
public class ExampleSpringbootOpenfeignApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootOpenfeignApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.print(
                """
                启动项目后，可通过单元测试进行测试观察
                """
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
}
