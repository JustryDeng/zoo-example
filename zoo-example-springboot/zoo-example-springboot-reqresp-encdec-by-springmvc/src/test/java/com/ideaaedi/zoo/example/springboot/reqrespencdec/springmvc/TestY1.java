package com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc;

import com.alibaba.fastjson2.JSON;
import com.ideaaedi.commonds.security.AES;
import com.ideaaedi.commonds.security.Base64;
import com.ideaaedi.zoo.diy.feature.reqresp.encdec.api.properties.ZooReqrespEncdecProperties;
import com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc.controller.DemoController;
import com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc.entity.XyzReqVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;

@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootReqrespSpringmvcApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TestY1 {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Value("${zoo.req-resp-enc-dec.req-decrypt.aes-key}")
    private String aesKey;
    
    @BeforeAll
    static void setUp() {
        System.setProperty("spring.profiles.active", "y1");
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.ReqDecryptProperties#getIncludeHosts()} 配置
     */
    @Test
    public void testY1() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(
                encryptData(XyzReqVO.builder().xyzName(DemoController.randomName()).xyzAge(DemoController.randomAge()).build()),
                headers
        );
    
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/b/y1", entity, Object.class);
        Object body = response.getBody();
        headers = response.getHeaders();
        System.err.printf(
                """
                响应头：%s
                响应体：%s
                """,
                headers, body
        );
    }
    
    public String encryptData(Object obj) {
        return AES.encryptSilently(JSON.toJSONString(obj),
                Base64.encode(aesKey.getBytes(StandardCharsets.UTF_8)));
    }
    
}