package com.miaoshaproject.exception;

import com.miaoshaproject.result.CodeMsg;
import com.miaoshaproject.result.Result;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author cyx
 * @data 2019/4/2 12:03
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public Result<String> exceptionhandler(HttpServletRequest request,Exception e){
        e.printStackTrace();
        if(e instanceof GlobalException){
            GlobalException ex=(GlobalException)e;
            return Result.error(ex.getCm());
        }else if(e instanceof BindException){
            BindException ex=(BindException)e;
            List<ObjectError> list = ex.getAllErrors();
            ObjectError err=list.get(0);
            String message=err.getDefaultMessage();
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(message));
        }else{
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}
