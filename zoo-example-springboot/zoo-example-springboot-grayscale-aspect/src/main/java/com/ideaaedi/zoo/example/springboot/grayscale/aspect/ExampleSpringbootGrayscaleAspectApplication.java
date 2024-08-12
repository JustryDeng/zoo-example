package com.ideaaedi.zoo.example.springboot.grayscale.aspect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-artifact-grayscale-aspect
 */
@SpringBootApplication
public class ExampleSpringbootGrayscaleAspectApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootGrayscaleAspectApplication.class, args);
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
