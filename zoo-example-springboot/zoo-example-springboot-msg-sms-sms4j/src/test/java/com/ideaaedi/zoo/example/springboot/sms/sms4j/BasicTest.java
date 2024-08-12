package com.ideaaedi.zoo.example.springboot.sms.sms4j;

import com.alibaba.fastjson2.JSON;
import com.ideaaedi.zoo.diy.feature.msg.api.sms.SmsFaceUtil;
import com.ideaaedi.zoo.diy.feature.msg.api.sms.core.SmsSender;
import com.ideaaedi.zoo.diy.feature.msg.api.sms.core.SmsSenderManager;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * 基础测试
 */
@Slf4j
@SpringBootTest(
        classes = ExampleSpringbootMsgSmsSms4jApplication.class
)
public class BasicTest {
    
    @Test
    @SneakyThrows
    public void test1() {
        // SmsSenderIdA为短信发送器id（在sms4j中，即为config-id），需要与配置文件中设置的相同
        SmsBlend smsBlend = SmsFactory.getSmsBlend("SmsSenderIdA");
        Assertions.assertNotNull(smsBlend);
        String senderId = smsBlend.getConfigId();
        System.out.println("我们获取到了sms4j对短信发送器门面类的实现：" + JSON.toJSONString(smsBlend));
    
        System.out.println();
        System.out.println();
        System.out.println();
    
        
        // 通过zoo.msg.sms.default-sender-id=SmsSenderIdA指定了默认的短信发送器
        SmsSender defaultSender = SmsFaceUtil.getDefaultSender();
        Assertions.assertNotNull(defaultSender);
        Assertions.assertEquals("SmsSenderIdA", defaultSender.id());
        
        SmsSenderManager senderManager = SmsFaceUtil.getSenderManager();
        SmsSender smsSender = senderManager.find(senderId);
        Assertions.assertNotNull(smsSender);
        System.out.println("我们获取到了短信发送器的门面类：" + JSON.toJSONString(smsSender));
        TimeUnit.MILLISECONDS.sleep(20);
        System.err.println("              >>> 开始使用短信门面类SmsFaceUtil（或SmsSender）发送短信吧！ <<< ");
    
        ///SmsSendResult sendResult = SmsFaceUtil.send(senderId, "SMS_471375016", Map.of("code", "8789"), "13751342504");
        ///System.err.println(JSON.toJSONString(sendResult, JSONWriter.Feature.PrettyFormat));
    }
}