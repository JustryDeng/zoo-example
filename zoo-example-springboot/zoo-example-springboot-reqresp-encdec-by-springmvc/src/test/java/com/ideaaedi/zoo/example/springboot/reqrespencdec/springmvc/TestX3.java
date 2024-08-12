package com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc;

import com.ideaaedi.zoo.diy.feature.reqresp.encdec.api.properties.ZooReqrespEncdecProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootReqrespSpringmvcApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TestX3 {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @BeforeAll
    static void setUp() {
        System.setProperty("spring.profiles.active", "x3");
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.RespEncryptProperties#getIncludeExistRespHeaders()} 配置
     */
    @Test
    public void testX3() {
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/a/x3", null, Object.class);
        Object body = response.getBody();
        HttpHeaders headers = response.getHeaders();
        System.err.printf(
                """
                响应头：%s
                响应体：%s
                """,
                headers, body
        );
    }
    
}