package com.ideaaedi.zoo.example.springboot.i18n.spring.controller;

import com.ideaaedi.zoo.diy.feature.i18n.api.I18nUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * demo
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@RestController
public class DemoController {
    
    
    @CrossOrigin
    @RequestMapping("/demo")
    public R demo() {
        return R.builder()
                .code("000000")
                .msg(I18nUtil.i18n("rs"))
                .data("业务数据")
                .build();
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class R {
        private String code;
        private String msg;
        private Object data;
    }
}
