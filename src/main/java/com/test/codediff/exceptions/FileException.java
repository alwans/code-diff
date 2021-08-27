package com.test.codediff.exceptions;

import com.test.codediff.enums.StatusCode;
import lombok.Data;

/**
 * @author wl
 */
@Data
public class FileException extends RuntimeException{

    private StatusCode statusCode;

    public FileException(){}

    public FileException(StatusCode statusCode){
        super(statusCode.getDesc());
    }

    public FileException(String msg){
        super(msg);
    }
}
