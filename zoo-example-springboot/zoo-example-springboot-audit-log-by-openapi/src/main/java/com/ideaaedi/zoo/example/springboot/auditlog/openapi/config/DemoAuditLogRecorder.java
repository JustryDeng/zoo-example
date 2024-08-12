package com.ideaaedi.zoo.example.springboot.auditlog.openapi.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.ideaaedi.zoo.diy.feature.auditlog.api.entity.AuditDTO;
import com.ideaaedi.zoo.diy.feature.auditlog.api.spi.AuditLogRecorder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DemoAuditLogRecorder implements AuditLogRecorder {
    @Override
    public void record(@Nonnull AuditDTO auditInfo) {
        System.err.println(
                "发现审计日志：" + JSON.toJSONString(auditInfo, JSONWriter.Feature.NotWriteNumberClassName, JSONWriter.Feature.PrettyFormat)
        );
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
