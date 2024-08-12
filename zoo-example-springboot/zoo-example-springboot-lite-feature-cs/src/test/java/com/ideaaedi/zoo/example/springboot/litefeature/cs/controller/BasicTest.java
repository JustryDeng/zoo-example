package com.ideaaedi.zoo.example.springboot.litefeature.cs.controller;

import com.ideaaedi.zoo.example.springboot.litefeature.cs.ExampleSpringbootLiteFeatureCsApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 基础测试
 */
@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootLiteFeatureCsApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class BasicTest {
    
    @Test
    public void test1() {
    }
    
}