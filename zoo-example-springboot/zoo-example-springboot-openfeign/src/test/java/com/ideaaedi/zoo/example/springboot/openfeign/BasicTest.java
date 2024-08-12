package com.ideaaedi.zoo.example.springboot.openfeign;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.ideaaedi.zoo.diy.artifact.openfeign.annotation.EnableZooFeignClients;
import com.ideaaedi.zoo.example.springboot.openfeign.controller.DemoController;
import com.ideaaedi.zoo.example.springboot.openfeign.feignclient.Demo1FeignRpc;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

/**
 * 基础测试
 */
@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootOpenfeignApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@EnableZooFeignClients(basePackages = "com.ideaaedi.zoo.example.springboot.openfeign.feignclient")
public class BasicTest {
    
    @Resource
    private Demo1FeignRpc demo1FeignRpc;
    
    @Test
    public void test1() {
        String result = demo1FeignRpc.welcome();
        System.err.println(result);
    }
    
    @Test
    public void test2() {
        Map<Integer, String> result = demo1FeignRpc.addData(1, RandomUtil.randomString(10));
        System.err.println(result);
    }
    
    @Test
    public void test3() {
        DemoController.DataRequest request = new DemoController.DataRequest();
        request.setId(9527);
        request.setData("周星驰");
        System.err.println(demo1FeignRpc.addDataJson(request));
    }
    
    @Test
    public void test4() {
        System.err.println(demo1FeignRpc.deleteData(1));
    }
    
    @Test
    public void test5() {
        System.err.println(demo1FeignRpc.getData(9527));
    }
    
    @Test
    public void test6() {
        System.err.println(demo1FeignRpc.updateData(9527, "12345，上山打老虎"));
    }
    
    @Test
    public void test7() {
        /*
         * 因为默认配置是：
         *  zoo:
         *    openfeign:
         *      append-headers:
         *        # 往请求头中追加请求头 request.setHeader("traceXd", MDC.get("traceXd"))
         *        - header: traceXd # 追加的请求头
         *          value: traceXd # 请求头的值或值在source中的键
         *          source: MDC # 请求头的值
         *          #encode-value: false # 是否对最终放入请求头中的值进行url-encode
         *          #default-value:  # 默认值
         *
         * 所以默认会传MDC域中的traceXd值到请求头中
         */
        MDC.put("traceXd", UUID.fastUUID().toString());
        System.err.println(demo1FeignRpc.getHeaders());
    }
    
}