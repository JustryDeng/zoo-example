package com.ideaaedi.zoo.example.springboot.openfeign.feignclient;

import com.ideaaedi.zoo.diy.artifact.openfeign.annotation.OnceFeignClient;
import com.ideaaedi.zoo.example.springboot.openfeign.config.SimpleFeignClientConfig;
import com.ideaaedi.zoo.example.springboot.openfeign.controller.DemoController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试通过占位符解析url
 */
@RequestMapping("/demo")
@OnceFeignClient(configuration = SimpleFeignClientConfig.class, url = "${dev-url}")
public interface Demo4FeignRpc {
    
    /**
     * 发送http请求，调用{@link DemoController#welcome()}
     */
    @GetMapping("/welcome")
    String welcome();
    
}
