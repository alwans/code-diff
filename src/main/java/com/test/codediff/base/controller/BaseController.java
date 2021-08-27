package com.test.codediff.base.controller;

import com.test.codediff.base.result.BaseResult;
import com.test.codediff.enums.StatusCode;
import com.test.codediff.exceptions.BizException;
import com.test.codediff.exceptions.FileException;
import com.test.codediff.exceptions.GitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

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
        return BaseResult.error(gitException.getStatusCode(), gitException.getMessage());
    }

    @ExceptionHandler
    public @ResponseBody BaseResult<?> handleFileException(FileException fileException){
        log.error(fileException.getMessage(), fileException.getCause());
        fileException.printStackTrace();
        if(Objects.isNull(fileException.getStatusCode())){
            return BaseResult.error(StatusCode.File_ERROR, fileException.getMessage());
        }
        return BaseResult.error(fileException.getStatusCode(), fileException.getMessage());
    }

    @ExceptionHandler
    public @ResponseBody BaseResult<?> handleBizException(BizException bizException){
        return BaseResult.error(bizException.getStatusCode(), bizException.getMessage());
    }

}