package com.ideaaedi.zoo.example.springboot.knife4j;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-artifact-apidoc-knife4j
 */
@SpringBootApplication
public class ExampleSpringbootKnife4jApplication implements ApplicationRunner {
    
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(ExampleSpringbootKnife4jApplication.class, args);
        Environment env = application.getEnvironment();
        System.err.println();
        System.err.println();
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignore) {
        }
        if (hostAddress == null) {
            hostAddress = "127.0.0.1";
        }
        System.err.printf(
                "访问：http://%s:%s/doc.html 查看接口文档！",
                hostAddress,
                env.getProperty("server.port", "8080")
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
    
    }
}
