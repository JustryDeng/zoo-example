package com.ideaaedi.zoo.example.springboot.generator;

import com.google.common.collect.Lists;
import com.ideaaedi.commonds.path.PathUtil;
import com.ideaaedi.commonds.time.DateTimeConverter;
import com.ideaaedi.zoo.diy.artifact.generator.code.entity.FieldInfo;
import com.ideaaedi.zoo.diy.artifact.generator.code.entity.PojoInfo;
import com.ideaaedi.zoo.diy.artifact.generator.code.enums.DefaultOutputEnum;
import com.ideaaedi.zoo.diy.artifact.generator.face.GeneratorFace4JavaCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = ExampleSpringbootGeneratorApplication.class)
public class GeneratorFace4JavaCodeTest {
    
    /**
     * 测试1：普通测试
     */
    @Test
    public void test1() {
        GeneratorFace4JavaCode.generate(
                PojoInfo.of("Abc", "测试",
                        FieldInfo.of("name", "姓名", String.class)
                )
        );
    
        System.err.println("观察控制台输出，会打印出输出位置");
    }
    
    /**
     * 测试1：输出到指定位置
     */
    @Test
    public void test2() {
        String projectRootDir = PathUtil.getProjectRootDir(this.getClass())
                .replace("/target/test-classes/", "/src/test/java/")
                .replace("/target/classes/", "/src/test/java/");
        
        GeneratorFace4JavaCode.generate(projectRootDir,
                PojoInfo.of("Abc", "测试",
                        FieldInfo.of("name", "姓名", String.class)
                )
        );
    
        System.err.println("观察控制台输出，会打印出输出位置");
    }
    
    /**
     * 测试1：输出到指定位置、指定包名
     */
    @Test
    public void test3() {
        String projectRootDir = PathUtil.getProjectRootDir(this.getClass())
                .replace("/target/test-classes/", "/src/test/java/")
                .replace("/target/classes/", "/src/test/java/");
    
        String packagePath = this.getClass().getPackageName() + ".jd" + DateTimeConverter.COMPACT_DATE_TIME.now();
        GeneratorFace4JavaCode.generate(projectRootDir, packagePath,
                PojoInfo.of("Abc", "测试",
                        FieldInfo.of("name", "姓名", String.class)
                )
        );
    
        System.err.println("观察控制台输出，会打印出输出位置");
    }
    
    /**
     * 测试1：输出 指定类型的文件测试
     */
    @Test
    public void test4() {
        String projectRootDir = PathUtil.getProjectRootDir(this.getClass())
                .replace("/target/test-classes/", "/src/test/java/")
                .replace("/target/classes/", "/src/test/java/");
        
        String packagePath = this.getClass().getPackageName() + ".jd" + DateTimeConverter.COMPACT_DATE_TIME.now();
        GeneratorFace4JavaCode.generate(projectRootDir, packagePath,
                Lists.newArrayList(DefaultOutputEnum.entity, DefaultOutputEnum.mapper, DefaultOutputEnum.service_impl, DefaultOutputEnum.controller),
                PojoInfo.of("Abc", "测试",
                        FieldInfo.of("name", "姓名", String.class)
                )
        );
        
        System.err.println("观察控制台输出，会打印出输出位置");
    }
    
    /**
     * 测试1：当项目中存在zoo基于knife4j对swagger的增强时，生成的controller使用了对应的增强注解
     * <pre>
     *  controller增强点：
     *  1.会用@ApiTag注解代替@Tag注解
     *  2.会用@ApiOperationSupport注解对方法排序
     * </pre>
     *
     * <pre>
     * 引入此依赖，然后生成代码，和引入前的controller代码进行对比观察
     * {@code
     * <dependency>
     *    <groupId>com.idea-aedi.zoo</groupId>
     *    <artifactId>zoo-diy-artifact-apidoc-knife4j</artifactId>
     *    <version>${zoo-diy-artifact-apidoc-knife4j.version}</version>
     *  </dependency>
     * }
     * </pre>
     */
    @Test
    public void test5() {
        String projectRootDir = PathUtil.getProjectRootDir(this.getClass())
                .replace("/target/test-classes/", "/src/test/java/")
                .replace("/target/classes/", "/src/test/java/");
        
        String packagePath = this.getClass().getPackageName() + ".jd" + DateTimeConverter.COMPACT_DATE_TIME.now();
        GeneratorFace4JavaCode.generate(projectRootDir, packagePath,
                PojoInfo.of("Abc", "测试",
                        FieldInfo.of("name", "姓名", String.class)
                )
        );
    }
    
}