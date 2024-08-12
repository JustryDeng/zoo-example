package com.ideaaedi.boot;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.ideaaedi.boot.config.DemoPermissionProvider;
import com.ideaaedi.zoo.commonbase.entity.BaseCodeMsgEnum;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.commonbase.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(classes = ExampleSpringbootAuditlogOpenapiApplication.class)
public class TempTest extends BaseTest {
    
    @Test
    void test() {
    }
    
    public static void main(String[] args) {
        visitPages(DemoPermissionProvider.ZHANG_SAN_NAME);
        
        System.out.println();
        System.out.println();
        System.out.println();
        visitPages(DemoPermissionProvider.LI_SI_NAME);
        
        System.out.println();
        System.out.println();
        System.out.println();
        visitPages(DemoPermissionProvider.WANG_WU_NAME);
    }
    
    private static void visitPages(String name) {
        String host = "http://localhost:8080";
        String resp = HttpUtil.postJson(host + "/login/doLogin?name=" + name, null);
        Result<String> result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.SUCCESS.code(), result.getCode(), "登录失败！");
        String token = result.getData();
        resp = HttpUtil.postJson(host + "/user-info/list", null, MapUtil.of("JD-AUTH-TOKEN", "Bearer " + token));
        System.err.println(name + " 访问/user-info/list页，返回：" + resp);
    }
}