package com.test.diff.services.base.controller;


import com.test.diff.services.enums.StatusCode;
import com.test.diff.services.exceptions.BizException;
import com.test.diff.services.exceptions.FileException;
import com.test.diff.services.exceptions.GitException;
import com.test.diff.services.base.controller.result.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author wl
 */
@Slf4j
@ControllerAdvice
public class BaseController {

    @ExceptionHandler(Exception.class)
    public @ResponseBody BaseResult<?> handleUncaughtException(Exception exception){
        log.error(exception.getMessage(), exception.getCause());
        exception.printStackTrace();
        return BaseResult.error(StatusCode.OTHER_ERROR, "系统异常");
    }

    @ExceptionHandler(GitException.class)
    public @ResponseBody BaseResult<?> handleGitException(GitException gitException){
        log.error(gitException.getMessage(), gitException.getCause());
        gitException.printStackTrace();
        if(Objects.isNull(gitException.getStatusCode())){
            return BaseResult.error(StatusCode.GIT_ERROR, gitException.getMessage());
        }
        return BaseResult.error(gitException.getStatusCode(), gitException.getStatusCode().getDesc());
    }

    @ExceptionHandler
    public @ResponseBody BaseResult<?> handleFileException(FileException fileException){
        log.error(fileException.getMessage(), fileException.getCause());
        fileException.printStackTrace();
        if(Objects.isNull(fileException.getStatusCode())){
            return BaseResult.error(StatusCode.File_ERROR, fileException.getMessage());
        }
        return BaseResult.error(fileException.getStatusCode(), fileException.getStatusCode().getDesc());
    }

    @ExceptionHandler
    public @ResponseBody BaseResult<?> handleBizException(BizException bizException){
        if(Objects.isNull(bizException.getStatusCode())){
            return BaseResult.error(StatusCode.OTHER_ERROR, bizException.getMessage());
        }
        return BaseResult.error(bizException.getStatusCode(), bizException.getStatusCode().getDesc());
    }

    @ExceptionHandler
    public @ResponseBody BaseResult<?> handleNotValidException(MethodArgumentNotValidException exception){
        List<String> errorMsgList = new ArrayList<>();
        for(FieldError fieldError: exception.getBindingResult().getFieldErrors()){
            errorMsgList.add(fieldError.getDefaultMessage());
        }
        String errorMsg = null;
        if(!CollectionUtils.isEmpty(errorMsgList)){
            errorMsg = errorMsgList.get(0);
        }
        return BaseResult.error(StatusCode.PARAMS_ERROR, errorMsg);
    }

}
