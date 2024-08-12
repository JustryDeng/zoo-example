package com.ideaaedi.zoo.example.springboot.grayscale.aspect.controller;

import com.ideaaedi.zoo.diy.artifact.grayscale.aspect.annotation.Grayscale;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test1Controller {
    
    @GetMapping("/test1")
    @Grayscale(gray = "grayAbc", condition = "conditionXyz")
    public String test(@RequestParam(required = false) String param) {
        return "原方法";
    }
    
    public String grayAbc(String param) {
        return "灰度方法";
    }
    
    public boolean conditionXyz(String param) {
        return param != null && param.contains("gray");
    }
}