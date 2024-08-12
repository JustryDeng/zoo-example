package com.ideaaedi.zoo.example.springboot.auditlog.openapi.controller;

import cn.hutool.core.util.RandomUtil;
import com.ideaaedi.zoo.commonbase.entity.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.annotation.Resource;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 如果你引入了zoo-diy-artifact-apidoc-knife4j依赖，在类上使用了@ApiTag注解，那么也支持对其内容的解析的
 */
@RestController
@RequestMapping("/demo/")
//@ApiTag(name = "示例", description = "蚂蚁牙，黑~") // 支持@ApiTag
//@Tag(name = "示例", description = "蚂蚁牙，黑~") // 支持@Tag
@Tags(value = @Tag(name = "示例", description = "蚂蚁牙，黑~")) // 支持@Tags
public class DemoController {
    
    @Resource
    private ApplicationContext applicationContext;
    
    @Operation(summary = "测试")
    @RequestMapping("test")
    public Result<String> test(String name) {
        return Result.success(RandomUtil.randomString(20));
    }
    
    @Operation(summary = "重入测试")
    @RequestMapping("re-in")
    public Result<String> reIn(String name) {
        return Result.success(applicationContext.getBean(DemoController.class).test(name));
    }
}
