package com.ideaaedi.zoo.example.springboot.litefeature.cs.config;

import com.ideaaedi.commonspring.spi.AbstractAntiDuplicateExecutor;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 防重复 demo
 * <p>
 * 当@EnableFeature(enableAntiDuplicate = true)时，需要提供AbstractAntiDuplicateExecutor实现
 * </p>
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Component
public class DemoAntiDuplicateExecutor extends AbstractAntiDuplicateExecutor {
    
    @Resource
    private HttpServletRequest httpServletRequest;
    
    private Map<String, Long> cache = new ConcurrentHashMap<>();
    
    @Override
    public String doExec(@Nullable String spelVal, long duringSeconds, @Nonnull String message) {
        String uid = httpServletRequest.getServletPath() + spelVal;
        Long pre = cache.get(uid);
        long curr = System.currentTimeMillis();
        if (pre == null) {
            cache.put(uid, curr);
            return uid;
        }
        if (curr - pre <= duringSeconds * 1000) {
            throw new IllegalStateException("请求太频繁了，请稍后再试" + message);
        }
        cache.put(uid, curr);
        return uid;
    }
}
