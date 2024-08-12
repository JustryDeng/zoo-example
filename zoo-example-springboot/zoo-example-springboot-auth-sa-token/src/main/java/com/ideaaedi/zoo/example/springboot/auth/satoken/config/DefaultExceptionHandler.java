package com.ideaaedi.zoo.example.springboot.auth.satoken.config;

import com.ideaaedi.commonds.exception.ExceptionUtil;
import com.ideaaedi.zoo.commonbase.constant.AutoConfigurationConstant;
import com.ideaaedi.zoo.commonbase.entity.BaseCodeMsgEnum;
import com.ideaaedi.zoo.commonbase.entity.CodeMsgProvider;
import com.ideaaedi.zoo.commonbase.entity.Result;
import com.ideaaedi.zoo.commonbase.exception.BaseException;
import com.ideaaedi.zoo.commonbase.exception.TextTipException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;

/**
 * spring-mvc 异常处理器
 *
 * @author <font size = "20" color = "#3CAA3C"><a href="https://gitee.com/JustryDeng">JustryDeng</a></font> <img src="https://gitee.com/JustryDeng/shared-files/raw/master/JustryDeng/avatar.jpg" />
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler implements Ordered {
    
    /**
     * 处理 基类业务异常
     */
    @ExceptionHandler(BaseException.class)
    public Result<Object> handleBaseException(BaseException e) {
        Object[] placeholders = e.getPlaceholders();
        CodeMsgProvider codeMsgProvider = e.getCodeMsgProvider();
        Result<Object> result = null;
        if (codeMsgProvider instanceof Result) {
            //noinspection unchecked,rawtypes
            result = (Result)codeMsgProvider;
        }
        result = Result.failure(result == null ? e.getData() : result.getData(), codeMsgProvider, placeholders);
        log.error("DefaultExceptionHandler#handleBaseException  e.getMessage() -> {}, Result -> {}", e.getMessage(), result, e);
        return result;
    }
    
    /**
     * 处理TextTipException异常
     */
    @ExceptionHandler(TextTipException.class)
    public Result<Object> handleTextTipException(TextTipException e) {
        Result<Object> result = Result.failure(null, BaseCodeMsgEnum.CONVENIENT_TIP, e.getTips());
        log.error("DefaultExceptionHandler#handleTextTipException  e.getMessage() -> {}, result -> {}", e.getMessage(), result, e);
        return result;
    }
    
    /**
     * 处理IllegalArgumentException异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        Result<Object> result = Result.failure(BaseCodeMsgEnum.ARGS_ILLEGAL);
        String errorMessage = e.getMessage();
        result.setMsg(BaseCodeMsgEnum.ARGS_ILLEGAL.getMessage() + ". " + errorMessage);
        log.error("DefaultExceptionHandler#handleIllegalArgumentException e.getMessage() -> {}, Result -> {}",
                e.getMessage(), result, e);
        return result;
    }
    
    /**
     * JSR303校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Object> handleMethodargumentnotvalidexception(MethodArgumentNotValidException e) {
        Result<Object> result = Result.failure(BaseCodeMsgEnum.ARGS_ILLEGAL);
        String errorMessage;
        try {
            List<ObjectError> errorList = e.getBindingResult().getAllErrors();
            errorMessage = errorList.get(0).getDefaultMessage();
            result.setMsg(BaseCodeMsgEnum.ARGS_ILLEGAL.getMessage() + ". " + errorMessage);
        } catch (Exception ex) {
            log.warn("DefaultExceptionHandler#handleMethodargumentnotvalidexception occur error while parsing error zoo_info. ex.getMessage() -> {}", ex.getMessage());
        }
        log.error("DefaultExceptionHandler#handleMethodargumentnotvalidexception e.getMessage() -> {}, result -> {}",
                e.getMessage(), result, e);
        return result;
    }
    
    /**
     * JSR303校验异常
     */
    @ExceptionHandler(ValidationException.class)
    public Result<Object> handleValidationException(ValidationException e) {
        Result<Object> result = Result.failure(BaseCodeMsgEnum.ARGS_ILLEGAL);
        String errorMessage;
        try {
            StringBuilder sb = new StringBuilder();
            if (e instanceof ConstraintViolationException ) {
                ConstraintViolationException exs = (ConstraintViolationException)e;
                Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
                for (ConstraintViolation<?> item : violations) {
                    sb.append(item.getMessage()).append(",");
                }
            }
            errorMessage = sb.toString();
            result.setMsg(BaseCodeMsgEnum.ARGS_ILLEGAL.getMessage() + ". " + errorMessage);
        } catch (Exception exception) {
            log.warn("DefaultExceptionHandler#handleValidationException occur error while parsing error zoo_info. exception.getMessage() -> {}",
                    exception.getMessage());
        }
        log.error("DefaultExceptionHandler#handleValidationException e.getMessage() -> {}, result -> {}", e.getMessage(), result, e);
        return result;
    }
    
    /**
     * JSR303校验异常
     */
    @ExceptionHandler(BindException.class)
    public Result<Object> handleBindException(BindException e) {
        Result<Object> result = Result.failure(BaseCodeMsgEnum.ARGS_ILLEGAL);
        String errorMessage;
        try {
            StringBuffer sb = new StringBuffer();
            e.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String tmpErrorMessage = error.getDefaultMessage();
                sb.append(fieldName).append("：").append(tmpErrorMessage).append(",");
            });
            errorMessage = sb.toString();
            result.setMsg(BaseCodeMsgEnum.ARGS_ILLEGAL.getMessage() + ". " + errorMessage);
        } catch (Exception ex) {
            log.warn("DefaultExceptionHandler#handleBindException occur error while parsing error zoo_info. ex.getMessage() -> {}", ex.getMessage());
        }
        log.error("DefaultExceptionHandler#handleBindException e.getMessage() -> {}, result -> {}", e.getMessage(), result, e);
        return result;
    }
    
    /**
     * 处理 读取http消息错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Result<Object> result = Result.failure(e.getMessage(), BaseCodeMsgEnum.HTTP_MSG_READ_ERROR);
        log.error("DefaultExceptionHandler#handleHttpMessageNotReadableException  e.getMessage() -> {}, Result -> {}", e.getMessage(), result, e);
        return result;
    }
    
    /**
     * 处理 http方法类型不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Object> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        Result<Object> result = Result.failure(e.getMessage(), BaseCodeMsgEnum.HTTP_REQUEST_METHOD_NOT_SUPPORTED_FAIL);
        log.error("DefaultExceptionHandler#handleHttpRequestMethodNotSupportedException  e.getMessage() -> {}, Result -> {}", e.getMessage(), result, e);
        return result;
    }
    
    /**
     * 处理 缺少RequestParameter参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Object> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        Result<Object> result = Result.failure(e.getMessage(), BaseCodeMsgEnum.MISSING_SERVLET_REQUEST_PARAMETER_FAIL);
        log.error("DefaultExceptionHandler#handleMissingServletRequestParameterException  e.getMessage() -> {}, Result -> {}", e.getMessage(), result, e);
        return result;
    }
    
    /**
     * 处理 检查异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Object> handleException(Exception e) {
        BaseException baseException = ExceptionUtil.extractThrowable(e, BaseException.class, 5);
        if (baseException != null) {
            log.error("redirect to handleBaseException. raw e.getMessage() -> {}", e.getMessage());
            return handleBaseException(baseException);
        }
        Result<Object> result = Result.failure();
        log.error("DefaultExceptionHandler#handleException e.getMessage() -> {}, result -> {}", e.getMessage(), result, e);
        return result;
    }
    
    @Override
    public int getOrder() {
        return AutoConfigurationConstant.DEFAULT_EXCEPTION_HANDLER_ORDER;
    }
}
