package com.ideaaedi.zoo.example.springboot.grayscale.aspect.controller;

import com.ideaaedi.zoo.diy.artifact.grayscale.aspect.annotation.Grayscale;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test3Controller implements EnvironmentAware {
    
    private static Environment environment;
    
    @GetMapping("/test3")
    @Grayscale(gray = "grayAbc", condition = "conditionXyz")
    public String test(@RequestParam(required = false) String param) {
        return "原方法 " + environment.getProperty("spring.application.name");
    }
    
    public static String grayAbc(String param) {
        return "灰度方法 " + environment.getProperty("spring.application.name");
    }
    
    public static boolean conditionXyz(String param) {
        return param != null && param.contains("gray");
    }
    
    @Override
    public void setEnvironment(Environment environment) {
        Test3Controller.environment = environment;
    }
}