package com.test.diff.services.base.controller.result;


import com.test.diff.services.enums.StatusCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class BaseResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static <T> BaseResult<T> success(T data){
        return new BaseResult<T>(StatusCode.SUCCESS, data);
    }

    public static  <T> BaseResult<T> success(T data, String msg){
        return new BaseResult<T>(StatusCode.SUCCESS, data, msg);
    }

    public static <T> BaseResult<T> error(StatusCode code){
        return new BaseResult<T>(code, null);
    }

    public static <T> BaseResult<T> error(StatusCode code, String msg){
        return new BaseResult<T>(code, null, msg);
    }


    public BaseResult(){};

    public BaseResult(StatusCode code, T data){
        this.code = code.getCode();
        this.msg = code.getDesc();
        this.data = data;
    }

    public BaseResult(StatusCode code, T data, String msg){
        this.code = code.getCode();
        this.msg = msg;
        this.data = data;
    }

    private int code;

    private T data;

    private String msg;




}
