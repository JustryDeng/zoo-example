package com.com.ideaaedi.zoo.example.springboot.grayscale.aspect;

import cn.hutool.http.HttpUtil;
import com.ideaaedi.zoo.diy.artifact.grayscale.aspect.annotation.Grayscale;
import com.ideaaedi.zoo.example.springboot.grayscale.aspect.ExampleSpringbootGrayscaleAspectApplication;
import com.ideaaedi.zoo.example.springboot.grayscale.aspect.controller.Test2Controller;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootGrayscaleAspectApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
public class BasicTest {
    
    @Resource
    Test2Controller test2Controller;
    
    /**
     * 基础测试
     */
    @Test
    public void test1() {
        String result = HttpUtil.get("http://localhost:8080/test1", StandardCharsets.UTF_8);
        System.err.println(result);
        Assertions.assertEquals("原方法", result);
        
        result = HttpUtil.get("http://localhost:8080/test1?param=gray", StandardCharsets.UTF_8);
        System.err.println(result);
        Assertions.assertEquals("灰度方法", result);
    }
    
    /**
     * 测试：如果{@link Grayscale#gray()}、{@link Grayscale#condition()}方法上有相关aop的话，aop不会生效
     * <p>
     * 效果：执行此单元测试，控制台没有【我是grayAbc的切面】输出
     */
    @Test
    @SneakyThrows
    public void test2() {
        // 通过gray的方式调用的话，不会走AOP， 控制台没有【我是grayAbc的切面】输出
        String result = HttpUtil.get("http://localhost:8080/test2?param=gray", StandardCharsets.UTF_8);
        Assertions.assertEquals("灰度方法 demo", result);
        System.err.println("可以看到：通过gray的方式调用的话，不会走AOP， 控制台没有【我是grayAbc的切面】输出!");
        System.err.println();
        System.err.println();
        System.err.println("------------------");
        System.err.println();
        System.err.println();
        TimeUnit.MILLISECONDS.sleep(100);
        // 直接调用的话，会走AOP， 控制台有【我是grayAbc的切面】输出
        System.err.println(test2Controller.grayAbc("asdf"));
        System.err.println("可以看到：直接调用的话，会走AOP， 控制台有【我是grayAbc的切面】输出!");
    }
    
    /**
     * 测试：{@link Grayscale#gray()}和{@link Grayscale#condition()}可以是静态方法
     */
    @Test
    public void test3() {
        String result = HttpUtil.get("http://localhost:8080/test3", StandardCharsets.UTF_8);
        System.err.println(result);
        Assertions.assertEquals("原方法 demo", result);
        
        result = HttpUtil.get("http://localhost:8080/test3?param=gray", StandardCharsets.UTF_8);
        System.err.println(result);
        Assertions.assertEquals("灰度方法 demo", result);
    }
    
    /**
     * 测试：{@link Grayscale#gray()}和{@link Grayscale#condition()}方法的访问级别无限制，甚至可以是private
     */
    @Test
    public void test4() {
        String result = HttpUtil.get("http://localhost:8080/test4", StandardCharsets.UTF_8);
        System.err.println(result);
        Assertions.assertEquals("原方法 demo", result);
        
        result = HttpUtil.get("http://localhost:8080/test4?param=gray", StandardCharsets.UTF_8);
        System.err.println(result);
        Assertions.assertEquals("灰度方法 demo", result);
    }
}