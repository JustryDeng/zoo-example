package com.ideaaedi.zoo.example.springboot.generator;

import com.google.common.collect.Lists;
import com.ideaaedi.commonds.path.PathUtil;
import com.ideaaedi.zoo.diy.artifact.generator.face.GeneratorFace4JavaCurdCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = ExampleSpringbootGeneratorApplication.class)
public class GeneratorFace4JavaCurdCodeTest {
    
    /**
     * 生成java代码curd 测试1：整库输出
     */
    @Test
    public void test1() {
        GeneratorFace4JavaCurdCode.generate(null);
    }
    
    /**
     * 生成java代码curd 测试2：指定输出目录、指定要生成表
     */
    @Test
    public void test2() {
        String projectRootDir = PathUtil.getProjectRootDir(this.getClass())
                .replace("/target/test-classes/", "/src/test/java/")
                .replace("/target/classes/", "/src/test/java/");
        
        GeneratorFace4JavaCurdCode.generate(projectRootDir, Lists.newArrayList("sys_menu"));
    }
    
    /**
     * 生成java代码curd 测试3：指定包名
     */
    @Test
    public void test3() {
        String projectRootDir = PathUtil.getProjectRootDir(this.getClass())
                .replace("/target/test-classes/", "/src/test/java/")
                .replace("/target/classes/", "/src/test/java/");
        
        GeneratorFace4JavaCurdCode.generate(projectRootDir, "aa.bb.cc.xx.yy.zz", Lists.newArrayList("sys_menu"));
    }
    
}