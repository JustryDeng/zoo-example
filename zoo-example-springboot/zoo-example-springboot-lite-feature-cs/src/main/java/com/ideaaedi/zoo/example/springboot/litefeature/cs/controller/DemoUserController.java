package com.ideaaedi.zoo.example.springboot.litefeature.cs.controller;

import com.ideaaedi.commonspring.lite.antidupli.AntiDuplication;
import com.ideaaedi.zoo.commonbase.entity.PageInfo;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.commonbase.zoo_injector.ExtAutowiredInjector;
import com.ideaaedi.zoo.diy.feature.litefeature.api.face.LockFaceUtil;
import com.ideaaedi.zoo.diy.feature.litefeature.api.lock.Lock;
import com.ideaaedi.zoo.example.springboot.litefeature.cs.entity.DemoUserListReqVO;
import com.ideaaedi.zoo.example.springboot.litefeature.cs.entity.DemoUserListRespVO;
import jakarta.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("removal")
@Validated
@RestController
@RequestMapping("/lite-feature")
public class DemoUserController {
    
    @Resource
    RedissonClient redissonClient;
    
    /*
     * 进出日志观察： 访问这些方法时，观察控制台进出口日志输出即可
     */
    
    
    
    
    /**
     * <pre>
     * 测试方式：连续发出好几个请求，观察响应、控制台报错输出
     * </pre>
     */
    @PostMapping("/anti-dupli/demo")
    @AntiDuplication(spel = "zhangsan")
    public Result<PageInfo<DemoUserListRespVO>> antiDupliDemo(@Validated @RequestBody DemoUserListReqVO req) {
        return Result.success();
    }
    
    /**
     * 分布式锁 使用方式一：直接使用门面类{@link ExtAutowiredInjector#inject(Object)}
     * <pre>
     * 测试方式：连续发出好几个请求，观察响应、控制台报错输出
     * </pre>
     */
    @GetMapping("/lock/demo1/{userId}")
    public Result<Object> lockDemo1(@PathVariable("userId") Long userId) {
        ///try {
//            LockFaceUtil.get(userId + "").voidExec(
            LockFaceUtil.get(userId + "", 3).voidExec(
                    () -> {
                        System.err.println(userId + " 开始做逻辑.");
                        try {
                            TimeUnit.SECONDS.sleep(5);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
    
                        System.err.println(userId + " 结束做逻辑.");
                    }
            );
        ///} catch (NotAcquiredLockException e) {
        ///    return Result.failure("异常了" +e.toString());
        ///}
        return Result.success();
    }
    
    /**
     * 分布式锁 使用方式二：使用注解{@link Lock}
     * <pre>
     * 测试方式：连续发出好几个请求，观察响应、控制台报错输出
     * </pre>
     */
    @GetMapping("/lock/demo2/{userId}")
//    @Lock(lockKey = "#{#userId}")
    @Lock(lockKey = "#{#userId}", waitTime = 3)
    public Result<String> lockDemo2(@PathVariable("userId") Long userId) {
        System.err.println(userId + " 开始做逻辑.");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.err.println(userId + " 结束做逻辑.");
        return Result.success();
    }
    
    /**
     * ExtAutowiredInjector 使用方式一：{@link ExtAutowiredInjector#inject(Object)}
     */
    @GetMapping("/ExtAutowiredInjector/demo1")
    public Result<String> extDemo1() {
        Student1 student1 = new Student1();
        ExtAutowiredInjector.inject(student1);
        Assert.notNull(student1.redissonClient);
        return Result.success(student1.redissonClient.getId());
    }
    
    /**
     * ExtAutowiredInjector 使用方式二：直接继承{@link ExtAutowiredInjector}
     */
    @GetMapping("/ExtAutowiredInjector/demo2")
    public Result<String> extDemo2() {
        Student2 student2 = new Student2();
        Assert.notNull(student2.redissonClient);
        return Result.success(student2.redissonClient.getId());
    }
    
    
    
    private static class Student1 {
        
        @Resource
        RedissonClient redissonClient;

    }
    
    
    
    private static class Student2 extends ExtAutowiredInjector {
        
        @Resource
        RedissonClient redissonClient;

    }
}
