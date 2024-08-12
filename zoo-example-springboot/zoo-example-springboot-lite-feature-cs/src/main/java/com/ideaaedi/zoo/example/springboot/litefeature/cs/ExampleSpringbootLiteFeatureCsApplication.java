package com.ideaaedi.zoo.example.springboot.litefeature.cs;

import com.ideaaedi.commonspring.lite.EnableFeature;
import com.ideaaedi.commonspring.lite.params.ParameterRecorder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.multipart.MultipartFile;

/*
 * 示例：在底座zoo-foundation上集成zoo-diy-feature-lite-feature-by-cs
 */
@EnableFeature(
        /*
         * 进出口日志记录。使用步骤：
         * 1. 这里设置为true （会扫描includePrefixes包下的所有以Controller结尾的类，并记录进出日志）
         * 2（可选）. 你也可以使用@RecordParameters和ParameterRecorder来主动标记或排除includePrefixes包下哪些方法需要记录进出日志
         */
        enableParameterRecorder = @ParameterRecorder(
                includePrefixes = "com.ideaaedi.zoo",
                ignoreParamTypes = {MultipartFile.class, HttpServletRequest.class, HttpServletResponse.class}
        ),
        /*
         * 请求防止重复。使用步骤：
         * 1. 这里设置为true
         * 2. 实现 AntiDuplicateExecutor
         * 3. 在需要放重复提交的方法上使用@AntiDuplication注解，demo见 DemoUserController.antiDupliDemo
         */
        enableAntiDuplicate = true,
        /*
         * 分布式锁。使用步骤：
         * 1. 这里设置为true
         * 2. 引入缓存组件org.redisson:redisson并注册RedissonClient
         * 3. 使用分布式锁
         *    使用方式一：使用门面工具类LockFaceUtil，    demo见 DemoUserController.lockDemo1
         *    使用方式二：使用注解@Lock，                demo见 DemoUserController.lockDemo2
         */
        enableLockAnno = true,
        /*
         * 注入增强。使用步骤：
         * 1. 这里设置为true
         * 2. 使用分布式锁
         *    使用方式一：使用工具类. ExtAutowiredInjector.inject(yourObj)         demo见 DemoUserController.extDemo1
         *    使用方式二：直接继承. YourObj extends ExtAutowiredInjector，         demo见 DemoUserController.extDemo2
         */
        enableExtAutowiredInjector = true
)
@SpringBootApplication
public class ExampleSpringbootLiteFeatureCsApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ExampleSpringbootLiteFeatureCsApplication.class, args);
        System.err.println();
        System.err.println();
        System.err.print(
                """
                        启动项目后，请访问相关controller api观察效果
                        """
        );
        System.err.println();
        System.err.println();
        System.err.println();
        System.err.println();
    }
    
}
