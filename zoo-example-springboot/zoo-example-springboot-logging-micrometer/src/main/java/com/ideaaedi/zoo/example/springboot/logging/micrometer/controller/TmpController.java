package com.ideaaedi.zoo.example.springboot.logging.micrometer.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.ideaaedi.commonds.time.DateTimeConverter;
import com.ideaaedi.zoo.commonbase.entity.Result;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping
public class TmpController {
    
    @SneakyThrows
    @RequestMapping("/test")
    public Result<String> doLogin() {
        System.err.println("MDC域中，所有的值有：" + JSON.toJSONString(MDC.getCopyOfContextMap()));
        for (int i = 0; i < 3; i++) {
            log.info(DateTimeConverter.REGULAR_DATE_TIME.now() + ": " + RandomUtil.randomString(20));
            TimeUnit.SECONDS.sleep(1);
        }
        return Result.success();
    }
    
}
