package com.ideaaedi.zoo.example.springboot.generator;

import com.ideaaedi.zoo.diy.artifact.generator.face.GeneratorFace4DatabaseDoc;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = ExampleSpringbootGeneratorApplication.class)
public class GeneratorFace4DatabaseDocTest {
    
    /**
     * 数据库文档测试1
     */
    @Test
    public void test1() {
        GeneratorFace4DatabaseDoc.generate();
    }
    
    /**
     * 数据库文档测试2
     */
    @Test
    public void test2() {
        GeneratorFace4DatabaseDoc.generate("C:\\Users\\Administrator\\Desktop\\tmp\\");
    }
}