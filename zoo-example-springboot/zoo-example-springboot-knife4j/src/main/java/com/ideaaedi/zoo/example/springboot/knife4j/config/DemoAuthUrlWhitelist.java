package com.ideaaedi.zoo.example.springboot.knife4j.config;

import com.google.common.collect.Lists;
import com.ideaaedi.zoo.commonbase.zoo_component.auth.AuthUrlWhitelist;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * (non-javadoc)
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Component
public class DemoAuthUrlWhitelist implements AuthUrlWhitelist {
    @Override
    public Collection<String> whitelist() {
        return Lists.newArrayList("/dept-a/**");
    }
}
