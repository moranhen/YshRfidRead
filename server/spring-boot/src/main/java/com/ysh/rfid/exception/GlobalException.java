package com.ysh.rfid.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalException {
    /**
     * 服务异常
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public ResponseObject handlerException(Exception e) {
        log.error(e.getMessage());
        return ResponseObject.fail("提交修改失败！");
    }


    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(ServiceException.class)
    public ResponseObject handleException(ServiceException e) {
        log.error(e.getMessage());
        return ResponseObject.fail(e.getMessage(), e.getCode());
    }
}

