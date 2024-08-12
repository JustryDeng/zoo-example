package com.ideaaedi.zoo.example.springboot.auth.satoken.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.example.springboot.auth.satoken.config.DemoPermissionProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录测试
 */
@RestController
@RequestMapping("")
public class NeedPermissionController {
    

    @RequestMapping("admin")
    public Result<String> admin() {
        return Result.success(
                    String.format("admin页面： 你好%s！",
                    DemoPermissionProvider.userIdAndNameMap.get(Long.parseLong(StpUtil.getLoginId().toString()))
                )
        );
    }
    
    @RequestMapping("dba")
    public Result<String> dba() {
        return Result.success(
                    String.format("dba页面： 你好%s！",
                    DemoPermissionProvider.userIdAndNameMap.get(Long.parseLong(StpUtil.getLoginId().toString()))
                )
        );
    }
    
    @RequestMapping("developer")
    public Result<String> developer() {
        return Result.success(
                    String.format("developer页面： 你好%s！",
                    DemoPermissionProvider.userIdAndNameMap.get(Long.parseLong(StpUtil.getLoginId().toString()))
                )
        );
    }
    
}
