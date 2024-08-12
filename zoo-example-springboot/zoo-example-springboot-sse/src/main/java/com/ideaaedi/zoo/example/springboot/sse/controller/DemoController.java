package com.ideaaedi.zoo.example.springboot.sse.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.ideaaedi.commonds.time.DateTimeConverter;
import com.ideaaedi.zoo.diy.artifact.sse.core.SsePusher;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * demo
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@RestController
public class DemoController {
    
    @Resource
    SsePusher ssePusher;
    
    @CrossOrigin
    @RequestMapping("/demo")
    public SseEmitter demo(@RequestHeader(value = "token", required = false) String token) {
        // 输出请求头
        System.err.println("token = " + token);
        
        
        String uuid = IdUtil.getSnowflakeNextIdStr();
        SseEmitter emitter = ssePusher.connect(uuid);
        
        // 模拟给客户端发送消息
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            ssePusher.sendMessage(uuid, DateTimeConverter.REGULAR_DATE_TIME.now() + "@" + RandomUtil.randomString(10));
        }, 0, 10, TimeUnit.SECONDS);
        return emitter;
    }
}
