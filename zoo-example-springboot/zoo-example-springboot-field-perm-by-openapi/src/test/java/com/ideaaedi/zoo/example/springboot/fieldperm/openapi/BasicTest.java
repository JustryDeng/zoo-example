package com.ideaaedi.zoo.example.springboot.fieldperm.openapi;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.ideaaedi.zoo.commonbase.entity.BaseCodeMsgEnum;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.commonbase.util.HttpUtil;
import com.ideaaedi.zoo.example.springboot.fieldperm.openapi.entity.XyzReqVO;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(classes = ExampleSpringbootFieldPermApplication.class)
public class BasicTest {
    
    String host = "http://localhost:8080";
    
    /*
     * 测试用户 1 访问 /qwer1
     *
     * 提示：运行单元测试前，请先将项目运行起来
     */
    @Test
    @SneakyThrows
    void testUser1() {
        String userId = "1";
        Map<String, String> headers = new HashMap<>();
        headers.put("CURR_USER_ID", userId);
        
        String resp = HttpUtil.postJson(host + "/qwer1", XyzReqVO.builder().name("张三").path("a,b,c,d,e,f,").build(),
                headers);
        System.err.println("访问qwer1时传有name，返回：" + resp);
        Result<String> result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.FIELD_PERMISSION_ERROR.code(), result.getCode());
        
        TimeUnit.MILLISECONDS.sleep(200);
        
        resp = HttpUtil.postJson(host + "/qwer1", XyzReqVO.builder().path("a,b,c,d,e,f,").build(), headers);
        System.err.println("访问qwer1时没传name，返回：" + resp);
        result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.SUCCESS.code(), result.getCode());
    }
    
    /*
     * 测试用户 2 访问 /qwer1
     *
     * 提示：运行单元测试前，请先将项目运行起来
     */
    @Test
    @SneakyThrows
    void testUser2() {
        String userId = "2";
        Map<String, String> headers = new HashMap<>();
        headers.put("CURR_USER_ID", userId);
        
        String resp = HttpUtil.postJson(host + "/qwer1", XyzReqVO.builder().name("张三").path("a,b,c,d,e,f,").build(),
                headers);
        System.err.println("访问qwer1时传有path，返回：" + resp);
        Result<String> result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.FIELD_PERMISSION_ERROR.code(), result.getCode());
        
        TimeUnit.MILLISECONDS.sleep(200);
        
        resp = HttpUtil.postJson(host + "/qwer1", XyzReqVO.builder().name("张三").build(), headers);
        System.err.println("访问qwer1时没传path，返回：" + resp);
        result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.SUCCESS.code(), result.getCode());
    }
    
    /*
     * 测试用户 3 访问 /qwer2
     *
     * 提示：运行单元测试前，请先将项目运行起来
     */
    @Test
    @SneakyThrows
    void testUser3() {
        String userId = "3";
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("CURR_USER_ID", Lists.newArrayList(userId));
        
        String resp = HttpUtil.get(host + "/qwer2?deptId=123", null, String.class, headers);
        System.err.println("访问qwer2时传有deptId，返回：" + resp);
        Result<String> result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.FIELD_PERMISSION_ERROR.code(), result.getCode());
        
        TimeUnit.MILLISECONDS.sleep(200);
    
        resp = HttpUtil.get(host + "/qwer2", null, String.class, headers);
        System.err.println("访问qwer2时没传deptId，返回：" + resp);
        result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.SUCCESS.code(), result.getCode());
    }
    
    /*
     * 测试用户 4 访问 /qwer3
     *
     * 提示：运行单元测试前，请先将项目运行起来
     */
    @Test
    @SneakyThrows
    void testUser4() {
        String userId = "4";
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("CURR_USER_ID", Lists.newArrayList(userId));
        
        String resp = HttpUtil.get(host + "/qwer3?deptName=jd", null, String.class, headers);
        System.err.println("访问qwer3时传有deptName，返回：" + resp);
        Result<String> result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.FIELD_PERMISSION_ERROR.code(), result.getCode());
        
        TimeUnit.MILLISECONDS.sleep(200);
    
        resp = HttpUtil.get(host + "/qwer3", null, String.class, headers);
        System.err.println("访问qwer3时没传deptName，返回：" + resp);
        result = JSON.parseObject(resp, new TypeReference<Result<String>>() {
        });
        Assertions.assertEquals(BaseCodeMsgEnum.SUCCESS.code(), result.getCode());
    }
    
}