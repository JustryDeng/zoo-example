package com.ideaaedi.zoo.example.springboot.auth.satoken.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.example.springboot.auth.satoken.config.DemoPermissionProvider;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 登录测试
 */
@RestController
@RequestMapping("/login/")
public class LoginInfoController {
    
    /**
     * <a href="http://localhost:8080/demo/doLogin?name=zhang&pwd=123456">测试登录</a>
     */
    @RequestMapping("doLogin")
    public Result<String> doLogin(String name) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        Map.Entry<Long, String> userInfo = DemoPermissionProvider.userIdAndNameMap.entrySet().stream().filter(x -> x.getValue().equals(name)).findFirst().orElse(null);
        if (userInfo == null) {
            return Result.failure("登录失败");
        }
        /// ZooJwtTokenPayloadAppender.append("tenant", "123sdf"); 往jwt token中记录一些附加信息
        StpUtil.login(userInfo.getKey());
        return Result.success(StpUtil.getTokenValue());
    }
    
    /**
     * <a href="http://localhost:8080/demo/isLogin">是否登录</a>
     */
    @RequestMapping("isLogin")
    public Result<String> isLogin() {
        return Result.success("是否登录：" + StpUtil.isLogin());
    }
    
    /**
     *  <a href="http://localhost:8080/demo/tokenInfo">查询 Token 信息</a>
     */
    @RequestMapping("tokenInfo")
    public Result<SaTokenInfo> tokenInfo() {
        return Result.success(StpUtil.getTokenInfo());
    }
    
    /**
     * <a href="http://localhost:8080/demo/logout">测试注销</a>
     */
    @RequestMapping("logout")
    public SaResult logout() {
        StpUtil.logout();
        return SaResult.ok();
    }
    
}
