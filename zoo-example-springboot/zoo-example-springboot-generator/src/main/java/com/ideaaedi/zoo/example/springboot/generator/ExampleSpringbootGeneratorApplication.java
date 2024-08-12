package com.ideaaedi.zoo.example.springboot.generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-artifact-generator
 */
@SpringBootApplication
public class ExampleSpringbootGeneratorApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootGeneratorApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.print(
                """
                        启动项目后，请使用单元测试进行测试观察
                        
                        推荐在test scope使用zoo-diy-artifact-generator组件
                        """
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
}
