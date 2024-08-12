package com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson2.JSON;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.diy.feature.reqresp.encdec.api.properties.ZooReqrespEncdecProperties;
import com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc.entity.QwerRespVO;
import com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc.entity.XyzReqVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

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
    private HttpServletResponse response;
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.RespEncryptProperties#getIncludeHosts()} 配置
     */
    @PostMapping("/a/x1")
    public Result<QwerRespVO> x1() {
        return Result.success(
                QwerRespVO.builder()
                        .qwerName(randomName())
                        .qwerAge(randomAge())
                        .build()
        );
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.RespEncryptProperties#getIncludePaths()} 配置
     */
    @PostMapping("/a/x2")
    public Result<QwerRespVO> x2() {
        return Result.success(
                QwerRespVO.builder()
                        .qwerName(randomName())
                        .qwerAge(randomAge())
                        .build()
        );
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.RespEncryptProperties#getIncludeExistRespHeaders()} 配置
     */
    @PostMapping("/a/x3")
    public Result<QwerRespVO> x3() {
        response.addHeader("needEncryptResp", "zhi_wu_suo_wei");
        return Result.success(
                QwerRespVO.builder()
                        .qwerName(randomName())
                        .qwerAge(randomAge())
                        .build()
        );
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.RespEncryptProperties#getExcludeHosts()} 配置
     */
    @PostMapping("/a/x4")
    public Result<QwerRespVO> x4() {
        return Result.success(
                QwerRespVO.builder()
                        .qwerName(randomName())
                        .qwerAge(randomAge())
                        .build()
        );
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.RespEncryptProperties#getExcludePaths()} 配置
     */
    @PostMapping("/a/x5")
    public Result<QwerRespVO> x5() {
        return Result.success(
                QwerRespVO.builder()
                        .qwerName(randomName())
                        .qwerAge(randomAge())
                        .build()
        );
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.RespEncryptProperties#getExcludeExistRespHeaders()} 配置
     */
    @PostMapping("/a/x6")
    public Result<QwerRespVO> x6() {
        response.addHeader("notNeedEncryptResp", "zhi_wu_suo_wei");
        return Result.success(
                QwerRespVO.builder()
                        .qwerName(randomName())
                        .qwerAge(randomAge())
                        .build()
        );
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.ReqDecryptProperties#getIncludeHosts()} 配置
     */
    @PostMapping("/b/y1")
    public Result<String> y1(@RequestBody XyzReqVO req) {
        return Result.success("您的请求体数据是：" + JSON.toJSONString(req));
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.ReqDecryptProperties#getIncludePaths()} 配置
     */
    @PostMapping("/b/y2")
    public Result<String> y2(@RequestBody XyzReqVO req) {
        return Result.success("您的请求体数据是：" + JSON.toJSONString(req));
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.ReqDecryptProperties#getIncludeExistReqHeaders()} 配置
     */
    @PostMapping("/b/y3")
    public Result<String> y3(@RequestBody XyzReqVO req) {
        return Result.success("您的请求体数据是：" + JSON.toJSONString(req));
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.ReqDecryptProperties#getExcludeHosts()} 配置
     */
    @PostMapping("/b/y4")
    public Result<String> y4(@RequestBody XyzReqVO req) {
        return Result.success("您的请求体数据是：" + JSON.toJSONString(req));
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.ReqDecryptProperties#getExcludePaths()} 配置
     */
    @PostMapping("/b/y5")
    public Result<String> y5(@RequestBody XyzReqVO req) {
        return Result.success("您的请求体数据是：" + JSON.toJSONString(req));
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.ReqDecryptProperties#getExcludeExistReqHeaders()} 配置
     */
    @PostMapping("/b/y6")
    public Result<String> y6(@RequestBody XyzReqVO req) {
        return Result.success("您的请求体数据是：" + JSON.toJSONString(req));
    }
    
    public static String randomName() {
        return RandomUtil.randomChinese() + (ThreadLocalRandom.current().nextBoolean() ? "女士" : "先生");
    }
    
    public static int randomAge() {
        return 10 + ThreadLocalRandom.current().nextInt(81);
    }
}
