package com.test.diff.services.exceptions;


import com.test.diff.services.enums.StatusCode;
import lombok.Data;

/**
 * @author wl
 */
@Data
public class BizException extends RuntimeException{

    private StatusCode statusCode;
    
    public BizException(){}

    public BizException(StatusCode statusCode){
        super(statusCode.getDesc());
        this.statusCode = statusCode;
    }

    public BizException(String msg){
        super(msg);
    }
}
