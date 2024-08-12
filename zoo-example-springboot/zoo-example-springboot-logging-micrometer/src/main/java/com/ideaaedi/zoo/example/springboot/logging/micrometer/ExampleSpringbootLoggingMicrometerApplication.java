package com.ideaaedi.zoo.example.springboot.logging.micrometer;

import jakarta.annotation.Resource;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-artifact-logging-micrometer
 */
@SpringBootApplication
public class ExampleSpringbootLoggingMicrometerApplication implements ApplicationRunner {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootLoggingMicrometerApplication.class, args);
    }
    
    @Resource
    ApplicationContext applicationContext;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        System.err.println(applicationContext.getBean(SdkTracerProviderBuilder.class));
    }
    
}
