package org.phoenix.aladdin.controller;

import org.phoenix.aladdin.error.BusinessException;
import org.phoenix.aladdin.error.EmBusinessError;
import org.phoenix.aladdin.response.CommonReturnType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    @Autowired
    HttpServletRequest request;

    //定义exceptionHandler解决未被controller层吸收的异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex){
        Map<String,Object> responseData=new HashMap<>();
        if (ex instanceof BusinessException){
            BusinessException businessException=(BusinessException)ex;
            responseData.put("error",businessException.getErrMsg());
            return CommonReturnType.create(responseData,((BusinessException) ex).getErrCode());
        }
        responseData.put("error",EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        return CommonReturnType.create(responseData,EmBusinessError.UNKNOWN_ERROR.getErrCode());
    }
}
