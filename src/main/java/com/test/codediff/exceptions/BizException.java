package com.test.codediff.exceptions;

import com.test.codediff.enums.StatusCode;
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
    }
}
