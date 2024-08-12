package com.ideaaedi.zoo.example.springboot.expengine.qlexpress;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.ideaaedi.zoo.diy.feature.expression.engine.api.annotation.ExpressionReplacer;
import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.TmpPageInfo;
import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.TmpResult;
import com.ideaaedi.zoo.example.springboot.expengine.qlexpress.entity.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * {@link ExpressionReplacer}注解测试
 */
@Slf4j
@SpringBootTest(classes = ExampleQLExpressApplication.class)
public class ExpressionReplacerTest {
    
    @Resource
    private DemoXyzBean demoXyzBean;
    
    /**
     * 测试：当没命中表达式替换时，会走原方法逻辑
     */
    @Test
    public void test1() {
        User user = User.builder().name("用户A").build();
        DemoXyzBean.Worker worker = DemoXyzBean.Worker.builder().name("工人X").build();
        String rawResult = demoXyzBean.qwer5NoAnno(user, worker);
        System.err.println("qwer5NoAnno返回： " + rawResult);
    
        String replaceResult = demoXyzBean.qwer5HasAnno(user, worker);
        System.err.println("qwer5HasAnno返回：" + replaceResult);
        
        // 当没命中表达式替换时，还是会走原方法逻辑，所以这里值相同
        Assertions.assertEquals(rawResult, replaceResult);
    }
    
    /**
     * 测试：当命中表达式替换时，会走表达式逻辑，不会走原方法逻辑
     */
    @Test
    public void test2() {
        User user = User.builder().name("张三").build();
        DemoXyzBean.Worker worker = DemoXyzBean.Worker.builder().name("李四").build();
        String rawResult = demoXyzBean.qwer5NoAnno(user, worker);
        System.err.println("qwer5NoAnno返回： " + rawResult);
    
        String replaceResult = demoXyzBean.qwer5HasAnno(user, worker);
        System.err.println("qwer5HasAnno返回：" + replaceResult);
        
        // 当命中表达式替换时，会用【表达式逻辑】代替【原方法逻辑】； 根据DemoExpressionScriptLoader中写的表达式，这里返回值和原方法的不等
        Assertions.assertNotEquals(rawResult, replaceResult);
    }
    
    /**
     * 测试：命中表达式，并返回复杂对象
     */
    @Test
    public void test3() {
        User user = User.builder().name("王五").age(88).build();
        DemoXyzBean.Worker worker = DemoXyzBean.Worker.builder().name("田七").age(66).build();
        TmpResult<TmpPageInfo<User>> result = demoXyzBean.qwer6(user, worker);
        System.err.println(JSON.toJSONString(result, JSONWriter.Feature.NotWriteDefaultValue));
        long total = result.getData().getTotal();
        Assertions.assertEquals(2, total);
    }
    
}