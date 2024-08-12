package com.ideaaedi.zoo.example.springboot.openfeign.config;

import feign.Logger;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.context.annotation.Bean;

/**
 * 可以通过@Bean定制哪些东西 ，可参考{@link FeignClientProperties.FeignClientConfiguration}字段
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
public class SimpleFeignClientConfig {

    /**
     * 这里需要@Bean，详见{@link FeignClient#configuration()}说明
     */
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }
}
