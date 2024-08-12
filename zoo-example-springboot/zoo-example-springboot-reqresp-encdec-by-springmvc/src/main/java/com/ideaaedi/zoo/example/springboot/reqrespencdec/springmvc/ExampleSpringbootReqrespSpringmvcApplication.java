package com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-feature-reqresp-encdec-by-aspect
 */
@SpringBootApplication
public class ExampleSpringbootReqrespSpringmvcApplication {
    
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(ExampleSpringbootReqrespSpringmvcApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.print(
                """
                启动项目后，请使用单元测试进行测试，并观察
                """
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
}
