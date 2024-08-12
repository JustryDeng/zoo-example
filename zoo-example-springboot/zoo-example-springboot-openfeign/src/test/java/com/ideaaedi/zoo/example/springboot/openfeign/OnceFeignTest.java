package com.ideaaedi.zoo.example.springboot.openfeign;

import com.ideaaedi.zoo.diy.artifact.openfeign.annotation.EnableZooFeignClients;
import com.ideaaedi.zoo.diy.artifact.openfeign.holder.OnceFeignProvider;
import com.ideaaedi.zoo.example.springboot.openfeign.feignclient.Demo2FeignRpc;
import com.ideaaedi.zoo.example.springboot.openfeign.feignclient.Demo3FeignRpc;
import com.ideaaedi.zoo.example.springboot.openfeign.feignclient.Demo4FeignRpc;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * {@link OnceFeignProvider}使用测试
 */
@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootOpenfeignApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@EnableZooFeignClients(basePackages = "com.ideaaedi.zoo.example.springboot.openfeign.feignclient")
public class OnceFeignTest {
    
    @Resource
    private Demo2FeignRpc demo2FeignRpc;
    
    @Resource
    private Demo3FeignRpc demo3FeignRpc;
    
    @Resource
    private Demo4FeignRpc demo4FeignRpc;
    
    /**
     * 不指定url的话，会报异常
     */
    @Test
    public void test1() {
        /*
         * 如果指使用@OnceFeignClient注解的话，那么需要指定url：
         * 方式一：直接在使用@OnceFeignClient时指定url，如：@OnceFeignClient(url = "http://127.0.0.1")
         * 方式二：使用OnceFeignProvider指定url, 见test2示例
         */
        Exception exp = null;
        try {
            demo2FeignRpc.welcome();
        } catch (Exception e) {
            exp = e;
        }
        Assertions.assertTrue(exp instanceof  IllegalArgumentException);
        exp.printStackTrace();
    }
    
    /**
     * 可以通过OnceFeignProvider指定本次feign请求的相关信息
     */
    @Test
    public void test2() {
        String result = OnceFeignProvider.exec(() -> demo2FeignRpc.welcome(), "http://127.0.0.1:8080");
        System.err.println(result);
        Assertions.assertTrue(StringUtils.isNotBlank(result));
    }
    
    /**
     * 可以在使用OnceFeignClient注解时，就指定url
     */
    @Test
    public void test3() {
        String result = demo3FeignRpc.welcome();
        System.err.println(result);
        Assertions.assertTrue(StringUtils.isNotBlank(result));
    }
    
    /**
     * 可以通过占位符解析url
     */
    @Test
    public void test4() {
        String result = demo4FeignRpc.welcome();
        System.err.println(result);
        Assertions.assertTrue(StringUtils.isNotBlank(result));
    }
}