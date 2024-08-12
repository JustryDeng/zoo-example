package com.ideaaedi.zoo.example.springboot.sms.sms4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-feature-msg-sms-by-sms4j
 */
@SpringBootApplication
public class ExampleSpringbootMsgSmsSms4jApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootMsgSmsSms4jApplication.class, args);
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
