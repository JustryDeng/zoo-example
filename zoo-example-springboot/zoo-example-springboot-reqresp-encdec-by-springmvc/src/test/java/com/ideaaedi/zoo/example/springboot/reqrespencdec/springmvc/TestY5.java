package com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc;

import com.ideaaedi.zoo.diy.feature.reqresp.encdec.api.properties.ZooReqrespEncdecProperties;
import com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc.controller.DemoController;
import com.ideaaedi.zoo.example.springboot.reqrespencdec.springmvc.entity.XyzReqVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootReqrespSpringmvcApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class TestY5 {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @BeforeAll
    static void setUp() {
        System.setProperty("spring.profiles.active", "y5");
    }
    
    /**
     * 测试{@link ZooReqrespEncdecProperties.ReqDecryptProperties#getExcludePaths()} 配置
     */
    @Test
    public void testY5() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<XyzReqVO> entity = new HttpEntity<>(
                XyzReqVO.builder().xyzName(DemoController.randomName()).xyzAge(DemoController.randomAge()).build(),
                headers
        );
    
        ResponseEntity<Object> response = restTemplate.postForEntity("http://localhost:" + port + "/b/y5", entity, Object.class);
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
    
}