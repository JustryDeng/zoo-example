package com.ideaaedi.zoo.example.springboot.fieldperm.openapi.controller;

import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.diy.feature.fieldperm.api.annotation.FieldPermission;
import com.ideaaedi.zoo.example.springboot.fieldperm.openapi.entity.XyzReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
    
    
    @FieldPermission
    @PostMapping("/qwer1")
    @Operation(summary = "示例1")
    Result<String> qwer1(@RequestBody XyzReqVO req) {
        return Result.success();
    }
    
    @FieldPermission
    @Operation(summary = "示例2")
    @GetMapping("/qwer2")
    Result<String> qwer2(@Parameter(description = "部门id")
                         @RequestParam(required = false) Integer deptId) {
        return Result.success();
    }
    
    
    @FieldPermission
    @Parameters(
            value = {
                    @Parameter(name = "deptName", description = "部门名称"),
                    @Parameter(name = "deptPath", description = "部门路径")
            }
    )
    @Operation(summary = "示例3")
    @GetMapping("/qwer3")
    Result<String> qwer3(@RequestParam(required = false) String deptName,
                         @RequestParam(required = false) String deptPath) {
        return Result.success();
    }
    
}
