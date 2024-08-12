package com.ideaaedi.zoo.example.springboot.litefeature.cs.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RedissonConfig 配置
 * <p>
 * 当@EnableFeature(enableLockAnno = true)时，需要提供RedissonClient
 * </p>
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class DemoRedissonConfig {
    
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = "127.0.0.1:6379";
        log.info("redis address -> {}", address);
        final SingleServerConfig singleServerConfig = config.useSingleServer()
                .setConnectionMinimumIdleSize(10)
                .setAddress("redis://" + address);
        //singleServerConfig.setPassword(password);
        RedissonClient redissonClient = Redisson.create(config);
        
//        // 给DefaultRedisLockSupport设置默认的redissonClient
//        DefaultRedisLockSupport.initDefaultRedissonClient(redissonClient);
        
        return redissonClient;
    }
}
