package com.app.controller.exception;


import com.sdk.exception.GlobalException;
import com.sdk.resp.RespEntity;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 对异常的统一返回
 * @author xxl
 * @since 2023/9/16
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController {
    /**
     * 捕捉spring boot容器所有的未知异常
     */
    @ExceptionHandler(Exception.class)
    public RespEntity<?> exception(Exception exception) {
        if (exception instanceof GlobalException com) {
            return RespEntity.fail(com.getCode(), com.getMsg());
        } else if (exception instanceof BindException bindException) {
            return RespEntity.fail(bindException.getFieldErrors().
                    stream().
                    map(FieldError::getDefaultMessage).
                    distinct().
                    toList().
                    toString());
        }else if (exception instanceof IllegalArgumentException){
            return RespEntity.fail(exception.getMessage());
        }else if (exception instanceof HttpMessageNotReadableException){
            return RespEntity.fail("枚举类型不存在");
        }else if (exception instanceof ConstraintViolationException){
            return RespEntity.fail(exception.getMessage());
        }
        return RespEntity.fail();
    }
}
