package com.ideaaedi.zoo.example.springboot.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-artifact-websocket
 */
@SpringBootApplication
public class ExampleSpringbootWebsocketApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootWebsocketApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.print(
                "启动项目后，可通过浏览器打开 src/main/resources/websocket/websocket-demo.html 进行访问测试"
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
}
