package com.ideaaedi.zoo.example.springboot.expengine.qlexpress;

import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.ideaaedi.zoo.diy.feature.expression.engine.api.annotation.ExpressionReplacer;
import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.config.DemoExpressionScriptLoader;
import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.TmpPageInfo;
import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.TmpResult;
import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * (non-javadoc)
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Component(DemoXyzBean.XYZ_NAME)
public class DemoXyzBean {
    public static final String XYZ_NAME = "demoXyzBean";
    
    public String qwer1() {
        return RandomUtil.randomChinese() + RandomUtil.randomString(2);
    }
    
    public String qwer2(String name, int age) {
        return name + age;
    }
    
    public String qwer3(User user) {
        return String.valueOf(user);
    }
    
    public String qwer4(Worker worker) {
        return String.valueOf(worker);
    }
    
    public String qwer5NoAnno(User user, Worker worker) {
        return user.getName() + "@" + worker.getName();
    }
    @ExpressionReplacer(DemoExpressionScriptLoader.BizKeyDemo)
    public String qwer5HasAnno(User user, Worker worker) {
        return user.getName() + "@" + worker.getName();
    }
    
    @ExpressionReplacer(DemoExpressionScriptLoader.qwer6)
    public TmpResult<TmpPageInfo<User>> qwer6(User user, Worker worker) {
        return TmpResult.success(TmpPageInfo.of(1, 1, 10, Lists.newArrayList(user)));
    }
    
    /**
     * (non-javadoc)
     *
     * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
     * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
     * @since 1.0.0
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Worker {
        private String name;
        private int age;
    
        public static Worker of(String name, int age) {
            Worker worker = new Worker();
            worker.setName(name);
            worker.setAge(age);
            return worker;
        }
    }
}
