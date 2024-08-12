package com.ideaaedi.zoo.example.springboot.fieldperm.openapi.config;

import com.ideaaedi.zoo.commonbase.entity.BaseCodeMsgEnum;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.diy.feature.fieldperm.api.entity.MethodArgFieldInfo;
import com.ideaaedi.zoo.diy.feature.fieldperm.api.exception.FieldPermException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * spring-mvc 异常处理器
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img
 * src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {
    
    /**
     * 处理 字段权限校验异常
     */
    @ExceptionHandler(FieldPermException.class)
    public Result<Object> handleFieldPermException(FieldPermException e) {
        MethodArgFieldInfo methodArgFieldInfo = e.getMethodArgFieldInfo();
        Result<Object> result = Result.failure(e.getMessage(), BaseCodeMsgEnum.FIELD_PERMISSION_ERROR, methodArgFieldInfo.getFieldName());
        log.error("DefaultExceptionHandler#handleFieldPermException e.getMessage() -> {}, result -> {}", e.getMessage(),
                result, e);
        return result;
    }
}
